package Entity;

public class DiscardPile extends Deck {
    public DiscardPile() {
        super();
    }

    public Card getTopCard() {
        if (!cards.isEmpty()) {
            return cards.getLast();
        }
        return null;
    }
}