/*Castagna 06/2011 >*/
package alice.tuprolog.event;

import java.util.EventListener;

public interface ExceptionListener extends EventListener {
    public abstract void onException(ExceptionEvent e);
}
/**/