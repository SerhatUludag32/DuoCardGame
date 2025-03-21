package Interface;

import Entity.Game;
import Entity.Player;
import Entity.Round;

import java.util.List;

public interface ILogger {
    void logFinalWinner(Game game);
    void logRound(Round round, List<Player> players, int roundNumber);
}
