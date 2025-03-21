package Entity;

public class DrawnPile extends Deck {
    public DrawnPile() {
        super();
    }

    // Draw 1 card by default
    public Card drawCard() {
        return draw();
    }
}