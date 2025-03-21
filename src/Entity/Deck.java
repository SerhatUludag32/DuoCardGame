package Entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    protected List<Card> cards;

    public Deck() {
        this.cards = new ArrayList<>();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card draw() {
        if (!cards.isEmpty()) {
            return cards.removeFirst();
        }
        return null;
    }


    public void addCard(Card card) {
        cards.add(card);
    }

    public void addCards(List<Card> newCards) {
        cards.addAll(newCards);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public List<Card> getCards() {
        return cards;
    }
}