package fr.insalyon.hexbomber;

/**
 *
 * @author paul
 */
public abstract class Entity {
    protected Position position;

    public Entity(Position position) {
        this.position = position;
    }
    
    public abstract Type getType();

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getX() {
        return position.getX();
    }

    public int getY() {
        return position.getY();
    }

    public void setX(int x) {
        position.setX(x);
    }

    public void setY(int y) {
        position.setY(y);
    }
    
}
