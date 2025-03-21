package Business;

import Entity.*;
import Interface.GameMediator;

import java.util.*;

public class GamePlay implements GameMediator {
    private Game game;
    private DrawnPile drawnPile;
    private DiscardPile discardPile;
    private Round currentRound;
    private int currentIndex;

    public GamePlay(List<Player> players) {
        this.game = new Game(players);
        this.drawnPile = new DrawnPile();

        this.discardPile = new DiscardPile();
        this.currentRound = new Round();
        this.currentIndex = 0;
    }

    private boolean debug = true;


    private void debugPrint(String msg) {
        if (debug) {
            System.out.println("[DEBUG] " + msg);
        }
    }

    private String cardListString(List<Card> cards) {
        if (cards.isEmpty()) return "  (empty)";
        StringBuilder sb = new StringBuilder();
        for (Card card : cards) {
            sb.append("  - ").append(card).append("\n");
        }
        return sb.toString();
    }

    public void initializeDeck(List<Card> cards) {
        drawnPile.clear();
        drawnPile.addCards(cards);
        drawnPile.shuffle();
    }

    public void dealCards(int handSize) {
        for (int i = 0; i < handSize; i++) {
            for (Player player : game.getPlayers()) {
                player.getHand().addCard(drawnPile.drawCard());
            }
        }

        // Start with one card on discard pile
        Card first = drawnPile.drawCard();
        if (first == null) {
            throw new IllegalStateException("Deck is empty. Did you forget to call initializeDeck?");
        }
        discardPile.addCard(first);
        currentRound.getTurn().setLastPlayedCard(first);
        currentRound.getTurn().setTurnColor(first.getColor());
    }

    @Override
    public void drawCards(Player player, int count) {
        for (int i = 0; i < count; i++) {
            if (drawnPile.isEmpty()) reshuffleFromDiscard();
            player.getHand().addCard(drawnPile.drawCard());
        }
    }

    @Override
    public void skipNextPlayer() {
        currentRound.getTurn().setSkip(true);
    }

    @Override
    public void reverseDirection() {
        RoundDirection dir = currentRound.getDirection();
        RoundDirection reversed = (dir == RoundDirection.LEFT) ? RoundDirection.RIGHT : RoundDirection.LEFT;
        currentRound.setDirection(reversed);
    }

    @Override
    public void setColor(CardColor color) {
        currentRound.getTurn().setTurnColor(color);
    }

    @Override
    public Player getNextPlayer(Player currentPlayer) {
        List<Player> players = game.getPlayers();
        int idx = players.indexOf(currentPlayer);
        RoundDirection dir = currentRound.getDirection();

        int nextIdx = (dir == RoundDirection.LEFT)
                ? (idx - 1 + players.size()) % players.size()
                : (idx + 1) % players.size();

        return players.get(nextIdx);
    }

    @Override
    public Round getCurrentRound() {
        return currentRound;
    }

    @Override
    public Turn getCurrentTurn() {
        return currentRound.getTurn();
    }

    @Override
    public void shuffleHands() {
        List<Player> players = game.getPlayers();
        List<Hand> hands = new ArrayList<>();
        for (Player p : players) {
            hands.add(p.getHand());
        }

        Collections.shuffle(hands);
        for (int i = 0; i < players.size(); i++) {
            players.get(i).resetHand();
            players.get(i).getHand().addCards(hands.get(i).getCards());
        }
    }

    private void reshuffleFromDiscard() {
        Card topCard = discardPile.getTopCard();
        List<Card> toReshuffle = new ArrayList<>(discardPile.getCards());
        toReshuffle.remove(topCard);

        discardPile.getCards().clear();
        discardPile.addCard(topCard);

        drawnPile.addCards(toReshuffle);
        drawnPile.shuffle();
    }

    public void startGame(int handSize, Interface.ILogger logger) {
        debugPrint("🎯 Checking game over condition: Scores = " +
                game.getPlayers().stream().map(p -> p.getName() + "=" + p.getScore()).toList());
        debugPrint("🎯 isGameOver() = " + game.isGameOver());
        while (!game.isGameOver()) {
            currentRound = new Round();
            game.addRound(currentRound);
            currentIndex = 0;

            debugPrint("\n=======================");
            debugPrint("🔄 STARTING ROUND " + game.getRounds().size());
            debugPrint("=======================\n");

            resetPlayerHands();
            discardPile.clear();
            initializeDeck(DeckGenerator.generateStandardDuoDeck());
            debugPrint("Draw pile before dealing: " + drawnPile.getCards().size());
            debugPrint("Discard pile before dealing: " + discardPile.getCards().size());
            dealCards(handSize);

            for (Player p : game.getPlayers()) {
                debugPrint(p.getName() + "'s starting hand:");
                debugPrint(cardListString(p.getHand().getCards()));
            }

            runRoundLoop();

            Player roundWinner = checkRoundWinner();
            if (roundWinner != null) {
                currentRound.setRoundWinner(roundWinner);
                int points = calculateScore(roundWinner);
                roundWinner.addPoints(points);
                currentRound.setWinningPoints(points);

                debugPrint("\n✅ ROUND END: " + roundWinner.getName() + " won the round!");
                debugPrint("🏆 Points earned: " + points);
            }

            Map<Player, Integer> scores = new HashMap<>();
            debugPrint("\n📊 Total scores:");
            for (Player p : game.getPlayers()) {
                scores.put(p, p.getScore());
                debugPrint(p.getName() + ": " + p.getScore());
            }
            currentRound.setPlayerScores(scores);

            logger.logRound(currentRound, game.getPlayers(), game.getRounds().size());
        }

        // ✅ Final debug output before logging winner
        debugPrint("🏁 Final check: " + game.getPlayers().stream()
                .map(p -> p.getName() + " - " + p.getScore())
                .toList());

        debugPrint("✅ Game Over. Final round complete.");

        logger.logFinalWinner(game);
    }

    private void runRoundLoop() {
        while (true) {
            Player currentPlayer = game.getPlayers().get(currentIndex);

            debugPrint("\n--- " + currentPlayer.getName() + "'s turn ---");
            debugPrint("Current hand:");
            debugPrint(cardListString(currentPlayer.getHand().getCards()));
            debugPrint("Top of discard pile: " + currentRound.getTurn().getLastPlayedCard());

            if (currentRound.getTurn().isSkip()) {
                debugPrint("🚫 Turn is skipped!");
                currentRound.getTurn().setSkip(false);
            } else {
                Card topCard = currentRound.getTurn().getLastPlayedCard();
                Card toPlay = currentPlayer.chooseCard(topCard);

                if (toPlay != null) {
                    currentPlayer.getHand().removeCard(toPlay);
                    discardPile.addCard(toPlay);
                    currentRound.getTurn().setLastPlayedCard(toPlay);

                    debugPrint("✅ Played: " + toPlay);

                    if (toPlay instanceof ActionCard actionCard) {
                        actionCard.action(this, currentPlayer);
                    }

                    currentRound.getTurn().setTurnColor(toPlay.getColor());

                    if (currentPlayer.isHandEmpty()) break;

                } else {
                    debugPrint("🛑 No playable card, drawing 1...");
                    if (drawnPile.isEmpty()) reshuffleFromDiscard();
                    Card drawn = drawnPile.drawCard();
                    if (drawn != null) {
                        currentPlayer.getHand().addCard(drawn);
                        debugPrint("🎴 Drew: " + drawn);
                    } else {
                        debugPrint("❌ Draw pile empty, couldn't draw!");
                    }
                }
            }

            debugPrint("Updated hand:");
            debugPrint(cardListString(currentPlayer.getHand().getCards()));
            debugPrint("Discard pile top: " + discardPile.getTopCard());
            debugPrint("Draw pile size: " + drawnPile.getCards().size());

            currentIndex = getNextPlayerIndex();
        }
    }

    private Player checkRoundWinner() {
        for (Player player : game.getPlayers()) {
            if (player.isHandEmpty()) {
                return player;
            }
        }
        return null;
    }

    private int calculateScore(Player winner) {
        int total = 0;
        for (Player p : game.getPlayers()) {
            if (p != winner) {
                total += p.getHand().getTotalPoints();
            }
        }
        return total;
    }

    private int getNextPlayerIndex() {
        int size = game.getPlayers().size();
        if (currentRound.getDirection() == RoundDirection.LEFT) {
            return (currentIndex - 1 + size) % size;
        } else {
            return (currentIndex + 1) % size;
        }
    }

    private void resetPlayerHands() {
        for (Player p : game.getPlayers()) {
            p.resetHand();
        }
    }

    private void reshuffleDeckIfNeeded() {
        if (drawnPile.isEmpty()) {
            reshuffleFromDiscard();
        }
    }
}