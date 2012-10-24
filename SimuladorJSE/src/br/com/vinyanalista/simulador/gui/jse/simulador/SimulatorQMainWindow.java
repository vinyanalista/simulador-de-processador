package br.com.vinyanalista.simulador.gui.jse.simulador;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import br.com.vinyanalista.simulador.data.Data;
import br.com.vinyanalista.simulador.simulation.Animation;
import br.com.vinyanalista.simulador.simulation.AnimationType;
import br.com.vinyanalista.simulador.software.Instruction;
import br.com.vinyanalista.simulador.software.ProgramParser;

import com.trolltech.qt.QtBlockedSlot;
import com.trolltech.qt.core.QByteArray;
import com.trolltech.qt.core.QTimer;
import com.trolltech.qt.core.Qt.WindowModality;
import com.trolltech.qt.gui.QAbstractItemView.SelectionBehavior;
import com.trolltech.qt.gui.QAbstractItemView.SelectionMode;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QFontDatabase;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QMainWindow;
import com.trolltech.qt.gui.QPaintEvent;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QPixmap;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QTableWidget;
import com.trolltech.qt.gui.QTableWidgetItem;

public class SimulatorQMainWindow extends QMainWindow {

	private QPixmap fundo;
	private static QFont fonte;
	
	QTableWidget tablePrincipal = new QTableWidget(this);
	QTableWidgetItem wigitem = null;
	
	
	
	public void povoatabela(){
				
		List<Instruction> inst = ProgramParser.parseFrom("").getInstructions();
		
		tablePrincipal.setColumnCount(2);
		tablePrincipal.setRowCount(inst.size());
		
		for(int i=0; i<inst.size(); i++){
			for(int j=0; j<2; j++){
				if(j==0){
					wigitem = new QTableWidgetItem(inst.get(i).getOpCode().getValueAsMnemonic());
					tablePrincipal.setItem(i, j, wigitem);
					}else{
					wigitem = new QTableWidgetItem(inst.get(i).getOperand().getValueAsBinary());
					tablePrincipal.setItem(i, j, wigitem);
				}
			}
		}
		List<String> labels = new ArrayList<String>();
		labels.add(0, "Op_Code");
		labels.add(1, "Operando");
		tablePrincipal.setHorizontalHeaderLabels(labels);
		tablePrincipal.setSelectionMode(SelectionMode.SingleSelection);
		tablePrincipal.setSelectionBehavior(SelectionBehavior.SelectRows);
		tablePrincipal.setColumnWidth(0, 102);
		tablePrincipal.setColumnWidth(1, 102);
		tablePrincipal.selectRow(0);
		tablePrincipal.setGeometry(800, 60, 270, 500);
		tablePrincipal.show();
	}
	
	
	//labels
	public static QLabel labelAcc;
	public static QLabel labelALU1;
	public static QLabel labelALU2;
	public static QLabel labelALUResult;
	public static QLabel labelPC;
	public static QLabel labelIROPCODE;
	public static QLabel labelIROPERAND;
	public static QLabel labelMAR;
	public static QLabel labelMBR;
	public static QLabel labelLed;
	
	
	private QPushButton[] bt = new QPushButton[20];
	public static QLabel genericLabel;

	public static QLabel byteDeExemplo;

	static QPalette corVerde;
	static QPalette corVermelha = new QPalette();

	QTimer mudarCorTimer = new QTimer();
	
	private static final int DURACAO_DO_PISCAR = 100;
	private static final int DURACAO_DA_ANIMACAO = 1000;
	int mudarCorCount = 0;
	
	private void initLabels(){
		labelAcc = new QLabel(this);
		labelALU1 = new QLabel(this);
		labelALU2 = new QLabel(this);
		labelALUResult = new QLabel(this);
		labelPC = new QLabel(this);
		labelIROPCODE = new QLabel(this);
		labelIROPERAND = new QLabel(this);
		labelMAR = new QLabel(this);
		labelMBR = new QLabel(this);
		labelLed = new QLabel(this);
		// http://kdedevelbr.codigolivre.org.br/html/manipulating-images.html
		
		// http://www.codeprogress.com/cpp/libraries/qt/QLabelSetTextColor.php
		
		// http://www.qtcentre.org/threads/8292-placing-widgets-on-top-of-other-widgets
		
		labelLed.setFont(fonte);
		labelAcc.setFont(fonte);
		labelALU1.setFont(fonte);
		labelALU2.setFont(fonte);
		labelALUResult.setFont(fonte);
		labelPC.setFont(fonte);
		labelIROPCODE.setFont(fonte);
		labelIROPERAND.setFont(fonte);
		labelMAR.setFont(fonte);
		labelMBR.setFont(fonte);
		
		labelLed.setPalette(corVermelha);
		labelAcc.setPalette(corVerde);
		labelALU1.setPalette(corVerde);
		labelALU2.setPalette(corVerde);
		labelALUResult.setPalette(corVerde);
		labelPC.setPalette(corVerde);
		labelIROPCODE.setPalette(corVerde);
		labelIROPERAND.setPalette(corVerde);
		labelMAR.setPalette(corVerde);
		labelMBR.setPalette(corVerde);
		
		labelLed.setGeometry(615, 82, labelAcc.width(), labelAcc.height());
		labelALUResult.setGeometry(93, 125, labelAcc.width(), labelAcc.height());
		labelALU1.setGeometry(43, 185, labelAcc.width(), labelAcc.height());
		labelALU2.setGeometry(147, 185, labelAcc.width(), labelAcc.height());
		labelAcc.setGeometry(268, 185, labelAcc.width(), labelAcc.height());
		labelPC.setGeometry(388, 185, labelAcc.width(), labelAcc.height());
		labelIROPCODE.setGeometry(43, 385, labelAcc.width(), labelAcc.height());
		labelIROPERAND.setGeometry(147, 385, labelAcc.width(), labelAcc.height());
		labelMBR.setGeometry(268, 385, labelAcc.width(), labelAcc.height());
		labelMAR.setGeometry(388, 385, labelAcc.width(), labelAcc.height());
		
		labelLed.show();
		labelAcc.show();
		labelALU1.show();
		labelALU2.show();
		labelALUResult.show();
		labelPC.show();
		labelIROPCODE.show();
		labelIROPERAND.show();
		labelMAR.show();
		labelMBR.show();
	}
	
	private void zeraLabels(){
		labelLed.setText("00000000");
		labelAcc.setText("00000000");
		labelALU1.setText("00000000");
		labelALU2.setText("00000000");
		labelALUResult.setText("00000000");
		labelPC.setText("00000000");
		labelIROPCODE.setText("00000000");
		labelIROPERAND.setText("00000000");
		labelMAR.setText("00000000");
		labelMBR.setText("00000000");
	}
	
	public static QLabel getGenericLabel() {
		return genericLabel;
	}

	public static void setGenericLabel(QLabel gener) {
		genericLabel = gener;
	}

	private void mudarCorDaLabel() {
		if (genericLabel.palette().equals(corVerde))
			genericLabel.setPalette(corVermelha);
		else
			genericLabel.setPalette(corVerde);
		genericLabel.show();
		//this.show();
		mudarCorCount++;
		if ((mudarCorCount > 4) || (mudarCorCount == 4)) {
			mudarCorCount = 0;
			mudarCorTimer.stop();
		}
	}

	public void play(){
		Data dt = new Data(110);
		Animation anima = new Animation(AnimationType.ACC_TO_ALU_IN_1, dt );
		
		QtAnimator qt = new QtAnimator();
		qt.animate(anima);
		anima = new Animation(AnimationType.ALU_IN_1_CHANGE, dt );
		qt.animate(anima);
	}
	
	
	public void table_memory_data(){
		
//		DataMemory me = new DataMemory();
				
		QTableWidget table = new QTableWidget();
		table.setColumnCount(1);
		table.setRowCount(256);
		List<String> labels = new ArrayList<String>();
		labels.add(0, "Conteudo");
//		labels.add(1, "");
		table.setHorizontalHeaderLabels(labels);
		table.setSelectionMode(SelectionMode.SingleSelection);
		table.setSelectionBehavior(SelectionBehavior.SelectRows);
		table.setColumnWidth(0, 120);
		table.selectRow(0);
		table.setGeometry(800, 60, 270, 500);
		table.setWindowTitle("Data Table");
		table.setWindowModality(WindowModality.WindowModal);
		table.show();
		}
	
		
	public SimulatorQMainWindow() {
		this.setWindowModified(false);
		this.setWindowTitle("AES");
//		this.setWin
		
		byteDeExemplo = new QLabel(this);
		byteDeExemplo.hide();

		
		
		
		
		//***************************************************	
		//*               Colocando a fonte                 *
		//***************************************************
		// http://forums.netbeans.org/ptopic26804.html
		InputStream fonteStream = SimulatorQMainWindow.class
				.getResourceAsStream("/br/com/vinyanalista/simulador/fonts/DroidSansMono.ttf");
		byte[] bytesDaFonte = null;
		try {
			bytesDaFonte = new byte[fonteStream.available()];
			fonteStream.read(bytesDaFonte);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// http://qt-project.org/forums/viewthread/10660
		// http://lists.trolltech.com/qt-jambi-interest/2007-04/thread00039-0.html
		int id = QFontDatabase.addApplicationFontFromData(new QByteArray(
				bytesDaFonte));
		fonte = new QFont(QFontDatabase.applicationFontFamilies(id).get(0));
		fonte.setBold(true);
		fonte.setPointSize(13);
		
		//***************************************************
		
		
		//***************************************************	
		//*               Colocando o fundo                 *
		//***************************************************
		InputStream imagemStream = SimulatorQMainWindow.class
				.getResourceAsStream("/br/com/vinyanalista/simulador/pictures/fundo.png");
		byte[] bytesDaImagem = null;
		try {
			bytesDaImagem = new byte[imagemStream.available()];
			imagemStream.read(bytesDaImagem);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		fundo = new QPixmap();
		fundo.loadFromData(bytesDaImagem);
		//***************************************************
		
		//***************************************************	
		//*                  Setando cores                  *
		//***************************************************
		corVerde = new QPalette();
		corVerde.setColor(QPalette.ColorRole.WindowText, new QColor(0, 200, 0));

		corVermelha = new QPalette();
		corVermelha.setColor(QPalette.ColorRole.WindowText, QColor.red);
		//***************************************************
	
		
		//***************************************************	
		//*         Setando Botoes de teste                 *
		//***************************************************
//	
//		bt[0] = new QPushButton(this);
//		bt[0].setText("CHANGE_MAR");
//		bt[0].clicked.connect(this, "CHANGE_MAR()");
//		bt[0].setGeometry(820, 100, bt[0].width(), bt[0].height());
//	
//		bt[4] = new QPushButton(this);
//		bt[4].setText("CHANGE_MBR");
//		bt[4].clicked.connect(this, "CHANGE_MBR()");
//		bt[4].setGeometry(820, 250, bt[0].width(), bt[0].height());
//		
//		bt[6] = new QPushButton(this);
//		bt[6].setText("CHANGE_IR_OPCODE");
//		bt[6].clicked.connect(this, "CHANGE_IR_OPCODE()");
//		bt[6].setGeometry(820, 350, bt[0].width(), bt[0].height());
//		
//		bt[7] = new QPushButton(this);
//		bt[7].setText("CHANGE_IR_OPERAND");
//		bt[7].clicked.connect(this, "CHANGE_IR_OPERAND()");
//		bt[7].setGeometry(820, 400, bt[0].width(), bt[0].height());
//		
//		bt[8] = new QPushButton(this);
//		bt[8].setText("CHANGE_IC");
//		bt[8].clicked.connect(this, "CHANGE_PC()");
//		bt[8].setGeometry(820, 450, bt[0].width(), bt[0].height());
//
//		bt[11] = new QPushButton(this);
//		bt[11].setText("CHANGE_ACC");
//		bt[11].clicked.connect(this, "CHANGE_ACC()");
//		bt[11].setGeometry(930, 100, bt[0].width(), bt[0].height());
//		
//		bt[13] = new QPushButton(this);
//		bt[13].setText("CHANGE_ALU_1");
//		bt[13].clicked.connect(this, "CHANGE_ALU_1()");
//		bt[13].setGeometry(930, 200, bt[0].width(), bt[0].height());
//
//		bt[15] = new QPushButton(this);
//		bt[15].setText("CHANGE_ALU_2");
//		bt[15].clicked.connect(this, "CHANGE_ALU_2()");
//		bt[15].setGeometry(930, 300, bt[0].width(), bt[0].height());
//		
//		bt[16] = new QPushButton(this);
//		bt[16].setText("CHANGE_ALU_OUTPUT");
//		bt[16].clicked.connect(this, "CHANGE_ALU_OUTPUT()");
//		bt[16].setGeometry(930, 350, bt[0].width(), bt[0].height());
//		
//		
//		
//		
//		bt[0] = new QPushButton(this);
//		bt[0].setText("PC_TO_MAR");
//		bt[0].clicked.connect(this, "PC_TO_MAR()");
//		bt[0].setGeometry(820, 50, bt[0].width(), bt[0].height());
//		
//		
//		bt[2] = new QPushButton(this);
//		bt[2].setText("MAR_TO_MEMORY");
//		bt[2].clicked.connect(this, "MAR_TO_MEMORY()");
//		bt[2].setGeometry(820, 150, bt[0].width(), bt[0].height());
//		
//		bt[3] = new QPushButton(this);
//		bt[3].setText("MEMORY_TO_MBR");
//		bt[3].clicked.connect(this, "MEMORY_TO_MBR()");
//		bt[3].setGeometry(820, 200, bt[0].width(), bt[0].height());
//		
//		
//		bt[5] = new QPushButton(this);
//		bt[5].setText("MBR_TO_IR_1");
//		bt[5].clicked.connect(this, "MBR_TO_IR_1()");
//		bt[5].setGeometry(820, 300, bt[0].width(), bt[0].height());
//		
//		
//		
//		bt[9] = new QPushButton(this);
//		bt[9].setText("IR_TO_MAR");
//		bt[9].clicked.connect(this, "IR_TO_MAR()");
//		bt[9].setGeometry(820, 500, bt[0].width(), bt[0].height());
//		
//		bt[10] = new QPushButton(this);
//		bt[10].setText("MBR_TO_ACC");
//		bt[10].clicked.connect(this, "MBR_TO_ACC()");
//		bt[10].setGeometry(930, 50, bt[0].width(), bt[0].height());
//		
//		bt[12] = new QPushButton(this);
//		bt[12].setText("ACC_TO_ALU_1");
//		bt[12].clicked.connect(this, "ACC_TO_ALU_1()");
//		bt[12].setGeometry(930, 150, bt[0].width(), bt[0].height());
//	
//		bt[14] = new QPushButton(this);
//		bt[14].setText("ACC_TO_ALU_2");
//		bt[14].clicked.connect(this, "ACC_TO_ALU_2()");
//		bt[14].setGeometry(930, 250, bt[0].width(), bt[0].height());
//
//		bt[17] = new QPushButton(this);
//		bt[17].setText("ALU_TO_ACC");
//		bt[17].clicked.connect(this, "ALU_TO_ACC()");
//		bt[17].setGeometry(930, 400, bt[0].width(), bt[0].height());
//		
//		bt[18] = new QPushButton(this);
//		bt[18].setText("CHANGE_LED");
//		bt[18].clicked.connect(this, "CHANGE_LED()");
//		bt[18].setGeometry(930, 450, bt[0].width(), bt[0].height());
//		
//		bt[19] = new QPushButton(this);
//		bt[19].setText("MBR_TO_IR_OPERAND");
//		bt[19].clicked.connect(this, "MBR_TO_IR_2()");
//		bt[19].setGeometry(930, 500, bt[0].width(), bt[0].height());
//
		
		
		
		
		bt[0] = new QPushButton(this);
		bt[0].setText("Play");
		bt[0].clicked.connect(this, "play()");
		bt[0].setGeometry(805, 20, bt[0].width(), bt[0].height());
		
		bt[1] = new QPushButton(this);
		bt[1].setText("Data");
//		bt[1].setFont(fonte);
		bt[1].clicked.connect(this, "table_memory_data()");
		bt[1].setGeometry(612, 416, bt[1].width(), bt[1].height());
				
//		QSignalMapper mapper;
//		
//		     Object sourceObject;
//			sourceObject.loaded.connect(mapper, "map()");
//		     mapper.setMapping(sourceObject, sourceObject);
		
		
//		bt[2] = new QPushButton(this);
//		bt[2].setText("teste");
//		bt[2].clicked.connect("11111111", "ACC_CHANGE()");
//		bt[2].setGeometry(805, 100, bt[0].width(), bt[0].height());
//		
		
		
		
		resize(fundo.width()+250, fundo.height());
		show();
	}

	@Override
	@QtBlockedSlot
	protected void paintEvent(QPaintEvent arg__1) {
		super.paintEvent(arg__1);
		QPainter painter = new QPainter(this);
		painter.drawPixmap(0, 0, fundo);
	}

	public static void main(String[] args) {
		QApplication.initialize(args);
		SimulatorQMainWindow anima = new SimulatorQMainWindow();
		anima.initLabels();
		anima.zeraLabels();
		anima.povoatabela();
		QApplication.exec();
	}
}