package Interface;

import Entity.Player;

public interface IActionable {
    void action(GameMediator mediator, Player currentPlayer);
}