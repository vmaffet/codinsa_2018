package fr.insalyon.hexbomber;

/**
 *
 * @author paul
 */
public class Cell {
    private Type type;
    private Entity entity;

    public Cell(Type type) {
        this(type, null);
    }
    
    public Cell(Type type, Entity entity) {
        this.type = type;
        this.entity = entity;
    }
    
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Object getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }
}
