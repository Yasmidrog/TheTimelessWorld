package TheTimeless.game;

import TheTimeless.gui.VTextRender;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import java.io.FileReader;
import java.util.ArrayList;

public class Table extends Entity {
    public String Text="",fileName="";
    private  transient  VTextRender vr=new VTextRender(25,"Sans");
    ArrayList<String> list =new ArrayList();
    private boolean showText=false;
    private transient Graphics gr=new Graphics(Display.getWidth()/2,Display.getHeight()/2);
    public Table(float x, float y,String text) {
        try {
            FileReader fin = new FileReader("data/hints/"+text);
            int c;
            fileName=text;
            while ((c = fin.read()) != -1)
                Text+=(char)c;
            this.x = x;
            this.y = y;
            Name = "HB";
            renderBehind=false;
        }catch(Exception e){e.printStackTrace();}
    }

    @Override
    public void onInit(World world) {
        try {
            CrWld = world;
            sprite = World.ResLoader.getSprite("Table");
            SzW = sprite.getWidth()+45;//get collider
            SzH = sprite.getHeight()+45;
            Rect = new Rectangle(x-20, y-20, SzW, SzH);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override

  public void onUpdate(int delta) {
        if (showText&&(CrWld.SpMn.x - x > SzW + 40|| x - CrWld.SpMn.x > SzW + 40)){
            showText=false;
            return;
        }
        if(this.Rect.intersects(CrWld.SpMn.Rect)&& CrWld.CrCntr.getInput().isKeyPressed(Input.KEY_E)) {
           if(!showText)
            {
                showText=true;
            }else if(showText) {
               showText=false;
            }
        }
    }
    public void onRender() {
        if (sprite != null)
            sprite.draw(-CrWld.SpMn.x + x + CrWld.CrCntr.getWidth() / 2-CrWld.SpMn.SzW /2,
                    -CrWld.SpMn.y + y + CrWld.CrCntr.getHeight() / 2-CrWld.SpMn.SzH /2);

    }
    public void drawTable() {
        int swidth=Display.getDesktopDisplayMode().getWidth();
        int sheight=Display.getDesktopDisplayMode().getHeight();
        if (showText) {
            gr.setColor(new Color(Color.white.getRed(), Color.white.getGreen(), Color.white.getBlue(), 40));
            String result = vr.splitString(Text, swidth/3, false);
            int height = result.split("\n").length * vr.getHeight() + 20;
            gr.fillRect(swidth/2-40-vr.getWidth(result)/2-20,
                    sheight/7, vr.getWidth(result) + 40, height);
            vr.drawString(result, swidth/2-40-vr.getWidth(result)/2,
                    sheight/7, Color.white);
        }
    }
}
