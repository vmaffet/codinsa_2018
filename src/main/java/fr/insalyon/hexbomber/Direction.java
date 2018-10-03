package fr.insalyon.hexbomber;

/**
 *
 * @author paul
 */
public enum Direction {
    UP("UP"),
    UP_LEFT("UPLEFT"),
    UP_RIGHT("UPRIGHT"),
    DOWN("DOWN"),
    DOWN_LEFT("DOWNLEFT"),
    DOWN_RIGHT("DOWNRIGHT");
    
    private final String name;

    private Direction(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
    
    public static Direction getOpposite(Direction direction) {
        switch (direction) {
            case UP:
                return DOWN;
            case UP_LEFT:
                return DOWN_RIGHT;
            case UP_RIGHT:
                return DOWN_LEFT;
            case DOWN:
                return UP;
            case DOWN_LEFT:
                return UP_RIGHT;
            case DOWN_RIGHT:
                return UP_LEFT;
            default:
                throw new AssertionError(direction.name());
        }
    }

}
