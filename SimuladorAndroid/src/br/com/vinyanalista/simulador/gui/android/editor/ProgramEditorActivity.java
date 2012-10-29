package br.com.vinyanalista.simulador.gui.android.editor;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import br.com.vinyanalista.simulador.gui.android.R;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

public class ProgramEditorActivity extends SherlockActivity {

	private MenuItem save, saveAs, undo, redo, cut, copy, paste, delete,
			selectAll, search, dateTime, zoomIn, defaultZoom, zoomOut;
	private EditText code;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Program Editor");
		code = new EditText(this);
		code.setGravity(Gravity.TOP);
		code.setTextSize(12);
		code.setOnCreateContextMenuListener(this);

		LinearLayout layout = new LinearLayout(this);

		layout.addView(code, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));

		setContentView(layout);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		SubMenu file = menu.addSubMenu("File")
				.setIcon(R.drawable.document_save);
		file.getItem().setShowAsAction(
				MenuItem.SHOW_AS_ACTION_ALWAYS
						| MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		save = file.add("Save");
		saveAs = file.add("Save as...");

		SubMenu edit = menu.addSubMenu("Edit")
				.setIcon(R.drawable.document_edit);
		edit.getItem().setShowAsAction(
				MenuItem.SHOW_AS_ACTION_ALWAYS
						| MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		undo = edit.add("Undo");
		redo = edit.add("Redo");
		cut = edit.add("Cut");
		copy = edit.add("Copy");
		paste = edit.add("Paste");
		delete = edit.add("Delete");
		selectAll = edit.add("Select all");
		dateTime = edit.add("Date/time");

		search = menu.add("Search").setIcon(R.drawable.edit_find);
		search.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
				| MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		SubMenu view = menu.addSubMenu("View").setIcon(R.drawable.page_zoom);
		view.getItem().setShowAsAction(
				MenuItem.SHOW_AS_ACTION_ALWAYS
						| MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		zoomIn = view.add("Zoom in");
		defaultZoom = view.add("Default zoom");
		zoomOut = view.add("Zoom out");

		SubMenu run = menu.addSubMenu("Run").setIcon(R.drawable.arrow_right);
		run.getItem().setShowAsAction(
				MenuItem.SHOW_AS_ACTION_ALWAYS
						| MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.add(Menu.NONE, Menu.FIRST, Menu.NONE, "Test menu");
	}

}