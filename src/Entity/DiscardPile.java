package Entity;

public class DiscardPile extends Deck {
    public DiscardPile() {
        super();
    }

    public Card getTopCard() {
        if (!getCards().isEmpty()) {
            return getCards().getLast();
        }
        return null;
    }

    public void clear() {
        getCards().clear();
    }
}