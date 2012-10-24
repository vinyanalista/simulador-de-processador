package br.com.vinyanalista.simulador.gui.jse.simulador;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import br.com.vinyanalista.simulador.data.Data;
import br.com.vinyanalista.simulador.simulation.Animation;
import br.com.vinyanalista.simulador.simulation.AnimationType;
import br.com.vinyanalista.simulador.simulation.Animator.AnimationEndListener;
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

public class SimulatorQMainWindow extends QMainWindow implements AnimationEndListener {

	private QPixmap fundo;
	private static QFont fonte;
	static QPalette corVerde;
	static QPalette corVermelha = new QPalette();
	
	QTableWidget tablePrincipal = new QTableWidget(this);
	QTableWidgetItem wigitem = null;
	
	QtAnimator qt;
	
	//labels
	public static QLabel labelAcc, labelALU1, labelALU2, labelALUResult, labelPC, labelIROPCODE, 
	labelIROPERAND, labelMAR, labelMBR, labelLed;	
	
	private QPushButton[] bt = new QPushButton[20];
	public static QLabel genericLabel;

	public static QLabel byteDeExemplo;

	
	public void initabel(){
		
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

	public static void setGenericLabel(QLabel gener) {
		genericLabel = gener;
	}

	public void play(){
		Data dt = new Data(110);
		Animation anima = new Animation(AnimationType.ACC_TO_ALU_IN_1, dt );
		
		qt.animate(anima);
		
	}
	
	@Override
	public void onAnimationEnd() {
		Data dt = new Data(110);
		Animation anima = new Animation(AnimationType.ALU_IN_1_CHANGE, dt );
	
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
	
	//construtor
	public SimulatorQMainWindow() {
		this.setWindowModified(false);
		this.setWindowTitle("AES");
		
		byteDeExemplo = new QLabel(this);
		byteDeExemplo.hide();

		qt = new QtAnimator();
		qt.setAnimationEndListener(this);
		
		
		
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
		anima.initabel();
		QApplication.exec();
	}
	
}