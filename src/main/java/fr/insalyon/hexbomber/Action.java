package fr.insalyon.hexbomber;

/**
 *
 * @author paul
 */
public enum Action {
    MOVE("MOVE"),
    BOMB_MOVE("BOMB_MOVE"),
    PASS("PASS");
    
    private final String name;

    private Action(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
