import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

/**
 * Created by Bazil on 08.11.2014.
 */
public class DrOctopus extends Entity implements IAgressive{

    public Shape BigRect;

    public DrOctopus(float x, float y) {

        Acceleration = 0.4f;
        Speed = 4;
        OnEarth = false;
        this.x = x+400;
        this.y = y+400;

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
        up = new Animation(movementUp, duration, false);
        down = new Animation(movementDown, duration, false);
        left = new Animation(movementLeft, duration, false);
        right = new Animation(movementRight, duration, false);
        // Спарйт смотрит вправо
        sprite = right;
        Rect=new Rectangle(400,400,SpriteSizeW,SpriteSizeW);

    }

    @Override
    public void onUpdate(int delta) {

        if (CurrentWorld.isBlocked(x, y + SpriteSizeH + 1)&&CurrentWorld.isBlocked(x+SpriteSizeW, y + SpriteSizeH + 1)) {
            OnEarth = true;

        } else {
            OnEarth = false;
        }

    }

    public void onRender() {
        sprite.draw(-CurrentWorld.SpiderMan.x+x, -CurrentWorld.SpiderMan.y+y);
    }

    @Override
    protected void OnBlockCollide(){Block.OnCollide(CurrentWorld.CurrentMap,(int)x+32,(int)y+32);}
    @Override
    protected void OnEntityCollide(Entity ent){}
    @Override
    public void followPlayer(){


        if(Speed>0) {
            if  (!CurrentWorld.isBlocked(x + SpriteSizeW + Acceleration*Speed, y) && !CurrentWorld.isBlocked(x + SpriteSizeW +Acceleration*Speed, y + SpriteSizeH-1))
            {
                x+=Acceleration*Speed;
                sprite =right;
                sprite.update(30);
            }
        }else if(Speed< 0 ){
            if  (!CurrentWorld.isBlocked(x +  Acceleration*Speed, y) && !CurrentWorld.isBlocked(x +Acceleration*Speed, y + SpriteSizeH-1))
            {
                x-=Acceleration*Speed;
                sprite =left;
                sprite.update(30);
            }
        }
    }
}
