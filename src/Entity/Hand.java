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

    @Override
    public String toString() {
        if (cards.isEmpty()) return "(empty hand)";
        StringBuilder sb = new StringBuilder();
        for (Card card : cards) {
            sb.append(card).append(", ");
        }
        return sb.substring(0, sb.length() - 2); // remove trailing comma and space
    }
}