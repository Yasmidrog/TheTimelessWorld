package TheTimeless.game;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

/**
 * Created by yasmidrog on 01.03.15.
 */
public class LavaBlock extends Entity {
    public LavaBlock(float x, float y) {

        this.x = x;
        this.y = y;
        Name = "LB";
        renderBehind=false;
    }

    @Override
    public void onInit(World world) {
        try {
            CrWld = world;

            sprite=World.ResLoader.getSprite("Lava");
            SzW = 64;//get collider
            SzH = 64;

            Rect = new Rectangle(x, y, SzW, SzH);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public void onRender() {
      CrWld.CrCntr.getGraphics().drawAnimation(sprite,-CrWld.SpMn.x + x + CrWld.CrCntr.getWidth() / 2 - CrWld.SpMn.SzW / 2,
       -CrWld.SpMn.y + y + CrWld.CrCntr.getHeight() / 2 - CrWld.SpMn.SzH / 2, new Color(191, 48, 0, 120));

    }
    @Override

    public void onUpdate(int delta) {
        for (Creature cr:CrWld.Creatures)
            if(this.Rect.intersects(cr.Rect)) {
                cr.Health-=0.05;
            }
    }
}
