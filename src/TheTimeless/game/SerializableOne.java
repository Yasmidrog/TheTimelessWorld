package TheTimeless.game;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * A class with main parameters of an Creature or Entity
 */
public class SerializableOne  implements Serializable{
    public float sx,sy;
    public String tableText;
    public float sHealth =0;
    public Map<String,Counter> Cntrs=new HashMap<String, Counter>();//counters
    public Entity.sides Side;
    String Type;
    public SerializableOne(Entity ent)
    {
        if(ent instanceof  Table){
            Type ="Table";
            sx=ent.x;
            sy=ent.y;
            tableText=((Table) ent).fileName;
        }else {
            Type = ent.getClass().getName();
            sx = ent.x;
            sy = ent.y;
        }
    }
    public SerializableOne(Creature ent)
    {
        Type =ent.getClass().getName();
        sx=ent.x;
        sy=ent.y;
        sHealth =ent.Health;
        Side=ent.Side;
        Cntrs=ent.Counters;
    }
}
