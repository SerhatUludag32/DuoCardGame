package Data;

import Entity.Game;
import Entity.Round;
import Entity.Player;
import Interface.ILogger;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class CSVLogger implements ILogger {
    private final String filePath;
    private boolean isHeaderWritten = false;

    private List<String> playerNames;



    public CSVLogger(String filePath) {
        this.filePath = filePath;
    }

    public void setPlayerOrder(List<Player> players) {
        playerNames = players.stream().map(Player::getName).toList();
    }

    @Override
    public void logRound(Round round, List<Player> currentPlayers, int roundNumber) {
        try (FileWriter writer = new FileWriter(filePath, true)) {
            if (!isHeaderWritten) {
                writer.write("Round");
                for (String name : playerNames) {
                    writer.write(", " + name);
                }
                writer.write("\n");
                isHeaderWritten = true;
            }

            writer.write("Round " + roundNumber);
            Map<Player, Integer> scores = round.getPlayerScores();
            for (String name : playerNames) {
                Player match = currentPlayers.stream()
                        .filter(p -> p.getName().equals(name))
                        .findFirst()
                        .orElse(null);

                int points = match != null ? scores.getOrDefault(match, 0) : 0;
                writer.write(", " + points);
            }
            writer.write("\n");

        } catch (IOException e) {
            System.out.println("Failed to log round: " + e.getMessage());
        }
    }

    public void logFinalWinner(Game game) {
        try (FileWriter writer = new FileWriter(filePath, true)) {
            Player winner = game.getPlayers().stream()
                    .max(Comparator.comparingInt(Player::getScore))
                    .orElse(null);

            if (winner != null) {
                writer.write("Winner, " + winner.getName() + "\n");
            }

        } catch (IOException e) {
            System.out.println("Failed to log winner: " + e.getMessage());
        }
    }
}

