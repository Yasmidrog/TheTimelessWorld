import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import java.util.ArrayList;

public class World {
   public  Spudi SpiderMan;


    public GameContainer CurrentContainer;//текущее игровое окно
    public TiledMap CurrentMap;//карта, на которой играем
  public ArrayList<Bullet> Bullets=new ArrayList<Bullet>();
   public ArrayList<Entity> Entities=new ArrayList<Entity>();//все сущности в мире
    private boolean[][] blocked;

    public void init(TiledMap Map,GameContainer Cont  )throws SlickException{
        SpiderMan= new Spudi(-80,2000 );//наш основной герой
        Entities.add(SpiderMan);

        CurrentMap = Map;
        CurrentContainer=Cont;
        Entities.add(new DrOctopus(100,2000));

        for(Entity ent:Entities) {
            ent.onInit(this);//для всех сущнстей вызываем их версию методов
        }
    }

    public void update(final int delta)throws SlickException{
        new Thread(){
            @Override
            public void run()
            {
                for(Entity ent:Entities) {
                ent.onUpdate(delta);
              }
            }
        }.start();

    }
    public void render()throws SlickException{
        //игрока рисуем на месте, а все остальное двигаем вокруг
        CurrentMap.render(0-(int)SpiderMan.x, 0-(int)SpiderMan.y);
        for(Entity ent : Entities)
            ent.onRender();
    }

    protected void GetBlocked() {//получаем непрходиме блоки
        blocked = new boolean[CurrentMap.getWidth()][CurrentMap.getHeight()];//массив с адресами блоков

        for (int xAxis=0;xAxis<CurrentMap.getWidth(); xAxis++)
        {
            for (int yAxis=0;yAxis<CurrentMap.getHeight(); yAxis++)
            {
                int tileID = CurrentMap.getTileId(xAxis , yAxis, 0);
                String value = CurrentMap.getTileProperty(tileID, "blocked", "false");
                if ("true".equals(value))
                {
                    blocked[xAxis][yAxis] = true;
                }
            }
        }
    }
    //проверяет, проходим ли блок в определенной точке+часть окна, которую мы отодвинули, чтоб игрок был в центре
    public boolean isBlocked(float x, float y) {
        int xBlock = (int) (x+CurrentContainer.getWidth()/2) / 64;
        int yBlock = (int) (y+CurrentContainer.getHeight()/2) / 64;
        return blocked[xBlock][yBlock];
    }
}
