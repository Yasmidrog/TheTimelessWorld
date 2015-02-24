package TheTimeless.game;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

import java.util.Objects;

public class HeroBullet extends Bullet {
    static  final long serialVersionUID=1488228l;
    private float cosx,siny;
    private int targetx,targety;
    private double alpha;
    public HeroBullet(float x, float y, int screenx,int screeny,Spudi killer, float speed) {
        Speed = speed;
        this.x = x;
        this.y = y;
        Speed = speed;
        Name = "Lazer";
        Killer = killer;
        targetx=screenx;
        targety=screeny;
    }
    @Override
    public void onInit(World world) {
        try {

            CrWld = world;
            sprite = World.ResLoader.getSprite("Lazer").copy();

            SzW = sprite.getWidth()+5;    //get collider
            SzH = sprite.getHeight()+45;

            Rect = new Rectangle(x, y, SzW, SzH);
            int deltax = targetx-(int)(-CrWld.SpMn.x + Rect.getX() + CrWld.CrCntr.getWidth() / 2-CrWld.SpMn.SzW / 2);
            int deltay = targety-(int) (-CrWld.SpMn.y + Rect.getY() + (Killer.SzH / 3) +CrWld.CrCntr.getHeight() / 2-CrWld.SpMn.SzH / 2);

            alpha=Math.atan2(deltay,deltax);
                cosx=(float)Math.cos(alpha);
            siny=(float) Math.sin(alpha);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void onRender() {
        if (sprite != null) {
            Image im=sprite.getCurrentFrame().copy();
            im.rotate(57.2f*(float)alpha);
            im.draw(-CrWld.SpMn.x + Rect.getX() + CrWld.CrCntr.getWidth() / 2 - CrWld.SpMn.SzW / 2,
                    -CrWld.SpMn.y + Rect.getY() + (Killer.SzH / 3) +
                            CrWld.CrCntr.getHeight() / 2 - CrWld.SpMn.SzH / 2, Rect.getWidth(), 13);
        }else {
            sprite= World.ResLoader.getSprite("Lazer").copy();
        }
    }
    @Override
    public void onUpdate(int delta) {
        FlyForward();
        checkvx();
        Rect.setY(y);
        Rect.setX(x);

        for (final Creature ent : CrWld.Creatures) {
            if (this.Rect.intersects(ent.Rect) ) {
                if(!Objects.equals(ent.getClass().getName(), Killer.getClass().getName())) {
                    ent.Health -= 5;
                    ent.Counters.put("collide",new Counter("collide",40) {
                                private int distance = 0;
                                public void tick() {
                                    super.tick();
                                    if (distance <= Period*(Ticks*0.9-ent.weight/100)) {
                                        if (ent.x < x) {
                                            ent.vx -= (Math.sqrt(Ticks*0.9-ent.weight/100));
                                        }
                                        if (ent.x > x) {
                                            ent.vx += (Math.sqrt(Ticks*0.9-ent.weight/100));
                                        }
                                        distance += Ticks*0.9-ent.weight/100;
                                    }
                                }
                            }
                    );
                    CrWld.Bullets.remove(this);
                }//if this collider intersects some mob, and the mob is not the shooter, push him and decrease health
            }
        }
    }
    @Override
    public void FlyForward() {
        try {
            vx+=cosx*20;
            vy+=siny*20;
        }catch(Exception e){
            e.printStackTrace();
        }
    }//fly, while there are no obstacles in front
    @Override
    protected void checkvx()
    {
        if(vx>0) {
            if(!sideLocked(sides.RIGHT,vx))
                x+=vx;
            else CrWld.Bullets.remove(this);
        }if(vx<0) {
            if(!sideLocked(sides.LEFT,-vx))
                x+=vx;
            else CrWld.Bullets.remove(this);
        }if(vy>0) {
            if(!sideLocked(sides.DOWN,vy))
                y+=vy;
            else CrWld.Bullets.remove(this);
        }if(vy<0){
            if(!sideLocked(sides.UP,-vy))
                y+=vy;
            else CrWld.Bullets.remove(this);
        }
        vx=vy=0;
    }
}
