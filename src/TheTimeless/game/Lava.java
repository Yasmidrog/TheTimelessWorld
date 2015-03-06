package TheTimeless.game;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Rectangle;

import java.util.ArrayList;

/**
 * Created by yasmidrog on 01.03.15.
 */
public class Lava extends Entity {
    private  transient ArrayList<Creature> swimmedIn;
    private transient Animation topSprite,middleSprite;
    public Lava(float x, float y, int width, int height) {

        this.x = x;
        this.y = y;
        Name = "LB";
        SzH = height;
        SzW = width;
        renderBehind = false;
    }

    @Override
    public void onInit(World world) {
        try {
            CrWld = world;
            swimmedIn = new ArrayList<Creature>();
            middleSprite = World.ResLoader.getSprite("Lava");
            topSprite=World.ResLoader.getSprite("LavaTop");
            Rect = new Rectangle(x, y, SzW, SzH);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onRender() {
        int starty = (int) (-CrWld.SpMn.y + y + CrWld.CrCntr.getHeight() / 2 - CrWld.SpMn.SzH / 2);
        int startx = (int) (-CrWld.SpMn.x + x + CrWld.CrCntr.getWidth() / 2 - CrWld.SpMn.SzW / 2);
        for (int xa = startx; xa <= startx + SzW; xa += 64) {
            for (int ya = starty; ya <= starty + SzH; ya += 64) {
            if(ya==starty) sprite = topSprite;
                      else sprite = middleSprite;
                //if there  is the top block on the x, draw top block
           CrWld.CrCntr.getGraphics().drawAnimation(sprite, xa, ya, new Color(191, 48, 0, 155));
            }
        }
    }

    @Override
    public void onUpdate(int delta) {

        for (Creature cr : CrWld.Creatures) {
            //add all the creatures in the lava to the list if swimmed and reduce speed

            if (cr.Rect.intersects(this.Rect)) {
                cr.changeHealth(-0.2f);
                if (!(swimmedIn.contains(cr))) {
                    swimmedIn.add(cr);
                    cr.Speed/= 3.2f;
                    cr.Gravity/=3;
                }
                if(cr.Health<=0){
                    if (swimmedIn.contains(cr)) {
                        swimmedIn.remove(cr);
                        cr.Speed *= 3.2f;
                        cr.Gravity*=3;
                    }
                }
            }
            //all the creatures int the list but not swimming gettheir speed back
            if (!cr.Rect.intersects(this.Rect)) {
                if (swimmedIn.contains(cr)) {
                    swimmedIn.remove(cr);
                    cr.Speed *= 3.2f;
                    cr.Gravity*=3;
                }
            }
        }

    }
}
