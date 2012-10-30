package br.com.vinyanalista.simulador.gui.android.simulador;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.ArrayAdapter;
import br.com.vinyanalista.simulador.gui.android.R;
import br.com.vinyanalista.simulador.hardware.Memory;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockListActivity;

public class MemoryActivity extends SherlockListActivity implements
		ActionBar.TabListener {
	public static final int DISPLAY_INSTRUCTION_MEMORY = 0;
	public static final int DISPLAY_DATA_MEMORY = 1;
	public static final String MEMORY_TO_DISPLAY = "which_memory";
	public static Memory INSTRUCTION_MEMORY;
	public static Memory DATA_MEMORY;

	private ActionBar.Tab instrucionMemoryTab;
	private ActionBar.Tab dataMemoryTab;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Memory");

		getSupportActionBar().setIcon(R.drawable.media_flash);
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		instrucionMemoryTab = getSupportActionBar().newTab();
		instrucionMemoryTab.setText("Instructions");
		instrucionMemoryTab.setIcon(R.drawable.system_run);
		instrucionMemoryTab.setTabListener(this);
		getSupportActionBar().addTab(instrucionMemoryTab);

		dataMemoryTab = getSupportActionBar().newTab();
		dataMemoryTab.setText("Data");
		dataMemoryTab.setIcon(R.drawable.server_database);
		dataMemoryTab.setTabListener(this);
		getSupportActionBar().addTab(dataMemoryTab);

		int memoryToDisplay = getIntent().getExtras().getInt(MEMORY_TO_DISPLAY);

		switch (memoryToDisplay) {
		case 0:
			getSupportActionBar().selectTab(instrucionMemoryTab);
			break;
		case 1:
			getSupportActionBar().selectTab(dataMemoryTab);
			break;
		}
		displayMemory(memoryToDisplay);
	}

	private void displayMemory(int memoryToDisplay) {
		// http://www.vogella.com/articles/AndroidListView/article.html#listsactivity_simple
		List<String> memoryCells = new ArrayList<String>();
		Memory memory = null;
		switch (memoryToDisplay) {
		case DISPLAY_INSTRUCTION_MEMORY:
			memory = INSTRUCTION_MEMORY;
			break;
		case DISPLAY_DATA_MEMORY:
			memory = DATA_MEMORY;
			break;
		}
		for (int address = memory.getMinAddress(); address <= memory
				.getMaxAddress(); address++) {
			String memoryCell = String.valueOf(address);
			for (int i = memoryCell.length(); i < 3; i++) {
				memoryCell = "0" + memoryCell;
			}
			memoryCell += ": "
					+ memory.readByte(address)
							.getValueAsPreferredRepresentation();
			memoryCells.add(memoryCell);
		}
		getListView().setAdapter(
				new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_1, memoryCells));
		getListView().invalidate();
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction transaction) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction transaction) {
		if (tab.equals(instrucionMemoryTab))
			displayMemory(DISPLAY_INSTRUCTION_MEMORY);
		else
			displayMemory(DISPLAY_DATA_MEMORY);
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction transaction) {
	}
}