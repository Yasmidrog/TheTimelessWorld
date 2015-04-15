package TheTimeless.game;

/**
 * This class upgades and adds abilities
 */
public abstract class Upgrade {
   public String Name,Description;
    public int Price;
    public static enum UpgradeType{GOD,DEVIL}
    private UpgradeType Type;
    public Upgrade(String name, String description, int price,UpgradeType type){
            Name = name;
            Description = description;
        Price=price;
        Type=type;
    }
    public abstract void apply(Spudi sp);
    public UpgradeType getType(){
        return Type;
    }
}
