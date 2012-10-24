package br.com.vinyanalista.simulador.gui.jse.simulador;

import java.io.IOException;
import java.io.InputStream;

import com.trolltech.qt.core.QByteArray;
import com.trolltech.qt.core.QPropertyAnimation;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QTimer;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QFontDatabase;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QPalette;

import br.com.vinyanalista.simulador.data.Byte;
import br.com.vinyanalista.simulador.simulation.Animation;
import br.com.vinyanalista.simulador.simulation.Animator;

public class QtAnimator extends Animator {

	private Animation animation;
	
//	QLabel byteDeExemplo;
	QPalette corVerde;
	QPalette corVermelha = new QPalette();

	QTimer mudarCorTimer = new QTimer();
	
	private static final int DURACAO_DO_PISCAR = 100;
	private static final int DURACAO_DA_ANIMACAO = 1000;
	int mudarCorCount = 0;
	
	private static QFont fonte;
	
	
	public QtAnimator(){

		//***************************************************	
		//*               Colocando a fonte                 *
		//***************************************************
		// http://forums.netbeans.org/ptopic26804.html
		InputStream fonteStream = QtAnimator.class
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
		//*                  Setando cores                  *
		//***************************************************
		corVerde = new QPalette();
		corVerde.setColor(QPalette.ColorRole.WindowText, new QColor(0, 200, 0));
		corVermelha = new QPalette();
		corVermelha.setColor(QPalette.ColorRole.WindowText, QColor.red);
		
	}

	private void byteDeExemploInit(String valor){	
		SimulatorQMainWindow.byteDeExemplo.setFont(fonte);
		SimulatorQMainWindow.byteDeExemplo.setPalette(corVerde);
		SimulatorQMainWindow.byteDeExemplo.setGeometry(740, 320, SimulatorQMainWindow.byteDeExemplo.width(),
				SimulatorQMainWindow.byteDeExemplo.height());
		
		SimulatorQMainWindow.byteDeExemplo.setText(valor);
		SimulatorQMainWindow.byteDeExemplo.setVisible(true);
		SimulatorQMainWindow.byteDeExemplo.show();
	}
	
	
	private void mudarCorDaLabel() {
		if (SimulatorQMainWindow.genericLabel.palette().equals(corVerde))
			SimulatorQMainWindow.genericLabel.setPalette(corVermelha);
		else
			SimulatorQMainWindow.genericLabel.setPalette(corVerde);
		SimulatorQMainWindow.genericLabel.show();
		mudarCorCount++;
		if ((mudarCorCount > 4) || (mudarCorCount == 4)) {
			mudarCorCount = 0;
			mudarCorTimer.stop();
		}
	}
	
	//***** metodos das animações
		/*
		 * 	PC_TO_MAR,
		MAR_CHANGE,
		MAR_TO_MEMORY,
		MEMORY_TO_MBR,
		MBR_CHANGE,
		MBR_TO_IR_OPCODE,
		MBR_TO_IR_OPERAND,
		IR_OPCODE_CHANGE,
		IR_OPERAND_CHANGE,
		PC_CHANGE,
		IR_OPERAND_TO_MAR,
		MBR_TO_ACC,
		ACC_CHANGE,
		ACC_TO_ALU_IN_1,
		ALU_IN_1_CHANGE,
		ACC_TO_ALU_IN_2,
		ALU_IN_2_CHANGE,
		ALU_OUTPUT_CHANGE,
		ALU_OUTPUT_TO_ACC,
		IR_OPERAND_TO_ACC,
		MBR_TO_MEMORY,
		ACC_TO_MBR,
		MBR_TO_LED,
		LED_CHANGE;
		 */

	
	
private void PC_TO_MAR(String valor) {
	byteDeExemploInit(valor);
	// http://doc.qt.digia.com/4.6/animation-overview.html#animating-qt-properties
	QPropertyAnimation moverValor = new QPropertyAnimation(
			SimulatorQMainWindow.byteDeExemplo, new QByteArray("geometry"));
	moverValor.setDuration(DURACAO_DA_ANIMACAO);

	moverValor.setKeyValueAt(0, new QRect(388, 210,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
	moverValor.setKeyValueAt(0.99, new QRect(388, 330,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
	moverValor.setKeyValueAt(1, new QRect(2000, 2000,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));

	moverValor.start();

//	moverValor.finished.connect(this, "MAR_CHANGE()");
}

private void MAR_CHANGE(String valor) {
	//http://lists.trolltech.com/qt-interest/2002-09/thread00617-0.html

	SimulatorQMainWindow.labelMAR.setText(valor);
	SimulatorQMainWindow.setGenericLabel(SimulatorQMainWindow.labelMAR);
	if(!mudarCorTimer.isActive()){
	mudarCorTimer = new QTimer();
	mudarCorTimer.timeout.connect(this, "mudarCorDaLabel()");
	mudarCorTimer.start(DURACAO_DO_PISCAR);
	}
}

private void MAR_TO_MEMORY(String valor) {
	byteDeExemploInit(valor);

	// http://doc.qt.digia.com/4.6/animation-overview.html#animating-qt-properties
	QPropertyAnimation moverValor = new QPropertyAnimation(
			SimulatorQMainWindow.byteDeExemplo, new QByteArray("geometry"));
	moverValor.setDuration(DURACAO_DA_ANIMACAO);

	moverValor.setKeyValueAt(0, new QRect(388, 335,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
	moverValor.setKeyValueAt(0.2, new QRect(388, 270,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
	moverValor.setKeyValueAt(0.8, new QRect(612, 270,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
	moverValor.setKeyValueAt(0.99, new QRect(612, 320,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
	moverValor.setKeyValueAt(1, new QRect(2000, 2000,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));

	moverValor.start();

//	moverValor.finished.connect(this, "MAR_CHANGE()");
}

private void MEMORY_TO_MBR(String valor){
	byteDeExemploInit(valor);

	// http://doc.qt.digia.com/4.6/animation-overview.html#animating-qt-properties
			QPropertyAnimation moverValor = new QPropertyAnimation(
					SimulatorQMainWindow.byteDeExemplo, new QByteArray("geometry"));
			moverValor.setDuration(DURACAO_DA_ANIMACAO);

			moverValor.setKeyValueAt(0, new QRect(612, 320,
					SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
			moverValor.setKeyValueAt(0.2, new QRect(612, 270,
					SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
			moverValor.setKeyValueAt(0.8, new QRect(268, 270,
					SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
			moverValor.setKeyValueAt(0.99, new QRect(268, 330,
					SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
			moverValor.setKeyValueAt(1, new QRect(2000, 2000,
					SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));

			moverValor.start();

			moverValor.finished.connect(this, "callAnimationEndListener()");
}

private void MBR_CHANGE(String valor) {
	//http://lists.trolltech.com/qt-interest/2002-09/thread00617-0.html

			SimulatorQMainWindow.labelMBR.setText(valor);
			SimulatorQMainWindow.setGenericLabel(SimulatorQMainWindow.labelMBR);
			if(!mudarCorTimer.isActive()){
				mudarCorTimer = new QTimer();
				mudarCorTimer.timeout.connect(this, "mudarCorDaLabel()");
				mudarCorTimer.start(DURACAO_DO_PISCAR);
				}
}

private void MBR_TO_IR_OPCODE(String valor) {
	byteDeExemploInit(valor);

	// http://doc.qt.digia.com/4.6/animation-overview.html#animating-qt-properties
	QPropertyAnimation moverValorDaMemoriaParaMAR = new QPropertyAnimation(
			SimulatorQMainWindow.byteDeExemplo, new QByteArray("geometry"));
	moverValorDaMemoriaParaMAR.setDuration(DURACAO_DA_ANIMACAO);

	moverValorDaMemoriaParaMAR.setKeyValueAt(0, new QRect(268, 330,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
	moverValorDaMemoriaParaMAR.setKeyValueAt(0.2, new QRect(268, 270,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
	moverValorDaMemoriaParaMAR.setKeyValueAt(0.8, new QRect(43, 270,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
	moverValorDaMemoriaParaMAR.setKeyValueAt(0.99, new QRect(43, 330,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
	moverValorDaMemoriaParaMAR.setKeyValueAt(1, new QRect(2000, 2000,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));

	moverValorDaMemoriaParaMAR.start();

//	moverValorDaMemoriaParaMAR.finished.connect(this, "callAnimationEndListener()");
	
	
}

private void MBR_TO_IR_OPERAND(String valor) {
	byteDeExemploInit(valor);

	// http://doc.qt.digia.com/4.6/animation-overview.html#animating-qt-properties
	QPropertyAnimation moverValorDaMemoriaParaMAR = new QPropertyAnimation(
			SimulatorQMainWindow.byteDeExemplo, new QByteArray("geometry"));
	moverValorDaMemoriaParaMAR.setDuration(DURACAO_DA_ANIMACAO);

	moverValorDaMemoriaParaMAR.setKeyValueAt(0, new QRect(268, 330,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
	moverValorDaMemoriaParaMAR.setKeyValueAt(0.2, new QRect(268, 270,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
	moverValorDaMemoriaParaMAR.setKeyValueAt(0.8, new QRect(147, 270,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
	moverValorDaMemoriaParaMAR.setKeyValueAt(0.99, new QRect(147, 330,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
	moverValorDaMemoriaParaMAR.setKeyValueAt(1, new QRect(2000, 2000,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));

	moverValorDaMemoriaParaMAR.start();

//	moverValorDaMemoriaParaMAR.finished.connect(this, "callAnimationEndListener()");
}

private void IR_OPCODE_CHANGE(String valor) {
	//http://lists.trolltech.com/qt-interest/2002-09/thread00617-0.html

			SimulatorQMainWindow.labelIROPCODE.setText(valor);
			SimulatorQMainWindow.setGenericLabel(SimulatorQMainWindow.labelIROPCODE);
			if(!mudarCorTimer.isActive()){
				mudarCorTimer = new QTimer();
				mudarCorTimer.timeout.connect(this, "mudarCorDaLabel()");
				mudarCorTimer.start(DURACAO_DO_PISCAR);
				}
}

private void IR_OPERAND_CHANGE(String valor) {
	//http://lists.trolltech.com/qt-interest/2002-09/thread00617-0.html

			SimulatorQMainWindow.labelIROPERAND.setText(valor);
			SimulatorQMainWindow.setGenericLabel(SimulatorQMainWindow.labelIROPERAND);
			if(!mudarCorTimer.isActive()){
				mudarCorTimer = new QTimer();
				mudarCorTimer.timeout.connect(this, "mudarCorDaLabel()");
				mudarCorTimer.start(DURACAO_DO_PISCAR);
				}
}

private void PC_CHANGE(String valor) {
	//http://lists.trolltech.com/qt-interest/2002-09/thread00617-0.html

			SimulatorQMainWindow.labelPC.setText(valor);
			SimulatorQMainWindow.setGenericLabel(SimulatorQMainWindow.labelPC);
			if(!mudarCorTimer.isActive()){
				mudarCorTimer = new QTimer();
				mudarCorTimer.timeout.connect(this, "mudarCorDaLabel()");
				mudarCorTimer.start(DURACAO_DO_PISCAR);
				}
}

private void IR_OPERAND_TO_MAR(String valor) {
	byteDeExemploInit(valor);

	// http://doc.qt.digia.com/4.6/animation-overview.html#animating-qt-properties
	QPropertyAnimation moverValorDaMemoriaParaMAR = new QPropertyAnimation(
			SimulatorQMainWindow.byteDeExemplo, new QByteArray("geometry"));
	moverValorDaMemoriaParaMAR.setDuration(DURACAO_DA_ANIMACAO);

	moverValorDaMemoriaParaMAR.setKeyValueAt(0, new QRect(147, 320,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
	moverValorDaMemoriaParaMAR.setKeyValueAt(0.2, new QRect(147, 270,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
	moverValorDaMemoriaParaMAR.setKeyValueAt(0.8, new QRect(388, 270,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
	moverValorDaMemoriaParaMAR.setKeyValueAt(0.99, new QRect(388, 330,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
	moverValorDaMemoriaParaMAR.setKeyValueAt(1, new QRect(2000, 2000,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));

	moverValorDaMemoriaParaMAR.start();

//	moverValorDaMemoriaParaMAR.finished.connect(this, "callAnimationEndListener()");
}

private void MBR_TO_ACC(String valor){
	byteDeExemploInit(valor);

	// http://doc.qt.digia.com/4.6/animation-overview.html#animating-qt-properties
	QPropertyAnimation moverValorDaMemoriaParaMAR = new QPropertyAnimation(
			SimulatorQMainWindow.byteDeExemplo, new QByteArray("geometry"));
	moverValorDaMemoriaParaMAR.setDuration(DURACAO_DA_ANIMACAO);

	moverValorDaMemoriaParaMAR.setKeyValueAt(0, new QRect(268, 330,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
	moverValorDaMemoriaParaMAR.setKeyValueAt(0.99, new QRect(268, 210,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
	moverValorDaMemoriaParaMAR.setKeyValueAt(1, new QRect(2000, 2000,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));

	moverValorDaMemoriaParaMAR.start();

//	moverValorDaMemoriaParaMAR.finished.connect(this, "callAnimationEndListener()");
}

private void ACC_CHANGE(String valor) {
	//http://lists.trolltech.com/qt-interest/2002-09/thread00617-0.html
	SimulatorQMainWindow.labelAcc.setText(valor);
	SimulatorQMainWindow.setGenericLabel(SimulatorQMainWindow.labelAcc);
			if(!mudarCorTimer.isActive()){
				mudarCorTimer = new QTimer();
				mudarCorTimer.timeout.connect(this, "mudarCorDaLabel()");
				mudarCorTimer.start(DURACAO_DO_PISCAR);
				}
}

private void ACC_TO_ALU_IN_1(String valor) {
	byteDeExemploInit(valor);

	// http://doc.qt.digia.com/4.6/animation-overview.html#animating-qt-properties
	QPropertyAnimation moverValorDaMemoriaParaMAR = new QPropertyAnimation(
			SimulatorQMainWindow.byteDeExemplo, new QByteArray("geometry"));
	moverValorDaMemoriaParaMAR.setDuration(DURACAO_DA_ANIMACAO);

	moverValorDaMemoriaParaMAR.setKeyValueAt(0, new QRect(268, 210,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
	moverValorDaMemoriaParaMAR.setKeyValueAt(0.2, new QRect(268, 270,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
	moverValorDaMemoriaParaMAR.setKeyValueAt(0.8, new QRect(43, 270,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
	moverValorDaMemoriaParaMAR.setKeyValueAt(0.99, new QRect(43, 210,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
	moverValorDaMemoriaParaMAR.setKeyValueAt(1, new QRect(2000, 2000,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));

	moverValorDaMemoriaParaMAR.start();

	moverValorDaMemoriaParaMAR.finished.connect(this, "callAnimationEndListener()");	
}

private void ALU_IN_1_CHANGE(String valor) {
	//http://lists.trolltech.com/qt-interest/2002-09/thread00617-0.html

	SimulatorQMainWindow.labelALU1.setText(valor);
	SimulatorQMainWindow.setGenericLabel(SimulatorQMainWindow.labelALU1);
			if(!mudarCorTimer.isActive()){
				mudarCorTimer = new QTimer();
				mudarCorTimer.timeout.connect(this, "mudarCorDaLabel()");
				mudarCorTimer.start(DURACAO_DO_PISCAR);
				}
}

private void ACC_TO_ALU_IN_2(String valor) {
	byteDeExemploInit(valor);

	// http://doc.qt.digia.com/4.6/animation-overview.html#animating-qt-properties
	QPropertyAnimation moverValorDaMemoriaParaMAR = new QPropertyAnimation(
			SimulatorQMainWindow.byteDeExemplo, new QByteArray("geometry"));
	moverValorDaMemoriaParaMAR.setDuration(DURACAO_DA_ANIMACAO);

	moverValorDaMemoriaParaMAR.setKeyValueAt(0, new QRect(268, 210,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
	moverValorDaMemoriaParaMAR.setKeyValueAt(0.2, new QRect(268, 270,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
	moverValorDaMemoriaParaMAR.setKeyValueAt(0.8, new QRect(147, 270,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
	moverValorDaMemoriaParaMAR.setKeyValueAt(0.99, new QRect(147, 210,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
	moverValorDaMemoriaParaMAR.setKeyValueAt(1, new QRect(2000, 2000,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));

	moverValorDaMemoriaParaMAR.start();

//	moverValorDaMemoriaParaMAR.finished.connect(this, "callAnimationEndListener()");		
}

private void ALU_IN_2_CHANGE(String valor) {
	//http://lists.trolltech.com/qt-interest/2002-09/thread00617-0.html

	SimulatorQMainWindow.labelALU2.setText(valor);
	SimulatorQMainWindow.setGenericLabel(SimulatorQMainWindow.labelALU2);
			if(!mudarCorTimer.isActive()){
				mudarCorTimer = new QTimer();
				mudarCorTimer.timeout.connect(this, "mudarCorDaLabel()");
				mudarCorTimer.start(DURACAO_DO_PISCAR);
				}
}

private void ALU_OUTPUT_CHANGE(String valor) {
	//http://lists.trolltech.com/qt-interest/2002-09/thread00617-0.html

	SimulatorQMainWindow.labelALUResult.setText(valor);
	SimulatorQMainWindow.setGenericLabel(SimulatorQMainWindow.labelALUResult);
			if(!mudarCorTimer.isActive()){
				mudarCorTimer = new QTimer();
				mudarCorTimer.timeout.connect(this, "mudarCorDaLabel()");
				mudarCorTimer.start(DURACAO_DO_PISCAR);
				}
}

private void ALU_OUTPUT_TO_ACC(String valor) {
	byteDeExemploInit(valor);
	// http://doc.qt.digia.com/4.6/animation-overview.html#animating-qt-properties
	QPropertyAnimation moverValorDaMemoriaParaMAR = new QPropertyAnimation(
			SimulatorQMainWindow.byteDeExemplo, new QByteArray("geometry"));
	moverValorDaMemoriaParaMAR.setDuration(DURACAO_DA_ANIMACAO);

	moverValorDaMemoriaParaMAR.setKeyValueAt(0, new QRect(90, 100,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
	moverValorDaMemoriaParaMAR.setKeyValueAt(0.2, new QRect(90, 65,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
	moverValorDaMemoriaParaMAR.setKeyValueAt(0.8, new QRect(268, 65,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
	moverValorDaMemoriaParaMAR.setKeyValueAt(0.99, new QRect(268, 150,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
	moverValorDaMemoriaParaMAR.setKeyValueAt(1, new QRect(2000, 2000,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));

	moverValorDaMemoriaParaMAR.start();

//	moverValorDaMemoriaParaMAR.finished.connect(this, "callAnimationEndListener()");
}

private void IR_OPERAND_TO_ACC(String valor){
	byteDeExemploInit(valor);

	// http://doc.qt.digia.com/4.6/animation-overview.html#animating-qt-properties
	QPropertyAnimation moverValorDaMemoriaParaMAR = new QPropertyAnimation(
			SimulatorQMainWindow.byteDeExemplo, new QByteArray("geometry"));
	moverValorDaMemoriaParaMAR.setDuration(DURACAO_DA_ANIMACAO);

	moverValorDaMemoriaParaMAR.setKeyValueAt(0, new QRect(147, 320,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
	moverValorDaMemoriaParaMAR.setKeyValueAt(0.2, new QRect(147, 270,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
	moverValorDaMemoriaParaMAR.setKeyValueAt(0.8, new QRect(268, 270,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
	moverValorDaMemoriaParaMAR.setKeyValueAt(0.99, new QRect(268, 150,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
	moverValorDaMemoriaParaMAR.setKeyValueAt(1, new QRect(2000, 2000,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));

	moverValorDaMemoriaParaMAR.start();

//	moverValorDaMemoriaParaMAR.finished.connect(this, "callAnimationEndListener()");
}

private void MBR_TO_MEMORY(String valor){
	byteDeExemploInit(valor);

	// http://doc.qt.digia.com/4.6/animation-overview.html#animating-qt-properties
	QPropertyAnimation moverValorDaMemoriaParaMAR = new QPropertyAnimation(
			SimulatorQMainWindow.byteDeExemplo, new QByteArray("geometry"));
	moverValorDaMemoriaParaMAR.setDuration(DURACAO_DA_ANIMACAO);

	moverValorDaMemoriaParaMAR.setKeyValueAt(0, new QRect(268, 330,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
	moverValorDaMemoriaParaMAR.setKeyValueAt(0.25, new QRect(268, 270,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
	moverValorDaMemoriaParaMAR.setKeyValueAt(0.8, new QRect(615, 270,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
	moverValorDaMemoriaParaMAR.setKeyValueAt(0.99, new QRect(615, 320,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
	moverValorDaMemoriaParaMAR.setKeyValueAt(1, new QRect(2000, 2000,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));

	moverValorDaMemoriaParaMAR.start();

//	moverValorDaMemoriaParaMAR.finished.connect(this, "callAnimationEndListener()");
}

private void ACC_TO_MBR(String valor){
	byteDeExemploInit(valor);

	// http://doc.qt.digia.com/4.6/animation-overview.html#animating-qt-properties
	QPropertyAnimation moverValorDaMemoriaParaMAR = new QPropertyAnimation(
			SimulatorQMainWindow.byteDeExemplo, new QByteArray("geometry"));
	moverValorDaMemoriaParaMAR.setDuration(DURACAO_DA_ANIMACAO);

	moverValorDaMemoriaParaMAR.setKeyValueAt(0, new QRect(268, 210,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
	moverValorDaMemoriaParaMAR.setKeyValueAt(0.99, new QRect(268, 330,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
	moverValorDaMemoriaParaMAR.setKeyValueAt(1, new QRect(2000, 2000,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));

	moverValorDaMemoriaParaMAR.start();

//	moverValorDaMemoriaParaMAR.finished.connect(this, "callAnimationEndListener()");
}

private void MBR_TO_LED(String valor){
	byteDeExemploInit(valor);

	// http://doc.qt.digia.com/4.6/animation-overview.html#animating-qt-properties
	QPropertyAnimation moverValorDaMemoriaParaMAR = new QPropertyAnimation(
			SimulatorQMainWindow.byteDeExemplo, new QByteArray("geometry"));
	moverValorDaMemoriaParaMAR.setDuration(DURACAO_DA_ANIMACAO);

	moverValorDaMemoriaParaMAR.setKeyValueAt(0, new QRect(268, 330,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
	moverValorDaMemoriaParaMAR.setKeyValueAt(0.25, new QRect(268, 270,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
	moverValorDaMemoriaParaMAR.setKeyValueAt(0.8, new QRect(615, 270,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
	moverValorDaMemoriaParaMAR.setKeyValueAt(0.99, new QRect(615, 47,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));
	moverValorDaMemoriaParaMAR.setKeyValueAt(1, new QRect(2000, 2000,
			SimulatorQMainWindow.byteDeExemplo.width(), SimulatorQMainWindow.byteDeExemplo.height()));

	moverValorDaMemoriaParaMAR.start();

//	moverValorDaMemoriaParaMAR.finished.connect(this, "callAnimationEndListener()");
}
	
private void LED_CHANGE(String valor){
	//http://lists.trolltech.com/qt-interest/2002-09/thread00617-0.html
	SimulatorQMainWindow.labelLed.setText(valor);
	SimulatorQMainWindow.setGenericLabel(SimulatorQMainWindow.labelLed);
	if(!mudarCorTimer.isActive()){
		mudarCorTimer = new QTimer();
		mudarCorTimer.timeout.connect(this, "mudarCorDaLabel()");
		mudarCorTimer.start(DURACAO_DO_PISCAR);
		}
}

	
	@Override
	public void animate(Animation animation) {
		this.animation = animation;
		switch (animation.getType()) {
		case MAR_CHANGE:
				MAR_CHANGE(animation.getValue().getValueAsBinary());
			break;
		case MBR_CHANGE:
			MBR_CHANGE(animation.getValue().getValueAsBinary());
			break;
		case IR_OPCODE_CHANGE:
			IR_OPCODE_CHANGE(animation.getValue().getValueAsBinary());
			break;
		case IR_OPERAND_CHANGE:
			IR_OPERAND_CHANGE(animation.getValue().getValueAsBinary());
			break;
		case PC_CHANGE:
			PC_CHANGE(animation.getValue().getValueAsBinary());
			break;
		case ACC_CHANGE:
			ACC_CHANGE(animation.getValue().getValueAsBinary());
			break;
		case ALU_IN_1_CHANGE:
			ALU_IN_1_CHANGE(animation.getValue().getValueAsBinary());
			break;
		case ALU_IN_2_CHANGE:
			ALU_IN_2_CHANGE(animation.getValue().getValueAsBinary());
			break;
		case ALU_OUTPUT_CHANGE:
			ALU_OUTPUT_CHANGE(animation.getValue().getValueAsBinary());
			break;
		case ACC_TO_ALU_IN_1:
			ACC_TO_ALU_IN_1(animation.getValue().getValueAsBinary());
			break;
		case ACC_TO_ALU_IN_2:
			ACC_TO_ALU_IN_2(animation.getValue().getValueAsBinary());
			break;
		case ALU_OUTPUT_TO_ACC:
			ALU_OUTPUT_TO_ACC(animation.getValue().getValueAsBinary());
			break;
		case IR_OPERAND_TO_MAR:
			IR_OPERAND_TO_MAR(animation.getValue().getValueAsBinary());
			break;
		case MAR_TO_MEMORY:
			MAR_TO_MEMORY(animation.getValue().getValueAsBinary());
			break;
		case MBR_TO_ACC:
			MBR_TO_ACC(animation.getValue().getValueAsBinary());
			break;
		case MBR_TO_IR_OPCODE:
			MBR_TO_IR_OPCODE(animation.getValue().getValueAsBinary());
			break;
		case MBR_TO_IR_OPERAND:
			MBR_TO_IR_OPERAND(animation.getValue().getValueAsBinary());
			break;
		case MEMORY_TO_MBR:
			MEMORY_TO_MBR(animation.getValue().getValueAsBinary());
			break;
		case PC_TO_MAR:
			PC_TO_MAR(animation.getValue().getValueAsBinary());
			break;
		case ACC_TO_MBR:
			ACC_TO_MBR(animation.getValue().getValueAsBinary());
			break;
		case IR_OPERAND_TO_ACC:
			IR_OPERAND_TO_ACC(animation.getValue().getValueAsBinary());
			break;
		case LED_CHANGE:
			LED_CHANGE(animation.getValue().getValueAsBinary());
			break;
		case MBR_TO_LED:
			MBR_TO_LED(animation.getValue().getValueAsBinary());
			break;
		case MBR_TO_MEMORY:
			MBR_TO_MEMORY(animation.getValue().getValueAsBinary());
			break;
		default:
			break;
		}
	}

}
