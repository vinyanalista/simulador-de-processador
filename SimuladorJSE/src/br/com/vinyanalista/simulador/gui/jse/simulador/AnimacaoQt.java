package br.com.vinyanalista.simulador.gui.jse.simulador;
import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import br.com.vinyanalista.simulador.hardware.DataMemory;
import br.com.vinyanalista.simulador.software.Instruction;
import br.com.vinyanalista.simulador.software.ProgramParser;

import com.trolltech.qt.QtBlockedSlot;
import com.trolltech.qt.core.QByteArray;
import com.trolltech.qt.core.QPropertyAnimation;
import com.trolltech.qt.core.QRect;
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
import com.trolltech.qt.gui.QPalette.ColorRole;
import com.trolltech.qt.gui.QPixmap;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QTableView;
import com.trolltech.qt.gui.QTableWidget;
import com.trolltech.qt.gui.QTableWidgetItem;

public class AnimacaoQt extends QMainWindow {

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
	QLabel labelAcc;
	QLabel labelALU1;
	QLabel labelALU2;
	QLabel labelALUResult;
	QLabel labelPC;
	QLabel labelIROPCODE;
	QLabel labelIROPERAND;
	QLabel labelMAR;
	QLabel labelMBR;
	QLabel labelLed;
	
	
	private QPushButton[] bt = new QPushButton[20];
	private QLabel genericLabel;
	private static final String VALOR_INICIAL = "12345678";
	private static final String VALOR_FINAL = "87654321";
	QLabel byteDeExemplo;

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
	
	private QLabel getGenericLabel() {
		return genericLabel;
	}

	private void setGenericLabel(QLabel genericLabel) {
		this.genericLabel = genericLabel;
	}

	private void mudarCorDaLabel() {
		if (genericLabel.palette().equals(corVerde))
			genericLabel.setPalette(corVermelha);
		else
			genericLabel.setPalette(corVerde);
		genericLabel.show();
		this.show();
		mudarCorCount++;
		if ((mudarCorCount > 4) || (mudarCorCount == 4)) {
			mudarCorCount = 0;
			mudarCorTimer.stop();
		}
	}
	
	//***** metodos das animações
	/*
	 * 	0	IC_TO_MAR,				
1	CHANGE_MAR,
2	MAR_TO_MEMORY,
3	MEMORY_TO_MBR,
4	CHANGE_MBR
5	MBR_TO_IR_OPCODE
6	CHANGE_IR_OPCODE
7	CHANGE_IR_OPERAND
8	CHANGE_IC
9	IR_TO_MAR
10	MBR_TO_ACC
11	CHANGE_ACC
12	ACC_TO_ALU_1
13	CHANGE_ALU_1
14	ACC_TO_ALU_2
15	CHANGE_ALU_2
16	CHANGE_ALU_OUTPUT
17	ALU_TO_ACC
18  CHANGE_LED
19  MBR_TO_IR_OPERAND



0	PC_TO_MAR,
1	CHANGE_MAR,
2	MAR_TO_MEMORY,
3	MEMORY_TO_MBR,
4	CHANGE_MBR,
5	MBR_TO_IR_1,
6	MBR_TO_IR_2,
7	CHANGE_IR_OPCODE,
8	CHANGE_IR_OPERAND,
9	CHANGE_PC,
10	IR_TO_MAR,
11	MBR_TO_ACC,
12	CHANGE_ACC,
13	ACC_TO_ALU_1,
14	CHANGE_ALU_1,
15	ACC_TO_ALU_2,
16	CHANGE_ALU_2,
17	CHANGE_ALU_OUTPUT,
18	ALU_TO_ACC;
	 */
	
	private void PC_TO_MAR() {
		byteDeExemplo = new QLabel(VALOR_FINAL, this);
		byteDeExemplo.setFont(fonte);
		byteDeExemplo.setPalette(corVerde);
		byteDeExemplo.setGeometry(740, 320, byteDeExemplo.width(),
				byteDeExemplo.height());
		
		byteDeExemplo.setText("11111111");

		// http://www.qtforum.org/article/1045/dynamically-add-widgets-to-layout.html
		byteDeExemplo.show();
		this.show();

		// http://doc.qt.digia.com/4.6/animation-overview.html#animating-qt-properties
		QPropertyAnimation moverValor = new QPropertyAnimation(
				byteDeExemplo, new QByteArray("geometry"));
		moverValor.setDuration(DURACAO_DA_ANIMACAO);

		moverValor.setKeyValueAt(0, new QRect(388, 210,
				byteDeExemplo.width(), byteDeExemplo.height()));
		moverValor.setKeyValueAt(0.99, new QRect(388, 330,
				byteDeExemplo.width(), byteDeExemplo.height()));
		moverValor.setKeyValueAt(1, new QRect(2000, 2000,
				byteDeExemplo.width(), byteDeExemplo.height()));

		moverValor.start();

		moverValor.finished.connect(this, "MAR_CHANGE()");
	}
	
	private void MAR_CHANGE() {
		//http://lists.trolltech.com/qt-interest/2002-09/thread00617-0.html
		if (byteDeExemplo==null){
		byteDeExemplo = new QLabel(VALOR_FINAL, this);
		byteDeExemplo.setFont(fonte);
		byteDeExemplo.setPalette(corVerde);
		byteDeExemplo.setGeometry(740, 320, byteDeExemplo.width(),
				byteDeExemplo.height());
		
		byteDeExemplo.setText("00000000");
		}
		labelMAR.setText(byteDeExemplo.text());
		setGenericLabel(labelMAR);
		if(!mudarCorTimer.isActive()){
		mudarCorTimer = new QTimer();
		mudarCorTimer.timeout.connect(this, "mudarCorDaLabel()");
		mudarCorTimer.start(DURACAO_DO_PISCAR);
		}
	}
	
	private void MAR_TO_MEMORY() {
		byteDeExemplo = new QLabel("87654321", this);
		byteDeExemplo.setFont(fonte);
		byteDeExemplo.setPalette(corVerde);
		byteDeExemplo.setGeometry(286, 385, byteDeExemplo.width(),
				byteDeExemplo.height());
		
		byteDeExemplo.setText("11111111");

		// http://www.qtforum.org/article/1045/dynamically-add-widgets-to-layout.html
		byteDeExemplo.show();
		this.show();

		// http://doc.qt.digia.com/4.6/animation-overview.html#animating-qt-properties
		QPropertyAnimation moverValor = new QPropertyAnimation(
				byteDeExemplo, new QByteArray("geometry"));
		moverValor.setDuration(DURACAO_DA_ANIMACAO);

		moverValor.setKeyValueAt(0, new QRect(388, 335,
				byteDeExemplo.width(), byteDeExemplo.height()));
		moverValor.setKeyValueAt(0.2, new QRect(388, 270,
				byteDeExemplo.width(), byteDeExemplo.height()));
		moverValor.setKeyValueAt(0.8, new QRect(612, 270,
				byteDeExemplo.width(), byteDeExemplo.height()));
		moverValor.setKeyValueAt(0.99, new QRect(612, 320,
				byteDeExemplo.width(), byteDeExemplo.height()));
		moverValor.setKeyValueAt(1, new QRect(2000, 2000,
				byteDeExemplo.width(), byteDeExemplo.height()));

		moverValor.start();

		moverValor.finished.connect(this, "MAR_CHANGE()");
	}
	
	private void MEMORY_TO_MBR(){
		byteDeExemplo = new QLabel(VALOR_FINAL, this);
		byteDeExemplo.setFont(fonte);
		byteDeExemplo.setPalette(corVerde);
		byteDeExemplo.setGeometry(612, 320, byteDeExemplo.width(),
				byteDeExemplo.height());
		
		byteDeExemplo.setText("11111111");

		// http://www.qtforum.org/article/1045/dynamically-add-widgets-to-layout.html
		byteDeExemplo.show();
		this.show();

		// http://doc.qt.digia.com/4.6/animation-overview.html#animating-qt-properties
				QPropertyAnimation moverValor = new QPropertyAnimation(
						byteDeExemplo, new QByteArray("geometry"));
				moverValor.setDuration(DURACAO_DA_ANIMACAO);

				moverValor.setKeyValueAt(0, new QRect(612, 320,
						byteDeExemplo.width(), byteDeExemplo.height()));
				moverValor.setKeyValueAt(0.2, new QRect(612, 270,
						byteDeExemplo.width(), byteDeExemplo.height()));
				moverValor.setKeyValueAt(0.8, new QRect(268, 270,
						byteDeExemplo.width(), byteDeExemplo.height()));
				moverValor.setKeyValueAt(0.99, new QRect(268, 330,
						byteDeExemplo.width(), byteDeExemplo.height()));
				moverValor.setKeyValueAt(1, new QRect(2000, 2000,
						byteDeExemplo.width(), byteDeExemplo.height()));

				moverValor.start();

				moverValor.finished.connect(this, "MBR_CHANGE()");
	}
	
	
	private void MBR_CHANGE() {
		//http://lists.trolltech.com/qt-interest/2002-09/thread00617-0.html
				if (byteDeExemplo==null){
				byteDeExemplo = new QLabel(VALOR_FINAL, this);
				byteDeExemplo.setFont(fonte);
				byteDeExemplo.setPalette(corVerde);
				byteDeExemplo.setGeometry(740, 320, byteDeExemplo.width(),
						byteDeExemplo.height());
				
				byteDeExemplo.setText("00000000");
				}
				labelMBR.setText(byteDeExemplo.text());
				setGenericLabel(labelMBR);
				if(!mudarCorTimer.isActive()){
					mudarCorTimer = new QTimer();
					mudarCorTimer.timeout.connect(this, "mudarCorDaLabel()");
					mudarCorTimer.start(DURACAO_DO_PISCAR);
					}
	}
	
	private void MBR_TO_IR_OPCODE() {
		byteDeExemplo = new QLabel(VALOR_FINAL, this);
		byteDeExemplo.setFont(fonte);
		byteDeExemplo.setPalette(corVerde);
		byteDeExemplo.setGeometry(740, 320, byteDeExemplo.width(),
				byteDeExemplo.height());
		
		byteDeExemplo.setText("11111111");

		// http://www.qtforum.org/article/1045/dynamically-add-widgets-to-layout.html
		byteDeExemplo.show();
		this.show();

		// http://doc.qt.digia.com/4.6/animation-overview.html#animating-qt-properties
		QPropertyAnimation moverValorDaMemoriaParaMAR = new QPropertyAnimation(
				byteDeExemplo, new QByteArray("geometry"));
		moverValorDaMemoriaParaMAR.setDuration(DURACAO_DA_ANIMACAO);

		moverValorDaMemoriaParaMAR.setKeyValueAt(0, new QRect(268, 330,
				byteDeExemplo.width(), byteDeExemplo.height()));
		moverValorDaMemoriaParaMAR.setKeyValueAt(0.2, new QRect(268, 270,
				byteDeExemplo.width(), byteDeExemplo.height()));
		moverValorDaMemoriaParaMAR.setKeyValueAt(0.8, new QRect(43, 270,
				byteDeExemplo.width(), byteDeExemplo.height()));
		moverValorDaMemoriaParaMAR.setKeyValueAt(0.99, new QRect(43, 330,
				byteDeExemplo.width(), byteDeExemplo.height()));
		moverValorDaMemoriaParaMAR.setKeyValueAt(1, new QRect(2000, 2000,
				byteDeExemplo.width(), byteDeExemplo.height()));

		moverValorDaMemoriaParaMAR.start();

		moverValorDaMemoriaParaMAR.finished.connect(this, "CHANGE_IR_OPCODE()");
		
		
	}
	
	private void MBR_TO_IR_OPERAND() {
		byteDeExemplo = new QLabel(VALOR_FINAL, this);
		byteDeExemplo.setFont(fonte);
		byteDeExemplo.setPalette(corVerde);
		byteDeExemplo.setGeometry(740, 320, byteDeExemplo.width(),
				byteDeExemplo.height());
		
		byteDeExemplo.setText("11111111");

		// http://www.qtforum.org/article/1045/dynamically-add-widgets-to-layout.html
		byteDeExemplo.show();
		this.show();

		// http://doc.qt.digia.com/4.6/animation-overview.html#animating-qt-properties
		QPropertyAnimation moverValorDaMemoriaParaMAR = new QPropertyAnimation(
				byteDeExemplo, new QByteArray("geometry"));
		moverValorDaMemoriaParaMAR.setDuration(DURACAO_DA_ANIMACAO);

		moverValorDaMemoriaParaMAR.setKeyValueAt(0, new QRect(268, 330,
				byteDeExemplo.width(), byteDeExemplo.height()));
		moverValorDaMemoriaParaMAR.setKeyValueAt(0.2, new QRect(268, 270,
				byteDeExemplo.width(), byteDeExemplo.height()));
		moverValorDaMemoriaParaMAR.setKeyValueAt(0.8, new QRect(147, 270,
				byteDeExemplo.width(), byteDeExemplo.height()));
		moverValorDaMemoriaParaMAR.setKeyValueAt(0.99, new QRect(147, 330,
				byteDeExemplo.width(), byteDeExemplo.height()));
		moverValorDaMemoriaParaMAR.setKeyValueAt(1, new QRect(2000, 2000,
				byteDeExemplo.width(), byteDeExemplo.height()));

		moverValorDaMemoriaParaMAR.start();

		moverValorDaMemoriaParaMAR.finished.connect(this, "CHANGE_IR_OPERAND()");
	}
	
	private void IR_OPCODE_CHANGE() {
		//http://lists.trolltech.com/qt-interest/2002-09/thread00617-0.html
				if (byteDeExemplo==null){
				byteDeExemplo = new QLabel(VALOR_FINAL, this);
				byteDeExemplo.setFont(fonte);
				byteDeExemplo.setPalette(corVerde);
				byteDeExemplo.setGeometry(740, 320, byteDeExemplo.width(),
						byteDeExemplo.height());
				
				byteDeExemplo.setText("00000000");
				}
				labelIROPCODE.setText(byteDeExemplo.text());
				setGenericLabel(labelIROPCODE);
				if(!mudarCorTimer.isActive()){
					mudarCorTimer = new QTimer();
					mudarCorTimer.timeout.connect(this, "mudarCorDaLabel()");
					mudarCorTimer.start(DURACAO_DO_PISCAR);
					}
	}

	private void IR_OPERAND_CHANGE() {
		//http://lists.trolltech.com/qt-interest/2002-09/thread00617-0.html
				if (byteDeExemplo==null){
				byteDeExemplo = new QLabel(VALOR_FINAL, this);
				byteDeExemplo.setFont(fonte);
				byteDeExemplo.setPalette(corVerde);
				byteDeExemplo.setGeometry(740, 320, byteDeExemplo.width(),
						byteDeExemplo.height());
				
				byteDeExemplo.setText("00000000");
				}
				labelIROPERAND.setText(byteDeExemplo.text());
				setGenericLabel(labelIROPERAND);
				if(!mudarCorTimer.isActive()){
					mudarCorTimer = new QTimer();
					mudarCorTimer.timeout.connect(this, "mudarCorDaLabel()");
					mudarCorTimer.start(DURACAO_DO_PISCAR);
					}
	}

	private void PC_CHANGE() {
		//http://lists.trolltech.com/qt-interest/2002-09/thread00617-0.html
				if (byteDeExemplo==null){
				byteDeExemplo = new QLabel(VALOR_FINAL, this);
				byteDeExemplo.setFont(fonte);
				byteDeExemplo.setPalette(corVerde);
				byteDeExemplo.setGeometry(740, 320, byteDeExemplo.width(),
						byteDeExemplo.height());
				
				byteDeExemplo.setText("00000000");
				}
				labelPC.setText(byteDeExemplo.text());
				setGenericLabel(labelPC);
				if(!mudarCorTimer.isActive()){
					mudarCorTimer = new QTimer();
					mudarCorTimer.timeout.connect(this, "mudarCorDaLabel()");
					mudarCorTimer.start(DURACAO_DO_PISCAR);
					}
	}
	
	private void IR_OPERAND_TO_MAR() {
		byteDeExemplo = new QLabel(VALOR_FINAL, this);
		byteDeExemplo.setFont(fonte);
		byteDeExemplo.setPalette(corVerde);
		byteDeExemplo.setGeometry(740, 320, byteDeExemplo.width(),
				byteDeExemplo.height());
		
		byteDeExemplo.setText("11111111");

		// http://www.qtforum.org/article/1045/dynamically-add-widgets-to-layout.html
		byteDeExemplo.show();
		this.show();

		// http://doc.qt.digia.com/4.6/animation-overview.html#animating-qt-properties
		QPropertyAnimation moverValorDaMemoriaParaMAR = new QPropertyAnimation(
				byteDeExemplo, new QByteArray("geometry"));
		moverValorDaMemoriaParaMAR.setDuration(DURACAO_DA_ANIMACAO);

		moverValorDaMemoriaParaMAR.setKeyValueAt(0, new QRect(147, 320,
				byteDeExemplo.width(), byteDeExemplo.height()));
		moverValorDaMemoriaParaMAR.setKeyValueAt(0.2, new QRect(147, 270,
				byteDeExemplo.width(), byteDeExemplo.height()));
		moverValorDaMemoriaParaMAR.setKeyValueAt(0.8, new QRect(388, 270,
				byteDeExemplo.width(), byteDeExemplo.height()));
		moverValorDaMemoriaParaMAR.setKeyValueAt(0.99, new QRect(388, 330,
				byteDeExemplo.width(), byteDeExemplo.height()));
		moverValorDaMemoriaParaMAR.setKeyValueAt(1, new QRect(2000, 2000,
				byteDeExemplo.width(), byteDeExemplo.height()));

		moverValorDaMemoriaParaMAR.start();

		moverValorDaMemoriaParaMAR.finished.connect(this, "MAR_CHANGE()");
	}
	
	private void MBR_TO_ACC(){
		byteDeExemplo = new QLabel(VALOR_FINAL, this);
		byteDeExemplo.setFont(fonte);
		byteDeExemplo.setPalette(corVerde);
		byteDeExemplo.setGeometry(740, 320, byteDeExemplo.width(),
				byteDeExemplo.height());
		
		byteDeExemplo.setText("11111111");

		// http://www.qtforum.org/article/1045/dynamically-add-widgets-to-layout.html
		byteDeExemplo.show();
		this.show();

		// http://doc.qt.digia.com/4.6/animation-overview.html#animating-qt-properties
		QPropertyAnimation moverValorDaMemoriaParaMAR = new QPropertyAnimation(
				byteDeExemplo, new QByteArray("geometry"));
		moverValorDaMemoriaParaMAR.setDuration(DURACAO_DA_ANIMACAO);

		moverValorDaMemoriaParaMAR.setKeyValueAt(0, new QRect(268, 330,
				byteDeExemplo.width(), byteDeExemplo.height()));
		moverValorDaMemoriaParaMAR.setKeyValueAt(0.99, new QRect(268, 210,
				byteDeExemplo.width(), byteDeExemplo.height()));
		moverValorDaMemoriaParaMAR.setKeyValueAt(1, new QRect(2000, 2000,
				byteDeExemplo.width(), byteDeExemplo.height()));

		moverValorDaMemoriaParaMAR.start();

		moverValorDaMemoriaParaMAR.finished.connect(this, "ACC_CHANGE()");
	}

	private void ACC_CHANGE() {
		//http://lists.trolltech.com/qt-interest/2002-09/thread00617-0.html
				if (byteDeExemplo==null){
				byteDeExemplo = new QLabel(VALOR_FINAL, this);
				byteDeExemplo.setFont(fonte);
				byteDeExemplo.setPalette(corVerde);
				byteDeExemplo.setGeometry(740, 320, byteDeExemplo.width(),
						byteDeExemplo.height());
				
				byteDeExemplo.setText("00000000");
				}
				labelAcc.setText(byteDeExemplo.text());
				setGenericLabel(labelAcc);
				if(!mudarCorTimer.isActive()){
					mudarCorTimer = new QTimer();
					mudarCorTimer.timeout.connect(this, "mudarCorDaLabel()");
					mudarCorTimer.start(DURACAO_DO_PISCAR);
					}
	}

	private void ACC_TO_ALU_IN_1() {
		byteDeExemplo = new QLabel(VALOR_FINAL, this);
		byteDeExemplo.setFont(fonte);
		byteDeExemplo.setPalette(corVerde);
		byteDeExemplo.setGeometry(740, 320, byteDeExemplo.width(),
				byteDeExemplo.height());
		
		byteDeExemplo.setText("11111111");

		// http://www.qtforum.org/article/1045/dynamically-add-widgets-to-layout.html
		byteDeExemplo.show();
		this.show();

		// http://doc.qt.digia.com/4.6/animation-overview.html#animating-qt-properties
		QPropertyAnimation moverValorDaMemoriaParaMAR = new QPropertyAnimation(
				byteDeExemplo, new QByteArray("geometry"));
		moverValorDaMemoriaParaMAR.setDuration(DURACAO_DA_ANIMACAO);

		moverValorDaMemoriaParaMAR.setKeyValueAt(0, new QRect(268, 210,
				byteDeExemplo.width(), byteDeExemplo.height()));
		moverValorDaMemoriaParaMAR.setKeyValueAt(0.2, new QRect(268, 270,
				byteDeExemplo.width(), byteDeExemplo.height()));
		moverValorDaMemoriaParaMAR.setKeyValueAt(0.8, new QRect(43, 270,
				byteDeExemplo.width(), byteDeExemplo.height()));
		moverValorDaMemoriaParaMAR.setKeyValueAt(0.99, new QRect(43, 210,
				byteDeExemplo.width(), byteDeExemplo.height()));
		moverValorDaMemoriaParaMAR.setKeyValueAt(1, new QRect(2000, 2000,
				byteDeExemplo.width(), byteDeExemplo.height()));

		moverValorDaMemoriaParaMAR.start();

		moverValorDaMemoriaParaMAR.finished.connect(this, "ALU_IN_1_CHANGE()");	
	}
	
	private void ALU_IN_1_CHANGE() {
		//http://lists.trolltech.com/qt-interest/2002-09/thread00617-0.html
				if (byteDeExemplo==null){
				byteDeExemplo = new QLabel(VALOR_FINAL, this);
				byteDeExemplo.setFont(fonte);
				byteDeExemplo.setPalette(corVerde);
				byteDeExemplo.setGeometry(740, 320, byteDeExemplo.width(),
						byteDeExemplo.height());
				
				byteDeExemplo.setText("00000000");
				}
				labelALU1.setText(byteDeExemplo.text());
				setGenericLabel(labelALU1);
				if(!mudarCorTimer.isActive()){
					mudarCorTimer = new QTimer();
					mudarCorTimer.timeout.connect(this, "mudarCorDaLabel()");
					mudarCorTimer.start(DURACAO_DO_PISCAR);
					}
	}

	private void ACC_TO_ALU_IN_2() {
		byteDeExemplo = new QLabel(VALOR_FINAL, this);
		byteDeExemplo.setFont(fonte);
		byteDeExemplo.setPalette(corVerde);
		byteDeExemplo.setGeometry(740, 320, byteDeExemplo.width(),
				byteDeExemplo.height());
		
		byteDeExemplo.setText("11111111");

		// http://www.qtforum.org/article/1045/dynamically-add-widgets-to-layout.html
		byteDeExemplo.show();
		this.show();

		// http://doc.qt.digia.com/4.6/animation-overview.html#animating-qt-properties
		QPropertyAnimation moverValorDaMemoriaParaMAR = new QPropertyAnimation(
				byteDeExemplo, new QByteArray("geometry"));
		moverValorDaMemoriaParaMAR.setDuration(DURACAO_DA_ANIMACAO);

		moverValorDaMemoriaParaMAR.setKeyValueAt(0, new QRect(268, 210,
				byteDeExemplo.width(), byteDeExemplo.height()));
		moverValorDaMemoriaParaMAR.setKeyValueAt(0.2, new QRect(268, 270,
				byteDeExemplo.width(), byteDeExemplo.height()));
		moverValorDaMemoriaParaMAR.setKeyValueAt(0.8, new QRect(147, 270,
				byteDeExemplo.width(), byteDeExemplo.height()));
		moverValorDaMemoriaParaMAR.setKeyValueAt(0.99, new QRect(147, 210,
				byteDeExemplo.width(), byteDeExemplo.height()));
		moverValorDaMemoriaParaMAR.setKeyValueAt(1, new QRect(2000, 2000,
				byteDeExemplo.width(), byteDeExemplo.height()));

		moverValorDaMemoriaParaMAR.start();

		moverValorDaMemoriaParaMAR.finished.connect(this, "ALU_IN_2_CHANGE()");		
	}
	
	private void ALU_IN_2_CHANGE() {
		//http://lists.trolltech.com/qt-interest/2002-09/thread00617-0.html
				if (byteDeExemplo==null){
				byteDeExemplo = new QLabel(VALOR_FINAL, this);
				byteDeExemplo.setFont(fonte);
				byteDeExemplo.setPalette(corVerde);
				byteDeExemplo.setGeometry(740, 320, byteDeExemplo.width(),
						byteDeExemplo.height());
				
				byteDeExemplo.setText("00000000");
				}
				labelALU2.setText(byteDeExemplo.text());
				setGenericLabel(labelALU2);
				if(!mudarCorTimer.isActive()){
					mudarCorTimer = new QTimer();
					mudarCorTimer.timeout.connect(this, "mudarCorDaLabel()");
					mudarCorTimer.start(DURACAO_DO_PISCAR);
					}
	}
	
	private void ALU_OUTPUT_CHANGE() {
		//http://lists.trolltech.com/qt-interest/2002-09/thread00617-0.html
				if (byteDeExemplo==null){
				byteDeExemplo = new QLabel(VALOR_FINAL, this);
				byteDeExemplo.setFont(fonte);
				byteDeExemplo.setPalette(corVerde);
				byteDeExemplo.setGeometry(740, 320, byteDeExemplo.width(),
						byteDeExemplo.height());
				
				byteDeExemplo.setText("00000000");
				}
				labelALUResult.setText(byteDeExemplo.text());
				setGenericLabel(labelALUResult);
				if(!mudarCorTimer.isActive()){
					mudarCorTimer = new QTimer();
					mudarCorTimer.timeout.connect(this, "mudarCorDaLabel()");
					mudarCorTimer.start(DURACAO_DO_PISCAR);
					}
	}
	
	private void ALU_OUTPUT_TO_ACC() {
		byteDeExemplo = new QLabel(VALOR_FINAL, this);
		byteDeExemplo.setFont(fonte);
		byteDeExemplo.setPalette(corVerde);
		byteDeExemplo.setGeometry(740, 320, byteDeExemplo.width(),
				byteDeExemplo.height());
		
		byteDeExemplo.setText("11111111");

		// http://www.qtforum.org/article/1045/dynamically-add-widgets-to-layout.html
		byteDeExemplo.show();
		this.show();

		// http://doc.qt.digia.com/4.6/animation-overview.html#animating-qt-properties
		QPropertyAnimation moverValorDaMemoriaParaMAR = new QPropertyAnimation(
				byteDeExemplo, new QByteArray("geometry"));
		moverValorDaMemoriaParaMAR.setDuration(DURACAO_DA_ANIMACAO);

		moverValorDaMemoriaParaMAR.setKeyValueAt(0, new QRect(90, 100,
				byteDeExemplo.width(), byteDeExemplo.height()));
		moverValorDaMemoriaParaMAR.setKeyValueAt(0.2, new QRect(90, 65,
				byteDeExemplo.width(), byteDeExemplo.height()));
		moverValorDaMemoriaParaMAR.setKeyValueAt(0.8, new QRect(268, 65,
				byteDeExemplo.width(), byteDeExemplo.height()));
		moverValorDaMemoriaParaMAR.setKeyValueAt(0.99, new QRect(268, 150,
				byteDeExemplo.width(), byteDeExemplo.height()));
		moverValorDaMemoriaParaMAR.setKeyValueAt(1, new QRect(2000, 2000,
				byteDeExemplo.width(), byteDeExemplo.height()));

		moverValorDaMemoriaParaMAR.start();

		moverValorDaMemoriaParaMAR.finished.connect(this, "ACC_CHANGE()");
	}
	
	private void LED_CHANGE(){
		//http://lists.trolltech.com/qt-interest/2002-09/thread00617-0.html
		if (byteDeExemplo==null){
		byteDeExemplo = new QLabel(VALOR_FINAL, this);
		byteDeExemplo.setFont(fonte);
		byteDeExemplo.setPalette(corVerde);
		byteDeExemplo.setGeometry(740, 320, byteDeExemplo.width(),
				byteDeExemplo.height());
		
		byteDeExemplo.setText("00000000");
		}
		labelLed.setText(byteDeExemplo.text());
		setGenericLabel(labelLed);
		if(!mudarCorTimer.isActive()){
			mudarCorTimer = new QTimer();
			mudarCorTimer.timeout.connect(this, "mudarCorDaLabel()");
			mudarCorTimer.start(DURACAO_DO_PISCAR);
			}
	}
	
	
	
	
	
	public void play(){
			
			PC_TO_MAR();		 				
			MAR_TO_MEMORY();
			MEMORY_TO_MBR();
			MBR_TO_IR_OPCODE();
			MBR_TO_IR_OPERAND();
			IR_OPERAND_TO_MAR();
			MBR_TO_ACC();
			ACC_TO_ALU_IN_1();
			ACC_TO_ALU_IN_2();
			ALU_OUTPUT_TO_ACC();

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
	

	
	public AnimacaoQt() {
		this.setWindowModified(false);
		this.setWindowTitle("AES");
//		this.setWin
		
		
		
		//***************************************************	
		//*               Colocando a fonte                 *
		//***************************************************
		// http://forums.netbeans.org/ptopic26804.html
		InputStream fonteStream = AnimacaoQt.class
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
		InputStream imagemStream = AnimacaoQt.class
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
		AnimacaoQt anima = new AnimacaoQt();
		anima.initLabels();
		anima.zeraLabels();
		anima.povoatabela();
		QApplication.exec();
	}
}