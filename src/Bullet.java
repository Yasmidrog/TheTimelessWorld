import org.newdawn.slick.*;

public class Bullet extends Entity {
    public String side;
    public Bullet(float x, float y,String side) {
        Acceleration = 0.4f;
        Speed = -5;
        OnEarth = false;
        this.x = x;
        this.y = y;
    }
    public boolean invincible;



    @Override
    public void onInit(World world) throws SlickException {


        CurrentWorld = world;
        CurrentWorld.GetBlocked();

        Image[] movementUpLeft = getImages("data/SpriteJumpREV");//спрайты для разных положений
        Image[] movementUpRight = getImages("data/SpriteJump");

        Image[] movementLeft =  getImages("data/SpriteStaticREV");
        Image[] movementRight =  getImages("data/SpriteStatic");
        SpriteSizeW = movementLeft[0].getWidth();//получаем параметры спрайта
        SpriteSizeH = movementLeft[0].getHeight();
        int duration=30;


        left = new Animation(movementLeft, duration, true);
        right = new Animation(movementRight, duration, true);
        // Спарйт смотрит вправо
        sprite = right;

        Rect=new org.newdawn.slick.geom.Rectangle(x,y,SpriteSizeW,SpriteSizeW);

    }

    @Override
    //Внизу и далее мы можем увидеть материалы не для людей с неустойчивой психикой
    //гравитация
    public void onUpdate(int delta) {
        Rect.setX(x);
        Rect.setY(y);
        FlyForward();
    }

    public void onRender() {
        sprite.draw(-CurrentWorld.SpiderMan.x+x+CurrentWorld.CurrentContainer.getWidth()/2,
                -CurrentWorld.SpiderMan.y+y+CurrentWorld.CurrentContainer.getHeight()/2);
    }
    void onBlockCollide() {
        Block.OnCollide(CurrentWorld.CurrentMap,(int)x+64,(int)y+64);
    }
    void onEntityCollide()
    {

    }
    public void FlyForward() {
       if(side=="right")
           x++;
        else x--;
    }

}
