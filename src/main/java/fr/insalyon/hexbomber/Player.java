package fr.insalyon.hexbomber;

import java.util.HashSet;

/**
 * 
 * @author Vincent
 *
 */
public class Player extends Entity {
    
    private int ID;
    private String name;

    private int numberOfBombs;
    private int bombCapacity;
    private int bombRange;
    
    private boolean alive;
    
    private int score;
    
    public Player(Position position, int id, String name) {
        super(position);
        ID= id;
        this.name= name;
    }
    
    public Player(Position position, int id, String name, int nBomb,int bombCap,int bombR, boolean alive,int sc) {
        super(position);
        ID= id;
        name= name;
        numberOfBombs=nBomb;
        bombCapacity=bombCap;
        bombRange=bombR;
        
        this.alive=alive;
        score=sc;
    }

    @Override
    public Type getType() {
        return Type.PLAYER;
    }

    public int getID() {
        return ID;
    }
    
    public String getName() {
        return name;
    }
    
    public boolean getAlive() {
        return alive;
    }
    
    public int getScore() {
        return score;
    }

    public int getNumberOfBombs() {
        return numberOfBombs;
    }

    public int getBombCapacity() {
        return bombCapacity;
    }

    public int getBombRange() {
        return bombRange;
    }

    public boolean isAlive() {
        return alive;
    }
}
