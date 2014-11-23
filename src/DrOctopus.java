import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import java.awt.*;
import java.awt.geom.Rectangle2D;

import org.newdawn.slick.gui.TextField;

public class DrOctopus extends Entity implements IAgressive{

    public Shape BigRect;


    public DrOctopus(float x, float y) {

        Acceleration = 0.4f;
        Speed = 3;
        OnEarth = false;
        this.x=x;
        this.y = y;
        health=100;

    }

    @Override
    public void onInit(World world) throws SlickException {


        CurrentWorld = world;
        CurrentWorld.GetBlocked();
        Image[] movementUp = {new Image("data/gg.png"), new Image("data/gg.png")};//спрайты для разных положений
        Image[] movementDown = {new Image("data/gg.png"), new Image("data/gg.png")};
        Image[] movementLeft = {new Image("data/gg.png"), new Image("data/gg.png")};
        Image[] movementRight = {new Image("data/gg.png"), new Image("data/gg.png")};
        SpriteSizeW = movementLeft[0].getWidth();//получаем параметры спрайта
        SpriteSizeH = movementLeft[0].getHeight();
        int[] duration = {300, 300};
        upleft= new Animation(movementUp, duration, false);
        upright = new Animation(movementDown, duration, false);
        left = new Animation(movementLeft, duration, false);
        right = new Animation(movementRight, duration, false);
        // Спарйт смотрит вправо
        sprite = right;
        Rect=new Rectangle(x,y,SpriteSizeW,SpriteSizeW);

    }

    @Override
    public void onUpdate(int delta) {
        Rect.setX(x);
        Rect.setY(y);
if(this.Rect.intersects(CurrentWorld.SpiderMan.Rect))
    CurrentWorld.SpiderMan.health-=0.05;
        OnEarth=sideLocked("down");
        if (OnEarth) {

            if(sprite==left||sprite==upleft)
                sprite=left;
            else  if(sprite==right||sprite==right)
                sprite=right;
            followPlayer();

        } else  if (!OnEarth){

            if(sprite==left||sprite==upleft)
                sprite=upleft;
            else  if(sprite==right||sprite==right)
                sprite=upright;
            y += Acceleration * Speed * 0.25;
        }

    }


    public void onRender() {
        sprite.draw(-CurrentWorld.SpiderMan.x+x+CurrentWorld.CurrentContainer.getWidth()/2,
        -CurrentWorld.SpiderMan.y+y+CurrentWorld.CurrentContainer.getHeight()/2);
    }

    @Override
    protected void OnBlockCollide(){Block.OnCollide(CurrentWorld.CurrentMap,(int)x+32,(int)y+32);}
    @Override
    protected void OnEntityCollide(Entity ent){}
    @Override
    public void followPlayer() {


            if (CurrentWorld.SpiderMan.x - x>60) {
                if (!sideLocked("right")) {
                    x += 0.2 * Speed;
                    sprite = right;

                }
            } else if ( x-CurrentWorld.SpiderMan.x >138) {
                if (!sideLocked("left")) {
                    x -= 0.2* Speed;
                    sprite = left;

                }
            }
        }
    }

