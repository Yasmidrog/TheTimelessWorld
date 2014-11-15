import org.newdawn.slick.GameContainer;

import java.util.Timer;

/**
 * Created by Bazil on 26.10.2014.
 */
public interface IControlable {

     void  control( int delta);
     void  interact(int delta);
}
