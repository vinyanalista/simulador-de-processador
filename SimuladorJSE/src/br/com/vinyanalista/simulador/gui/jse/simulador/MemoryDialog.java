package br.com.vinyanalista.simulador.gui.jse.simulador;



import java.util.ArrayList;
import java.util.List;

import br.com.vinyanalista.simulador.simulation.Simulation;

import com.trolltech.qt.core.Qt.Orientation;
import com.trolltech.qt.core.Qt.WindowModality;
import com.trolltech.qt.gui.QAbstractItemView.SelectionBehavior;
import com.trolltech.qt.gui.QAbstractItemView.SelectionMode;
import com.trolltech.qt.gui.QAbstractItemView;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QDialogButtonBox;
import com.trolltech.qt.gui.QFormLayout;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QTabWidget;
import com.trolltech.qt.gui.QTableWidget;
import com.trolltech.qt.gui.QTableWidgetItem;
import com.trolltech.qt.gui.QWidget;

public class MemoryDialog extends QDialog {
	private static Simulation simulation;
	QTableWidgetItem wigitem = null;
	
	private QTabWidget tabWidget;

	public static final int OPERATION_PROGRAM = 0;
	public static final int OPERATION_DATA = 1;

	private class DataTab extends QWidget {
		public DataTab() {
			QHBoxLayout mainLayout = new QHBoxLayout();			
			QTableWidget table = new QTableWidget(this);
			table.setColumnCount(1);
			table.setColumnWidth(0, this.width());
			table.setRowCount(128);
			List<String> labels = new ArrayList<String>();
			for(int i=128; i<256; i++){
				labels.add(String.valueOf(i));
			}
			table.setVerticalHeaderLabels(labels);
			List<String> labels1 = new ArrayList<String>();
			labels1.add("Data");
			table.setHorizontalHeaderLabels(labels1);
			table.setSelectionMode(SelectionMode.SingleSelection);
			table.setSelectionBehavior(SelectionBehavior.SelectItems);
			for(int i=128; i<256; i++){
//				for(int j=0; j<10; j++){
//					wigitem = new QTableWidgetItem((i)+""+(j));
					int address = (i);
					
					if((address>127)&&(address<256)){
						wigitem = new QTableWidgetItem(simulation.getDataMemory().readByte(address).getValueAsPreferredRepresentation());
					table.setItem(i-128, 0, wigitem);
					
				}
			}
			table.setEditTriggers(QAbstractItemView.EditTrigger.NoEditTriggers);
//			table.setGeometry(100, 100, table.width()+390, table.height()-17);
			table.setWindowTitle("Data Memory Table");
			table.setWindowModality(WindowModality.WindowModal);
			
			mainLayout.addWidget(table);
			resize(table.width(), table.height());
			setLayout(mainLayout);
		}
	}

	private class ProgramTab extends QWidget {
		public ProgramTab() {
			QHBoxLayout mainLayout = new QHBoxLayout();			
			QTableWidget table = new QTableWidget(this);
			table.setColumnCount(1);
			table.setColumnWidth(0, this.width());
			table.setRowCount(128);
			List<String> labels = new ArrayList<String>();
			for(int i=0; i<128; i++){
				labels.add(String.valueOf(i));
			}
			table.setVerticalHeaderLabels(labels);
			List<String> labels1 = new ArrayList<String>();
			labels1.add("Instructions");
			table.setHorizontalHeaderLabels(labels1);
			table.setSelectionMode(SelectionMode.SingleSelection);
			table.setSelectionBehavior(SelectionBehavior.SelectItems);
			for(int i=0; i<128; i++){
//				for(int j=0; j<10; j++){
//					wigitem = new QTableWidgetItem((i)+""+(j));
					int address = (i);
					if(address<128){
						wigitem = new QTableWidgetItem(simulation.getProgramMemory().readByte(address).getValueAsPreferredRepresentation());
						table.setItem(i, 0, wigitem);
					}
				}
			table.setEditTriggers(QAbstractItemView.EditTrigger.NoEditTriggers);
//			table.setGeometry(100, 100, table.width()+390, table.height()-17);
			table.setWindowTitle("Program Memory Table");
			table.setWindowModality(WindowModality.WindowModal);
			
			
			mainLayout.addWidget(table);
			resize(table.width(), table.height());
			setLayout(mainLayout);
		}
	}

	public MemoryDialog(QWidget owner, int operation, Simulation simulation) {
		super(owner);
		setWindowTitle("Data/Program Memory");
		// setModal(true);
		this.simulation = simulation;
		tabWidget = new QTabWidget(this);

		tabWidget.addTab(new ProgramTab(), new QIcon(
				"icons/system_run.png"), tr("Program Memory"));
		
		tabWidget.addTab(new DataTab(), new QIcon("icons/server_database.png"),
				tr("Data Memory"));

		tabWidget.setCurrentIndex(operation);
		tabWidget.resize(250, 500);
		setFixedSize(tabWidget.size());
	}
}
