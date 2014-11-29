import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Transform;

public class Bullet extends Entity {

    public Entity Killer;

    Transform Trans = Transform.createScaleTransform(1.15f, 1);
    Transform negTrans = Transform.createScaleTransform(-1.15f, 1);
    boolean exists=true;

    public Bullet(float x, float y, Entity killer, float acceleration, float speed) {
        Acceleration = 0.4f;
        Speed = -5;
        OnEarth = false;
        this.x = x;
        this.y = y;
        Speed = speed;
        Acceleration = acceleration;
        Name = "Lazer";
        Killer = killer;
    }

    @Override
    public void onInit(World world) {
        try {
            CurrentWorld = world;
            CurrentWorld.GetBlocked();

            int duration = 30;

            // Спарйт смотрит вправо
            sprite = CurrentWorld.Animations.get("Lazer");

            SpriteSizeW = sprite.getWidth();//получаем параметры спрайта
            SpriteSizeH = sprite.getHeight();

            Rect = new Rectangle(x, y, SpriteSizeW, SpriteSizeH);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override

    public void onUpdate(int delta) {
        FlyForward();
        Rect.setY(y);
        Rect.setX(x);
        for (Entity ent : CurrentWorld.Entities) {
            if (this.Rect.intersects(ent.Rect) ) {
                if(ent.getClass().getName()!=Killer.getClass().getName())
                ent.health -= 30;
                if(ent!=Killer)
                exists=false;
            }
        }
    }

    public void onRender() {
        if (sprite != null) {
            sprite.draw(-CurrentWorld.SpiderMan.x + Rect.getX() + CurrentWorld.CurrentContainer.getWidth() / 2,
                    -CurrentWorld.SpiderMan.y + Rect.getY() + (Killer.SpriteSizeH / 3) +
                            CurrentWorld.CurrentContainer.getHeight() / 2, Rect.getWidth(), 13);
        }else {
            sprite= CurrentWorld.Animations.get("Lazer");
        }
    }
    void onBlockCollide() {
    }
    void onEntityCollide() {
    }

    public void FlyForward() {
        x -= Acceleration * Speed;
        if (sideLocked("right") || sideLocked("left"))
            exists = false;
    }
}
