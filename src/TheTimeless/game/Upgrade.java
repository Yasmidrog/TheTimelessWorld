package TheTimeless.game;

/**
 * This class upgades and adds abilities
 */
public abstract class Upgrade {
   public String Name,Description,Picture;
    public Upgrade(String name, String description, String picture){
            Name = name;
            Description = description;
            Picture = picture;
    }
    public abstract void apply(Spudi sp);
}
