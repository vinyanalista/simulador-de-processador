package br.com.vinyanalista.simulador.gui.android;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.TypeEvaluator;
import com.nineoldandroids.animation.ValueAnimator;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import br.com.vinyanalista.simulador.data.Byte;
import br.com.vinyanalista.simulador.simulation.Animator;
import br.com.vinyanalista.simulador.simulation.Animation;

public class AndroidAnimator implements Animator {

	public final static int COR_VERDE = Color.parseColor("#008000");
	public final static int COR_VERMELHA = Color.RED;

	SimulationActivity activity;
	Animation animation = null;

	private AnimatorSet position(TextView textView, TextView reference) {
		ObjectAnimator positionX = ObjectAnimator.ofFloat(textView,
				"translationX", reference.getLeft()).setDuration(1);

		ObjectAnimator positionY = ObjectAnimator.ofFloat(textView,
				"translationY", reference.getTop()).setDuration(1);

		AnimatorSet position = new AnimatorSet();
		position.playTogether(positionX, positionY);
		return position;
	}

	private AnimatorSet move(TextView position1, TextView position2,
			TextView position3, TextView position4, Byte value) {
		ValueAnimator show = ObjectAnimator.ofInt(activity.movingByte,
				"visibility", View.VISIBLE).setDuration(1);

		ObjectAnimator step1 = ObjectAnimator.ofFloat(activity.movingByte,
				"translationY", position1.getTop(), position2.getTop())
				.setDuration(1000);

		ObjectAnimator step2 = ObjectAnimator.ofFloat(activity.movingByte,
				"translationX", position2.getLeft(), position3.getLeft())
				.setDuration(1000);

		ObjectAnimator step3 = ObjectAnimator.ofFloat(activity.movingByte,
				"translationY", position3.getTop(), position4.getTop())
				.setDuration(1000);

		ValueAnimator hide = ObjectAnimator.ofInt(activity.movingByte,
				"visibility", View.INVISIBLE).setDuration(1);

		AnimatorSet fromMemory = new AnimatorSet();
		fromMemory.playSequentially(
				position(activity.movingByte, activity.from_memory_1), show,
				step1, step2, step3, hide);
		return fromMemory;
	}

	private AnimatorSet changeValue(TextView textView, Byte newValue) {
		ValueAnimator changeValue = ObjectAnimator.ofObject(textView, "text",
				new TypeEvaluator<CharSequence>() {
					@Override
					public CharSequence evaluate(float fraction,
							CharSequence startValue, CharSequence endValue) {
						return endValue;
					}
				}, newValue.getValueAsBinary()).setDuration(1);

		ValueAnimator blink = ObjectAnimator.ofInt(textView, "textColor",
				COR_VERDE, COR_VERMELHA, COR_VERDE, COR_VERMELHA, COR_VERDE)
				.setDuration(3000);
		blink.setEvaluator(new ArgbEvaluator());
		blink.setRepeatMode(ValueAnimator.REVERSE);

		AnimatorSet mudarValor = new AnimatorSet();
		mudarValor.playSequentially(changeValue, blink);
		return mudarValor;
	}

	@Override
	public void animate(Animation animation) {
		this.animation = animation;
		switch (animation.getType()) {
		case MAR_CHANGE:
			changeValue(activity.mar, animation.getValue()).start();
			break;
		case MBR_CHANGE:
			changeValue(activity.mbr, animation.getValue()).start();
			break;
		case IR_OPCODE_CHANGE:
			changeValue(activity.ir1, animation.getValue()).start();
			break;
		case IR_OPERAND_CHANGE:
			changeValue(activity.ir2, animation.getValue()).start();
			break;
		case PC_CHANGE:
			changeValue(activity.pc, animation.getValue()).start();
			break;
		case ACC_CHANGE:
			changeValue(activity.acc, animation.getValue()).start();
			break;
		case ALU_IN_1_CHANGE:
			changeValue(activity.alu1, animation.getValue()).start();
			break;
		case ALU_IN_2_CHANGE:
			changeValue(activity.alu2, animation.getValue()).start();
			break;
		case ALU_OUTPUT_CHANGE:
			changeValue(activity.alu_out, animation.getValue()).start();
			break;
		case ACC_TO_ALU_IN_1:
			move(activity.to_acc, activity.to_acc_or_mar,
					activity.to_alu1_or_ir1, activity.to_alu1,
					animation.getValue()).start();
			break;
		case ACC_TO_ALU_IN_2:
			move(activity.to_acc, activity.to_acc_or_mar,
					activity.to_alu2_or_ir2, activity.to_alu2,
					animation.getValue()).start();
			break;
		case ALU_OUTPUT_TO_ACC:
			break;
		case IR_OPERAND_TO_MAR:
			move(activity.to_mar, activity.to_acc_or_mar,
					activity.to_alu2_or_ir2, activity.to_ir2,
					animation.getValue()).start();
			break;
		case MAR_TO_MEMORY:
			move(activity.to_mar, activity.to_acc_or_mar,
					activity.from_memory_2, activity.from_memory_1,
					animation.getValue()).start();
			break;
		case MBR_TO_ACC:
			move(activity.to_mbr, activity.to_pc_or_mbr,
					activity.to_acc_or_mar, activity.to_acc,
					animation.getValue()).start();
			break;
		case MBR_TO_IR_OPCODE:
			move(activity.to_mbr, activity.to_pc_or_mbr,
					activity.to_alu1_or_ir1, activity.to_ir1,
					animation.getValue()).start();
			break;
		case MBR_TO_IR_OPERAND:
			move(activity.to_mbr, activity.to_pc_or_mbr,
					activity.to_alu2_or_ir2, activity.to_ir2,
					animation.getValue()).start();
			break;
		case MEMORY_TO_MBR:
			move(activity.from_memory_1, activity.from_memory_2,
					activity.to_pc_or_mbr, activity.to_mbr,
					animation.getValue()).start();
			break;
		case PC_TO_MAR:
		case ACC_TO_MBR:
		case IR_OPERAND_TO_ACC:
		case LED_CHANGE:
		case MBR_TO_LED:
		case MBR_TO_MEMORY:
		default:
			break;
		}
	}

	public AndroidAnimator(SimulationActivity activity) {
		this.activity = activity;
	}
}