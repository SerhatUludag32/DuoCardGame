package Entity;

import java.util.HashMap;
import java.util.Map;

public class Round {
    private Turn turn;
    private RoundDirection direction;
    private Player roundWinner;
    private int winningPoints;
    private Map<Player, Integer> playerScores;

    public Round() {
        this.turn = new Turn();
        this.direction = RoundDirection.LEFT;
        this.playerScores = new HashMap<>();
    }

    public Turn getTurn() {
        return turn;
    }

    public RoundDirection getDirection() {
        return direction;
    }

    public void setDirection(RoundDirection direction) {
        this.direction = direction;
    }

    public Player getRoundWinner() {
        return roundWinner;
    }

    public void setRoundWinner(Player winner) {
        this.roundWinner = winner;
    }

    public int getWinningPoints() {
        return winningPoints;
    }

    public void setWinningPoints(int points) {
        this.winningPoints = points;
    }

    public Map<Player, Integer> getPlayerScores() {
        return playerScores;
    }

    public void setPlayerScores(Map<Player, Integer> scores) {
        this.playerScores = scores;
    }
}