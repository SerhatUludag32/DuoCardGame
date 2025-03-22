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

        // Redraw if Wild Draw Four
        while (first instanceof WildDrawFourCard) {
            drawnPile.addCard(first);
            drawnPile.shuffle();
            first = drawnPile.drawCard();
            System.out.println("Initial card was Wild Draw Four → redrawn new card: " + first);
        }

        discardPile.addCard(first);
        currentRound.getTurn().setLastPlayedCard(first);
        currentRound.getTurn().setTurnColor(first.getColor());

        handleInitialCard(first);

        System.out.println("Starting card: " + first);
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

    private void handleInitialCard(Card first) {
        // Handle initial action card effects
        if (first instanceof ReverseCard) {
            currentRound.setDirection(RoundDirection.RIGHT);
            System.out.println("Starting card is Reverse → direction set to RIGHT");
        }

        if (first instanceof SkipCard) {
            currentRound.getTurn().setSkip(true);
            currentIndex = getNextPlayerIndex(0); // skip left of dealer
            System.out.println("Starting card is Skip → first player will be skipped");
        }

        if (first instanceof DrawTwoCard) {
            Player target = game.getPlayers().get(getNextPlayerIndex(currentIndex));
            drawCards(target, 2);
            currentRound.getTurn().setSkip(true);
            System.out.println("Starting card is Draw Two → " + target.getName() + " draws 2 cards and is skipped");
        }

        if (first instanceof ShuffleHandsCard shuffle) {
            Player chooser = game.getPlayers().get(getNextPlayerIndex(0));
            CardColor color = chooser.chooseColor();
            shuffle.setColor(color);
            currentRound.getTurn().setTurnColor(color);
            System.out.println("Starting card is Shuffle Hands → " + chooser.getName() + " chose color: " + color);
        }

        if (first instanceof WildCard wild) {
            Player chooser = game.getPlayers().get(getNextPlayerIndex(0));
            CardColor color = chooser.chooseColor();
            wild.setColor(color);
            currentRound.getTurn().setTurnColor(color);
            System.out.println("Starting card is Wild → " + chooser.getName() + " chose color: " + color);
        }
    }

    @Override
    public void reverseDirection() {
        RoundDirection dir = currentRound.getDirection();
        currentRound.setDirection(dir == RoundDirection.LEFT ? RoundDirection.RIGHT : RoundDirection.LEFT);
        System.out.println("Direction reversed! Now: " + currentRound.getDirection());
    }

    @Override
    public void setColor(CardColor color) {
        currentRound.getTurn().setTurnColor(color);
        System.out.println("Color changed to: " + color);
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
    public void shuffleHands(Player playedBy) {
        List<Player> players = game.getPlayers();
        List<Card> pile = new ArrayList<>();
        for (Player p : players) {
            pile.addAll(p.getHand().getCards());
            p.resetHand();
        }
        Collections.shuffle(pile);

        int currentIndex = getNextPlayerIndex(players.indexOf(playedBy));
        while (!pile.isEmpty()) {
            Player p = players.get(currentIndex);
            p.getHand().addCard(pile.remove(0));
            currentIndex = getNextPlayerIndex(currentIndex);
        }
        System.out.println(playedBy.getName() + " played SHUFFLE HANDS and redistributed hands.");
    }

    private int getNextPlayerIndex(int fromIndex) {
        int size = game.getPlayers().size();
        return currentRound.getDirection() == RoundDirection.LEFT
                ? (fromIndex - 1 + size) % size
                : (fromIndex + 1) % size;
    }

    private void reshuffleFromDiscard() {
        Card top = discardPile.getTopCard();
        List<Card> toReshuffle = new ArrayList<>(discardPile.getCards());
        toReshuffle.remove(top);

        discardPile.getCards().clear();
        discardPile.addCard(top);
        drawnPile.addCards(toReshuffle);
        drawnPile.shuffle();
    }

    private void selectDealer() {
        Map<Player, Card> drawnCards = new HashMap<>();

        for (Player p : game.getPlayers()) {
            Card drawn = drawnPile.drawCard();
            drawnCards.put(p, drawn);
            System.out.println(p.getName() + " drew " + drawn);
        }

        Player dealer = null;
        int maxValue = -1;

        for (Map.Entry<Player, Card> entry : drawnCards.entrySet()) {
            int value = entry.getValue().getCardValueForDealer();
            if (value > maxValue) {
                dealer = entry.getKey();
                maxValue = value;
            }
        }

        drawnPile.addCards(new ArrayList<>(drawnCards.values()));
        drawnPile.shuffle();

        List<Player> players = game.getPlayers();
        while (!players.getFirst().equals(dealer)) {
            Player first = players.removeFirst();
            players.add(first);
        }

        System.out.println("Dealer: " + dealer.getName());
    }

    public void startGame(int handSize, Interface.ILogger logger) {
        selectDealer();
        while (!game.isGameOver()) {
            currentRound = new Round();
            game.addRound(currentRound);
            currentIndex = 0;

            resetPlayerHands();
            discardPile.clear();
            initializeDeck(DeckGenerator.generateStandardDuoDeck());
            dealCards(handSize);

            // Print players' initial hands
            System.out.println("Initial hands:");
            for (Player p : game.getPlayers()) {
                System.out.print(p.getName() + "'s hand: ");
                List<Card> cards = p.getHand().getCards();
                for (int i = 0; i < cards.size(); i++) {
                    System.out.print(cards.get(i));
                    if (i != cards.size() - 1) System.out.print(", ");
                }
                System.out.println();
            }
            System.out.println();

            runRoundLoop();

            Player roundWinner = checkRoundWinner();
            if (roundWinner != null) {
                currentRound.setRoundWinner(roundWinner);
                int points = calculateScore(roundWinner);
                roundWinner.addPoints(points);
                currentRound.setWinningPoints(points);
                System.out.println("Round End: " + roundWinner.getName() + " won the round with " + points + " points.");

                Map<Player, Integer> scores = new HashMap<>();
                for (Player p : game.getPlayers()) {
                    scores.put(p, p == roundWinner ? points : 0);
                }
                currentRound.setPlayerScores(scores);
            }

            logger.logRound(currentRound, game.getPlayers(), game.getRounds().size());
        }

        Player finalWinner = game.getPlayers().stream().filter(Player::hasWon).findFirst().orElse(null);
        if (finalWinner != null) {
            System.out.println(finalWinner.getName() + " won the game");
            System.out.println("Score: " + finalWinner.getScore());
        }

        logger.logFinalWinner(game);
    }



    private void runRoundLoop() {
        while (true) {
            Player currentPlayer = game.getPlayers().get(currentIndex);
            System.out.println("Direction: " + currentRound.getDirection());
            System.out.println(currentPlayer.getName() + "'s turn");

            if (currentRound.getTurn().isSkip()) {
                System.out.println("Turn skipped.");
                currentRound.getTurn().setSkip(false);
            } else {
                Card topCard = currentRound.getTurn().getLastPlayedCard();
                Card toPlay = currentPlayer.chooseCard(topCard);

                if (toPlay != null) {
                    currentPlayer.getHand().removeCard(toPlay);
                    discardPile.addCard(toPlay);
                    currentRound.getTurn().setLastPlayedCard(toPlay);

                    System.out.println("Play card: " + toPlay);

                    if (toPlay instanceof ActionCard actionCard) {
                        actionCard.action(this, currentPlayer);
                    }

                    if (!(toPlay instanceof WildCard)) {
                        currentRound.getTurn().setTurnColor(toPlay.getColor());
                    }

                    if (currentPlayer.isHandEmpty()) break;

                } else {
                    System.out.println("Draw card...");
                    if (drawnPile.isEmpty()) reshuffleFromDiscard();
                    Card drawn = drawnPile.drawCard();
                    if (drawn != null) {
                        currentPlayer.getHand().addCard(drawn);
                        System.out.println("Draw card: " + drawn);
                    }
                }
            }

            currentIndex = getNextPlayerIndex();
        }
    }

    private Player checkRoundWinner() {
        return game.getPlayers().stream().filter(Player::isHandEmpty).findFirst().orElse(null);
    }

    private int calculateScore(Player winner) {
        return game.getPlayers().stream()
                .filter(p -> !p.equals(winner))
                .mapToInt(p -> p.getHand().getTotalPoints())
                .sum();
    }

    private int getNextPlayerIndex() {
        int size = game.getPlayers().size();
        return currentRound.getDirection() == RoundDirection.LEFT
                ? (currentIndex - 1 + size) % size
                : (currentIndex + 1) % size;
    }

    private void resetPlayerHands() {
        for (Player p : game.getPlayers()) {
            p.resetHand();
        }
    }

    private void reshuffleDeckIfNeeded() {
        if (drawnPile.isEmpty()) reshuffleFromDiscard();
    }
}