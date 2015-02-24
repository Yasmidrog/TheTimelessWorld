package TheTimeless.game;

import java.io.Serializable;

/**
 * A counter to make some things for fixed time
 */
public class Counter implements Serializable{
    public int Ticks =0, Period =0;
    String name;
    static  final long serialVersionUID=1488228l;
    public Counter(String Name,int period)
    {
        name=Name;
        Period =period;
        Ticks=Period;
    }
    /**
     * Is the time over
     */
   public boolean is()
    {
        return Ticks >= Period;
    }
    /**
     * Things to do on every tick
     */
    public void tick()
    {
        if(Ticks<Period+10)
        Ticks++;
    }
      public void restoreTime()
{
    Ticks =0;
}
}
