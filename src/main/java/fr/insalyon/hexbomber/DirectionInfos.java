package fr.insalyon.hexbomber;

public class DirectionInfos {
    
    public enum SafeLevel {
        IMPOSSIBLE,
        UNKNOWN,
        SAFE
    }
    
    public int step;
    public SafeLevel safe;
    public int nbBonus;
    
    public DirectionInfos () {
        this.step = 0;
        this.safe = SafeLevel.IMPOSSIBLE;
        this.nbBonus = 0;
    }
    
    public DirectionInfos (int step, SafeLevel safe, int nbBonus) {
        this.step = step;
        this.safe = safe;
        this.nbBonus = nbBonus;
    }
    
    
    /*public static DirectionInfos getDirectionsInfos(Position position, Direction direction, int maxStep, Board board) {
        return getDirectionInfos(position, direction, 0, maxStep, board);
    }
    
    public static DirectionInfos getDirectionInfos(Position position, Direction direction, int step, int maxStep, Board board) {
        DirectionInfos  infoDir = new DirectionInfos();
        infoDir.step = 1;
        Position nextPos = position.getNeighbour(direction);
        Cell nextCell = board.getCell(nextPos);
        
        if (board.getCell(position).getType() == Type.BONUS) {
            infoDir.nbBonus ++;
        }    
        
        if (nextCell.getType() == Type.WALL || nextCell.getType() == Type.WOOD_BOX || nextCell.getType() == Type.BOMB) {
            infoDir.safe = SafeLevel.IMPOSSIBLE;
        }
        else if(!board.getDanger().containsKey(nextPos)) {
            infoDir.safe = SafeLevel.SAFE;
        }
        else if(step + 1 >= maxStep) {
            infoDir.safe = SafeLevel.UNKNOWN;
        }
        else {
            if (step + 1 == board.getDanger().get(nextPos)-1) {
                infoDir.safe = SafeLevel.IMPOSSIBLE;
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
    }*/

    @Override
    public String toString() {
        return "DirectionInfos{" + "step=" + step + ", safe=" + safe + ", nbBonus=" + nbBonus + '}';
    }
}
