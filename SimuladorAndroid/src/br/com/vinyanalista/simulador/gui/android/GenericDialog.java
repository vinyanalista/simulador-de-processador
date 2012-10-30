package br.com.vinyanalista.simulador.gui.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;

public class GenericDialog extends SherlockActivity implements OnClickListener {

	public static final String TITLE = "title";
	public static final String TEXT = "text";
	public static final String OPTIONS = "options";

	private String[] options;
	private Button[] buttons;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle(getIntent().getStringExtra(TITLE));

		TextView text = new TextView(this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(30, 30, 30, 30);
		text.setLayoutParams(params);
		text.setMinWidth(600);
		text.setText(getIntent().getStringExtra(TEXT));

		LinearLayout buttonsLayout = new LinearLayout(this);
		params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		buttonsLayout.setLayoutParams(params);
		buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);

		options = getIntent().getStringArrayExtra(OPTIONS);

		buttons = new Button[options.length];
		params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		int buttonIndex = 0;
		for (String option : options) {
			Button button = new Button(this);
			button.setLayoutParams(params);
			button.setText(option);
			button.setMinWidth(100);
			button.setOnClickListener(this);
			buttons[buttonIndex] = button;
			buttonsLayout.addView(button);
			buttonIndex++;
		}

		LinearLayout buttonsWrapper = new LinearLayout(this);
		params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		buttonsWrapper.setLayoutParams(params);
		buttonsWrapper.setMinimumWidth(600);
		buttonsWrapper.setOrientation(LinearLayout.HORIZONTAL);
		buttonsWrapper.setGravity(Gravity.CENTER_HORIZONTAL);

		buttonsWrapper.addView(buttonsLayout);

		LinearLayout mainLayout = new LinearLayout(this);
		params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		mainLayout.setLayoutParams(params);
		mainLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		mainLayout.setMinimumWidth(600);

		mainLayout.addView(text);
		mainLayout.addView(buttonsWrapper);

		setContentView(mainLayout);
	}

	@Override
	public void onBackPressed() {
		Intent i = new Intent();
		setResult(RESULT_CANCELED, i);
		finish();
	}

	@Override
	public void onClick(View view) {
		int buttonIndex = 0;
		for (Button button : buttons) {
			if (view.equals(button)) {
				setResult(buttonIndex);
				break;
			}
			buttonIndex++;
		}
		finish();
	}

}