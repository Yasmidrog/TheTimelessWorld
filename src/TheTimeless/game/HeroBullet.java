package TheTimeless.game;

import org.newdawn.slick.geom.Rectangle;

public class HeroBullet extends Bullet {
    static  final long serialVersionUID=1488228l;
    private float cosx,siny;
    private int targetx,targety;
    private double alpha;
    public HeroBullet(float x, float y, int screenx,int screeny,Spudi killer, float speed) {
        Speed = speed;
        this.x = x;
        this.y = y;
        Speed = speed;
        Name = "Lazer";
        Killer = killer;
        targetx=screenx;
        targety=screeny;
    }
    @Override
    public void onInit(World world) {
        try {

            CrWld = world;
            sprite = CrWld.ResLoader.getSprite("Lazer");

            SzW = sprite.getWidth()+5;    //get collider
            SzH = sprite.getHeight()+45;

            Rect = new Rectangle(x, y, SzW, SzH);
            int deltax = targetx-(int)(-CrWld.SpMn.x + Rect.getX() + CrWld.CrCntr.getWidth() / 2-CrWld.SpMn.SzW / 2);
            int deltay = targety-(int) (-CrWld.SpMn.y + Rect.getY() + (Killer.SzH / 3) +CrWld.CrCntr.getHeight() / 2-CrWld.SpMn.SzH / 2);

            alpha=Math.atan2(deltay,deltax);
System.out.print(alpha+"\n");
                cosx=(float)Math.cos(alpha);
            siny=(float) Math.sin(alpha);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public void FlyForward() {
        try {

            x+=cosx*20;
            y+=siny*20;
        }catch(Exception e){
            e.printStackTrace();
        }
    }//fly, while there are no obstacles in front
}
