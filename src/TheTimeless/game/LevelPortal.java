package TheTimeless.game;

import org.newdawn.slick.geom.Rectangle;

/**
 * A portal to the next level
 */
public class LevelPortal extends Entity{
    public LevelPortal(float x, float y){
        this.x = x;
        this.y = y;
        Name = "Lp";
    }
    @Override
    public void onInit(World world) {
        try {
            CrWld = world;
            sprite=World.ResLoader.getSprite("Teleport");
            SzW = sprite.getWidth();
            SzH = sprite.getHeight();
            Rect = new Rectangle(x, y, SzW, SzH);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public void onUpdate(int delta){
        try {
            if (this.Rect.intersects(CrWld.SpMn.Rect)) {
                CrWld.exsists=false;
                CrWld.StaticObjects.remove(this);
            }
        }catch(Exception d){
            d.printStackTrace();
        }
    }
    public void onRender() {
        if (sprite != null)
            sprite.draw(-CrWld.SpMn.x + x + CrWld.CrCntr.getWidth() / 2-CrWld.SpMn.SzW /2,
                    -CrWld.SpMn.y + y + CrWld.CrCntr.getHeight() / 2-CrWld.SpMn.SzH /2);
    }

}
