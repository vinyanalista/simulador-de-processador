package br.com.vinyanalista.simulador.gui.jse.simulador;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import br.com.vinyanalista.simulador.examples.Example;
import br.com.vinyanalista.simulador.examples.Examples;
import br.com.vinyanalista.simulador.hardware.InstructionRegister;
import br.com.vinyanalista.simulador.hardware.Processor;
import br.com.vinyanalista.simulador.simulation.Animator.AnimationListener;
import br.com.vinyanalista.simulador.simulation.Simulation;
import br.com.vinyanalista.simulador.simulation.Simulation.SimulationListener;
import br.com.vinyanalista.simulador.software.Instruction;
import br.com.vinyanalista.simulador.software.Program;

import com.trolltech.qt.QtBlockedSlot;
import com.trolltech.qt.core.QByteArray;
import com.trolltech.qt.gui.QAbstractItemView;
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
import com.trolltech.qt.gui.QPixmap;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QStatusBar;
import com.trolltech.qt.gui.QTableWidget;
import com.trolltech.qt.gui.QTableWidgetItem;

public class SimulatorQMainWindow extends QMainWindow implements AnimationListener, SimulationListener {

	private QPixmap fundo;
	private static QFont fonte;
	static QPalette corVerde;
	static QPalette corVermelha;
	QPalette corPreta;
	
	public static QTableWidget tablePrincipal;
	QTableWidgetItem wigitem = null;
	
	private static Simulation simulation;
	QtAnimator animator;
	
	private Program program;
	
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
						if (inst.get(i).getOperand() != null) {
							wigitem = new QTableWidgetItem(inst.get(i).getOperand().getValueAsPreferredRepresentation());
							tablePrincipal.setItem(i, j, wigitem);
						}
				}
			}
		}
		List<String> labels = new ArrayList<String>();
		labels.add(0, "Op_Code");
		labels.add(1, "Operando");
		tablePrincipal.setHorizontalHeaderLabels(labels);
		tablePrincipal.setSelectionMode(SelectionMode.SingleSelection);
		tablePrincipal.setSelectionBehavior(SelectionBehavior.SelectRows);
		tablePrincipal.setColumnWidth(0, 65);
		tablePrincipal.setColumnWidth(1, 65);
		tablePrincipal.setEditTriggers(QAbstractItemView.EditTrigger.NoEditTriggers);
		tablePrincipal.selectRow(0);
		tablePrincipal.setGeometry(800, 90, 180, 500);
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
		labelAcc.setText(simulation.getProcessor().getRegister(Processor.ACC).getValue().getValueAsPreferredRepresentation());
		labelALU1.setText(simulation.getProcessor().getALU().getIn1().getValueAsPreferredRepresentation());
		labelALU2.setText(simulation.getProcessor().getALU().getIn2().getValueAsPreferredRepresentation());
		labelALUResult.setText(simulation.getProcessor().getALU().getOut().getValueAsPreferredRepresentation());
		labelPC.setText(simulation.getProcessor().getRegister(Processor.PC).getValue().getValueAsPreferredRepresentation());
		labelIROPCODE.setText(((InstructionRegister)simulation.getProcessor().getRegister(Processor.IR)).getInstruction().getOpCode().getValueAsPreferredRepresentation());
		labelIROPERAND.setText(((InstructionRegister)simulation.getProcessor().getRegister(Processor.IR)).getInstruction().getOperand().getValueAsPreferredRepresentation());
		labelMAR.setText(simulation.getProcessor().getRegister(Processor.MAR).getValue().getValueAsPreferredRepresentation());
		labelMBR.setText(simulation.getProcessor().getRegister(Processor.MBR).getValue().getValueAsPreferredRepresentation());
	}

	public static void setGenericLabel(QLabel gener) {
		genericLabel = gener;
	}
	
	public static void atualizarPonteiroDeInstrucao() {
		tablePrincipal.setCurrentCell(simulation.getInstructionIndex() - 1, 0);
	}

	private void play(){
		simulation.start();
		bt[0].hide();
		bt[1].show();
		bt[2].setEnabled(true);
	}
	
	private void pause(){
		exibirAguarde("Aguarde...");
		simulation.pause();
		bt[1].hide();
		bt[0].show();
	}
	
	private void stop(){
		exibirAguarde("Aguarde...");
		simulation.stop();
		zeraLabels();
		tablePrincipal.selectRow(0);
		bt[2].setEnabled(false);
		bt[1].hide();
		bt[0].show();
	}
	
	private void config(){
		exibirAguarde("Aguarde...");
		simulation.stop();
		zeraLabels();
		tablePrincipal.selectRow(0);
		bt[2].setEnabled(false);
		bt[1].hide();
		bt[0].show();
		
	}
	
	private void exibirAguarde(String texto){
		aguardeLabel.setText(texto);
		aguardeLabel.show();
	}
	
	private void esconderAguarde(){
		aguardeLabel.hide();
	}
	
public void table_program_memory(){
		if (!simulation.isPaused() && !simulation.isStopped())
		pause();
		showMemoryDialog(MemoryDialog.OPERATION_PROGRAM);
		}

public void table_data_memory(){
	if (!simulation.isPaused() && !simulation.isStopped())
	pause();
	showMemoryDialog(MemoryDialog.OPERATION_DATA);
	}
	
	//construtor
	public SimulatorQMainWindow(Program prog) {
		this.setWindowModified(false);
		this.setWindowTitle("AES Processor Simulator");
		setWindowIcon(new QIcon("icons/cpu.png"));
		
		byteDeExemplo = new QLabel(this);
		byteDeExemplo.hide();

		animator = new QtAnimator();
		animator.addAnimationListener(this);
		
		this.program = prog;
		
		simulation = new Simulation(program, animator);
		simulation.addSimulationListener(this);
		
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
		bt[0].setGeometry(815, 15, 55, bt[0].height());
		
		bt[1] = new QPushButton(this);
		bt[1].setText("Pause");
		bt[1].clicked.connect(this, "pause()");
		bt[1].setIcon(new QIcon("icons/media_playback_pause.png"));
		bt[1].setGeometry(815, 15, 55, bt[0].height());
		bt[1].hide();
		
		bt[2] = new QPushButton(this);
		bt[2].setText("Stop");
		bt[2].clicked.connect(this, "stop()");
		bt[2].setIcon(new QIcon("icons/media_playback_stop.png"));
		bt[2].setGeometry(815+65/*805+120*/, 15, 55, bt[0].height());
		bt[2].setEnabled(false);
		
		bt[3] = new QPushButton(this);
		bt[3].setText("Program Memory");
		bt[3].clicked.connect(this, "table_program_memory()");
		bt[3].setGeometry(551, 397, bt[3].width()+35+83, bt[3].height()+38);	
		
		bt[4] = new QPushButton(this);
		bt[4].setText("Data Memory");
		bt[4].clicked.connect(this, "table_data_memory()");
		bt[4].setGeometry(551, 473, bt[4].width()+35+83, bt[4].height()+38);
		
		bt[5] = new QPushButton(this);
		bt[5].setText("Config");
		bt[5].clicked.connect(this, "config()");
		bt[5].setIcon(new QIcon("icons/system_run.png"));
		bt[5].setGeometry(815+32, bt[0].height()+25, 60, bt[0].height());
		bt[5].setEnabled(false);
				
		statusBar = statusBar();
		status = new QLabel("Click Play to start simulation.");
		status.setPalette(corPreta);
		statusBar.addWidget(status);
		
		QFont f = new QFont();
		f.setBold(true);
		f.setPointSize(25);
		
		aguardeLabel = new QLabel(this);
		aguardeLabel.setText("Aguarde...");
		aguardeLabel.setPalette(corPreta);
		aguardeLabel.setFont(f);
		aguardeLabel.setGeometry(240, 185, 200, 200);
		aguardeLabel.setVisible(false);
		
		initLabels();
		zeraLabels();
		initabel();
		
		resize(fundo.width()+155, fundo.height() + statusBar.height()-10);
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
		new SimulatorQMainWindow(Examples.getExample(Example.CRASH));
		QApplication.exec();
		
	}

	@Override
	public void onAnimationEnd() {
		esconderAguarde();
	}

	@Override
	public void beforeStart() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProgramCrash() {
		// TODO Auto-generated method stub
		new EndProgramWindow(false);
	}

	@Override
	public void onProgramHalt() {
		// TODO Auto-generated method stub
		new EndProgramWindow(true);
	}

	@Override
	public void onRepresentationChange() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSimulationStop() {
		// TODO Auto-generated method stub
	}
	
}