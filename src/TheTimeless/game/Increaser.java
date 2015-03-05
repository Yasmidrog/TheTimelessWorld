package TheTimeless.game;

import org.newdawn.slick.geom.Rectangle;

public class Increaser extends Entity {
    protected int Amount,upMove=0;
    protected boolean exists=true;
    protected int IntColor;
    protected  Increaser(){}
    public Increaser(float x, float y, int amount) {
        this.x = x;
        this.y = y;
        Name = "XP";
        Amount=amount;
        renderBehind=false;
    }

    @Override
    public void onInit(World world) {
        try {
            CrWld = world;
          setColorAndSprite();
            SzW = sprite.getWidth();
            SzH = sprite.getHeight();
            Rect = new Rectangle(x, y, SzW, SzH);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override

    public void onUpdate(int delta) {
        checkCounters();
        if(this.Rect.intersects(CrWld.SpMn.Rect)&&exists) {
           onPick();
        }
        if(!exists){
                y-=0.5;upMove+=1;
            if(upMove>100){
                CrWld.StaticObjects.remove(this);
            }
        }
    }
    public void onRender() {
        if(exists) {
            if (sprite != null)
                sprite.draw(-CrWld.SpMn.x + x + CrWld.CrCntr.getWidth() / 2 - CrWld.SpMn.SzW / 2,
                        -CrWld.SpMn.y + y + CrWld.CrCntr.getHeight() / 2 - CrWld.SpMn.SzH / 2);
        }else {
            Fonts.MediumText.drawString(String.valueOf(Amount),
                (int)(-CrWld.SpMn.x + x + CrWld.CrCntr.getWidth() / 2 - CrWld.SpMn.SzW / 2),
                (int)(-CrWld.SpMn.y + y + CrWld.CrCntr.getHeight() / 2 - CrWld.SpMn.SzH / 2),
                new org.newdawn.slick.Color(IntColor));
       }
    }
public void onPick(){

}
public void setColorAndSprite(){

}

}

