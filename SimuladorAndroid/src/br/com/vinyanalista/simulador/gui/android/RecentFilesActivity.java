package br.com.vinyanalista.simulador.gui.android;

//http://wptrafficanalyzer.in/blog/listview-with-images-and-text-using-simple-adapter-in-android/
//http://stackoverflow.com/questions/4920528/iterate-through-rows-from-sqlite-query
//http://developer.android.com/training/notepad/notepad-ex1.html

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.actionbarsherlock.app.SherlockListActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class RecentFilesActivity extends SherlockListActivity {

	public static final String FILE_NAME = "file_name";

	private RecentFilesDAO dao;
	List<String> recentFiles;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Programas recentes");
		fetchRecentFiles();
		fillInList();
	}
	
	private void fetchRecentFiles() {
		dao = new RecentFilesDAO(this);
		dao.open();
		recentFiles = dao.fetchAllRecentFiles();
		dao.close();
	}

	private void fillInList() {
		String[] from = { "icon", RecentFilesDAO.KEY_PATH };
		int[] to = { R.id.list_view_icon, R.id.list_view_label };

		List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

		for (String recentFile : recentFiles) {
			HashMap<String, String> hashMap = new HashMap<String, String>();
			hashMap.put("icon", Integer.toString(R.drawable.application_x_m4));
			hashMap.put(RecentFilesDAO.KEY_PATH, new File(recentFile).getName());
			aList.add(hashMap);
		}

		SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList,
				R.layout.list_view_icon_label_layout, from, to);

		setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent i = new Intent();
		i.putExtra(FILE_NAME, recentFiles.get(position));
		setResult(RESULT_OK, i);
		finish();
	}

}