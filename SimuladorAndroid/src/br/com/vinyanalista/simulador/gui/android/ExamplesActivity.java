package br.com.vinyanalista.simulador.gui.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.vinyanalista.simulador.examples.Example;
import br.com.vinyanalista.simulador.examples.Examples;
import br.com.vinyanalista.simulador.gui.android.R;
import br.com.vinyanalista.simulador.gui.android.simulador.SimulationActivity;
import br.com.vinyanalista.simulador.software.Program;

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
				Example.OVERFLOW.toString(), Example.NEGATIVE.toString(),
				Example.OR.toString(), Example.AND.toString(),
				Example.NOT.toString(), Example.CRASH.toString() };

		List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

		for (int opcao = 0; opcao < 7; opcao++) {
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
		Program program = null;
		switch (position) {
		case 0:
			program = Examples.getExample(Example.ADD);
			break;
		case 1:
			program = Examples.getExample(Example.OVERFLOW);
			break;
		case 2:
			program = Examples.getExample(Example.NEGATIVE);
			break;
		case 3:
			program = Examples.getExample(Example.OR);
			break;
		case 4:
			program = Examples.getExample(Example.AND);
			break;
		case 5:
			program = Examples.getExample(Example.NOT);
			break;
		case 6:
			program = Examples.getExample(Example.CRASH);
			break;
		}
		SimulationActivity.PROGRAM = program;
		startActivity(new Intent(ExamplesActivity.this,
				SimulationActivity.class));
	}
}