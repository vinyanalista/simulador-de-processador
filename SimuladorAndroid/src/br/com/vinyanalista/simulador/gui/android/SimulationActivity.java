package br.com.vinyanalista.simulador.gui.android;

import com.actionbarsherlock.app.SherlockActivity;

import br.com.vinyanalista.simulador.data.Data;
//import br.com.vinyanalista.simulador.gui.android.AndroidAnimator.OnAnimationEndListener;
import br.com.vinyanalista.simulador.simulation.AnimationType;
import br.com.vinyanalista.simulador.simulation.Simulation;
//import br.com.vinyanalista.simulador.simulation.Animator;
import br.com.vinyanalista.simulador.simulation.Animation;
import br.com.vinyanalista.simulador.software.Program;

//import com.nineoldandroids.animation.AnimatorListenerAdapter;
//import com.nineoldandroids.animation.AnimatorSet;
//import com.nineoldandroids.animation.ArgbEvaluator;
//import com.nineoldandroids.animation.ObjectAnimator;
//import com.nineoldandroids.animation.ValueAnimator;

import android.os.Bundle;
//import android.os.Handler;
import android.app.Activity;
//import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class SimulationActivity extends SherlockActivity implements OnClickListener,
		OnItemClickListener {

	// private final static int COR_VERDE = Color.parseColor("#008000");
	// private final static int COR_VERMELHA = Color.RED;

	public static final String EXTRA_PROGRAM = "program";

	private RelativeLayout layout;
	private LayoutParams params;

	TextView from_memory_1, from_memory_2, to_pc_or_mbr, to_pc, pc, to_mbr,
			mbr, to_acc_or_mar, to_acc, acc, to_mar, mar, to_alu2_or_ir2,
			to_alu2, alu2, to_ir2, ir2, to_alu1_or_ir1, to_alu1, alu1, to_ir1,
			ir1, alu_to_acc1, alu_to_acc2, alu_to_acc3, alu_to_acc4, alu_out,
			to_led, led, movingByte;

	private Program program;
	private Simulation simulation;
	private AndroidAnimator animator;

	private Button button;
	private ListView animationsListView;

	// TextView byteDeExemplo;

	// public void mudarValorDoMbr() {
	// animator.animate(new SingleAnimation(AnimationType.CHANGE_MBR,
	// new Data(10)));
	// mbr.setText(byteDeExemplo.getText());
	// ValueAnimator colorAnim = ObjectAnimator.ofInt(mbr, "textColor",
	// COR_VERDE, COR_VERMELHA, COR_VERDE, COR_VERMELHA, COR_VERDE);
	// colorAnim.setDuration(3000);
	// colorAnim.setEvaluator(new ArgbEvaluator());
	// colorAnim.setRepeatMode(ValueAnimator.REVERSE);
	// colorAnim.start();
	// }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simulation);

		layout = (RelativeLayout) findViewById(R.id.relativeLayout2);

		params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);

		from_memory_1 = (TextView) findViewById(R.id.from_memory_1);
		from_memory_2 = (TextView) findViewById(R.id.from_memory_2);
		to_pc_or_mbr = (TextView) findViewById(R.id.to_pc_or_mbr);
		to_pc = (TextView) findViewById(R.id.to_pc);
		pc = (TextView) findViewById(R.id.pc);
		to_mbr = (TextView) findViewById(R.id.to_mbr);
		mbr = (TextView) findViewById(R.id.mbr);
		to_acc_or_mar = (TextView) findViewById(R.id.to_acc_or_mar);
		to_acc = (TextView) findViewById(R.id.to_acc);
		acc = (TextView) findViewById(R.id.acc);
		to_mar = (TextView) findViewById(R.id.to_mar);
		mar = (TextView) findViewById(R.id.mar);
		to_alu2_or_ir2 = (TextView) findViewById(R.id.to_alu2_or_ir2);
		to_alu2 = (TextView) findViewById(R.id.to_alu2);
		alu2 = (TextView) findViewById(R.id.alu2);
		to_ir2 = (TextView) findViewById(R.id.to_ir2);
		ir2 = (TextView) findViewById(R.id.ir2);
		to_alu1_or_ir1 = (TextView) findViewById(R.id.to_alu1_or_ir1);
		to_alu1 = (TextView) findViewById(R.id.to_alu1);
		alu1 = (TextView) findViewById(R.id.alu1);
		to_ir1 = (TextView) findViewById(R.id.to_ir1);
		ir1 = (TextView) findViewById(R.id.ir1);
		alu_to_acc1 = (TextView) findViewById(R.id.alu_to_acc1);
		alu_to_acc2 = (TextView) findViewById(R.id.alu_to_acc2);
		alu_to_acc3 = (TextView) findViewById(R.id.alu_to_acc3);
		alu_to_acc4 = (TextView) findViewById(R.id.alu_to_acc4);
		alu_out = (TextView) findViewById(R.id.alu_out);
		to_led = (TextView) findViewById(R.id.to_led);
		led = (TextView) findViewById(R.id.led);

		movingByte = new TextView(this);
		movingByte.setText("87654321");
		movingByte.setTextSize(12);
		movingByte.setTypeface(Typeface.DEFAULT_BOLD);
		movingByte.setTextColor(AndroidAnimator.COR_VERDE);
		movingByte.setVisibility(View.INVISIBLE);
		layout.addView(movingByte, params);

		animator = new AndroidAnimator(this);

		button = (Button) findViewById(R.id.button2);
		button.setOnClickListener(this);

		// http://www.vogella.com/articles/AndroidListView/article.html#listsactivity_simple

		String[] animations = new String[] { "PC_TO_MAR", "CHANGE_MAR",
				"MAR_TO_MEMORY", "MEMORY_TO_MBR", "CHANGE_MBR", "MBR_TO_IR",
				"CHANGE_IR_OPCODE", "CHANGE_IR_OPERAND", "CHANGE_PC",
				"IR_TO_MAR", "MBR_TO_ACC", "CHANGE_ACC", "ACC_TO_ALU_1",
				"CHANGE_ALU_1", "ACC_TO_ALU_2", "CHANGE_ALU_2",
				"CHANGE_ALU_OUTPUT", "ALU_TO_ACC" };
		animationsListView = (ListView) findViewById(R.id.listView1);
		animationsListView.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, animations));
		animationsListView.setOnItemClickListener(this);
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.simulation, menu);
//		return true;
//	}

	// private void criarTextView() {
	// http://stackoverflow.com/questions/4733021/add-new-view-to-layout-upon-button-click-in-android
	// byteDeExemplo = new TextView(this);
	// byteDeExemplo.setText("87654321");
	// byteDeExemplo.setTextSize(12);
	// byteDeExemplo.setTypeface(Typeface.DEFAULT_BOLD);
	// byteDeExemplo.setTextColor(COR_VERDE);
	// params.bottomMargin = ((LayoutParams)
	// from_memory_1.getLayoutParams()).bottomMargin;
	// params.rightMargin = ((LayoutParams)
	// from_memory_1.getLayoutParams()).rightMargin;
	// byteDeExemplo.setVisibility(View.VISIBLE);

	// }

	public void onClick(View view) {
		if (view.equals(button)) {
			// criarTextView();
			//
			// ObjectAnimator posicionarX =
			// ObjectAnimator.ofFloat(byteDeExemplo,
			// "translationX", from_memory_1.getLeft(),
			// from_memory_1.getLeft()).setDuration(1);
			//
			// ObjectAnimator posicionarY =
			// ObjectAnimator.ofFloat(byteDeExemplo,
			// "translationY", from_memory_1.getTop(),
			// from_memory_1.getTop()).setDuration(1);
			//
			// ObjectAnimator moverParaCima1 = ObjectAnimator.ofFloat(
			// byteDeExemplo, "translationY", from_memory_1.getTop(),
			// from_memory_2.getTop()).setDuration(1000);
			//
			// ObjectAnimator moverParaEsquerda = ObjectAnimator.ofFloat(
			// byteDeExemplo, "translationX", from_memory_2.getLeft(),
			// to_pc_or_mbr.getLeft()).setDuration(1000);
			//
			// ObjectAnimator moverParaCima2 = ObjectAnimator.ofFloat(
			// byteDeExemplo, "translationY", to_pc_or_mbr.getTop(),
			// to_mbr.getTop()).setDuration(1000);
			//
			// AnimatorSet animacao = new AnimatorSet();
			//
			// animacao.playTogether(posicionarX, posicionarY);
			// animacao.playSequentially(moverParaCima1, moverParaEsquerda,
			// moverParaCima2);
			//
			// animacao.addListener(new AnimatorListenerAdapter() {
			// @Override
			// public void onAnimationEnd(Animator arg0) {
			// layout.removeView(byteDeExemplo);
			// mudarValorDoMbr();
			// }
			// });
			//
			// animacao.start();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		// TODO Auto-generated method stub
	}

}