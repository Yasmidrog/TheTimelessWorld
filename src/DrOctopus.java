import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Shape;

public class DrOctopus extends Entity implements IAgressive {

    public Shape BigRect;

    Graphics HealthBack = new org.newdawn.slick.Graphics();
    Graphics HealthFore = new org.newdawn.slick.Graphics();

    public DrOctopus(float x, float y) {
        Acceleration = 0.4f;
        Speed = 3;
        OnEarth = false;
        this.x = x;
        this.y = y;
        health = 100;
        Name = "DrOCtopus";
    }

    @Override
    public void onInit(World world) {
        try {
            CurrentWorld = world;
            CurrentWorld.GetBlocked();


            upleft = CurrentWorld.Animations.get("DrOctUpLeft");
            upright = CurrentWorld.Animations.get("DrOctUp");
            left = CurrentWorld.Animations.get("DrOctLeft");
            right = CurrentWorld.Animations.get("DrOctRight");

            // Спарйт смотрит вправо
            sprite = right;
            SpriteSizeH = right.getHeight();
            SpriteSizeW = right.getWidth();
            Rect = new org.newdawn.slick.geom.Rectangle(x, y, SpriteSizeW, SpriteSizeH);

        } catch (Exception ex) {
        }
    }

    @Override
    public void onUpdate(int delta) {
        Rect.setY(y);
        Rect.setX(x);
        OnEarth = sideLocked("down");
        if (OnEarth) {

            if (sprite == left || sprite == upleft)
                sprite = left;
            else if (sprite == right || sprite == right)
                sprite = right;
            followPlayer();


        } else if (!OnEarth) {

            if (sprite == left || sprite == upleft)
                sprite = upleft;
            else if (sprite == right || sprite == right)
                sprite = upright;
            y += Acceleration * Speed * 0.25;
        }
        for (Entity ent : CurrentWorld.Entities) {
            if (ent instanceof IControlable && this.Rect.intersects(ent.Rect))
                ent.health -= 0.05;
        }
    }


    public void onRender() {
        if (sprite != null)
            sprite.draw(-CurrentWorld.SpiderMan.x + x + CurrentWorld.CurrentContainer.getWidth() / 2,
                    -CurrentWorld.SpiderMan.y + y + CurrentWorld.CurrentContainer.getHeight() / 2);

        HealthBack.setColor(org.newdawn.slick.Color.cyan);
        HealthBack.fillRect((int) -CurrentWorld.SpiderMan.x + x + CurrentWorld.CurrentContainer.getWidth() / 2,
                (int) -CurrentWorld.SpiderMan.y + y + CurrentWorld.CurrentContainer.getHeight() / 2, SpriteSizeW, 5);
        HealthFore.setColor(org.newdawn.slick.Color.red);
        HealthFore.fillRect((int) -CurrentWorld.SpiderMan.x + x + CurrentWorld.CurrentContainer.getWidth() / 2,
                (int) -CurrentWorld.SpiderMan.y + y + CurrentWorld.CurrentContainer.getHeight() / 2, SpriteSizeW * health / 100, 5);
    }

    @Override
    protected void OnBlockCollide() {
        Block.OnCollide(CurrentWorld.CurrentMap, (int) x + 32, (int) y + 32);
    }

    @Override
    protected void OnEntityCollide(Entity ent) {
    }

    @Override
    public void followPlayer() {


        if (CurrentWorld.SpiderMan.x - x > 60) {
            if (!sideLocked("right")) {
                x += 0.2 * Speed;
                sprite = right;
            }
        } else if (x - CurrentWorld.SpiderMan.x > 138) {
            if (!sideLocked("left")) {
                x -= 0.2 * Speed;
                sprite = left;
            }
        }
    }
}

