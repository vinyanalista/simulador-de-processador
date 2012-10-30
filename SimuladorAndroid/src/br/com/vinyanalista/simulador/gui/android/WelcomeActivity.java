package br.com.vinyanalista.simulador.gui.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.vinyanalista.simulador.gui.android.R;
import br.com.vinyanalista.simulador.gui.android.editor.ProgramEditorActivity;

import com.actionbarsherlock.app.SherlockListActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class WelcomeActivity extends SherlockListActivity {

	// http://wptrafficanalyzer.in/blog/listview-with-images-and-text-using-simple-adapter-in-android/

	protected static final int OPCAO_NOVO = 0;
	protected static final int OPCAO_ABRIR = 1;
	protected static final int OPCAO_RECENTES = 2;
	protected static final int OPCAO_EXEMPLOS = 3;
	protected static final int OPCAO_SOBRE = 4;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String ICONE = "icone";
		String ROTULO = "opcao";

		String[] rotulos = new String[] { "Novo programa", "Abrir programa",
				"Programas recentes", "Programas de exemplo", "Sobre" };

		int[] icones = new int[] { R.drawable.document_new,
				R.drawable.document_open, R.drawable.document_open_recent,
				R.drawable.document_open_recent, R.drawable.help_about };

		List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

		for (int opcao = 0; opcao < 5; opcao++) {
			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put(ICONE, Integer.toString(icones[opcao]));
			hm.put(ROTULO, rotulos[opcao]);
			aList.add(hm);
		}

		String[] from = { ICONE, ROTULO };

		int[] to = { R.id.list_view_icon, R.id.list_view_label };

		SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList,
				R.layout.list_view_icon_label_layout, from, to);

		getListView().setAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		switch (position) {
		case OPCAO_NOVO:
			startActivity(new Intent(WelcomeActivity.this,
					ProgramEditorActivity.class));
			break;
		case OPCAO_ABRIR:
			startActivityForResult(new Intent(WelcomeActivity.this,
					FileChooserActivity.class), OPCAO_ABRIR);
			break;
		case OPCAO_RECENTES:
			startActivityForResult(new Intent(WelcomeActivity.this,
					RecentFilesActivity.class), OPCAO_RECENTES);
			break;
		case OPCAO_EXEMPLOS:
			startActivity(new Intent(WelcomeActivity.this,
					ExamplesActivity.class));
			break;
		case OPCAO_SOBRE:
			Toast.makeText(WelcomeActivity.this, "Não implementado ainda",
					Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case OPCAO_ABRIR:
			if (resultCode == RESULT_OK) {
				Intent i = new Intent(this, ProgramEditorActivity.class);
				i.putExtra(ProgramEditorActivity.FILE_PATH,
						data.getStringExtra(FileChooserActivity.FILE_PATH));
				startActivity(i);
			}
			break;
		case OPCAO_RECENTES:
			if (resultCode == RESULT_OK) {
				Intent i = new Intent(this, ProgramEditorActivity.class);
				i.putExtra(ProgramEditorActivity.FILE_PATH,
						data.getStringExtra(FileChooserActivity.FILE_PATH));
				startActivity(i);
			}
			break;
		}
	}
}