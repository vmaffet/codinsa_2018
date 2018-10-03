package fr.insalyon.hexbomber;

/**
 *
 * @author paul
 */
public final class Outputs {

    private Outputs() {
    }
    
    public static void print(Action action, Direction direction) {
        System.out.println(action + " " + direction);
    }
    
    public static void main(String args[]) {
        print(Action.BOMB_MOVE, Direction.DOWN);
        print(Action.PASS, null);
    }
}
