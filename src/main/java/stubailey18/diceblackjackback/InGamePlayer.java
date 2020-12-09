package stubailey18.diceblackjackback;

public class InGamePlayer {

    private Player player;
    private int die1Value;
    private int die2Value;
    private int numRolls;
    private boolean standing;
    private int total;

    public InGamePlayer(Player player) {
        this.player = player;
        this.die1Value = 0;
        this.die2Value = 0;
        this.numRolls = 0;
        this.standing = false;
        this.total = 0;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Integer getDie1Value() {
        return die1Value;
    }

    public void setDie1Value(int die1Value) {
        this.die1Value = die1Value;
    }

    public Integer getDie2Value() {
        return die2Value;
    }

    public void setDie2Value(int die2Value) {
        this.die2Value = die2Value;
    }

    public int getNumRolls() {
        return numRolls;
    }

    public void setNumRolls(int numRolls) {
        this.numRolls = numRolls;
    }

    public boolean isStanding() {
        return standing;
    }

    public void setStanding(boolean standing) {
        this.standing = standing;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
