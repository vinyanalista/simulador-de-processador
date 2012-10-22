package br.com.vinyanalista.simulador.gui.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class WelcomeActivity extends Activity {

	// http://wptrafficanalyzer.in/blog/listview-with-images-and-text-using-simple-adapter-in-android/
	private ListView menu;

	protected static final int OPCAO_ABRIR = 0;
	protected static final int OPCAO_RECENTES = 1;
	protected static final int OPCAO_SOBRE = 2;

	private String ICONE = "icone";
	private String ROTULO = "opcao";

	String[] rotulos = new String[] { "Abrir programa", "Programas recentes",
			"Sobre" };

	int[] icones = new int[] { R.drawable.document_open,
			R.drawable.document_open_recent, R.drawable.help_about };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);

		List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

		for (int opcao = 0; opcao < 3; opcao++) {
			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put(ICONE, Integer.toString(icones[opcao]));
			hm.put(ROTULO, rotulos[opcao]);
			aList.add(hm);
		}

		String[] from = { ICONE, ROTULO };

		int[] to = { R.id.list_view_icon, R.id.list_view_label };

		SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList,
				R.layout.list_view_icon_label_layout, from, to);

		menu = (ListView) findViewById(R.id.welcome_menu);
		menu.setAdapter(adapter);
		menu.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				switch (position) {
				case OPCAO_ABRIR:
					startActivityForResult(new Intent(WelcomeActivity.this,
							FileChooserActivity.class), OPCAO_ABRIR);
					break;
				case OPCAO_RECENTES:
					startActivity(new Intent(WelcomeActivity.this,
							SimulationActivity.class));
					break;
				default:
					break;
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.welcome, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case OPCAO_ABRIR:
			if (resultCode == RESULT_OK) {
				Toast.makeText(
						this,
						"File Clicked: "
								+ data.getStringExtra(FileChooserActivity.FILE_NAME),
						Toast.LENGTH_SHORT).show();
				startActivity(new Intent(this, SimulationActivity.class));
			}
			break;
		}
	}
}
