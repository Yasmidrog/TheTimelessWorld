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
            // Спарйт смотрит вправо

            SzW = 100;//получаем параметры спрайта
            SzH = 500;
            Rect = new Rectangle(x, y, SzW, SzH);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void onRender() {
        try {
            if (this.Rect.intersects(CrWld.SpMn.Rect)) {
                CrWld.exsists=false;
                CrWld.StaticObjects.remove(this);
            }
        }catch(Exception d){
            d.printStackTrace();
        }
    }

}
