package TheTimeless.game;

import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 * our main hero
 */
public class Spudi extends Creature implements IControlable {

    Graphics indicators = new org.newdawn.slick.Graphics();
       Image healthImage = World.ResLoader.getSprite("Health").getImage(0);
       Image manaImage = World.ResLoader.getSprite("Mana").getImage(0);
       Image energyImage = World.ResLoader.getSprite("Energy").getImage(0);
    Image boowIcon= World.ResLoader.getSprite("HeroIcon").getImage(0);
    static  final long serialVersionUID=2281488l;
    public Spudi(float X, float Y) {
        Acceleration = 0.4f;
        Speed = 18;
        OnEarth = false;
        this.x = X;
        this.y = Y;
        Health = 100;
        Name = "Spudi";
        weight=60;
        MAXMANA =150; MAXHEALTH =100;MAXENERGY=55;
        MANAREGENSTEP =0.027f;
        ENERGYREGENSTEP=0.016f;
        Mana =MAXMANA;//current mana
        Energy =MAXENERGY;//amount of remaining energy
        Counters.put("shoot",new Counter("shoot",70){
            @Override
            public void tick() {
                super.tick();
                if(Ticks<50) {

                    if(Side==sides.RIGHT)
                        vx -= (Ticks * 0.05 - 0.1);
                    if(Side==sides.LEFT)
                        vx += (Ticks * 0.05- 0.1);
                }
            }
        });
        //delays between shoots
    }

    @Override
    public void onInit(World world) {
        CrWld = world;
        try {

            Shootleft = World.ResLoader.getSprite("BoowShootLeft");
            Shootright = World.ResLoader.getSprite("BoowShootRight");
            Upleft = World.ResLoader.getSprite("BoowJumpLeft");
            Upright = World.ResLoader.getSprite("BoowJumpRight");
            Left = World.ResLoader.getSprite("BoowLeft");
            Right = World.ResLoader.getSprite("BoowRight");

            // Спарйт смотрит вправо
            sprite = Right;
            SzW = sprite.getWidth();//получаем параметры спрайта
            SzH = sprite.getHeight();
            Rect = new org.newdawn.slick.geom.Rectangle(x, y, SzW, SzH);
            CrWld.startSpeaking(0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onUpdate(int delta) {
        SzW = sprite.getWidth();//получаем параметры спрайта
        SzH = sprite.getHeight();
        Rect = new org.newdawn.slick.geom.Rectangle(x, y, SzW, SzH);
        if (Health <=  0) {
            System.out.print("********Health=0********");
            CrWld.CrCntr.exit();
        }
    if(Energy ==75|| Energy ==25) {
        World.ResLoader.playSound("fly", 1, 1, false, 1, 1, 80);
    }

        checkCounters();
        control(delta);
     if(Mana <MAXMANA) {
         Mana += MANAREGENSTEP;
     }
        if(Energy <MAXENERGY) {
            Energy+=ENERGYREGENSTEP;
        }
       Gravity();
        shoot();
        checkvx();
    }

    public void onRender() {
        try {

            if (sprite != null) {
                sprite.update(CrWld.delta);
                sprite.draw(CrWld.CrCntr.getWidth() / 2 - SzW / 2, CrWld.CrCntr.getHeight() / 2 - SzH / 2);
            }
            indicators.setColor(new Color(Color.green.getRed(), Color.green.getGreen(), Color.green.getBlue(), 120));
            indicators.fillRect(64 + 10, 82, 150 - 44, 13);
            indicators.fillRect(64 + 10, 82, (150 - 44) * Energy / MAXENERGY, 12, energyImage,1,1);

            indicators.setColor(new Color(Color.blue.getRed(), Color.blue.getGreen(), Color.blue.getBlue(), 80));
            indicators.fillRect(64 + 10, 30, 150 - 44, 13);
            indicators.fillRect(64 + 10, 30, (150 - 44) * Mana / MAXMANA, 12, manaImage,1,1);

            indicators.setColor(new Color(Color.red.getRed(), Color.red.getGreen(), Color.red.getBlue(), 80));
            indicators.fillRect(64 + 10, 57, 150 - 44, 13);
            indicators.fillRect(64+10,57, (150-44) * Health / MAXHEALTH, 12, healthImage,1,1);
            boowIcon.draw(5,30);

        } catch(Exception e){
                e.printStackTrace();
        }
    }

    @Override
    public void control(int delta) {
        try {
            Input input = CrWld.CrCntr.getInput();
            if (Side == sides.RIGHT)
                sprite = Upright;
            else if (Side == sides.LEFT) sprite = Upleft;

            if (input.isKeyDown(Input.KEY_A)) {
                if (OnEarth) sprite = Left;
                else sprite = Upleft;
                Side = sides.LEFT;

                    vx -= Speed * Acceleration;

            } else if (input.isKeyDown(Input.KEY_D)) {
                if (OnEarth) sprite = Right;
                else sprite = Upright;

                Side = sides.RIGHT;

                    vx+=Speed*Acceleration;

            } else if (Side == sides.LEFT) sprite = Upleft;
            if (input.isKeyDown(Input.KEY_SPACE)) {
                jump();
            }

        }catch(Exception e){}
    }
    public void shoot() {
        try {
            Input input = CrWld.CrCntr.getInput();
            if (input.isMouseButtonDown(0)) {
                if(Side==sides.RIGHT&&CrWld.CrCntr.getWidth() / 2 - SzW / 2>input.getMouseX()) {
                    Side = sides.LEFT;
                }
                if(Side==sides.LEFT&&CrWld.CrCntr.getWidth() / 2 - SzW / 2<input.getMouseX()) {
                    Side = sides.RIGHT;
                }
                if (Side == sides.RIGHT) {
                    if (sprite != Shootright)
                        sprite = Shootright;
                }
                if (Side == sides.LEFT) {
                    if (sprite != Shootleft) {
                        sprite = Shootleft;
                    }
                }
                if (Mana > 5) {
                    if (Counters.get("shoot").is()) {
                        Bullet bullet = null;

                        if (Side == sides.RIGHT) {
                            bullet = new HeroBullet(x + SzW - 15, y,input.getMouseX(),input.getMouseY(),  this, -21);
                        } else if (Side == sides.LEFT) {
                            bullet = new HeroBullet(x - 30, y,input.getMouseX(),input.getMouseY(),  this, 21);
                        }
                        World.ResLoader.playSound("shoot", 1, 2, false, 2, 2, 10);

                        assert bullet != null;
                        bullet.onInit(CrWld);
                        CrWld.Bullets.add(bullet);
                        Counters.get("shoot").restoreTime();
                        Mana -= 5;
                    }
                }
            }

        } catch (Exception e) {
        }
    }
    @Override
    public void interact(int delta) {
    }
    @Override
    protected void onBlockCollide() {
        if (Side ==sides.LEFT)
            sprite = Left;
        if (Side ==sides.RIGHT)
            sprite = Right;
        Energy =MAXENERGY;
    }
@Override
    public void onEntityCollide(final Creature ent) {}
}







    

