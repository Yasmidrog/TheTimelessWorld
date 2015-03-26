package TheTimeless.game;

import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.util.HashMap;

/**
 * our main hero
 */
public class Spudi extends Creature implements IControlable {

    private transient Graphics indicators ;
    private transient Image healthImage,manaImage,energyImage,boowIcon;
    private transient  Image rightHand,leftHand;
    int XP=50;
    public Spudi(float X, float Y) {
        Acceleration = 0.4f;
        Speed = 18;
        OnEarth = false;
        this.x = X;
        this.y = Y;
        Health = 100;
        Name = "Spudi";
        weight=60;
        MAXMANA =150;
        MAXHEALTH =100;
        MAXENERGY=82;
        MANAREGENSTEP =0.027f;
        ENERGYREGENSTEP=0.6f;
        Mana =MAXMANA;//current mana
        Energy =MAXENERGY;//amount of remaining energy
        Counters = new HashMap<String, Counter>();
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

            Upleft = World.ResLoader.getSprite("BoowJumpLeft");
            Upright = World.ResLoader.getSprite("BoowJumpRight");
            Left = World.ResLoader.getSprite("BoowLeft");
            Right = World.ResLoader.getSprite("BoowRight");
            indicators = new org.newdawn.slick.Graphics();
            healthImage = World.ResLoader.getSprite("Health").getImage(0);
            manaImage = World.ResLoader.getSprite("Mana").getImage(0);
            energyImage = World.ResLoader.getSprite("Energy").getImage(0);
            boowIcon= World.ResLoader.getSprite("HeroIcon").getImage(0);

            BodyRight=World.ResLoader.getSprite("BoowBodyRight");
            BodyLeft=World.ResLoader.getSprite("BoowBodyLeft");
            rightHand=World.ResLoader.getSprite("BoowHandRight").getImage(0);
            leftHand=World.ResLoader.getSprite("BoowHandLeft").getImage(0);
            sprite = Right;
            SzW = sprite.getWidth();//получаем параметры спрайта
            SzH = sprite.getHeight();
            Rect = new org.newdawn.slick.geom.Rectangle(x, y, SzW, SzH);
                CrWld.startSpeaking();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onUpdate(int delta) {
        SzW = sprite.getWidth();//получаем параметры спрайта
        SzH = sprite.getHeight();
        Rect = new org.newdawn.slick.geom.Rectangle(x, y, SzW, SzH);
        checkCounters();
        control(delta);
        shoot();
     if(Mana <MAXMANA) {
         Mana += MANAREGENSTEP;
     }
       Gravity();
       checkvx();
    }

    public void onRender() {
        try {
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
            indicators.drawImage(World.ResLoader.getSprite("Coin").getImage(0).getScaledCopy(16,16),5,98);
            Fonts.SmallText.drawString("XP: "+XP,23,98,Color.white);
            if (CrWld!=null&&CrWld.CrCntr.getInput().isMouseButtonDown(0)) {
                drawShoot();
            }else {
                if (sprite != null) {
                    sprite.update(CrWld.delta);
                    sprite.draw(CrWld.CrCntr.getWidth() / 2 - SzW / 2, CrWld.CrCntr.getHeight() / 2 - SzH / 2);
                }
            }
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
            if (input.isKeyDown(Input.KEY_SPACE)&&!(OnEarth&&Energy<26)) {
                jump();
            }else if (input.isKeyDown(Input.KEY_LSHIFT)&&vx!=0) {
                if (Energy>= 1) {
                    if (Side == sides.RIGHT&&input.isKeyDown(Input.KEY_D)) {
                        vx += 3.2;
                    }
                    if (Side == sides.LEFT&&input.isKeyDown(Input.KEY_A)) {
                        vx -= 3.2;
                    }
                    Energy -= 1;
                }
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
                if (Mana > 5) {
                    if (Counters.get("shoot").is()) {
                        Bullet bullet = null;

                        if (Side == sides.RIGHT) {
                            bullet = new HeroBullet(x + SzW/2+17, y,input.getMouseX(),input.getMouseY(),  this, -15);
                        } else if (Side == sides.LEFT) {
                            bullet = new HeroBullet(x +SzH/2-17, y,input.getMouseX(),input.getMouseY(),  this, 15);
                        }
                        Loader.playSound("shoot", 20, 2,  2, 2, 10);
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
    private void drawShoot(){
        Input input = CrWld.CrCntr.getInput();
        Image Hand;
        int targetx=input.getMouseX();
        int targety=input.getMouseY();
        if(Side==sides.LEFT) {
            int deltax = targetx-(CrWld.CrCntr.getWidth() / 2 - SzW / 2+21);
            int deltay = targety-(CrWld.CrCntr.getHeight() / 2-SzH/4+10);
            float alpha=(float)Math.atan2(deltay,deltax);
            Hand=leftHand.copy();
            Hand.setCenterOfRotation(20,10);
            Hand.rotate(alpha*57.295f);
            Hand.draw(CrWld.CrCntr.getWidth() / 2 - SzW / 2+21,  CrWld.CrCntr.getHeight() / 2-SzH/4+10);
            BodyLeft.draw(CrWld.CrCntr.getWidth() / 2 - SzW / 2, CrWld.CrCntr.getHeight() / 2 - SzH / 2);
        }
        if(Side==sides.RIGHT){
            int deltax = targetx-(CrWld.CrCntr.getWidth() / 2 - SzW / 2+48);
            int deltay = targety-( CrWld.CrCntr.getHeight() / 2-SzH/4+3);
            float alpha=(float)Math.atan2(deltay,deltax);
            Hand=rightHand.copy();
            Hand.setCenterOfRotation(5,5);
            Hand.rotate(alpha*57.295f);
            BodyRight.draw(CrWld.CrCntr.getWidth() / 2 - SzW / 2, CrWld.CrCntr.getHeight() / 2 - SzH / 2);
            Hand.draw(CrWld.CrCntr.getWidth() / 2 - SzW / 2+48,  CrWld.CrCntr.getHeight() / 2-SzH/4+3);
        }
    }
    @Override
    public void interact(int delta) {
    }

    @Override
    protected void onBlockCollide() {
        try {
            if (Side == sides.LEFT)
                sprite = Left;
            if (Side == sides.RIGHT)
                sprite = Right;
            if (Energy < MAXENERGY && !(CrWld.CrCntr.getInput().isKeyDown(Input.KEY_LSHIFT) && vx != 0)) {
                Energy += ENERGYREGENSTEP;
            }
        }catch (IllegalStateException ignored){}
    }
@Override
    public void onEntityCollide(final Creature ent) {}
}







    

