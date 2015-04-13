package TheTimeless.game;

/**
 * This class upgades and adds abilities
 */
public abstract class Upgrade {
   public String Name,Description;
    public int Price;
    public Upgrade(String name, String description,int price){
            Name = name;
            Description = description;
    }
    public abstract void apply(Spudi sp);
}
