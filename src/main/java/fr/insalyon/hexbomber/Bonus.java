package fr.insalyon.hexbomber;

/**
 *
 * @author paul
 */
public class Bonus extends Entity {
    private BonusType type;

    public Bonus(Position position, BonusType type) {
        super(position);
        this.type = type;
    }
    
    @Override
    public Type getType() {
        return Type.BONUS;
    }
    
    public BonusType getBonusType() {
        return type;
    }

    public void setType(BonusType type) {
        this.type = type;
    }
}
