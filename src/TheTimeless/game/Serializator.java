package TheTimeless.game;

import org.newdawn.slick.tiled.TiledMap;

import java.util.ArrayList;

/**
 * A class with object's parameters lists to save
 */
    public class Serializator{
    private String Map;
    private int Level;
    private ArrayList<Creature> Creatures =new ArrayList<>();
    private ArrayList<Entity> StaticObjects =new ArrayList<>();
    public Serializator (int level, ArrayList<Creature> creatures,ArrayList<Entity> entities){
        Map="data/levels/"+level+"/world.tmx";
        Creatures =creatures;
        StaticObjects =entities;
    }
    public ArrayList<Creature> getCreatures(){
        return Creatures;
    }
    public ArrayList<Entity> getEntities(){
        return StaticObjects;
    }
    public int getLevel(){return Level;}
    public TiledMap getMap(){
        System.out.print("Load"+Map);
        TiledMap map=null;
        try {
           map= new TiledMap(Map);
        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }
}
