package TheTimeless.game;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A class with object's parameters lists to save
 */
    public class Serializator implements Serializable {
    public ArrayList<SerializableOne> SCrts=new ArrayList<SerializableOne>();
    public ArrayList<SerializableOne> SEnts=new ArrayList<SerializableOne>();
    public String Map;
    public Serializator(World wld,int level)
    {
        Map="data/levels/"+level+"/"+"world.tmx";
        SCrts.add(0,new SerializableOne(wld.SpMn));
       for(Entity ent:wld.StaticObjects)
       {
           SEnts.add(new SerializableOne(ent));
       }
        for(Creature ent:wld.Creatures)
        {
            SCrts.add(new SerializableOne(ent));
        }
        // write every serializable one to the list

    }

}
