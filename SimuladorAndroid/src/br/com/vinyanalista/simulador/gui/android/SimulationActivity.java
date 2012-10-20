package br.com.vinyanalista.simulador.gui.android;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

import android.os.Bundle;
//import android.os.Handler;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class SimulationActivity extends Activity implements OnClickListener {

	private Button button;
	private RelativeLayout layout;
	private LayoutParams params;

	private final static int COR_VERDE = Color.parseColor("#008000");
	private final static int COR_VERMELHA = Color.RED;

	private TextView from_memory_1;
	private TextView from_memory_2;
	private TextView to_pc_or_mbr;
	private TextView to_mbr;
	private TextView mbr;
	private TextView byteDeExemplo;

	public void mudarValorDoMbr() {
		mbr.setText(byteDeExemplo.getText());
		ValueAnimator colorAnim = ObjectAnimator.ofInt(mbr, "textColor",
				COR_VERDE, COR_VERMELHA, COR_VERDE, COR_VERMELHA, COR_VERDE);
		colorAnim.setDuration(3000);
		colorAnim.setEvaluator(new ArgbEvaluator());
		colorAnim.setRepeatMode(ValueAnimator.REVERSE);
		colorAnim.start();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simulation);

		layout = (RelativeLayout) findViewById(R.id.relativeLayout2);

		from_memory_1 = (TextView) findViewById(R.id.from_memory_1);
		from_memory_2 = (TextView) findViewById(R.id.from_memory_2);
		to_pc_or_mbr = (TextView) findViewById(R.id.to_pc_or_mbr);
		to_mbr = (TextView) findViewById(R.id.to_mbr);
		mbr = (TextView) findViewById(R.id.mbr);

		params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);

		button = (Button) findViewById(R.id.button2);
		button.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.simulation, menu);
		return true;
	}

	private void criarTextView() {
		// http://stackoverflow.com/questions/4733021/add-new-view-to-layout-upon-button-click-in-android
		byteDeExemplo = new TextView(this);
		byteDeExemplo.setText("87654321");
		byteDeExemplo.setTextSize(12);
		byteDeExemplo.setTypeface(Typeface.DEFAULT_BOLD);
		byteDeExemplo.setTextColor(COR_VERDE);
		// params.bottomMargin = ((LayoutParams)
		// from_memory_1.getLayoutParams()).bottomMargin;
		// params.rightMargin = ((LayoutParams)
		// from_memory_1.getLayoutParams()).rightMargin;
		// byteDeExemplo.setVisibility(View.VISIBLE);
		layout.addView(byteDeExemplo, params);
	}

	public void onClick(View view) {
		if (view.equals(button)) {
			criarTextView();

			ObjectAnimator posicionarX = ObjectAnimator.ofFloat(byteDeExemplo,
					"translationX", from_memory_1.getLeft(),
					from_memory_1.getLeft()).setDuration(1);

			ObjectAnimator posicionarY = ObjectAnimator.ofFloat(byteDeExemplo,
					"translationY", from_memory_1.getTop(),
					from_memory_1.getTop()).setDuration(1);

			ObjectAnimator moverParaCima1 = ObjectAnimator.ofFloat(
					byteDeExemplo, "translationY", from_memory_1.getTop(),
					from_memory_2.getTop()).setDuration(1000);

			ObjectAnimator moverParaEsquerda = ObjectAnimator.ofFloat(
					byteDeExemplo, "translationX", from_memory_2.getLeft(),
					to_pc_or_mbr.getLeft()).setDuration(1000);

			ObjectAnimator moverParaCima2 = ObjectAnimator.ofFloat(
					byteDeExemplo, "translationY", to_pc_or_mbr.getTop(),
					to_mbr.getTop()).setDuration(1000);

			AnimatorSet animacao = new AnimatorSet();

			animacao.playTogether(posicionarX, posicionarY);
			animacao.playSequentially(moverParaCima1, moverParaEsquerda,
					moverParaCima2);

			animacao.addListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator arg0) {
					layout.removeView(byteDeExemplo);
					mudarValorDoMbr();
				}
			});

			animacao.start();
		}
	}

}