package TheTimeless.game;

import java.io.Serializable;

/**
 * A counter to make some things for fixed time
 */
public class Counter implements Serializable{
    public int Ticks =0, Period =0;
    String name;
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
     if(Ticks >= Period) {
         return true;
     }
        else {return false;}
    }
    /**
     * Things to do on every tick
     */
    public void tick()
    {
        if( Ticks <= Period )
        Ticks++;
    }
      public void restoreTime()
{
    Ticks =0;
}
}
