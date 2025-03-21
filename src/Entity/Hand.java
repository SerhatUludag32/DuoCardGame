package Entity;

public class Hand extends Deck {
    public Hand() {
        super();
    }

    public int getTotalPoints() {
        return cards.stream().mapToInt(Card::getValue).sum();
    }

    public void removeCard(Card card) {
        cards.remove(card);
    }

    public int size() {
        return cards.size();
    }
}