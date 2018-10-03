package fr.insalyon.hexbomber;

/**
 *
 * @author paul
 */
public class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Position getNeighbour(Direction direction){
        switch (direction){
            case UP:
                return new Position(this.x+1,this.y-1);
            case UP_RIGHT:
                return new Position(this.x+1,this.y);
            case DOWN_RIGHT:
                return new Position(this.x,this.y+1);
            case DOWN:
                return new Position(this.x-1,this.y+1);
            case DOWN_LEFT:
                return new Position(this.x-1,this.y);
            case UP_LEFT:
                return new Position(this.x,this.y-1);
        }
        return null;

    }

    public Position[] getNeighbours(){
        Position[] positions = new Position[6];
        int i=0;
        for(Direction dir : Direction.values()){
            positions[i]=getNeighbour(dir);
            ++i;
        }
        return positions;
    }
    
    public int distance(Position p2) {
        return (Math.abs(x-p2.getX())
                +Math.abs(y-p2.getY())
                +Math.abs(x+y-p2.getX()-p2.getY())/2);
    }
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + this.x;
        hash = 47 * hash + this.y;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Position other = (Position) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        return true;
    }

}
