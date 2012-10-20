//http://www.dreamincode.net/forums/topic/190013-creating-simple-file-chooser/

package br.com.vinyanalista.simulador.gui.android;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FileChooserActivity extends ListActivity {

	private class Option implements Comparable<Option> {

		public static final int TYPE_FOLDER = 0;
		public static final int TYPE_FILE = 1;
		public static final int TYPE_PARENT_FOLDER = 2;

		public static final String FILE_EXTENSION = ".jpg";

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
				case Option.TYPE_PARENT_FOLDER:
					icon.setImageResource(R.drawable.go_up);
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

	public static final String FILE_NAME = "FILE_NAME";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initialDir = Environment.getExternalStorageDirectory();
		currentDir = initialDir;
		fillInList(currentDir);
	}

	private void fillInList(File f) {
		File[] dirs = f.listFiles();
		setTitle("Current directory: " + f.getName());
		List<Option> dir = new ArrayList<Option>();
		List<Option> fls = new ArrayList<Option>();
		try {
			for (File ff : dirs) {
				if (ff.isDirectory())
					dir.add(new Option(ff.getName(), ff.getAbsolutePath(),
							Option.TYPE_FOLDER));
				else if (ff.getName().endsWith(Option.FILE_EXTENSION)) {
					fls.add(new Option(ff.getName(), ff.getAbsolutePath(),
							Option.TYPE_FILE));
				}
			}
		} catch (Exception e) {

		}
		Collections.sort(dir);
		Collections.sort(fls);
		dir.addAll(fls);
		if (!f.getName().equalsIgnoreCase(initialDir.getName()))
			dir.add(0, new Option("Parent directory", f.getParent(),
					Option.TYPE_PARENT_FOLDER));
		adapter = new FileArrayAdapter(FileChooserActivity.this, dir);
		this.setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Option o = adapter.getItem(position);
		if (o.getType() == Option.TYPE_FOLDER
				|| o.getType() == Option.TYPE_PARENT_FOLDER) {
			currentDir = new File(o.getPath());
			fillInList(currentDir);
		} else {
			onFileClick(o);
		}
	}

	private void onFileClick(Option o) {
		Intent i = new Intent();
		i.putExtra(FILE_NAME, o.getName());
		setResult(RESULT_OK, i);
		finish();
	}
}