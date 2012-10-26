package br.com.vinyanalista.simulador.gui.android;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.ActionBar.Tab;

public class MemoryActivity extends SherlockActivity implements
		ActionBar.TabListener {
	public static final int TAB_INSTRUCTION_MEMORY = 0;
	public static final int TAB_DATA_MEMORY = 1;

	private TextView mSelected;

	private TableLayout table;

	private ActionBar.Tab instrucionMemoryTab;
	private ActionBar.Tab dataMemoryTab;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		table = new TableLayout(this);
		table.setStretchAllColumns(true);
		table.setShrinkAllColumns(true);
		setContentView(table);

		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		instrucionMemoryTab = getSupportActionBar().newTab();
		instrucionMemoryTab.setText("Instruction Memory");
		instrucionMemoryTab.setTabListener(this);
		getSupportActionBar().addTab(instrucionMemoryTab);

		dataMemoryTab = getSupportActionBar().newTab();
		dataMemoryTab.setText("Data Memory");
		dataMemoryTab.setTabListener(this);
		getSupportActionBar().addTab(dataMemoryTab);
	}

	private void displayMemory(int whichMemory) {
		TableRow tableHeader = new TableRow(this);  
		tableHeader.setGravity(Gravity.CENTER_HORIZONTAL); 
		TextView lineNumber = new TextView(this);
		lineNumber.setText("");
		tableHeader.addView(lineNumber);
		for (int column = 0; column < 10; column++) {
			TextView columnNumber = new TextView(this);
			columnNumber.setText(column);
			tableHeader.addView(lineNumber);
			
		}
		
		
//		int column = 0;
//		for (int line = 0; line < 15; line++) {
//			TextView lineNumber = new TextView();
//			table.addView;
//		}
		
		
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction transaction) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction transaction) {
		if (tab.equals(instrucionMemoryTab))
			displayMemory(TAB_INSTRUCTION_MEMORY);
		else
			displayMemory(TAB_DATA_MEMORY);
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction transaction) {
	}
}