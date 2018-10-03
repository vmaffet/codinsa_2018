package fr.insalyon.hexbomber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Board {
    private int radius;
    
    //size +2 for the walls at the limits
    private int sizeBoard;
    private int round;
    private Cell[][] board;
    
    private Player[] players;
    private List<Bomb> bombs;
    private List<Bonus> bonuses;
    private HashMap<Position,Integer> danger;

    public Board(){
        radius = 0;
        players = new Player[2];
        bombs = new ArrayList<>();
        bonuses = new LinkedList<>();
        danger=new HashMap<Position,Integer>();
    }

    public void setRadius(int r){
        radius = r+1;
        sizeBoard = radius*2+1;
        reset();
    }

    private void placeLimitWalls(){
        if (radius == 0)
            return;
        
        //limits of the board

        for(int i=-radius;i<=radius;i++){
            addWall(new Position(radius,i));
            addWall(new Position(i,radius));
            addWall(new Position(-radius,i));
            addWall(new Position(i,-radius));
        }
        for(int i=0;i<radius;i++){
            addWall(new Position(radius-i,i));
            addWall(new Position(-radius+i,-i));
        }
    }

    private void assignCell(Position p, Type type){
        board[p.getX()+radius][p.getY()+radius] = new Cell(type);
    }
    
    private void assignCell(Entity entity){
        board[entity.getX()+radius][entity.getY()+radius] = new Cell(entity.getType(), entity);
    }

    public Cell getCell(Position p){
        return board[p.getX()+radius][p.getY()+radius];
    }

    public void setPlayer(int i, Player player) {
        this.players[i] = player;
        assignCell(player);
    }
    public void setMyselfFirst(int id) {
        Player buffer;
        if(players[0].getID()!=id) {
            buffer=players[0];
            players[0]=players[1];
            players[1]=buffer;
        }
    }
    
    public void addBomb(Bomb bomb) {
        this.bombs.add(bomb);
        assignCell(bomb);
    }
    
    public void addBonus(Bonus bonus) {
        this.bonuses.add(bonus);
        assignCell(bonus);
    }
    
    public void addWall(Position position) {
        assignCell(position, Type.WALL);
    }
    
    public void addWoodBox(Position position) {
        assignCell(position, Type.WOOD_BOX);
    }

    public int getRadius() {
        return radius;
    }

    public int getSizeBoard() {
        return sizeBoard;
    }

    public Player[] getPlayers() {
        return players;
    }

    public List<Bomb> getBombs() {
        return bombs;
    }

    public List<Bonus> getBonuses() {
        return bonuses;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int r){
        this.round = r;
    }
    
    public void reset() {
        players = new Player[2];
        bombs.clear();
        bonuses.clear();
        board = new Cell[sizeBoard][sizeBoard];
        for (int i = 0; i < sizeBoard; i++) {
            for (int j = 0; j < sizeBoard; j++) {
                board[i][j] = new Cell(Type.EMPTY);
            }
        }
        placeLimitWalls();
    }
    
    public void updateTimerBombs() {
        for (Bomb b : bombs) {
            b.computeTargetted(this);
        }
        
        boolean changed = true;
        while (changed) {
            changed = false;
            for (Bomb bRef : bombs) {
                Set<Position> sp = bRef.getTargetted().keySet();

                for (Bomb bComp : bombs) {
                    if (sp.contains(bComp.getPosition()) && bRef.getTimer() < bComp.getTimer()) {
                        bComp.setTimer(bRef.getTimer());
                        changed = true;
                    }
                }
            }
        }

        //on recalcule un efois que tout les changments sont faits
        for (Bomb b : bombs) {
            b.computeTargetted(this);
        }
    }
    
    public void computeDanger() {
        danger = new HashMap<>();
        updateTimerBombs();
        for (Bomb b : bombs) {
            for (Map.Entry<Position, Integer> p : b.getTargetted().entrySet()) {
                Integer currentVal = danger.putIfAbsent(p.getKey(), p.getValue());
                if (currentVal != null) {
                    //Il n'était pas absent
                    if (currentVal > p.getValue()) {
                        danger.put(p.getKey(),p.getValue());
                    }
                }
            }
        }
    }
    
    public HashMap<Position,Integer> getDanger(){
        return this.danger;
    }
    
    public static void main(String args[] ){

    }
}
