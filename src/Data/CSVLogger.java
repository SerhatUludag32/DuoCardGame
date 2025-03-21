package Data;

import Entity.Game;
import Entity.Round;
import Entity.Player;
import Interface.ILogger;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVLogger implements ILogger {
    private final String filePath;
    private boolean isHeaderWritten = false;

    public CSVLogger(String filePath) {
        this.filePath = filePath;
    }

    public void logRound(Round round, List<Player> players, int roundNumber) {
        try (FileWriter writer = new FileWriter(filePath, true)) {
            // Write header only once
            if (!isHeaderWritten) {
                writer.write("Round");
                for (int i = 1; i <= players.size(); i++) {
                    writer.write(", Player " + i);
                }
                writer.write("\n");
                isHeaderWritten = true;
            }

            // Write round data
            writer.write("Round " + roundNumber);
            for (Player p : players) {
                writer.write(", " + round.getPlayerScores().getOrDefault(p, 0));
            }
            writer.write("\n");

        } catch (IOException e) {
            System.out.println("Failed to log round: " + e.getMessage());
        }
    }

    public void logFinalWinner(Game game) {
        try (FileWriter writer = new FileWriter(filePath, true)) {
            Round last = !game.getRounds().isEmpty() ? game.getRounds().get(game.getRounds().size() - 1) : null;
            if (last != null && last.getRoundWinner() != null) {
                writer.write("Winner, " + last.getRoundWinner().getName() + "\n");
            } else {
                writer.write("Winner, NOT FOUND (Check logic)\n");
            }
        } catch (IOException e) {
            System.out.println("Failed to log winner: " + e.getMessage());
        }
    }
}

/*

CSVLogger logger = new CSVLogger("game_log.csv");

public void playRound(Game game, Round round) {
    // Existing logic to play round...

    // Log round immediately after it finishes
    logger.logRound(round, game.getPlayerArray(), game.getRounds().size());

    // Check if the game is over, log the final winner
    if (game.isGameOver()) {
        logger.logFinalWinner(game);
    }
}
 */