package TheTimeless.game;

import TheTimeless.gui.VTextRender;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import java.io.FileReader;

public class Table extends Entity {
    public String Text="",fileName="";
    private boolean showText=false;
    private transient Graphics gr;
    public Table(float x, float y,String text) {
        try {
          fileName=text;
            this.x = x;
            this.y = y;
            Name = "HB";
            renderBehind=true;
        }catch(Exception e){e.printStackTrace();}
    }

    @Override
    public void onInit(World world) {
        try {
            CrWld = world;
            sprite = World.ResLoader.getSprite("Table");
            SzW = sprite.getWidth()+45;//get collider
            SzH = sprite.getHeight()+45;
            gr=new Graphics(Display.getWidth()/2,Display.getHeight()/2);
            Rect = new Rectangle(x-20, y-20, SzW, SzH);
            FileReader fin = new FileReader("data/hints/"+Loader.Locale+"/"+fileName);
            Text="";
            int c;
            while ((c = fin.read()) != -1)
                Text+=(char)c;
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
        super.onRender();
        drawTable();
    }
    public void drawTable() {
        if (showText) {
            int swidth=Display.getDesktopDisplayMode().getWidth();
            int sheight=Display.getDesktopDisplayMode().getHeight();
            gr.setColor(new Color(Color.white.getRed(), Color.white.getGreen(), Color.white.getBlue(), 40));
            String result = Fonts.TableText.splitString(Text, swidth/3, false);
            int height = result.split("\n").length * Fonts.TableText.getHeight() + 20;
            gr.fillRect(swidth/2-40-Fonts.TableText.getWidth(result)/2-20,
                    sheight/7, Fonts.TableText.getWidth(result) + 40, height);
            Fonts.TableText.drawString(result, swidth/2-40-Fonts.TableText.getWidth(result)/2,
                    sheight/7, Color.white);
        }
    }
}
