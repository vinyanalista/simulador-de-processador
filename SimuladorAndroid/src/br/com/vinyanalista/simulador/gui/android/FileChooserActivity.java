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
import android.widget.ListView;
import android.widget.TextView;

public class FileChooserActivity extends ListActivity {

	private class Option implements Comparable<Option> {
		private String name;
		private String data;
		private String path;

		public Option(String n, String d, String p) {
			name = n;
			data = d;
			path = p;
		}

		public String getName() {
			return name;
		}

		public String getData() {
			return data;
		}

		public String getPath() {
			return path;
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

		private Context c;
		private int id;
		private List<Option> items;

		public FileArrayAdapter(Context context, int textViewResourceId,
				List<Option> objects) {
			super(context, textViewResourceId, objects);
			c = context;
			id = textViewResourceId;
			items = objects;
		}

		public Option getItem(int i) {
			return items.get(i);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) c
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(id, null);
			}
			final Option o = items.get(position);
			if (o != null) {
				TextView t1 = (TextView) v.findViewById(R.id.TextView01);
				TextView t2 = (TextView) v.findViewById(R.id.TextView02);

				if (t1 != null)
					t1.setText(o.getName());
				if (t2 != null)
					t2.setText(o.getData());
			}
			return v;
		}

	}

	private File currentDir;
	private FileArrayAdapter adapter;

	public static final String FILE_NAME = "FILE_NAME";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		currentDir = Environment.getExternalStorageDirectory();
		fillInList(currentDir);
	}

	private void fillInList(File f) {
		File[] dirs = f.listFiles();
		this.setTitle("Current Dir: " + f.getName());
		List<Option> dir = new ArrayList<Option>();
		List<Option> fls = new ArrayList<Option>();
		try {
			for (File ff : dirs) {
				if (ff.isDirectory())
					dir.add(new Option(ff.getName(), "Folder", ff
							.getAbsolutePath()));
				else {
					fls.add(new Option(ff.getName(), "File Size: "
							+ ff.length(), ff.getAbsolutePath()));
				}
			}
		} catch (Exception e) {

		}
		Collections.sort(dir);
		Collections.sort(fls);
		dir.addAll(fls);
		if (!f.getName().equalsIgnoreCase("sdcard"))
			dir.add(0, new Option("..", "Parent Directory", f.getParent()));
		adapter = new FileArrayAdapter(FileChooserActivity.this,
				R.layout.file_view, dir);
		this.setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Option o = adapter.getItem(position);
		if (o.getData().equalsIgnoreCase("folder")
				|| o.getData().equalsIgnoreCase("parent directory")) {
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