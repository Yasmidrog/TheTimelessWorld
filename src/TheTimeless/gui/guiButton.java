package TheTimeless.gui;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;


public class guiButton extends guiElement {


    public guiButton(GameContainer cntr, String str, VTextRender vr, int x, int y) {
        this.container = cntr;
        string=str;
        shown=true;
        this.render=vr;
        input = container.getInput();
        rect=new Rectangle(x,y,vr.getWidth(str),vr.getHeight());
        setLocation(x, y);
        listener.start();
    }

    public void render() throws SlickException{
            if(shown) {
                if(active){
                    render.drawString( string, (int) rect.getX(), (int) rect.getY(),Color.white);
             }else {
             render.drawString( string, (int) rect.getX(), (int) rect.getY(),
                                                  new Color(275,255,254,70));
             }
          }
      }


        public void setLocation(int x, int y){
            rect.setX(x);
            rect.setY(y);
        }




}


