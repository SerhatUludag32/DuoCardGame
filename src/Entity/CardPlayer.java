package Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardPlayer {
    public static Card choosePlayableCard(Hand hand, Card topCard) {
        List<Card> playable = new ArrayList<>();

        for (Card card : hand.getCards()) {
            if (card.canBePlayedOn(topCard)) {
                playable.add(card);
            }
        }

        if (playable.isEmpty()) return null;

        // Try to find the highest-numbered NumberCard
        Card best = null;
        int max = -1;
        for (Card card : playable) {
            if (card instanceof NumberCard numberCard) {
                if (numberCard.getNumber() > max) {
                    max = numberCard.getNumber();
                    best = numberCard;
                }
            }
        }

        if (best != null) return best;

        // Fallback: just return the first playable action/wild card
        return playable.get(0);
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