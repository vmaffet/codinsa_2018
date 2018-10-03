package fr.insalyon.hexbomber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

/**
 *
 * @author paul
 */
public class SemiDefensiveBot {
    private Board board;
    
    private ArrayList<Direction> directions;
    
    private Position position;
    
    private Direction currentDirection;
    
    boolean bombingSimulation;
    
    private final static double CHANGE_DIR = 0.5;

    public SemiDefensiveBot(Board board) {
        this.board = board;
        this.directions = new ArrayList<Direction>(Arrays.asList(Direction.values()));
        Collections.shuffle(this.directions);
        
        this.bombingSimulation = false;
    }
    
    public Direction update() {
        position = board.getPlayers()[0].getPosition();
        
        if (Math.random() < CHANGE_DIR || (currentDirection != null && isCollidable(nextPosition(position, currentDirection))))
            shuffleDirections();

        if (!turnSafely(6))
            turnRandomly();
        
        for (Direction d : Direction.values()) {
            System.err.println(d + " " + board.getDanger().get(nextPosition(position, d)) + " ");
        }
        return currentDirection;
    }
    
    private void shuffleDirections() {
        if (isSafe(position)) {
            Collections.shuffle(directions);
            updateDirectionList();
        }
    }
    
    private void updateDirectionList() {
        if (currentDirection != null && !bombingSimulation) {
            Direction opposite = Direction.getOpposite(currentDirection);
            if (directions.get(directions.size()-1) != opposite) {
                directions.remove(opposite);
                directions.add(opposite);
            }
        }
    }
    
    private boolean isCollidable(Position position) {
        Type type = board.getCell(position).getType();
        return type == Type.WOOD_BOX || type == Type.WALL || type == Type.BOMB;
    }
    
    private boolean isSafe(Position target) {
        if (board.getDanger().containsKey(target))
            return false;

        return true;
    }
    
    private boolean isEmpty(Position target) {
        Type type = board.getCell(position).getType();
        return type == Type.EMPTY || type == Type.BONUS;
    }
    
    private boolean isSafeToStand() {
        return isSafe(position);
    }
    
    private static Position nextPosition(Position position, Direction direction) {
        return position.getNeighbour(direction);
    }

    private void setCurrentDirection(Direction direction) {
        currentDirection = direction;
        updateDirectionList();
    }
        
    private boolean turnSafely(int maxStep) {
        return turnSafely(maxStep, -1, null);
    }

    private boolean turnSafely(int maxStep, Direction exclude) {
        return turnSafely(maxStep, -1, exclude);
    }

    private boolean turnSafely(int maxStep, int minSafe) {
        return turnSafely(maxStep, minSafe, null);
    }

    private boolean turnSafely(int maxStep, int minSafe, Direction exclude) {
        if (maxStep <= 0)
            return false;

        DirectionInfos safest = null;
        Direction direction = null;

        for (Direction nextDirection : directions) {
            if (isCollidable(nextPosition(position, nextDirection)))
                continue;

            if (nextDirection == exclude)
                continue;

            DirectionInfos next = getDirectionsInfos(position, nextDirection, maxStep, board);
            if (isBetterThan(next, safest)) {
                safest = next;
                direction = nextDirection;
            }
        }

        if ((safest == null || safest.safe.ordinal() <= DirectionInfos.SafeLevel.UNKNOWN.ordinal() || safest.step > 1) && isSafeToStand()) {
            setCurrentDirection(null);
            System.err.println("no dir " + safest + " " + safest.safe + " " + safest.step);
            return true;
        } else if (safest != null && safest.safe.ordinal() >= minSafe) {
            System.err.println(direction);
            System.err.println(safest);
            setCurrentDirection(direction);
            return true;
        }
        else
            return false;
    }

    private void turnRandomly() {
        shuffleDirections();
        for (Direction direction : directions) {
            if (currentDirection != direction && isCollidable(nextPosition(position, direction))) {
                setCurrentDirection(direction);
                System.err.println("RANDOM TURN !");
                return;
            }
        }
    }
    
    public DirectionInfos getDirectionsInfos(Position position, Direction direction, int maxStep, Board board) {
        return getDirectionInfos(position, direction, 0, maxStep, board);
    }
    
    public DirectionInfos getDirectionInfos(Position position, Direction direction, int step, int maxStep, Board board) {
        DirectionInfos  infoDir = new DirectionInfos();
        infoDir.step = 1;
        Position nextPos = position.getNeighbour(direction);
        
        if (board.getCell(position).getType() == Type.BONUS) {
            infoDir.nbBonus ++;
        }    
        
        if (isCollidable(nextPos)) {
            infoDir.safe = DirectionInfos.SafeLevel.IMPOSSIBLE;
        }
        else if(isSafe(nextPos)) {
            infoDir.safe = DirectionInfos.SafeLevel.SAFE;
        }
        else if(step + 1 >= maxStep) {
            infoDir.safe = DirectionInfos.SafeLevel.UNKNOWN;
        }
        else {
            if (step + 1 == board.getDanger().get(nextPos)-1) {
                infoDir.safe = DirectionInfos.SafeLevel.IMPOSSIBLE;
            }
            else {
                DirectionInfos safestDir = null;
                for(Direction nextDir : Direction.values()) {
                    DirectionInfos nextInfo = getDirectionInfos(nextPos, nextDir, step+1, maxStep, board);
                    if (isBetterThan(nextInfo, safestDir))
                        safestDir = nextInfo;
                }
                
                infoDir.safe = safestDir.safe;
                infoDir.step += safestDir.step;
                infoDir.nbBonus += safestDir.nbBonus;        
            }
        }
            
        return infoDir;
    }
    
    private static boolean isBetterThan(DirectionInfos a, DirectionInfos b) {
        if (b == null)
            return true;

        if (a.safe.ordinal() > b.safe.ordinal())
            return true;
        else if (a.safe.ordinal() < b.safe.ordinal())
            return false;
        else if (a.nbBonus > b.nbBonus)
            return true;
        else if (a.nbBonus < b.nbBonus)
            return false;
        else if (a.step < b.step)
            return true;
        else if (a.step > b.step)
            return false;
        else
            return false;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Board board = new Board();
        SemiDefensiveBot bot = new SemiDefensiveBot(board);
        Inputs i = new Inputs();
        while (true) {
            i.readInputs(board);
            Direction out = bot.update();
            if (out != null)
                Outputs.print(Action.MOVE, out);
            else
                Outputs.print(Action.PASS, null);
        }
        
        /*board.setRadius(10);
        board.addBomb(new Bomb(new Position(0, 0), 6, 0, 2));
        board.computeDanger();
        for (Map.Entry<Position, Integer> p : board.getDanger().entrySet()) {
            System.out.println(p.getKey().getX() + "," + p.getKey().getY() + " : " + p.getValue());
        }*/
    }
    
}
