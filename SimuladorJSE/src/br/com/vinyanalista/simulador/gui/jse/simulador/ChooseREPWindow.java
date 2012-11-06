package br.com.vinyanalista.simulador.gui.jse.simulador;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter.DEFAULT;

import br.com.vinyanalista.simulador.examples.Example;
import br.com.vinyanalista.simulador.examples.Examples;
import br.com.vinyanalista.simulador.gui.jse.editor.ChooseQWindow;
import br.com.vinyanalista.simulador.simulation.Simulation;

import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QMainWindow;
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QRadioButton;

public class ChooseREPWindow extends QMainWindow {
	QLabel l = new QLabel(this);
	QRadioButton[] radio = new QRadioButton[4];
	QPushButton bt = new QPushButton(this);
	int choose;
	Simulation s;
	SimulatorQMainWindow qwind;

	
	QFont f = new QFont();
	QPalette corPreta = new QPalette();
	
	public static final int REPRESENTATION_RECOMMENDED = 0;
	public static final int REPRESENTATION_DECIMAL = 1;
	public static final int REPRESENTATION_HEX = 2;
	public static final int REPRESENTATION_BINARY = 3;
	
	private void initLabelAndRadios(){
		
		for(int i=0; i<4; i++){
			radio[i] = new QRadioButton(this);
			radio[i].setPalette(corPreta);
			radio[i].setFont(f);
		}
		
		l.setGeometry(45, 20, radio[0].width()+140, radio[0].height());
		l.setText("Choose your prefer representation: ");
		
		radio[REPRESENTATION_RECOMMENDED].setGeometry(65, 40, radio[0].width()+50, radio[0].height());
		radio[REPRESENTATION_RECOMMENDED].setText("Recommended");
		
		radio[REPRESENTATION_BINARY].setGeometry(65, 60, radio[0].width()+50, radio[0].height());
		radio[REPRESENTATION_BINARY].setText("Binary");
		
		radio[REPRESENTATION_DECIMAL].setGeometry(65, 80, radio[0].width()+50, radio[0].height());
		radio[REPRESENTATION_DECIMAL].setText("Decimal");
		
		radio[REPRESENTATION_HEX].setGeometry(65, 100, radio[0].width()+50, radio[0].height());
		radio[REPRESENTATION_HEX].setText("Hexadecimal");
		
		for(int i=0; i<4; i++){
			radio[i].show();
		}
		
		l.show();
	}
	
	private void initButton(){
		bt.setText("Save");
		bt.clicked.connect(this, "save()");
		bt.setIcon(new QIcon("icons/document-save.png"));
		bt.setGeometry(80, 140, 65, bt.height());
		bt.setEnabled(true);
		bt.show();
	}
	
	private void getChoose(){
		for(int i=0; i<4; i++){
			if(radio[i].isChecked()){
				choose = i;
			}
		}
	}
	
	
	
	private void save(){
		getChoose();
		switch (choose) {
		case REPRESENTATION_RECOMMENDED:
			s.setRepresentation(REPRESENTATION_RECOMMENDED);
			qwind.zeraLabels();
			this.close();
			break;
		case REPRESENTATION_BINARY:
			s.setRepresentation(REPRESENTATION_BINARY);
			qwind.zeraLabels();
			this.close();
			break;
		case REPRESENTATION_HEX:
			s.setRepresentation(REPRESENTATION_HEX);
			qwind.zeraLabels();
			this.close();
			break;
		case REPRESENTATION_DECIMAL:
			s.setRepresentation(REPRESENTATION_DECIMAL);
			qwind.zeraLabels();
			this.close();
			break;
		default:
			this.close();
			break;
		}
	}
	
	public ChooseREPWindow(Simulation simulation, SimulatorQMainWindow qwind) {
		f.setBold(true);
		f.setPointSize(10);
		corPreta.setColor(QPalette.ColorRole.WindowText, QColor.black);
		s=simulation;
		this.qwind=qwind;
		
		initLabelAndRadios();
		initButton();
		
		this.setWindowModified(false);
		this.setWindowTitle("Choose your representation");
		resize(250, 200);
		move(QApplication.desktop().screen().rect().center().x()
				- this.rect().center().x(), QApplication.desktop().screen()
				.rect().center().y()
				- this.rect().center().y());
		this.setFixedSize(this.size());
		show();

	}

//	public static void main(String[] args) {
//		QApplication.initialize(args);
//		new ChooseREPWindow(null, null);
//		QApplication.exec();
//	}
}
