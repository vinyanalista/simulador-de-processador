package br.com.vinyanalista.simulador.gui.jse.simulador;

import java.util.ArrayList;
import java.util.List;

import br.com.vinyanalista.simulador.examples.Example;

import com.trolltech.qt.gui.QAbstractItemView;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QMainWindow;
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QTableWidget;
import com.trolltech.qt.gui.QTableWidgetItem;
import com.trolltech.qt.gui.QAbstractItemView.SelectionBehavior;
import com.trolltech.qt.gui.QAbstractItemView.SelectionMode;

public class ChooseQWindow extends QMainWindow {
	
	private QLabel message;
	private QTableWidget table;
	private QTableWidgetItem wigitem;
	private int rowCount = 4;
	
	private void initTable(){	
		table = new QTableWidget(this);
		table.setColumnCount(1);
		table.setRowCount(rowCount);
		table.setAutoScroll(true);
		List<String> samplers = new ArrayList<String>();
		samplers.add(Example.ADD.toString());
		samplers.add(Example.SUB.toString());
		samplers.add(Example.OVERFLOW.toString());
		samplers.add(Example.NOT.toString());
		for(int i=0; i<samplers.size(); i++){
					wigitem = new QTableWidgetItem(samplers.get(i));
					table.setItem(i, 0, wigitem);
			}
		List<String> labels = new ArrayList<String>();
		labels.add(0, "Samplers");
		
		table.setHorizontalHeaderLabels(labels);
		
		table.setSelectionMode(SelectionMode.SingleSelection);
		table.setSelectionBehavior(SelectionBehavior.SelectRows);
		table.setColumnWidth(0, 900);
		table.setEditTriggers(QAbstractItemView.EditTrigger.NoEditTriggers);
		table.selectRow(0);
		table.setGeometry(5, message.height(), 180, 900);
		table.show();
	}
	
	public ChooseQWindow(){
		QFont f = new QFont();
		f.setBold(true);
		f.setPointSize(10);
		
		QPalette corPreta = new QPalette();
		corPreta.setColor(QPalette.ColorRole.WindowText, QColor.black);
		
		message = new QLabel(this);
		message.setText("Escolha o exemplo a ser simulado: ");
		message.setPalette(corPreta);
		message.setFont(f);
		message.setGeometry(5, 0, message.width()+500, message.height());
		
		initTable();
		
		
		this.setWindowModified(false);
		this.setWindowTitle("Choose your sample");
		resize(500, 600);
		move(QApplication.desktop().screen().rect().center().x()
				- this.rect().center().x(), QApplication.desktop().screen()
				.rect().center().y()
				- this.rect().center().y());
		this.setFixedSize(this.size());
		show();
		
	}
	
	
	public static void main(String[] args) {
		QApplication.initialize(args);
		new ChooseQWindow();
		QApplication.exec();
	}
	
}
