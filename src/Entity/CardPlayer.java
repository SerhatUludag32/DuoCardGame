package Entity;

import java.util.*;

public class CardPlayer {
    public static Card choosePlayableCard(Hand hand, Card topCard) {
        List<Card> playable = new ArrayList<>();
        List<NumberCard> numberCards = new ArrayList<>();
        List<Card> otherPlayable = new ArrayList<>();
        WildDrawFourCard wildDrawFour = null;

        CardColor currentColor = topCard.getColor();

        for (Card card : hand.getCards()) {
            if (card.canBePlayedOn(topCard)) {
                playable.add(card);

                if (card instanceof NumberCard numberCard) {
                    numberCards.add(numberCard);
                } else if (card instanceof WildDrawFourCard wdf) {
                    wildDrawFour = wdf; // only one WildDrawFour expected
                } else {
                    otherPlayable.add(card);
                }
            }
        }

        if (playable.isEmpty()) return null;

        // 1️⃣ If any NumberCard playable, return highest
        if (!numberCards.isEmpty()) {
            return numberCards.stream()
                    .max(Comparator.comparingInt(NumberCard::getNumber))
                    .orElse(numberCards.getFirst());
        }

        // 2️⃣ If color-matching cards exist, WildDrawFour is NOT allowed
        boolean hasColorMatch = hand.getCards().stream()
                .anyMatch(card -> card.getColor() == currentColor && card.canBePlayedOn(topCard) && !(card instanceof WildDrawFourCard));

        if (!hasColorMatch && wildDrawFour != null) {
            return wildDrawFour;
        }

        // 3️⃣ Pick randomly among other action/wild cards
        if (!otherPlayable.isEmpty()) {
            int index = (int) (Math.random() * otherPlayable.size());
            return otherPlayable.get(index);
        }

        // 4️⃣ If nothing else, fallback to WildDrawFour (even if color match, just to not return null)
        return wildDrawFour;
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