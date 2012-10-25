package br.com.vinyanalista.simulador.gui.android;

import java.util.List;

import com.actionbarsherlock.app.SherlockActivity;

import br.com.vinyanalista.simulador.simulation.Simulation;
import br.com.vinyanalista.simulador.software.Instruction;
import br.com.vinyanalista.simulador.software.ProgramParser;

import android.os.Bundle;
import android.content.Context;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SimulationActivity extends SherlockActivity {

	public static final String EXTRA_PROGRAM = "program";

	TextView from_memory_1, from_memory_2, to_pc_or_mar, to_pc, pc, to_mar,
			mar, to_acc_or_mbr, to_acc, acc, to_mbr, mbr, to_alu2_or_ir2,
			to_alu2, alu2, to_ir2, ir2, to_alu1_or_ir1, to_alu1, alu1, to_ir1,
			ir1, alu_to_acc1, alu_to_acc2, alu_to_acc3, alu_to_acc4, alu_out,
			to_led, led, moving_byte, status;

	ListView instructionsListView;

	InstructionArrayAdapter adapter;

	private MenuItem playPause;
	private MenuItem stop;

	private Simulation simulation;
	private AndroidAnimator animator;

	// http://www.mkyong.com/android/android-listview-example/
	private class InstructionArrayAdapter extends ArrayAdapter<Instruction> {
		private final Context context;
		private final List<Instruction> instructions;

		public InstructionArrayAdapter(Context context,
				List<Instruction> instructions) {
			super(context, R.layout.list_view_instruction, instructions);
			this.context = context;
			this.instructions = instructions;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View rowView = inflater.inflate(R.layout.list_view_instruction,
					parent, false);
			TextView instructionOpcode = (TextView) rowView
					.findViewById(R.id.instruction_opcode);
			TextView instructionOperand = (TextView) rowView
					.findViewById(R.id.instruction_operand);
			ImageView instructionIcon = (ImageView) rowView
					.findViewById(R.id.instruction_icon);

			instructionOpcode.setText(instructions.get(position).getOpCode()
					.getValueAsPreferredRepresentation());
			instructionOperand.setText(instructions.get(position).getOperand()
					.getValueAsPreferredRepresentation());
			instructionIcon.setImageResource(R.drawable.arrow_right);

			if (position == simulation.getInstructionIndex() - 1) {
				instructionIcon.setVisibility(View.VISIBLE);
			} else {
				instructionIcon.setVisibility(View.INVISIBLE);
			}

			return rowView;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simulation);
		setTitle("Simulation");

		from_memory_1 = (TextView) findViewById(R.id.from_memory_1);
		from_memory_2 = (TextView) findViewById(R.id.from_memory_2);
		to_pc_or_mar = (TextView) findViewById(R.id.to_pc_or_mar);
		to_pc = (TextView) findViewById(R.id.to_pc);
		pc = (TextView) findViewById(R.id.pc);
		to_mar = (TextView) findViewById(R.id.to_mar);
		mar = (TextView) findViewById(R.id.mar);
		to_acc_or_mbr = (TextView) findViewById(R.id.to_acc_or_mbr);
		to_acc = (TextView) findViewById(R.id.to_acc);
		acc = (TextView) findViewById(R.id.acc);
		to_mbr = (TextView) findViewById(R.id.to_mbr);
		mbr = (TextView) findViewById(R.id.mbr);
		to_alu2_or_ir2 = (TextView) findViewById(R.id.to_alu2_or_ir2);
		to_alu2 = (TextView) findViewById(R.id.to_alu2);
		alu2 = (TextView) findViewById(R.id.alu2);
		to_ir2 = (TextView) findViewById(R.id.to_ir2);
		ir2 = (TextView) findViewById(R.id.ir2);
		to_alu1_or_ir1 = (TextView) findViewById(R.id.to_alu1_or_ir1);
		to_alu1 = (TextView) findViewById(R.id.to_alu1);
		alu1 = (TextView) findViewById(R.id.alu1);
		to_ir1 = (TextView) findViewById(R.id.to_ir1);
		ir1 = (TextView) findViewById(R.id.ir1);
		alu_to_acc1 = (TextView) findViewById(R.id.alu_to_acc1);
		alu_to_acc2 = (TextView) findViewById(R.id.alu_to_acc2);
		alu_to_acc3 = (TextView) findViewById(R.id.alu_to_acc3);
		alu_to_acc4 = (TextView) findViewById(R.id.alu_to_acc4);
		alu_out = (TextView) findViewById(R.id.alu_out);
		to_led = (TextView) findViewById(R.id.to_led);
		led = (TextView) findViewById(R.id.led);
		moving_byte = (TextView) findViewById(R.id.moving_byte);
		status = (TextView) findViewById(R.id.status);
		status.setText("Click Resume to start the simulation!");

		animator = new AndroidAnimator(this);
		simulation = new Simulation(ProgramParser.parseFrom(null), animator);

		instructionsListView = (ListView) findViewById(R.id.instructions);

		adapter = new InstructionArrayAdapter(this, simulation.getProgram()
				.getInstructions());

		instructionsListView.setAdapter(adapter);

		// http://www.vogella.com/articles/AndroidListView/article.html#listsactivity_simple

		// String[] animations = new String[] { "PC_TO_MAR", "MAR_CHANGE",
		// "MAR_TO_MEMORY", "MEMORY_TO_MBR", "MBR_CHANGE",
		// "MBR_TO_IR_OPCODE", "MBR_TO_IR_OPERAND", "IR_OPCODE_CHANGE",
		// "IR_OPERAND_CHANGE", "PC_CHANGE", "IR_OPERAND_TO_MAR",
		// "MBR_TO_ACC", "ACC_CHANGE", "ACC_TO_ALU_IN_1",
		// "ALU_IN_1_CHANGE", "ACC_TO_ALU_IN_2", "ALU_IN_2_CHANGE",
		// "ALU_OUTPUT_CHANGE", "ALU_OUTPUT_TO_ACC", "IR_OPERAND_TO_ACC",
		// "MBR_TO_MEMORY", "ACC_TO_MBR", "MBR_TO_LED", "LED_CHANGE" };
		// animationsListView = (ListView) findViewById(R.id.listView1);
		// animationsListView.setAdapter(new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1, animations));
		// animationsListView.setOnItemClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		playPause = menu.add("Resume").setIcon(R.drawable.media_playback_start);
		playPause.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM
				| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		stop = menu.add("Stop").setIcon(R.drawable.media_playback_stop);
		stop.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM
				| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Toast.makeText(this, "Got click: " + item, Toast.LENGTH_SHORT).show();
		if (item.equals(playPause)) {
			if (item.getTitle().equals("Resume")) {
				item.setTitle("Pause");
				item.setIcon(R.drawable.media_playback_pause);
				simulation.start();
			} else {
				item.setTitle("Resume");
				item.setIcon(R.drawable.media_playback_start);
				simulation.pause();
			}
		}
		if (item.equals(stop)) {
			playPause.setTitle("Resume");
			playPause.setIcon(R.drawable.media_playback_start);
			simulation.stop();
			atualizarPonteiroDeInstrucao();
			status.setText("Click Resume to start the simulation!");
		}
		return true;
	}

	void atualizarPonteiroDeInstrucao() {
		adapter.notifyDataSetChanged();
	}

	// @Override
	// public void onItemClick(AdapterView<?> arg0, View arg1, int position,
	// long arg3) {
	// switch (position) {
	// case 0:
	// animator.animate(new Animation(AnimationType.PC_TO_MAR, new Data(
	// position)));
	// break;
	// case 1:
	// animator.animate(new Animation(AnimationType.MAR_CHANGE, new Data(
	// position)));
	// break;
	// case 2:
	// animator.animate(new Animation(AnimationType.MAR_TO_MEMORY,
	// new Data(position)));
	// break;
	// case 3:
	// animator.animate(new Animation(AnimationType.MEMORY_TO_MBR,
	// new Data(position)));
	// break;
	// case 4:
	// animator.animate(new Animation(AnimationType.MBR_CHANGE, new Data(
	// position)));
	// break;
	// case 5:
	// animator.animate(new Animation(AnimationType.MBR_TO_IR_OPCODE,
	// new Data(position)));
	// break;
	// case 6:
	// animator.animate(new Animation(AnimationType.MBR_TO_IR_OPERAND,
	// new Data(position)));
	// break;
	// case 7:
	// animator.animate(new Animation(AnimationType.IR_OPCODE_CHANGE,
	// new Data(position)));
	// break;
	// case 8:
	// animator.animate(new Animation(AnimationType.IR_OPERAND_CHANGE,
	// new Data(position)));
	// break;
	// case 9:
	// animator.animate(new Animation(AnimationType.PC_CHANGE, new Data(
	// position)));
	// break;
	// case 10:
	// animator.animate(new Animation(AnimationType.IR_OPERAND_TO_MAR,
	// new Data(position)));
	// break;
	// case 11:
	// animator.animate(new Animation(AnimationType.MBR_TO_ACC, new Data(
	// position)));
	// break;
	// case 12:
	// animator.animate(new Animation(AnimationType.ACC_CHANGE, new Data(
	// position)));
	// break;
	// case 13:
	// animator.animate(new Animation(AnimationType.ACC_TO_ALU_IN_1,
	// new Data(position)));
	// break;
	// case 14:
	// animator.animate(new Animation(AnimationType.ALU_IN_1_CHANGE,
	// new Data(position)));
	// break;
	// case 15:
	// animator.animate(new Animation(AnimationType.ACC_TO_ALU_IN_2,
	// new Data(position)));
	// break;
	// case 16:
	// animator.animate(new Animation(AnimationType.ALU_IN_2_CHANGE,
	// new Data(position)));
	// break;
	// case 17:
	// animator.animate(new Animation(AnimationType.ALU_OUTPUT_CHANGE,
	// new Data(position)));
	// break;
	// case 18:
	// animator.animate(new Animation(AnimationType.ALU_OUTPUT_TO_ACC,
	// new Data(position)));
	// break;
	// case 19:
	// animator.animate(new Animation(AnimationType.IR_OPERAND_TO_ACC,
	// new Data(position)));
	// break;
	// case 20:
	// animator.animate(new Animation(AnimationType.MBR_TO_MEMORY,
	// new Data(position)));
	// break;
	// case 21:
	// animator.animate(new Animation(AnimationType.ACC_TO_MBR, new Data(
	// position)));
	// break;
	// case 22:
	// animator.animate(new Animation(AnimationType.MBR_TO_LED, new Data(
	// position)));
	// break;
	// case 23:
	// animator.animate(new Animation(AnimationType.LED_CHANGE, new Data(
	// position)));
	// break;
	// }
	// }
}