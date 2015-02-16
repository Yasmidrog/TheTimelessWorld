package TheTimeless.game;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.util.HashMap;
import java.util.Random;

public class DrOctopus extends Creature implements IAgressive {


    transient Graphics HealthBack = new org.newdawn.slick.Graphics();
    transient Graphics HealthFore = new org.newdawn.slick.Graphics();
    static  final long serialVersionUID=1488228l;
    private boolean Following = true;

    public DrOctopus(float x, float y) {
        Acceleration = 0.3f;
        Speed = 18;
        OnEarth = false;
        this.x = x;
        this.y = y;
        Health = 50;
        Name = "DrOctopus";
        MAXENERGY=80;Flight=MAXENERGY;
        Counters = new HashMap<String, Counter>();
        Counters.put("patrol", new Counter("patrol", 110));

    }

    @Override
    public void onInit(World world) {
        try {
            CrWld = world;
            Shootright = CrWld.ResLoader.getSprite("DrOctShootRight");
            Shootleft = CrWld.ResLoader.getSprite("DrOctShootLeft");
            Upleft = CrWld.ResLoader.getSprite("DrOctUpLeft");
            Upright = CrWld.ResLoader.getSprite("DrOctUp");
            Left = CrWld.ResLoader.getSprite("DrOctLeft");
            Right = CrWld.ResLoader.getSprite("DrOctRight");
            Counters.put("shoot", new Counter("shoot", 100){
                @Override
                public void tick() {
                    super.tick();
                    if(Ticks<50) {
                        if(Side==sides.RIGHT) {
                            sprite=Shootright;
                            vx -= (Ticks * 0.05 - 0.1);
                        }
                        if(Side==sides.LEFT) {
                            sprite=Shootleft;
                            vx += (Ticks * 0.05 - 0.1);
                        }
                    }
                }
            });
            // Спарйт смотрит вправо
            sprite = Right;
            SzH = Right.getHeight();
            SzW = Right.getWidth();
            Rect = new org.newdawn.slick.geom.Rectangle(x, y, SzW, SzH);
        } catch (Exception ex) {
        }

    }

    @Override
    public void onUpdate(int delta) {
        if (Health <= 0) {
            CrWld.ResLoader.playSound("hurt", 1, 2, false, 6,6,0);
                     CrWld.Creatures.remove(this);
            System.gc();
        }

        Rect.setY(y);
        Rect.setX(x);

        if(sideLocked(Side,Acceleration*Speed) && Following)
            jump();
        OnEarth = sideLocked(sides.DOWN, 1);
        if (OnEarth) {
          onBlockCollide();
        } else if (!OnEarth) {

            if (Side == sides.LEFT)
                sprite = Upleft;
            if (Side == sides.RIGHT)
                sprite = Upright;

            vy += Acceleration * Speed * 0.5;
        }
        followPlayer();
        checkCounters();
        checkvx();
    }

    public void onRender() {
        sprite.update(CrWld.delta);
            sprite.draw(-CrWld.SpMn.x + x + CrWld.CrCntr.getWidth() / 2 - CrWld.SpMn.SzW / 2,
                    -CrWld.SpMn.y + y + CrWld.CrCntr.getHeight() / 2 - CrWld.SpMn.SzH / 2);
            HealthBack.setColor(new Color(Color.red.getRed(), Color.red.getGreen(), Color.red.getBlue(), 80));
            HealthBack.fillRect((int) -CrWld.SpMn.x + x + CrWld.CrCntr.getWidth() / 2 - CrWld.SpMn.SzW / 2,
                    (int) -CrWld.SpMn.y + y + CrWld.CrCntr.getHeight() / 2 - CrWld.SpMn.SzH / 2, SzW, 5);
            HealthFore.setColor(org.newdawn.slick.Color.red);
            HealthFore.fillRect((int) -CrWld.SpMn.x + x + CrWld.CrCntr.getWidth() / 2 - CrWld.SpMn.SzW / 2,
                    (int) -CrWld.SpMn.y + y + CrWld.CrCntr.getHeight() / 2 - CrWld.SpMn.SzH / 2, SzW * Health / 50, 5);

    }

    @Override
    protected void onBlockCollide() {
        if (Side == sides.LEFT)
            sprite = Left;
        if (Side == sides.RIGHT)
            sprite = Right;
       Flight=MAXENERGY;
    }

    @Override
    public void onEntityCollide(final Creature ent) {

    }
    @Override
    public void followPlayer() {

        if(CrWld.SpMn.x>x) {
            Side = sides.RIGHT;
        } else if(CrWld.SpMn.x<x){
            Side=sides.LEFT;
        }

        if (CrWld.SpMn.x - x > SzW + 10) {
            if (CrWld.SpMn.x - x < 64 * 9) {
                Following = true;

                vx += Speed * Acceleration;

            } else {
                Following = false;
                patrol();
            }

        } else if (x - CrWld.SpMn.x > SzW + 10) {
            if (x - CrWld.SpMn.x < 64 * 9) {
                Following = true;

                vx -= Speed * Acceleration;

            } else {
                Following = false;
                patrol();
            }
        }

        if (new Random().nextInt() % 73 == 0 && Following) {
            Counter cnt = Counters.get("shoot");
            if (cnt.is()) {

                Bullet bullet = null;

                if (Side == sides.RIGHT) {
                    bullet = new Bullet(x + SzW - 15, y, this, 0.4f, -21);
                } else if (Side == sides.LEFT) {
                    bullet = new Bullet(x - 30, y, this, 0.4f, 21);
                }
                bullet.onInit(CrWld);
                CrWld.Bullets.add(bullet);
                CrWld.ResLoader.playSound("shoot", 1, 2, false, Math.abs((CrWld.SpMn.x / 10 - x / 10)),
                        Math.abs(CrWld.SpMn.y/10-y/10),0);
                Counters.get("shoot").restoreTime();
            }
        }
    }

    @Override
    protected void jump() {
           super.jump();
            if (!sideLocked(Side, Speed * Acceleration))
                followPlayer();
    }
    @Override
    public void patrol() {
        if (Counters.get("patrol").is() || sideLocked(Side, Acceleration * Speed)) {
            if (new Random().nextInt() % 3 == 0)
                if (Side == sides.RIGHT)
                    Side = sides.LEFT;
                else Side = sides.RIGHT;
            Counters.get("patrol").restoreTime();

        }
        if (Side == sides.RIGHT) {
            vx += Speed * Acceleration;
        }
        if (Side == sides.LEFT) {
            vx -= Speed * Acceleration;
        }
    }
}

