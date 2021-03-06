package TheTimeless.game;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.util.HashMap;
import java.util.Random;

public class DrOctopus extends Creature implements IAgressive {
    private boolean Following = true;

    public DrOctopus(float x, float y) {
        Acceleration = 0.3f;
        Speed = 18;
        OnEarth = false;
        this.x = x;
        this.y = y;

        Name = "DrOctopus";
        MAXENERGY=82;
        weight=10;
        MAXHEALTH=50;
        Health = MAXHEALTH;
        Energy =MAXENERGY;
        Counters = new HashMap<String, Counter>();
        Counters.put("patrol", new Counter("patrol", 110));
    }

    @Override
    public void onInit(World world) {
        try {
            CrWld = world;
            ShootRight = World.ResLoader.getSprite("DrOctShootRight");
            ShootLeft = World.ResLoader.getSprite("DrOctShootLeft");
            Upleft = World.ResLoader.getSprite("DrOctUpLeft");
            Upright = World.ResLoader.getSprite("DrOctUp");
            Left = World.ResLoader.getSprite("DrOctLeft");
            Right = World.ResLoader.getSprite("DrOctRight");
            Counters.put("shoot", new Counter("shoot", 100){
                @Override
                public void tick() {
                    super.tick();
                    if(Ticks<50) {
                        if(Side==sides.RIGHT) {
                            sprite=ShootRight;
                            vx -= (Ticks * 0.06 - 0.1);
                        }
                        if(Side==sides.LEFT) {
                            sprite=ShootLeft;
                            vx += (Ticks * 0.06 - 0.1);
                        }
                    }
                }
            });
            sprite = Right;
            SzW = sprite.getWidth();//получаем параметры спрайта
            SzH = sprite.getHeight();
            Rect = new org.newdawn.slick.geom.Rectangle(x, y, SzW, SzH);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void onUpdate(int delta) {
        if (Health <= 0) {
            Loader.playSound("hurt", 1, 2,  6, 6, 0);
            if(new Random().nextInt()%1==0)
            spawnXP();
                     CrWld.Creatures.remove(this);
            System.gc();
        }
        SzW = sprite.getWidth();//получаем параметры спрайта
        SzH = sprite.getHeight();
        Rect = new org.newdawn.slick.geom.Rectangle(x, y, SzW, SzH);

        if(sideLocked(Side,Acceleration*Speed) && Following)
            jump();

        Gravity();
        followPlayer();
        checkCounters();
        checkvx();
    }

    public void onRender() {
        sprite.update(CrWld.delta);
        Graphics g=CrWld.CrCntr.getGraphics();
            sprite.draw(-CrWld.SpMn.x + x + CrWld.CrCntr.getWidth() / 2 - CrWld.SpMn.SzW / 2,
                    -CrWld.SpMn.y + y + CrWld.CrCntr.getHeight() / 2 - CrWld.SpMn.SzH / 2);
            g.setColor(new Color(Color.red.getRed(), Color.red.getGreen(), Color.red.getBlue(), 80));
            g.fillRect((int) -CrWld.SpMn.x + x + CrWld.CrCntr.getWidth() / 2 - CrWld.SpMn.SzW / 2,
                    (int) -CrWld.SpMn.y + y + CrWld.CrCntr.getHeight() / 2 - CrWld.SpMn.SzH / 2, SzW, 5);
            g.setColor(org.newdawn.slick.Color.red);
            g.fillRect((int) -CrWld.SpMn.x + x + CrWld.CrCntr.getWidth() / 2 - CrWld.SpMn.SzW / 2,
                    (int) -CrWld.SpMn.y + y + CrWld.CrCntr.getHeight() / 2 - CrWld.SpMn.SzH / 2, SzW * Health / MAXHEALTH, 5);

    }

    @Override
    protected void onBlockCollide() {
        if (Side == sides.LEFT)
            sprite = Left;
        if (Side == sides.RIGHT)
            sprite = Right;
       Energy =MAXENERGY;
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
                    bullet = new Bullet(x + SzW - 15, y, this,0.4f,-15);
                } else if (Side == sides.LEFT) {
                    bullet = new Bullet(x - 30, y, this,0.4f, 15);
                }
                assert bullet != null;
                bullet.onInit(CrWld);
                CrWld.Bullets.add(bullet);
                Loader.playSound("shoot", 1, 2, ((CrWld.SpMn.x / 10 - x / 10)),
                        (CrWld.SpMn.y / 10 - y / 10), 0);
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
    protected void spawnXP(){
        CrWld.spawn(new Increaser(x+SzW/2,y+SzH/2,new Random().nextInt(21-5)+5){
            @Override
            public void setColorAndSprite(){
                IntColor= 14706969;
                sprite=World.ResLoader.getSprite("Coin");
            }
            @Override
             public  void  onPick(){
                CrWld.SpMn.XP+=Amount;
                exists=false;
            }
        });
    }
}

