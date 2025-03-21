package Entity;


import Interface.IActionable;

public abstract class ActionCard extends Card implements IActionable {
    public ActionCard(int value, CardColor color) {
        super(value, color);
    }

    @Override
    public abstract void action(Interface.GameMediator mediator, Player currentPlayer);


}