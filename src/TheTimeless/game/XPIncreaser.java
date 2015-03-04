package TheTimeless.game;

import org.newdawn.slick.geom.Rectangle;

public class XPIncreaser extends Entity {
    private int Amount,upMove=0;
    private boolean exists=true;
    public XPIncreaser(float x, float y,int amount) {

        this.x = x;
        this.y = y;
        Name = "XP";
        Amount=amount;

    }

    @Override
    public void onInit(World world) {
        try {
            CrWld = world;
            sprite = World.ResLoader.getSprite("Coin");

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
            CrWld.SpMn.XP+=Amount;
           exists=false;
        }
        if(!exists){
            System.out.println(upMove);
                y-=0.5;upMove+=1;
            if(upMove>100){
                System.out.println("not");
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
                org.newdawn.slick.Color.blue);
       }
    }

}

