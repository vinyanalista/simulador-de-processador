//http://www.dreamincode.net/forums/topic/190013-creating-simple-file-chooser/

package br.com.vinyanalista.simulador.gui.android;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.vinyanalista.simulador.gui.android.R;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FileChooserActivity extends SherlockListActivity implements
		OnClickListener {

	public static final String OPERATION = "operation";

	public static final int OPERATION_OPEN_FILE = 0;
	public static final int OPERATION_SAVE_FILE = 1;

	private class Option implements Comparable<Option> {

		public static final int TYPE_FOLDER = 0;
		public static final int TYPE_FILE = 1;

		public static final String FILE_EXTENSION = ".aes";

		private String name;
		private String path;
		private int type;

		public Option(String name, String path, int type) {
			this.name = name;
			this.path = path;
			this.type = type;
		}

		public String getName() {
			return name;
		}

		public String getPath() {
			return path;
		}

		public int getType() {
			return type;
		}

		public int compareTo(Option o) {
			if (this.name != null)
				return this.name.toLowerCase().compareTo(
						o.getName().toLowerCase());
			else
				throw new IllegalArgumentException();
		}
	}

	private class FileArrayAdapter extends ArrayAdapter<Option> {

		private List<Option> items;

		public FileArrayAdapter(Context context, List<Option> objects) {
			super(context, R.layout.list_view_icon_label_layout, objects);
			items = objects;
		}

		public Option getItem(int i) {
			return items.get(i);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				LayoutInflater vi = (LayoutInflater) getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = vi.inflate(R.layout.list_view_icon_label_layout, null);
			}
			final Option o = items.get(position);
			if (o != null) {
				TextView label = (TextView) view
						.findViewById(R.id.list_view_label);
				label.setText(o.getName());
				ImageView icon = (ImageView) view
						.findViewById(R.id.list_view_icon);
				switch (o.getType()) {
				case Option.TYPE_FOLDER:
					icon.setImageResource(R.drawable.folder);
					break;
				case Option.TYPE_FILE:
					icon.setImageResource(R.drawable.application_x_m4);
					break;
				default:
					break;
				}
			}
			return view;
		}

	}

	private File initialDir;
	private File currentDir;
	private FileArrayAdapter adapter;
	private int operation;

	public static final String FILE_PATH = "file_path";

	private LinearLayout fileNameBar;
	private EditText fileName;
	private Button save;
	private LinearLayout folderIsEmpty;

	private MenuItem goUp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		operation = getIntent().getIntExtra(OPERATION, OPERATION_OPEN_FILE);

		setContentView(R.layout.file_chooser);

		fileNameBar = (LinearLayout) findViewById(R.id.enter_filename_bar);
		fileNameBar.setVisibility(View.GONE);

		fileName = (EditText) findViewById(R.id.filename);
		save = (Button) findViewById(R.id.button_save);
		save.setOnClickListener(this);

		folderIsEmpty = (LinearLayout) findViewById(R.id.folder_is_empty);

		getListView().setEmptyView(folderIsEmpty);
		getListView().requestFocus();
		getListView().requestFocusFromTouch();

		if (operation == OPERATION_OPEN_FILE)
			getSupportActionBar().setIcon(R.drawable.document_open);
		else
			getSupportActionBar().setIcon(R.drawable.document_save_as);

		initialDir = Environment.getExternalStorageDirectory();
		currentDir = initialDir;
		fillInList(currentDir);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		goUp = menu.add("Up").setIcon(R.drawable.go_up);
		goUp.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		currentDir = new File(currentDir.getParent());
		fillInList(currentDir);
		return true;
	}

	private void fillInList(File f) {
		File[] dirs = f.listFiles();
		if (currentDir.getParent() != null)
			setTitle("Pasta atual: " + f.getName());
		else
			setTitle("Pasta atual: RAIZ");
		List<Option> dir = new ArrayList<Option>();
		List<Option> fls = new ArrayList<Option>();
		try {
			for (File ff : dirs) {
				if (ff.isDirectory())
					dir.add(new Option(ff.getName(), ff.getAbsolutePath(),
							Option.TYPE_FOLDER));
				else if (ff.getName().endsWith(Option.FILE_EXTENSION)) {
					fls.add(new Option(ff.getName().replace(
							Option.FILE_EXTENSION, ""), ff.getAbsolutePath(),
							Option.TYPE_FILE));
				}
			}
		} catch (Exception e) {
		}
		Collections.sort(dir);
		Collections.sort(fls);
		dir.addAll(fls);
		// if (!f.getName().equalsIgnoreCase(initialDir.getName()))
		// dir.add(0, new Option("Parent directory", f.getParent(),
		// Option.TYPE_PARENT_FOLDER));
		adapter = new FileArrayAdapter(FileChooserActivity.this, dir);
		this.setListAdapter(adapter);
		if (goUp != null)
			goUp.setVisible(currentDir.getParent() != null);
		if (operation == OPERATION_SAVE_FILE) {
			fileNameBar.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Option o = adapter.getItem(position);
		if (o.getType() == Option.TYPE_FOLDER) {
			currentDir = new File(o.getPath());
			fillInList(currentDir);
		} else {
			onFileClick(o);
		}
	}

	private void onFileClick(Option o) {
		switch (operation) {
		case OPERATION_OPEN_FILE:
			returnFileName(o.getPath());
			break;
		case OPERATION_SAVE_FILE:
			fileName.setText(o.getName());
			break;
		}
	}

	private static int REQUEST_CONFIRM_OVERWRITE = 1;

	@Override
	public void onClick(View v) {
		String fileName = this.fileName.getText().toString();
		if (fileName.length() == 0) {
			Toast.makeText(this, "Você deve fornecer um nome para o arquivo!",
					Toast.LENGTH_LONG).show();
			return;
		}
		if (!fileName.endsWith(Option.FILE_EXTENSION))
			fileName += Option.FILE_EXTENSION;
		File file = new File(currentDir, fileName);
		boolean fileExists = file.exists();
		File parent = file.getParentFile();
		if (!fileExists && !parent.canWrite()) {
			Toast.makeText(this, "Erro: accesso negado!", Toast.LENGTH_LONG)
					.show();
			return;
		}
		if (fileExists) {
			// Intent i = new Intent(this, TwoButtonDialog.class);
			// i.putExtra(TwoButtonDialog.TITLE, "File exists");
			// i.putExtra(TwoButtonDialog.TEXT,
			// "Are you sure you want to overwrite this file?");
			// i.putExtra(TwoButtonDialog.OPTION_1, "Overwrite");
			// i.putExtra(TwoButtonDialog.OPTION_2, "Cancel");
			// startActivityForResult(i, REQUEST_CONFIRM_OVERWRITE);

			String[] options = { "Sobrescrever", "Cancelar" };
			Intent i = new Intent(this, GenericDialog.class);
			i.putExtra(GenericDialog.TITLE,
					"Um arquivo com esse nome já existe");
			i.putExtra(GenericDialog.TEXT,
					"Você deseja sobrescrever este arquivo?");
			i.putExtra(GenericDialog.OPTIONS, options);
			startActivityForResult(i, REQUEST_CONFIRM_OVERWRITE);
		} else {
			returnFileName(file.getAbsolutePath());
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CONFIRM_OVERWRITE) {
			if (resultCode == 0) {
				returnFileName(new File(currentDir, fileName.getText()
						.toString()).getAbsolutePath());
			}
		}
	}

	private void returnFileName(String filePath) {
		if (!filePath.endsWith(Option.FILE_EXTENSION))
			filePath += Option.FILE_EXTENSION;

		RecentFilesDAO dao = new RecentFilesDAO(this);
		dao.open();
		dao.addRecentFile(filePath);
		dao.close();

		Intent i = new Intent();
		i.putExtra(FILE_PATH, filePath);
		setResult(RESULT_OK, i);
		finish();
	}
}