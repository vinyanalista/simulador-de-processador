package br.com.vinyanalista.simulador.gui.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.vinyanalista.simulador.examples.Example;
import br.com.vinyanalista.simulador.gui.android.R;
import br.com.vinyanalista.simulador.gui.android.simulador.SimulationActivity;

import com.actionbarsherlock.app.SherlockListActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ExamplesActivity extends SherlockListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Examples");

		String ICONE = "icone";
		String ROTULO = "opcao";

		String[] rotulos = new String[] { Example.ADD.toString(),
				Example.SUB.toString(), Example.OVERFLOW.toString(),
				Example.NOT.toString() };

		List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

		for (int opcao = 0; opcao < 4; opcao++) {
			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put(ICONE, Integer.toString(R.drawable.application_x_m4));
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
		super.onListItemClick(l, v, position, id);
		Intent i = new Intent(ExamplesActivity.this, SimulationActivity.class);
		i.putExtra(SimulationActivity.EXTRA_PROGRAM, position);
		startActivity(i);
	}
}