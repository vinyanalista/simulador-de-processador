package br.com.vinyanalista.simulador.gui.android;

import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import br.com.vinyanalista.simulador.data.Byte;
import br.com.vinyanalista.simulador.simulation.Animator;
import br.com.vinyanalista.simulador.simulation.Animation;

public class AndroidAnimator extends Animator {

	public final static int COR_VERDE = Color.parseColor("#008000");
	public final static int COR_VERMELHA = Color.parseColor("#ff0000");
	public final static int COR_VERMELHA_ESCURA = Color.parseColor("#7b0010");
	public final static int COR_BRANCA = Color.WHITE;

	SimulationActivity activity;
	Animation animation = null;

	private AnimatorSet position(TextView textView, int x, int y) {
		ObjectAnimator positionX = ObjectAnimator.ofFloat(textView,
				"translationX", x).setDuration(1);

		ObjectAnimator positionY = ObjectAnimator.ofFloat(textView,
				"translationY", y).setDuration(1);

		AnimatorSet position = new AnimatorSet();
		position.playTogether(positionX, positionY);
		return position;
	}

	private AnimatorSet position(TextView textView, TextView reference) {
		return position(textView, reference.getLeft(), reference.getTop());
	}

	private AnimatorSet translateY(TextView position1, TextView position2,
			Byte value) {
		AnimatorSet position = position(activity.moving_byte, position1);
		activity.moving_byte.setText(value.getValueAsPreferredRepresentation());

		ValueAnimator show = ObjectAnimator.ofInt(activity.moving_byte,
				"visibility", View.VISIBLE).setDuration(1);

		ObjectAnimator translation = ObjectAnimator.ofFloat(
				activity.moving_byte, "translationY", position1.getTop(),
				position2.getTop()).setDuration(1000);

		AnimatorSet hide = position(activity.moving_byte, 1000000, 1000000);

		AnimatorSet translateY = new AnimatorSet();
		translateY.playSequentially(position, show, translation, hide);
		return translateY;
	}

	private AnimatorSet move(TextView position1, TextView position2,
			TextView position3, TextView position4, Byte value) {
		AnimatorSet position = position(activity.moving_byte, position1);
		activity.moving_byte.setText(value.getValueAsPreferredRepresentation());

		ValueAnimator show = ObjectAnimator.ofInt(activity.moving_byte,
				"visibility", View.VISIBLE).setDuration(1);

		ObjectAnimator step1 = ObjectAnimator.ofFloat(activity.moving_byte,
				"translationY", position1.getTop(), position2.getTop())
				.setDuration(1000);

		ObjectAnimator step2 = ObjectAnimator.ofFloat(activity.moving_byte,
				"translationX", position2.getLeft(), position3.getLeft())
				.setDuration(1000);

		ObjectAnimator step3 = ObjectAnimator.ofFloat(activity.moving_byte,
				"translationY", position3.getTop(), position4.getTop())
				.setDuration(1000);

		AnimatorSet hide = position(activity.moving_byte, 1000000, 1000000);

		AnimatorSet move = new AnimatorSet();
		move.playSequentially(position, show, step1, step2, step3, hide);
		return move;
	}

	private ValueAnimator changeValueAndBlink(TextView textView, int color1,
			int color2, int color3, int color4, int color5, String newValue) {
		textView.setText(newValue);

		ValueAnimator mudarValorPiscar = ObjectAnimator.ofInt(textView,
				"textColor", color1, color2, color3, color4, color5)
				.setDuration(3000);
		mudarValorPiscar.setEvaluator(new ArgbEvaluator());
		mudarValorPiscar.setRepeatMode(ValueAnimator.REVERSE);

		return mudarValorPiscar;
	}

	private ValueAnimator changeValue(TextView textView, Byte newValue) {
		return changeValueAndBlink(textView, COR_VERDE, COR_VERMELHA,
				COR_VERDE, COR_VERMELHA, COR_VERDE,
				newValue.getValueAsPreferredRepresentation());
	}

	private ValueAnimator changeLedValue(Byte newValue) {
		return changeValueAndBlink(activity.led, COR_VERMELHA,
				COR_VERMELHA_ESCURA, COR_VERMELHA, COR_VERMELHA_ESCURA,
				COR_VERMELHA, newValue.getValueAsPreferredRepresentation());
	}

	private ValueAnimator changeStatusBarValue(String newValue) {
		return changeValueAndBlink(activity.status, COR_BRANCA, COR_VERMELHA,
				COR_BRANCA, COR_VERMELHA, COR_BRANCA, newValue);
	}

	@Override
	public void animate(Animation animation) {
		this.animation = animation;
		com.nineoldandroids.animation.Animator animator = null;
		switch (animation.getType()) {
		case MAR_CHANGE:
			animator = changeValue(activity.mar, animation.getValue());
			break;
		case MBR_CHANGE:
			animator = changeValue(activity.mbr, animation.getValue());
			break;
		case IR_OPCODE_CHANGE:
			animator = changeValue(activity.ir1, animation.getValue());
			break;
		case IR_OPERAND_CHANGE:
			animator = changeValue(activity.ir2, animation.getValue());
			break;
		case PC_CHANGE:
			animator = changeValue(activity.pc, animation.getValue());
			break;
		case ACC_CHANGE:
			animator = changeValue(activity.acc, animation.getValue());
			break;
		case ALU_IN_1_CHANGE:
			animator = changeValue(activity.alu1, animation.getValue());
			break;
		case ALU_IN_2_CHANGE:
			animator = changeValue(activity.alu2, animation.getValue());
			break;
		case ALU_OUTPUT_CHANGE:
			animator = changeValue(activity.alu_out, animation.getValue());
			break;
		case ACC_TO_ALU_IN_1:
			animator = move(activity.to_acc, activity.to_acc_or_mbr,
					activity.to_alu1_or_ir1, activity.to_alu1,
					animation.getValue());
			break;
		case ACC_TO_ALU_IN_2:
			animator = move(activity.to_acc, activity.to_acc_or_mbr,
					activity.to_alu2_or_ir2, activity.to_alu2,
					animation.getValue());
			break;
		case ALU_OUTPUT_TO_ACC:
			animator = move(activity.alu_to_acc1, activity.alu_to_acc2,
					activity.alu_to_acc3, activity.alu_to_acc4,
					animation.getValue());
			break;
		case IR_OPERAND_TO_MAR:
			animator = move(activity.to_ir2, activity.to_alu2_or_ir2,
					activity.to_pc_or_mar, activity.to_mar,
					animation.getValue());
			break;
		case MAR_TO_MEMORY:
			animator = move(activity.to_mar, activity.to_pc_or_mar,
					activity.from_memory_2, activity.from_memory_1,
					animation.getValue());
			break;
		case MBR_TO_ACC:
			animator = translateY(activity.to_mbr, activity.to_acc,
					animation.getValue());
			break;
		case MBR_TO_IR_OPCODE:
			animator = move(activity.to_mbr, activity.to_acc_or_mbr,
					activity.to_alu1_or_ir1, activity.to_ir1,
					animation.getValue());
			break;
		case MBR_TO_IR_OPERAND:
			animator = move(activity.to_mbr, activity.to_acc_or_mbr,
					activity.to_alu2_or_ir2, activity.to_ir2,
					animation.getValue());
			break;
		case MEMORY_TO_MBR:
			animator = move(activity.from_memory_1, activity.from_memory_2,
					activity.to_acc_or_mbr, activity.to_mbr,
					animation.getValue());
			break;
		case PC_TO_MAR:
			animator = translateY(activity.to_pc, activity.to_mar,
					animation.getValue());
			break;
		case ACC_TO_MBR:
			animator = translateY(activity.to_acc, activity.to_mbr,
					animation.getValue());
			break;
		case IR_OPERAND_TO_ACC:
			animator = move(activity.to_ir2, activity.to_alu2_or_ir2,
					activity.to_acc_or_mbr, activity.to_acc,
					animation.getValue());
			break;
		case LED_CHANGE:
			animator = changeLedValue(animation.getValue());
			break;
		case MBR_TO_LED:
			animator = move(activity.to_mbr, activity.to_acc_or_mbr,
					activity.from_memory_2, activity.to_led,
					animation.getValue());
			break;
		case MBR_TO_MEMORY:
			animator = move(activity.to_mbr, activity.to_acc_or_mbr,
					activity.from_memory_2, activity.from_memory_1,
					animation.getValue());
			break;
		case UPDATE_INSTRUCTION:
			activity.atualizarPonteiroDeInstrucao();
			callAnimationEndListener();
			break;		
		case STATUS_FETCH_INSTRUCTION:
			animator = changeStatusBarValue("Fetch instruction cycle");
			break;
		case STATUS_FETCH_OPERAND:
			animator = changeStatusBarValue("Fetch operand cycle");
			break;
		case STATUS_PC_INCREMENT:
			animator = changeStatusBarValue("Program Counter (PC) increment");
			break;
		case STATUS_EXECUTE:
			animator = changeStatusBarValue("Execution cycle");
			break;
		
		default:
			break;
		}
		if (animator != null) {
			animator.addListener(new AnimatorListener() {
				@Override
				public void onAnimationStart(
						com.nineoldandroids.animation.Animator arg0) {
				}

				@Override
				public void onAnimationRepeat(
						com.nineoldandroids.animation.Animator arg0) {
				}

				@Override
				public void onAnimationEnd(
						com.nineoldandroids.animation.Animator arg0) {
					callAnimationEndListener();
				}

				@Override
				public void onAnimationCancel(
						com.nineoldandroids.animation.Animator arg0) {
				}
			});
			animator.start();
		}
	}

	public AndroidAnimator(SimulationActivity activity) {
		this.activity = activity;
	}
}