package stubailey18.diceblackjackback;

public class Player {

    private int id;
    private String name;
    private int gamesPlayed;
    private int points;

    private static int nextId = 1;

    public Player(String name) {
        this.id = nextId++;
        this.name = name;
        this.gamesPlayed = 0;
        this.points = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
