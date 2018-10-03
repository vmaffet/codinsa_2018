package fr.insalyon.hexbomber;

import java.util.HashMap;

/**
*
* @author Romain
*/

public class Bomb extends Entity {
    private int timer = 0;
    private int idOwner = 0;
    private int range = 0;
    private HashMap<Position, Integer> targetted=new HashMap<Position,Integer>();
    Bomb(Position position, int timer, int idOwner, int range){
        super(position);
        this.setTimer(timer);
        this.setIdOwner(idOwner);
        this.setRange(range);
    }

    @Override
    public Type getType() {
        return Type.BOMB;
    }
    
    public int getTimer() {
        return this.timer;
    }
    
    public void setTimer(int timer) {
        this.timer = timer;
    }
    
    public int getIdOwner() {
        return this.idOwner;
    }
    
    public void setIdOwner(int idOwner) {
        this.idOwner = idOwner;
    }
    
    public int getRange() {
        return this.range;
    }
    
    public void setRange(int range) {
        this.range = range;
    }
    
    public void computeTargetted(Board carte) {
        targetted.put(this.position, this.timer);
        for(Direction d: Direction.values()) {
            boolean block=false;
            int dist=0;
            Position current=this.getPosition();
            while(!block && dist<this.range) {
                Type typeOfCell=carte.getCell(current.getNeighbour(d)).getType();
                if(typeOfCell==Type.EMPTY || typeOfCell==Type.BONUS || typeOfCell==Type.PLAYER || typeOfCell == Type.BOMB) {
                    current=current.getNeighbour(d);
                    targetted.put(current,this.timer);
                    dist+=1;
                }
                else {block=true;}
            }
        }
    }
    public HashMap<Position,Integer> getTargetted(){
        return this.targetted;}
}
