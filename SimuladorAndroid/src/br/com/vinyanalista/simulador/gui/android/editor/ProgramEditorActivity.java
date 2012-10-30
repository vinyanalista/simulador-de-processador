package br.com.vinyanalista.simulador.gui.android.editor;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.StringTokenizer;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import br.com.vinyanalista.simulador.gui.android.ExamplesActivity;
import br.com.vinyanalista.simulador.gui.android.FileChooserActivity;
import br.com.vinyanalista.simulador.gui.android.GenericDialog;
import br.com.vinyanalista.simulador.gui.android.R;
import br.com.vinyanalista.simulador.gui.android.simulador.SimulationActivity;
import br.com.vinyanalista.simulador.parser.ParsingException;
import br.com.vinyanalista.simulador.parser.ProgramParser;
import br.com.vinyanalista.simulador.software.Program;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.actionbarsherlock.view.SubMenu;

public class ProgramEditorActivity extends SherlockActivity implements
		OnMenuItemClickListener, TextWatcher {

	private static final float MIN_ZOOM = 10f;
	private static final float DEFAULT_ZOOM = 30f;
	private static final float MAX_ZOOM = 70f;

	private SubMenu fileSubMenu;
	private MenuItem save, saveAs, zoomIn, defaultZoom, zoomOut, run;
	private EditText editor;

	private String filePath, fileContent;

	private void setFilePath(String filePath) {
		this.filePath = filePath;
		if (filePath != null)
			setTitle(new File(filePath).getName().replace(".aes", ""));
		else
			setTitle("Unnamed");
	}

	private boolean fileChanged() {
		boolean result = !editor.getText().toString().equals(fileContent);
		fileSubMenu.getItem().setEnabled(result);
		save.setEnabled(result);
		saveAs.setEnabled(result);
		run.setEnabled(editor.getText().toString().length() != 0);
		return result;
	}

	public static final String FILE_PATH = "file_path";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setIcon(R.drawable.accessories_text_editor);

		editor = new EditText(this);
		editor.setGravity(Gravity.TOP);
		setTextSize(DEFAULT_ZOOM);

		LinearLayout layout = new LinearLayout(this);

		layout.addView(editor, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));

		setContentView(layout);

		setFilePath(getIntent().getStringExtra(FILE_PATH));

		if (filePath != null) {
			open();
		} else {
			fileContent = "";
		}

		editor.addTextChangedListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		fileSubMenu = menu.addSubMenu("Save").setIcon(R.drawable.document_save);
		fileSubMenu.getItem().setEnabled(filePath != null);
		fileSubMenu.getItem().setShowAsAction(
				MenuItem.SHOW_AS_ACTION_ALWAYS
						| MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		save = fileSubMenu.add("Save").setIcon(R.drawable.document_save);
		save.setEnabled(filePath != null);
		save.setOnMenuItemClickListener(this);

		saveAs = fileSubMenu.add("Save as...").setIcon(
				R.drawable.document_save_as);
		saveAs.setEnabled(filePath != null);
		saveAs.setOnMenuItemClickListener(this);

		SubMenu view = menu.addSubMenu("Zoom").setIcon(R.drawable.page_zoom);
		view.getItem().setShowAsAction(
				MenuItem.SHOW_AS_ACTION_ALWAYS
						| MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		zoomIn = view.add("In").setIcon(R.drawable.zoom_in);
		zoomIn.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		zoomIn.setOnMenuItemClickListener(this);

		defaultZoom = view.add("Default").setIcon(R.drawable.zoom_original);
		defaultZoom.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		defaultZoom.setOnMenuItemClickListener(this);

		zoomOut = view.add("Out").setIcon(R.drawable.zoom_out);
		zoomOut.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		zoomOut.setOnMenuItemClickListener(this);

		run = menu.add("Run").setIcon(R.drawable.arrow_right);
		run.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
				| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		run.setEnabled(editor.getText().toString().length() != 0);
		run.setOnMenuItemClickListener(this);

		return true;
	}

	private void setTextSize(float size) {
		editor.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
	}

	private void open() {
		try {
			Reader in = new FileReader(new File(filePath));
			char[] buff = new char[100000];
			int nch;
			while ((nch = in.read(buff, 0, buff.length)) != -1)
				editor.append(new String(buff, 0, nch));
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void save() {
		if (filePath == null) {
			saveAs();
		} else {
			save(filePath);
		}
	}

	private void save(String filePath) {
		try {
			File file = new File(filePath);
			PrintWriter fout = new PrintWriter(new FileWriter(file));
			String code = editor.getText().toString();
			StringTokenizer st = new StringTokenizer(code,
					System.getProperty("line.separator"));
			while (st.hasMoreTokens()) {
				fout.println(st.nextToken());
			}
			fout.close();
			setFilePath(filePath);
			Toast.makeText(this, "File saved!", Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(this, "I/O Error on saving file!", Toast.LENGTH_LONG)
					.show();
		}
	}

	private static final int REQUEST_SAVE_AS = 1;

	private void saveAs() {
		startActivityForResult(
				new Intent(this, FileChooserActivity.class).putExtra(
						FileChooserActivity.OPERATION,
						FileChooserActivity.OPERATION_SAVE_FILE),
				REQUEST_SAVE_AS);
	}

	private void zoomIn() {
		setTextSize(editor.getTextSize() + 2f);
		zoomIn.setEnabled(editor.getTextSize() != MAX_ZOOM);
		zoomOut.setEnabled(editor.getTextSize() != MIN_ZOOM);
	}

	private void defaultZoom() {
		setTextSize(DEFAULT_ZOOM);
		zoomIn.setEnabled(true);
		zoomOut.setEnabled(true);
	}

	private void zoomOut() {
		setTextSize(editor.getTextSize() - 2f);
		zoomIn.setEnabled(editor.getTextSize() != MAX_ZOOM);
		zoomOut.setEnabled(editor.getTextSize() != MIN_ZOOM);
	}

	private static final int REQUEST_RUN = 2;

	private void run() {
		ProgramParser parser = ProgramParser.getParser();
		try {
			Program program = parser.parseFrom(editor.getText().toString());
			SimulationActivity.PROGRAM = program;
			startActivity(new Intent(this, SimulationActivity.class));
		} catch (ParsingException e) {
			String[] options = { "OK" };
			Intent i = new Intent(this, GenericDialog.class);
			i.putExtra(GenericDialog.TITLE, "Error while parsing");
			i.putExtra(GenericDialog.TEXT, e.getErrors().get(0).toString());
			i.putExtra(GenericDialog.OPTIONS, options);
			startActivityForResult(i, REQUEST_RUN);
		}
	}

	private void showNotImplementedYet() {
		Toast.makeText(this, "Not implemented yet.", Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		if (item.equals(save))
			save();
		else if (item.equals(saveAs))
			saveAs();
		else if (item.equals(zoomIn))
			zoomIn();
		else if (item.equals(defaultZoom))
			defaultZoom();
		else if (item.equals(zoomOut))
			zoomOut();
		else if (item.equals(run))
			run();
		else
			showNotImplementedYet();
		return false;
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		fileChanged();
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	private static final int REQUEST_CONFIRM_DISCARD_CHANGES = 3;

	@Override
	public void onBackPressed() {
		String[] options = { "Save", "Discard", "Cancel" };
		Intent i = new Intent(this, GenericDialog.class);
		i.putExtra(GenericDialog.TITLE, "Save changes");
		i.putExtra(GenericDialog.TEXT,
				"Text has been modified.\n\nDo you want to save the changes?");
		i.putExtra(GenericDialog.OPTIONS, options);
		startActivityForResult(i, REQUEST_CONFIRM_DISCARD_CHANGES);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_SAVE_AS) {
			if (resultCode == FileChooserActivity.RESULT_OK) {
				save(data.getStringExtra(FileChooserActivity.FILE_PATH));
			}
		} else if (requestCode == REQUEST_CONFIRM_DISCARD_CHANGES) {
			switch (resultCode) {
			case 0:
				save();
				if (fileChanged())
					break;
			case 1:
				finish();
			default:
				break;
			}
		}
	}

}