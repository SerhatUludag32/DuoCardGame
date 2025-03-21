package Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardPlayer {
    public static Card choosePlayableCard(Hand hand, Card topCard) {
        for (Card card : hand.getCards()) {
            if (card.canBePlayedOn(topCard)) {
                return card;
            }
        }
        return null;
    }

    public static CardColor chooseMostCommonColor(Hand hand) {
        Map<CardColor, Integer> freq = new HashMap<>();

        for (Card card : hand.getCards()) {
            CardColor color = card.getColor();
            if (color == null || color == CardColor.NONE) continue;

            freq.put(color, freq.getOrDefault(color, 0) + 1);
        }

        List<CardColor> maxColors = new ArrayList<>();
        int max = 0;

        for (Map.Entry<CardColor, Integer> entry : freq.entrySet()) {
            int count = entry.getValue();
            if (count > max) {
                max = count;
                maxColors.clear();
                maxColors.add(entry.getKey());
            } else if (count == max) {
                maxColors.add(entry.getKey());
            }
        }

        // Pick randomly among tied max colors
        if (!maxColors.isEmpty()) {
            int index = (int) (Math.random() * maxColors.size());
            return maxColors.get(index);
        }

        return CardColor.RED; // fallback default
    }
}