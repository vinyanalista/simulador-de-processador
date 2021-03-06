package br.com.vinyanalista.simulador.gui.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;

public class ThreeButtonDialog extends SherlockActivity implements
		OnClickListener {

	public static final String TITLE = "title";
	public static final String TEXT = "text";
	public static final String OPTION_1 = "option_1";
	public static final String OPTION_2 = "option_2";
	public static final String OPTION_3 = "option_3";

	public static final int RESULT_1 = 1;
	public static final int RESULT_2 = 2;
	public static final int RESULT_3 = 3;

	private Button option1, option2, option3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.three_button_dialog);

		setTitle(getIntent().getStringExtra(TITLE));

		((TextView) findViewById(R.id.three_button_dialog_text))
				.setText(getIntent().getStringExtra(TEXT));

		option1 = (Button) findViewById(R.id.three_button_dialog_button1);
		option1.setText(getIntent().getStringExtra(OPTION_1));
		option1.setOnClickListener(this);

		option2 = (Button) findViewById(R.id.three_button_dialog_button2);
		option2.setText(getIntent().getStringExtra(OPTION_2));
		option2.setOnClickListener(this);

		option3 = (Button) findViewById(R.id.three_button_dialog_button3);
		option3.setText(getIntent().getStringExtra(OPTION_3));
		option3.setOnClickListener(this);
	}

	@Override
	public void onBackPressed() {
		Intent i = new Intent();
		setResult(RESULT_CANCELED, i);
		finish();
	}

	@Override
	public void onClick(View view) {
		Intent i = new Intent();
		if (view.equals(option1)) {
			setResult(RESULT_1, i);
		} else if (view.equals(option2)) {
			setResult(RESULT_2, i);
		} else {
			setResult(RESULT_3, i);
		}
		finish();
	}

}