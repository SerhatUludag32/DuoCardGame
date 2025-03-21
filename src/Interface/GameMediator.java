package Interface;

import Entity.*;

public interface GameMediator {
    void drawCards(Player player, int count);
    void skipNextPlayer();
    void reverseDirection();
    void setColor(CardColor color);
    Player getNextPlayer(Player currentPlayer);
    void shuffleHands();
    Round getCurrentRound();
    Turn getCurrentTurn();
}