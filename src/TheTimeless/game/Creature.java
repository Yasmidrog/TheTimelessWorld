package TheTimeless.game;

import org.newdawn.slick.Animation;

import java.util.HashMap;
import java.util.Map;


public class Creature extends Entity{
    protected float Health;
    transient public Animation Upleft, Upright, Left, Right,BodyRight,BodyLeft,ShootRight,ShootLeft;
    public boolean OnEarth;//is the creature stands on earth
    public float Speed;
    public float vx,vy;//the distance to move at
    protected float Acceleration;
    public sides Side = sides.RIGHT;
    public float Gravity=5.1f;
    public float armorCoef=0.0f;
    public float MAXMANA , MAXHEALTH, MAXENERGY;
    public float Mana,Energy ;//current mana
    public float MANAREGENSTEP,ENERGYREGENSTEP;//shows how fast will mana regenerate
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


    protected void Gravity(){
        OnEarth = sideLocked(sides.DOWN, Gravity);
        if (OnEarth) {
            onBlockCollide();
        } else if (!OnEarth) {

            if (Side == sides.LEFT)
                sprite = Upleft;
            if (Side == sides.RIGHT)
                sprite = Upright;
            vy += Gravity;
        }
    }
    public int changeHealth(float healthDifference){
        float diff=healthDifference;
        if(diff+Health>MAXHEALTH) {
            diff=MAXHEALTH-Health;
            Health=MAXHEALTH;
            return (int) diff;
        }
        Health+=diff;
        return (int) diff;
    }
}


