package Business;
import Entity.*;
import java.util.*;

public class DeckGenerator {
    public static List<Card> generateStandardDuoDeck() {
        List<Card> cards = new ArrayList<>();

        for (CardColor color : CardColor.values()) {
            if (color == CardColor.NONE) continue;

            cards.add(new NumberCard(0, color));
            for (int i = 1; i <= 9; i++) {
                cards.add(new NumberCard(i, color));
                cards.add(new NumberCard(i, color));
            }

            cards.add(new SkipCard(color));
            cards.add(new SkipCard(color));
            cards.add(new ReverseCard(color));
            cards.add(new ReverseCard(color));
            cards.add(new DrawTwoCard(color));
            cards.add(new DrawTwoCard(color));
        }

        for (int i = 0; i < 4; i++) {
            cards.add(new WildCard());
            cards.add(new WildDrawFourCard());
            cards.add(new ShuffleHandsCard());
        }

        return cards;
    }
}