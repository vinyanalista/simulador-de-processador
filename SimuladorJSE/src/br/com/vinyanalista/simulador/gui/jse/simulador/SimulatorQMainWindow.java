package br.com.vinyanalista.simulador.gui.jse.simulador;
import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import br.com.vinyanalista.simulador.simulation.Animator.AnimationEndListener;
import br.com.vinyanalista.simulador.simulation.Simulation;
import br.com.vinyanalista.simulador.software.Instruction;
import br.com.vinyanalista.simulador.software.ProgramParser;

import com.trolltech.qt.QtBlockedSlot;
import com.trolltech.qt.core.QByteArray;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QTimer;
import com.trolltech.qt.gui.QAbstractItemView.SelectionBehavior;
import com.trolltech.qt.gui.QAbstractItemView.SelectionMode;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QFontDatabase;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QMainWindow;
import com.trolltech.qt.gui.QPaintEvent;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QPalette.ColorRole;
import com.trolltech.qt.gui.QPixmap;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QStatusBar;
import com.trolltech.qt.gui.QTableWidget;
import com.trolltech.qt.gui.QTableWidgetItem;

public class SimulatorQMainWindow extends QMainWindow implements AnimationEndListener {

	private QPixmap fundo;
	private static QFont fonte;
	static QPalette corVerde;
	static QPalette corVermelha;
	QPalette corPreta;
	QTimer Timer;
	
	private int contaPisca=0;
	
	public static QTableWidget tablePrincipal;
	QTableWidgetItem wigitem = null;
	
	private static Simulation simulation;
	QtAnimator animator;
	
	//labels
	public static QLabel labelAcc, labelALU1, labelALU2, labelALUResult, labelPC, labelIROPCODE, 
	labelIROPERAND, labelMAR, labelMBR, labelLed;	
	
	private QPushButton[] bt = new QPushButton[20];
	
	
	public static QLabel genericLabel;
	
	private static QLabel aguardeLabel;
	private static QLabel[] dotLabel = new QLabel[3];

	public static QLabel byteDeExemplo;
	
	QStatusBar statusBar;
	public static QLabel status;

	
	public void initabel(){
		
		List<Instruction> inst = simulation.getProgram().getInstructions();
		
		tablePrincipal = new QTableWidget(this);		
		tablePrincipal.setColumnCount(2);
		tablePrincipal.setRowCount(inst.size());
		tablePrincipal.setAutoScroll(true);
		
		for(int i=0; i<inst.size(); i++){
			for(int j=0; j<2; j++){
				if(j==0){
					wigitem = new QTableWidgetItem(inst.get(i).getOpCode().getValueAsMnemonic());
					tablePrincipal.setItem(i, j, wigitem);
					}else{
					wigitem = new QTableWidgetItem(inst.get(i).getOperand().getValueAsPreferredRepresentation());
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
	
	public static void atualizarPonteiroDeInstrucao() {
		tablePrincipal.setCurrentCell(simulation.getInstructionIndex() - 1, 0);
	}

	private void play(){
		simulation.start();
	}
	
	private void pause(){
		simulation.pause();
		piscador("Aguarde...", true);
	}
	
	private void stop(){
		simulation.stop();
		piscador("Aguarde...", true);
		zeraLabels();
		tablePrincipal.selectRow(0);
	}
	
	private void piscador(String texto, boolean seisPiscadas){
		QPalette cor = new QPalette();
		cor.setColor(QPalette.ColorRole.WindowText, QColor.darkBlue);
		
		QPalette cor2 = new QPalette();
		cor2.setColor(QPalette.ColorRole.WindowText, QColor.gray);
	
		QFont f = new QFont();
		f.setBold(true);
		f.setPointSize(25);
		
		aguardeLabel = new QLabel(this);
		aguardeLabel.setText(texto);
		aguardeLabel.setPalette(corPreta);
		aguardeLabel.setFont(f);
		
		aguardeLabel.setGeometry(240, 185, 200, 200);
		
	
		
		if(Timer == null || !Timer.isActive()){
			Timer = new QTimer();
			if(seisPiscadas){
				Timer.timeout.connect(this, "pisca6()");
				Timer.start(300);
			}else{
				Timer.timeout.connect(this, "pisca2()");
				Timer.start(500);
			}
		}
	}
	
	private void pisca6() {
		if(aguardeLabel.isHidden()){
			aguardeLabel.show();
		}else{
			aguardeLabel.hide();
		}
		if(contaPisca>6){
			contaPisca = 0;
			Timer.stop();
		}else{
			contaPisca++;
		}
		
	}
	
	private void pisca2() {
		if(aguardeLabel.isHidden()){
			aguardeLabel.show();
		}else{
			aguardeLabel.hide();
		}
		if(contaPisca>2){
			contaPisca = 0;
			Timer.stop();
		}else{
			contaPisca++;
		}
		
	}
	
	
	
	@Override
	public void onAnimationEnd() {
		
	}
	
public void table_program_memory(){
		pause();
		showMemoryDialog(MemoryDialog.OPERATION_PROGRAM);
		}


public void table_data_memory(){
	pause();
	showMemoryDialog(MemoryDialog.OPERATION_DATA);
	}
	
	//construtor
	public SimulatorQMainWindow() {
		this.setWindowModified(false);
		this.setWindowTitle("AES");
		
		byteDeExemplo = new QLabel(this);
		byteDeExemplo.hide();

		animator = new QtAnimator();
//		animator.setAnimationEndListener(this);
		simulation = new Simulation(ProgramParser.parseFrom(null), animator);
		
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
		
		corPreta = new QPalette();
		corPreta.setColor(QPalette.ColorRole.WindowText, QColor.black);
		//***************************************************		
		
		//***************************************************	
		//*         Setando Botoes de teste                 *
		//***************************************************
	
		bt[0] = new QPushButton(this);
		bt[0].setText("Play");
		bt[0].clicked.connect(this, "play()");
		bt[0].setIcon(new QIcon("icons/media_playback_start.png"));
		bt[0].setGeometry(830, 15, 50, bt[0].height());
		
		bt[1] = new QPushButton(this);
		bt[1].setText("Pause");
		bt[1].clicked.connect(this, "pause()");
		bt[1].setIcon(new QIcon("icons/media_playback_pause.png"));
		bt[1].setGeometry(830+60, 15, 55, bt[0].height());
		
		bt[2] = new QPushButton(this);
		bt[2].setText("Stop");
		bt[2].clicked.connect(this, "stop()");
		bt[2].setIcon(new QIcon("icons/media_playback_stop.png"));
		bt[2].setGeometry(830+120, 15, 50, bt[0].height());
		
		bt[3] = new QPushButton(this);
		bt[3].setText("Data Memory");
		bt[3].clicked.connect(this, "table_data_memory()");
		bt[3].setGeometry(551, 397, bt[3].width()+35+83, bt[3].height()+38);	
		
		bt[4] = new QPushButton(this);
		bt[4].setText("Program Memory");
		bt[4].clicked.connect(this, "table_program_memory()");
		bt[4].setGeometry(551, 473, bt[4].width()+35+83, bt[4].height()+38);
		
		
		
		
		
		statusBar = statusBar();
		status = new QLabel("Click Play to start simulation.");
		status.setPalette(corPreta);
		statusBar.addWidget(status);
		
		initLabels();
		zeraLabels();
		initabel();
		
		resize(fundo.width()+240, fundo.height() + statusBar.height()-10);
		move(QApplication.desktop().screen().rect().center().x()
				- this.rect().center().x(), QApplication.desktop().screen()
				.rect().center().y()
				- this.rect().center().y());
		this.setFixedSize(this.size());
		show();
	}
	
	public void showMemoryDialog(int operation) {
		MemoryDialog frd = new MemoryDialog(this, operation, simulation);
		frd.exec();
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
		new SimulatorQMainWindow();
		QApplication.exec();
		
	}
	
}