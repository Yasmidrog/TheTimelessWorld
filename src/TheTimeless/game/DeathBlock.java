package TheTimeless.game;

import org.newdawn.slick.geom.Rectangle;

public class DeathBlock extends Entity {
    public DeathBlock(float x, float y) {

        this.x = x;
        this.y = y;
        Name = "DB";

    }

    @Override
    public void onInit(World world) {
        try {
            CrWld = world;
            SzW =64;
            SzH = 64;
            Rect = new Rectangle(x, y, SzW, SzH);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override

    public void onUpdate(int delta) {
        for (Creature cr:CrWld.Creatures)
        if(this.Rect.intersects(cr.Rect)) {
           cr.Health=0;
        }
    }
}
