package br.com.vinyanalista.simulador.gui.jse.simulador;

import java.awt.print.Printable;
import java.util.ArrayList;
import java.util.List;

import br.com.vinyanalista.simulador.examples.Example;
import br.com.vinyanalista.simulador.examples.Examples;
import br.com.vinyanalista.simulador.gui.jse.editor.MyAction;
import br.com.vinyanalista.simulador.software.Program;

import com.trolltech.qt.core.QLocale;
import com.trolltech.qt.gui.QAbstractItemView;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QMainWindow;
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QTableWidget;
import com.trolltech.qt.gui.QTableWidgetItem;
import com.trolltech.qt.gui.QToolBar;
import com.trolltech.qt.gui.QAbstractItemView.SelectionBehavior;
import com.trolltech.qt.gui.QAbstractItemView.SelectionMode;
import com.trolltech.qt.gui.QKeySequence.StandardKey;
import com.trolltech.qt.gui.QWidgetItem;

public class ChooseQWindow extends QMainWindow {
	
	private QLabel message;
	private QTableWidget table;
	private QTableWidgetItem wigitem;
	private int rowCount = 4;
//	private Examples sam = new Examples();
	private Program p = new Program();
	
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
		table.setGeometry(5, message.height()+10, 285, 150);
		table.show();
	}
	
	private void initToolBar(){
		QToolBar toolbar = addToolBar("main toolbar");
		QAction playAction = new MyAction(
				new QIcon("icons/arrow_right.png"), "Inicia a simulação", this, this,
				"play()");//TODO
		playAction.setShortcut(StandardKey.Print);
		toolbar.addAction(playAction);
		toolbar.setFixedSize(40, 40);
//		toolbar.setF
		toolbar.setGeometry(100, 0, 40, 40);
	}
	
	private void play() {
		getChoose();
		new SimulatorQMainWindow(p);
	}

	private void getChoose(){
		
		
		List<QTableWidgetItem> list = table.selectedItems();
		if(list.size()==0){
			System.out.println("Zerada");
		}else{
			System.out.println(list.get(0).row());
		if(list.get(0).row()==0){
			p=Examples.getExample(Example.ADD);
		}
		if(list.get(0).row()==1){
			p=Examples.getExample(Example.NOT);
		}
		if(list.get(0).row()==2){
			p=Examples.getExample(Example.OVERFLOW);
		}
		if(list.get(0).row()==3){
			p=Examples.getExample(Example.NOT);
		}
		}
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
		message.setGeometry(45, 0, message.width()+500, message.height());
		
		initTable();
		initToolBar();
		
		
		this.setWindowModified(false);
		this.setWindowTitle("Choose your sample");
		resize(300, 200);
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
