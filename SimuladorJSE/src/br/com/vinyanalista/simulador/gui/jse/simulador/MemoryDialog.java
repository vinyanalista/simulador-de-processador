package br.com.vinyanalista.simulador.gui.jse.simulador;



import java.util.ArrayList;
import java.util.List;

import br.com.vinyanalista.simulador.simulation.Simulation;

import com.trolltech.qt.core.Qt.Orientation;
import com.trolltech.qt.core.Qt.WindowModality;
import com.trolltech.qt.gui.QAbstractItemView.SelectionBehavior;
import com.trolltech.qt.gui.QAbstractItemView.SelectionMode;
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

	public static final int OPERATION_DATA = 0;
	public static final int OPERATION_PROGRAM = 1;
	

	private class DataTab extends QWidget {
		public DataTab() {
			QHBoxLayout mainLayout = new QHBoxLayout();			
			QTableWidget table = new QTableWidget(this);
			table.setColumnCount(10);
			table.setRowCount(14);
			List<String> labels = new ArrayList<String>();
			for(int i=12; i<26; i++){
				labels.add(String.valueOf(i));
			}
			table.setVerticalHeaderLabels(labels);
			List<String> labels1 = new ArrayList<String>();
			for(int i=0; i<10; i++){
				labels1.add(String.valueOf(i));
			}
			table.setHorizontalHeaderLabels(labels1);
			table.setSelectionMode(SelectionMode.SingleSelection);
			table.setSelectionBehavior(SelectionBehavior.SelectItems);
			for(int i=12; i<26; i++){
				for(int j=0; j<10; j++){
//					wigitem = new QTableWidgetItem((i)+""+(j));
					int address = Integer.parseInt((i)+""+(j));
					
					if((address>127)&&(address<256)){
						wigitem = new QTableWidgetItem(simulation.getDataMemory().readByte(address).getValueAsPreferredRepresentation());
					table.setItem(i-12, j, wigitem);
					}
				}
			}
			table.setGeometry(100, 100, table.width()+390, table.height()-17);
			table.setWindowTitle("Data Memory Table");
			table.setWindowModality(WindowModality.WindowModal);
			
			mainLayout.addWidget(table);
			setLayout(mainLayout);
		}
	}

	private class ProgramTab extends QWidget {
		public ProgramTab() {
			QHBoxLayout mainLayout = new QHBoxLayout();
			QTableWidget table = new QTableWidget();
			table.setColumnCount(10);
			table.setRowCount(13);
			List<String> labels = new ArrayList<String>();
			for(int i=0; i<13; i++){
				labels.add(String.valueOf(i));
			}
			table.setHorizontalHeaderLabels(labels);
			table.setVerticalHeaderLabels(labels);
			table.setSelectionMode(SelectionMode.SingleSelection);
			table.setSelectionBehavior(SelectionBehavior.SelectItems);
			for(int i=0; i<13; i++){
				for(int j=0; j<10; j++){
					int address = Integer.parseInt((i)+""+(j));
					if(address<128){
						wigitem = new QTableWidgetItem(simulation.getProgramMemory().readByte(address).getValueAsPreferredRepresentation());
						table.setItem(i, j, wigitem);
					}
				}
			}
			table.setGeometry(100, 100, table.width()+390, table.height()-17);
			table.setWindowTitle("Program Memory Table");
			table.setWindowModality(WindowModality.WindowModal);
			
			
			mainLayout.addWidget(table);
			setLayout(mainLayout);
		}
	}

	public MemoryDialog(QWidget owner, int operation, Simulation simulation) {
		super(owner);
		setWindowTitle("Data/Program Memory");
		// setModal(true);
		this.simulation = simulation;
		tabWidget = new QTabWidget(this);

		tabWidget.addTab(new DataTab(), new QIcon("icons/edit-find.png"),
				tr("Data Memory"));

		tabWidget.addTab(new ProgramTab(), new QIcon(
				"icons/edit-find-replace.png"), tr("Program Memory"));

		tabWidget.setCurrentIndex(operation);
		tabWidget.resize(1050, 500);
		setFixedSize(tabWidget.size());
	}
}
