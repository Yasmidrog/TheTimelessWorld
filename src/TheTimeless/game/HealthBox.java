package TheTimeless.game;

import org.newdawn.slick.geom.Rectangle;


/**
 * A box which increases current health of hero
 */
public class HealthBox extends Entity{
    public HealthBox(float x, float y) {

        this.x = x;
        this.y = y;
        Name = "HB";

    }

    @Override
    public void onInit(World world) {
        try {
            CrWld = world;
            sprite = World.ResLoader.getSprite("HealthBox");

            SzW = sprite.getWidth()+5;//get collider
            SzH = sprite.getHeight()+45;

            Rect = new Rectangle(x, y, SzW, SzH);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override

    public void onUpdate(int delta) {
        if(this.Rect.intersects(CrWld.SpMn.Rect)) {
            if( CrWld.SpMn.Health <=90) {
                CrWld.SpMn.Health = 100;
                CrWld.StaticObjects.remove(this);
                //increase hero's healthImage if the box is taken by him
            }
        }
    }
    public void onRender() {
        if (sprite != null)
            sprite.draw(-CrWld.SpMn.x + x + CrWld.CrCntr.getWidth() / 2-CrWld.SpMn.SzW /2,
                    -CrWld.SpMn.y + y + CrWld.CrCntr.getHeight() / 2-CrWld.SpMn.SzH /2);
    }
}

