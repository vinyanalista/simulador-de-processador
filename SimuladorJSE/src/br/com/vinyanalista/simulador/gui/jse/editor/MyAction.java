package br.com.vinyanalista.simulador.gui.jse.editor;

import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.*;

public class MyAction extends QAction {
	public MyAction(QIcon icon, String name, QObject parent, Object receiver,
			String method) {
		super(icon, name, parent);
		triggered.connect(receiver, method);
	}
}