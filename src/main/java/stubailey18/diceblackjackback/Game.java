package stubailey18.diceblackjackback;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Game {

    private List<InGamePlayer> inGamePlayers;

    public Game() {
        this.inGamePlayers = new LinkedList<>();
    }

    public void addPlayer(String name) {
        inGamePlayers.add(new InGamePlayer(new Player(name)));
    }

    public void hit(int playerId) {
        inGamePlayers
                .stream()
                .filter(inGamePlayer -> inGamePlayer.getPlayer().getId() == playerId)
                .findFirst()
                .ifPresent(inGamePlayer -> {
                    if (inGamePlayer.getTotal() < 16) {
                        inGamePlayer.setDie1Value((int) Math.floor(Math.random() * 6 + 1));
                        inGamePlayer.setDie2Value((int) Math.floor(Math.random() * 6 + 1));
                    } else {
                        inGamePlayer.setDie1Value((int) Math.floor(Math.random() * 6 + 1));
                        inGamePlayer.setDie2Value(0);
                    }
                    var sumOfDieValues = inGamePlayer.getDie1Value() + inGamePlayer.getDie2Value();
                    inGamePlayer.setTotal(inGamePlayer.getTotal() + sumOfDieValues);
                    inGamePlayer.setNumRolls(inGamePlayer.getNumRolls() + 1);
                });
        if (isOver()) {
            incrementEachPlayersGamesPlayed();
            computeAndAssignPoints();
        }
    }

    public void stand(int playerId) {
        inGamePlayers
                .stream()
                .filter(inGamePlayer -> inGamePlayer.getPlayer().getId() == playerId)
                .findFirst()
                .ifPresent(inGamePlayer -> inGamePlayer.setStanding(true));
        if (isOver()) {
            incrementEachPlayersGamesPlayed();
            computeAndAssignPoints();
        }
    }

    public void computeAndAssignPoints() {
        List<Player> playersWithTotalOf21 = findPlayersWithTotalOf21();
        if (playersWithTotalOf21.size() > 0) {
            playersWithTotalOf21.forEach(player -> player.setPoints(player.getPoints() + 2));
        } else {
            List<Player> playersWithMaxTotalLessThan21 = findPlayersWithMaxTotalLessThan21();
            if (playersWithMaxTotalLessThan21.size() == 1) {
                playersWithMaxTotalLessThan21.forEach(player -> player.setPoints(player.getPoints() + 1));
            }
        }
    }

    public void reset() {
        inGamePlayers.forEach(inGamePlayer -> {
            inGamePlayer.setDie1Value(0);
            inGamePlayer.setDie2Value(0);
            inGamePlayer.setNumRolls(0);
            inGamePlayer.setStanding(false);
            inGamePlayer.setTotal(0);
        });
    }

    private boolean isOver() {
        return inGamePlayers.stream().allMatch(inGamePlayer ->
                inGamePlayer.getTotal() > 21 || inGamePlayer.isStanding());
    }

    private void incrementEachPlayersGamesPlayed() {
        inGamePlayers
                .stream()
                .map(inGamePlayer -> inGamePlayer.getPlayer())
                .forEach(player -> player.setGamesPlayed(player.getGamesPlayed() + 1));
    }

    private List<Player> findPlayersWithTotalOf21() {
        return inGamePlayers
                .stream()
                .filter(inGamePlayer -> inGamePlayer.getTotal() == 21)
                .map(inGamePlayer -> inGamePlayer.getPlayer())
                .collect(Collectors.toList());
    }

    private int findMaxTotalLessThan21() {
        return inGamePlayers
                .stream()
                .filter(inGamePlayer -> inGamePlayer.getTotal() < 21)
                .mapToInt(inGamePlayer -> inGamePlayer.getTotal())
                .max()
                .getAsInt();
    }

    private List<Player> findPlayersWithMaxTotalLessThan21() {
        int maxTotalLessThan21 = findMaxTotalLessThan21();
        return inGamePlayers
                .stream()
                .filter(inGamePlayer -> inGamePlayer.getTotal() == maxTotalLessThan21)
                .map(inGamePlayer -> inGamePlayer.getPlayer())
                .collect(Collectors.toList());
    }

    public List<InGamePlayer> getInGamePlayers() {
        return inGamePlayers;
    }
}