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
		text.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		text.setText(getIntent().getStringExtra(TEXT));

		LinearLayout buttonsLayout = new LinearLayout(this);
		buttonsLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);

		options = getIntent().getStringArrayExtra(OPTIONS);

		buttons = new Button[options.length];

		int buttonIndex = 0;
		for (String option : options) {
			Button button = new Button(this);
			button.setText(option);
			button.setOnClickListener(this);
			buttons[buttonIndex] = button;
			buttonsLayout.addView(button);
			buttonIndex++;
		}

		LinearLayout buttonsWrapper = new LinearLayout(this);
		buttonsWrapper.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		buttonsWrapper.setOrientation(LinearLayout.HORIZONTAL);
		buttonsWrapper.setGravity(Gravity.CENTER_HORIZONTAL);

		buttonsWrapper.addView(buttonsLayout);

		LinearLayout mainLayout = new LinearLayout(this);
		mainLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
		mainLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		mainLayout.setOrientation(LinearLayout.VERTICAL);

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