package br.com.vinyanalista.simulador.gui.jse.simulador;

import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QMainWindow;
import com.trolltech.qt.gui.QPalette;

public class EndProgramWindow extends QMainWindow {
	QLabel message, sucessmsg;
	
	public EndProgramWindow(boolean sucess){
		QFont f = new QFont();
		f.setBold(true);
		f.setPointSize(20);
		
		QPalette corPreta = new QPalette();
		corPreta.setColor(QPalette.ColorRole.WindowText, QColor.black);
		
		message = new QLabel(this);
		sucessmsg = new QLabel(this);
		if(sucess){
			sucessmsg.setText("Sucess: ");
			message.setText("Program ended.");
		}else{
			sucessmsg.setText("Fail: ");
			message.setText("  HLT no found, \n program ended.");
		}
		sucessmsg.setPalette(corPreta);
		sucessmsg.setFont(f);
		message.setPalette(corPreta);
		message.setFont(f);
		
		sucessmsg.setGeometry(100, 10, 500, 50);	
		message.setGeometry(44, 53, 500, 100);
			
		
		this.setWindowModified(false);
		this.setWindowTitle("Program ended");
		resize(300, 200);
		move(QApplication.desktop().screen().rect().center().x()
				- this.rect().center().x(), QApplication.desktop().screen()
				.rect().center().y()
				- this.rect().center().y());
		this.setFixedSize(this.size());
		show();
		
	}

	
//	public static void main(String[] args) {
//		QApplication.initialize(args);
//		new EndProgramWindow(false);
//		QApplication.exec();
//	}
	
}
