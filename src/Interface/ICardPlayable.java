package Interface;

import Entity.*;

public interface ICardPlayable {
    Card drawCard(DiscardPile discardPile, DrawnPile drawnPile);
    boolean canPlayCard(Card cardToPlay, Card lastPlayedCard);
}