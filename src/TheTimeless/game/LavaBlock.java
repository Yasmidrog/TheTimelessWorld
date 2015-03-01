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
            sprite = World.ResLoader.getSprite("Lava");

            SzW = sprite.getWidth()+5;//get collider
            SzH = sprite.getHeight()+45;

            Rect = new Rectangle(x, y, SzW, SzH);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public void onRender() {
        if (sprite != null) {
            Image im = sprite.getCurrentFrame().copy();
            im.setAlpha(30);
            im.draw(-CrWld.SpMn.x + x + CrWld.CrCntr.getWidth() / 2 - CrWld.SpMn.SzW / 2,
                    -CrWld.SpMn.y + y + CrWld.CrCntr.getHeight() / 2 - CrWld.SpMn.SzH / 2);
        }
    }
    @Override

    public void onUpdate(int delta) {
        for (Creature cr:CrWld.Creatures)
            if(this.Rect.intersects(cr.Rect)) {
                cr.Health-=0.05;
                cr.vx=cr.vx/2;
                cr.vy=cr.vy/2;
            }
    }
}
