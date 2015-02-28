package TheTimeless.game;

import org.newdawn.slick.Animation;

import java.util.HashMap;
import java.util.Map;


public class Creature extends Entity{
    public float Health;
    transient public Animation Upleft, Upright, Left, Right,BodyRight,BodyLeft,ShootRight,ShootLeft;
    public boolean OnEarth;//is the creature stands on earth
    public float Speed;
    public float vx,vy;//the distance to move at
    protected float Acceleration;
    public sides Side = sides.RIGHT;
    protected Map<String,Counter> Counters;//the counters of an object
    protected   float MAXMANA , MAXHEALTH, MAXENERGY;
    protected float Mana,Energy ;//current mana
    protected float MANAREGENSTEP,ENERGYREGENSTEP;//shows how fast will mana regenerate
    public float getAccelration() {
        return Acceleration;
    }
    protected void onEntityCollide(Creature ent) {}
    protected void onBlockCollide() {}

    /**
     * Check the vx
     */
   protected void checkvx()
   {
    if(vx>0)
    {
        if(!sideLocked(sides.RIGHT,vx))
        x+=vx;
    }
    if(vx<0)
    {
        if(!sideLocked(sides.LEFT,-vx))
            x+=vx;
    }
    if(vy>0)
    {
        if(!sideLocked(sides.DOWN,vy))
            y+=vy;
    }
    if(vy<0)
    {
        if(!sideLocked(sides.UP,-vy))
            y+=vy;
    }
    vx=vy=0;
}
    /**
     * Check if can move to some direction
     */
    protected boolean sideLocked(final sides side,float vx) {
     //check every point at the given distance,
     // if some point is located on some block you cannot move in this direction
        if (side==sides.UP) {
            for (int j = 4; j <= SzW-4; j += 5) {
                if (CrWld.isBlocked(x + j, y - vx))
                    return  true;
            }//go th the given distance and check every pixel on the line
        }
        if (side==sides.LEFT) {
            for (int j = 4; j <= SzH; j += 5) {

                if (CrWld.isBlocked(x - vx, y + j))
                    return true;
            }
        }
        if (side==sides.RIGHT) {
            for (int j = 4; j <= SzH; j += 5) {
                if (CrWld.isBlocked(x + SzW + vx, y + j))
                    return true;
            }
        }
        if (side==sides.DOWN) {
            for (int j = 4; j <= SzW-4; j += 5) {
                if (CrWld.isBlocked(x + j, y + SzH + vx))
                    return  true;
            }
           return false;
        }
            return false;
        }
    protected void jump(){
        if (Energy >0) {
            vy -= (Acceleration * Speed * +Math.sqrt(Energy *0.1));
            Energy-=0.7;
        }
    }

    /**
     * call tick() of every counter
     */
    protected void checkCounters()
    {
        for(Map.Entry<String, Counter> entry : Counters.entrySet()) {
            Counter cnt = entry.getValue();
            cnt.tick();
        }
    }
    protected void Gravity(){
        OnEarth = sideLocked(sides.DOWN, 5.1f);
        if (OnEarth) {
            onBlockCollide();
        } else if (!OnEarth) {

            if (Side == sides.LEFT)
                sprite = Upleft;
            if (Side == sides.RIGHT)
                sprite = Upright;
            vy += 5.1;
        }
    }
}


