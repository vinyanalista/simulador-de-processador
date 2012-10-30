/*Castagna 06/2011*/
package alice.tuprolog.event;

import java.util.*;

@SuppressWarnings("serial")
public class ExceptionEvent extends EventObject{

	private String msg;

	public ExceptionEvent(Object source, String msg_) {
		super(source);
		msg=msg_;
	}

	public String getMsg(){
		return msg;
	}

}
/**/