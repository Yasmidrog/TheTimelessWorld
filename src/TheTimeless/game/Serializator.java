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
    static  final long serialVersionUID=22814881337l;
    public int level;
}
