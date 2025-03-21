package Entity;

public class Player {
    private String name;
    private Hand hand;
    private int score;

    public Player(String name) {
        this.name = name;
        this.hand = new Hand();
        this.score = 0;
    }

    public String getName() {
        return name;
    }

    public Hand getHand() {
        return hand;
    }

    public int getScore() {
        return score;
    }

    public void addPoints(int points) {
        this.score += points;
    }

    public boolean hasWon() {
        return score >= 500;
    }

    public boolean isHandEmpty() {
        return hand.isEmpty();
    }

    public void resetHand() {
        this.hand = new Hand();
    }

    public CardColor chooseColor() {
        return CardPlayer.chooseMostCommonColor(this.hand);
    }

    public Card chooseCard(Card topCard) {
        return CardPlayer.choosePlayableCard(this.hand, topCard);
    }
}