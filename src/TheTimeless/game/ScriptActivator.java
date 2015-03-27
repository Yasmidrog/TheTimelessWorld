package TheTimeless.game;


import TheTimeless.scripts.MainScripts;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Rectangle;

import java.lang.reflect.Method;

/**
 * Created by yasmidrog on 27.03.15.
 */
public class ScriptActivator extends Entity {
    private Method method;
    public ScriptActivator(float x, float y,int width,int height,String meth) {
        try {
            this.x = x;
            this.y = y;
            SzH=height;
            SzW=width;
            Name = "SA";
            method = MainScripts.class.getMethod(meth);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInit(World world) {
        try {
            CrWld = world;
            Rect = new Rectangle(x, y, SzW, SzH);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @Override

    public void onUpdate(int delta) {
       if(CrWld.SpMn.Rect.intersects(this.Rect))
       {
           try {
               MainScripts.setCrWorld(CrWld);
               method.invoke(null);
               CrWld.StaticObjects.remove(this);
           }catch (Exception e){
               e.printStackTrace();
           }
       }
    }

}

