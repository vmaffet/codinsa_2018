package fr.insalyon.hexbomber;

import java.util.*;

public class BomberBotLyon {
        
    private static final int LinkedList = 0;
    static Position prev;
    
    public static void main(String[] args) {
        Inputs i= new Inputs();
        Board board = new Board();
        int turn= 0;
        while(true) {
            i.readInputs(board);
            //randomPlay();
            //mimique(board.getPlayers(), board.getBombs());
            if (turn <= 18) {
                getStartBonus(turn, board.getPlayers()[0].getPosition());
                turn++;
            } else {
               board.addWall(board.getPlayers()[0].getPosition());
               if (hunter(board, board.getPlayers()[1].getPosition(), new Position(0, 7)).equals("PASS")) {
                   System.out.println(hunter(board, board.getPlayers()[0].getPosition(), new Position(0, 7)).equals("PASS")?"PASS":"BOMB_"+hunter(board, board.getPlayers()[0].getPosition(), new Position(0, 7)));
               } else {
                   System.err.println("fait");
                   System.out.println(hunter(board, board.getPlayers()[0].getPosition(), board.getPlayers()[1].getPosition())); 
               }
            }
        }
    }
    
    public static boolean culDeSac(Board board) {
        Position enemy = board.getPlayers()[1].getPosition();
        Position me = board.getPlayers()[0].getPosition();
        int dX = me.getX() - enemy.getX();
        int dY = me.getY() - enemy.getY();
        if ((dX == 0 && dY != 0) || (dX != 0 && dY == 0) || (dX == -dY)) {
            return hunter(board, enemy, new Position(me.getX() + dX / Math.max(Math.abs(dX), Math.abs(dY)),
                    me.getY() + dY / Math.max(Math.abs(dX), Math.abs(dY)))).equals("PASS");
        } else {
            return false;
        }
    }
    
    public static void getStartBonus(int turn, Position player) {
        String UPforblue= player.getX() > 0 ? "DOWN":"UP";
        String DOWNforblue= player.getX() > 0 ? "UP":"DOWN";
        switch(turn) {
            case 0:
            case 1:
            case 4:
            case 14:
            case 17:
            case 18:
                System.out.println("MOVE "+UPforblue+"LEFT");
                break;
            case 2:
            case 5:
                System.out.println("MOVE "+UPforblue);
                break;
            case 3:
                System.out.println("BOMB_MOVE "+DOWNforblue+"LEFT");
                break;
            case 10:
                System.out.println("BOMB_MOVE "+DOWNforblue);
                break;
            case 6:
            case 7:
            case 8:
            case 9:
            case 15:
            case 16:
                System.out.println("PASS");
                break;
            case 11:
                System.out.println("MOVE "+DOWNforblue+"RIGHT");
                break;
            case 12:
            case 13:
                System.out.println("MOVE "+UPforblue+"RIGHT");
                break;
        }
    }
    
    private static void mimique(Player[] players, List<Bomb> bombs) {
        int myID = players[0].getID();
        
        if (prev == null) {
            prev= players[1].getPosition();
            System.out.println("PASS");
            return;
        }
        int dx= players[1].getPosition().getX()-prev.getX();
        int dy= players[1].getPosition().getY()-prev.getY();
        prev= players[1].getPosition();
        String s="";
        switch(dx) {
            case 1:
                switch(dy) {
                case 0:
                    s= "MOVE DOWNLEFT";
                    break;
                case -1:
                    s= "MOVE DOWN";
                    break;
                }
                break;
            case 0:
                switch(dy) {
                case 1:
                    s= "MOVE UPLEFT";
                    break;
                case 0:
                    s= "PASS";
                    break;
                case -1:
                    s= "MOVE DOWNRIGHT";
                    break;
                }
                break;
            case -1:
                switch(dy) {
                case 1:
                    s= "MOVE UP";
                    break;
                case 0:
                    s= "MOVE UPRIGHT";
                    break;
                }
                break;
        }
        for (int i= 0; i<bombs.size(); i++) {
          if (bombs.get(i).getIdOwner() != myID && bombs.get(i).getTimer() == 6) {
            s= "BOMB_"+s;
            break;
          }  
        }
        
        System.out.println(s);
    }

    public static void randomPlay() {
        int rnd;
        String s= "";
        rnd= (int)(Math.random()*1);
        switch(rnd) {
            case 0:
                s= "MOVE";
                break;
            case 1:
                s= "BOMB_MOVE";
                break;
            case 2:
                s= "PASS";
                break;
        }
        rnd= (int)(Math.random()*6);
        switch(rnd) {
            case 0:
                s+= " UP";
                break;
            case 1:
                s+= " UPLEFT";
                break;
            case 2:
                s+= " UPRIGHT";
                break;
            case 3:
                s+= " DOWN";
                break;
            case 4:
                s+= " DOWNLEFT";
                break;
            case 5:
                s+= " DOWNRIGHT";
                break;
        }
        System.out.println(s);
    }
    
    public static String hunter(Board board, Position start, Position goal) {        
        ArrayList<Position> open= new ArrayList<Position>();
        ArrayList<Position> closed= new ArrayList<Position>();
        
        open.add(start);
        
        HashMap<Position, Position> cameFrom= new HashMap<Position, Position>();
        
        HashMap<Position, Integer> gScore= new HashMap<Position, Integer>();
        gScore.put(start, 0);
        
        HashMap<Position, Integer> fScore= new HashMap<Position, Integer>();
        fScore.put(start, start.distance(goal));
        
        Position current, neighbor;
        int tentativeScore;
        while (!open.isEmpty()) {
            current= open.get(open.size()-1);
            if (current.equals(goal)) {
                return reconstruct_path(cameFrom, current, start, board);
            }
            
            open.remove(open.size()-1);
            closed.add(current);
            
            for (Direction dir : Direction.values()) {
                neighbor= current.getNeighbour(dir);
                
                if (!board.getCell(neighbor).getType().equals(Type.EMPTY) && !board.getCell(neighbor).getType().equals(Type.BONUS) && !board.getCell(neighbor).getType().equals(Type.PLAYER)) {
                    closed.add(neighbor);
                    continue;
                }
                
                if (closed.contains(neighbor)) {
                    continue;
                }
                
                if (!open.contains(neighbor)) {
                    open.add(neighbor);
                }
                
                tentativeScore= gScore.get(current)+1;
                if (gScore.get(neighbor) != null && tentativeScore >= gScore.get(neighbor)) {
                    continue;
                }
                
                cameFrom.put(neighbor, current);
                gScore.put(neighbor, tentativeScore);
                fScore.put(neighbor, gScore.get(neighbor)+neighbor.distance(goal));
                
            }
            
            Collections.sort(open, new Comparator<Position>(){
                public int compare(Position pos0, Position pos1) {
                    return fScore.get(pos1) - fScore.get(pos0);
                }
            });
        }
        System.err.println("nopath");
        return getSafe(board);
    }

    public static String reconstruct_path(HashMap<Position, Position> cameFrom, Position current, Position start, Board b) {
        Position previous= null;
        LinkedList<Position> route= new LinkedList<Position>();
        route.push(current);
        while (cameFrom.containsKey(current)) {
            previous= cameFrom.get(current);
            route.push(previous);
            if (previous.equals(start)) break;
            current= cameFrom.get(current);
        }
        b.computeDanger();
        if (dangerousRoute(b, route)) {
            System.err.println("DANGER "+current.getX()+" "+current.getY());
            return getSafe(b);
        }
        int dx= start.getX()-current.getX();
        int dy= start.getY()-current.getY();
        String s= null;
        switch(dx) {
            case 1:
                switch(dy) {
                    case 0:
                        s= "MOVE DOWNLEFT";
                        break;
                    case -1:
                        s= "MOVE DOWN";
                        break;
                }
                break;
            case 0:
                switch(dy) {
                    case 1:
                        s= "MOVE UPLEFT";
                        break;
                    case 0:
                        s= "PASS";
                        System.err.println("ici");
                        break;
                    case -1:
                        s= "MOVE DOWNRIGHT";
                        break;
                }
                break;
            case -1:
                switch(dy) {
                    case 1:
                        s= "MOVE UP";
                        break;
                    case 0:
                        s= "MOVE UPRIGHT";
                        break;
                }
                break;
        }
        return s;
    }
    
    public static boolean dangerousRoute(Board board, LinkedList<Position> route) {
        Position test;
        board.computeDanger();
        HashMap<Position, Integer> danger= board.getDanger();
        for (int i= 0; i<4; i++) {
            test= route.poll();
            if (danger.get(test) != null && danger.get(test) == i) {
                return true;
            }
            if (route.isEmpty()) break;
        }
        return false;
    }
    
    public static String getSafe(Board board) {
        System.err.println("Getting safe");
        if (board.getDanger().containsKey(board.getPlayers()[0].getPosition())) {
            String ret= turnSafely(6, board);
            return ret.equals("PASS")?"PASS":"MOVE "+ret;
        }
        return "PASS";
    }
    
    private static String turnSafely(int maxStep, Board b) {
        return turnSafely(maxStep, -1, null, b);
    }
    
    private static Position nextPosition(Position position, Direction direction) {
        return position.getNeighbour(direction);
    }
    
    private static boolean isCollidable(Position position, Board board) {
        Type type = board.getCell(position).getType();
        return type == Type.WOOD_BOX || type == Type.WALL || type == Type.BOMB;
    }
    
    private static String turnSafely(int maxStep, int minSafe, Direction exclude, Board board) {
        if (maxStep <= 0)
            return "PASS";

        DirectionInfos safest = null;
        Direction direction = null;

        for (Direction nextDirection : Direction.values()) {
            if (isCollidable(nextPosition(board.getPlayers()[0].getPosition(), nextDirection), board))
                continue;

            if (nextDirection == exclude)
                continue;

            DirectionInfos next = getDirectionsInfos(board.getPlayers()[0].getPosition(), nextDirection, maxStep, board);
            if (isBetterThan(next, safest)) {
                safest = next;
                direction = nextDirection;
            }
        }

        if ((safest == null || safest.safe.ordinal() <= DirectionInfos.SafeLevel.UNKNOWN.ordinal() || safest.step > 1) && isSafeToStand(board.getPlayers()[0].getPosition(), board)) {
            return "PASS";
        } else if (safest != null && safest.safe.ordinal() >= minSafe) {
            return ""+direction;
        }
        else
            return "PASS";
    }
    
    public static DirectionInfos getDirectionsInfos(Position position, Direction direction, int maxStep, Board board) {
        return getDirectionInfos(position, direction, 0, maxStep, board);
    }
    
    public static DirectionInfos getDirectionInfos(Position position, Direction direction, int step, int maxStep, Board board) {
        DirectionInfos  infoDir = new DirectionInfos();
        infoDir.step = 1;
        Position nextPos = position.getNeighbour(direction);
        
        if (board.getCell(position).getType() == Type.BONUS) {
            infoDir.nbBonus ++;
        }    
        
        if (isCollidable(nextPos, board)) {
            infoDir.safe = DirectionInfos.SafeLevel.IMPOSSIBLE;
        }
        else if(isSafe(nextPos, board)) {
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
    
    private static boolean isSafeToStand(Position pos, Board board) {
        return isSafe(pos, board);
    }
    
    private static boolean isSafe(Position target, Board board) {
        if (board.getDanger().containsKey(target))
            return false;

        return true;
    }
    
    
}
