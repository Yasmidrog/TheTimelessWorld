package TheTimeless.game;

import org.newdawn.slick.geom.Rectangle;

public class Bullet extends Creature {

    public Creature Killer;//The bad(or good) guy who shot.
    static  final long serialVersionUID=1488228l;
    protected Bullet(){}
    public Bullet(float x, float y, Creature killer, float acceleration,float speed) {
        Speed = speed;
        OnEarth = false;
        this.x = x;
        this.y = y;
        Acceleration=acceleration;
        Speed = speed;
        Name = "Lazer";
        Killer = killer;

    }

    @Override
    public void onInit(World world) {
        try {

            CrWld = world;
            sprite = World.ResLoader.getSprite("Lazer");

            SzW = sprite.getWidth()+5;    //get collider
            SzH = sprite.getHeight()+45;

            Rect = new Rectangle(x, y, SzW, SzH);

        } catch (Exception ex) {
            ex.printStackTrace();
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
                if(ent.getClass().getName()!=Killer.getClass().getName()) {
                    ent.Health -= 5;
                    ent.Counters.put("collide",new Counter("collide",40) {
                                private int distance = 0;
                                public void tick() {
                                    super.tick();
                                    if (distance <= Period*(Ticks*0.08-ent.weight/100)) {
                                        if (ent.x < x) {
                                            ent.vx -= Ticks*0.08-ent.weight/100;
                                        }
                                        if (ent.x > x) {
                                            ent.vx += Ticks*0.08-ent.weight/100;
                                        }
                                        distance += Ticks*0.08-ent.weight/100;
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
    public void onRender() {
        if (sprite != null) {
            sprite.draw(-CrWld.SpMn.x + Rect.getX() + CrWld.CrCntr.getWidth() / 2-CrWld.SpMn.SzW / 2,
                    -CrWld.SpMn.y + Rect.getY() + (Killer.SzH / 3) +
                            CrWld.CrCntr.getHeight() / 2-CrWld.SpMn.SzH / 2, Rect.getWidth(), 13);
        }else {
            sprite= World.ResLoader.getSprite("Lazer");
        }
    }
    @Override
    protected void onBlockCollide() {
    }
    public void onEntityCollide() {
    }

    public void FlyForward() {
        x -= Acceleration * Speed;
        if (sideLocked(sides.RIGHT,Acceleration*Speed) || sideLocked(sides.LEFT,Acceleration*Speed))
            CrWld.Bullets.remove(this);
    }//fly, while there are no obstacles in front
}