package br.com.vinyanalista.simulador.gui.android;

import com.nineoldandroids.animation.Animator.AnimatorListener;
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
import br.com.vinyanalista.simulador.simulation.SingleAnimation;

public class AndroidAnimator implements Animator, AnimatorListener {

	public abstract class OnAnimationEndListener {

		public abstract void onAnimationEnd(SingleAnimation animation);

	}

	public final static int COR_VERDE = Color.parseColor("#008000");
	public final static int COR_VERMELHA = Color.RED;

	SimulationActivity activity;
	SingleAnimation animation = null;
	OnAnimationEndListener listener;

	private AnimatorSet position(TextView textView, TextView reference) {
		ObjectAnimator positionX = ObjectAnimator.ofFloat(textView,
				"translationX", reference.getLeft()).setDuration(1);

		ObjectAnimator positionY = ObjectAnimator.ofFloat(textView,
				"translationY", reference.getTop()).setDuration(1);

		AnimatorSet position = new AnimatorSet();
		position.playTogether(positionX, positionY);
		return position;
	}

	private AnimatorSet fromMemory(TextView intermediateStep, TextView to,
			Byte value) {
		ValueAnimator show = ObjectAnimator.ofInt(activity.movingByte,
				"visibility", View.VISIBLE).setDuration(1);

		ObjectAnimator step1 = ObjectAnimator.ofFloat(activity.movingByte,
				"translationY", activity.from_memory_1.getTop(),
				activity.from_memory_2.getTop()).setDuration(1000);

		ObjectAnimator step2 = ObjectAnimator.ofFloat(activity.movingByte,
				"translationX", activity.from_memory_2.getLeft(),
				intermediateStep.getLeft()).setDuration(1000);

		ObjectAnimator step3 = ObjectAnimator.ofFloat(activity.movingByte,
				"translationY", intermediateStep.getTop(), to.getTop())
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
	public void animate(SingleAnimation animation) {
		this.animation = animation;
		switch (animation.getType()) {
		case CHANGE_MAR:
			AnimatorSet changeMar = changeValue(activity.mar,
					animation.getValue());
			changeMar.addListener(this);
			changeMar.start();
			break;
		case CHANGE_MBR:
			AnimatorSet changeMbr = changeValue(activity.mbr,
					animation.getValue());
			changeMbr.addListener(this);
			changeMbr.start();
			break;
		case CHANGE_IR_OPCODE:
			AnimatorSet changeIrOpcode = changeValue(activity.ir1,
					animation.getValue());
			changeIrOpcode.addListener(this);
			changeIrOpcode.start();
			break;
		case CHANGE_IR_OPERAND:
			AnimatorSet changeIrOperand = changeValue(activity.ir2,
					animation.getValue());
			changeIrOperand.addListener(this);
			changeIrOperand.start();
			break;
		case CHANGE_PC:
			AnimatorSet changePc = changeValue(activity.pc,
					animation.getValue());
			changePc.addListener(this);
			changePc.start();
			break;
		case CHANGE_ACC:
			AnimatorSet changeAcc = changeValue(activity.acc,
					animation.getValue());
			changeAcc.addListener(this);
			changeAcc.start();
			break;
		case CHANGE_ALU_1:
			AnimatorSet changeAlu1 = changeValue(activity.alu1,
					animation.getValue());
			changeAlu1.addListener(this);
			changeAlu1.start();
			break;
		case CHANGE_ALU_2:
			AnimatorSet changeAlu2 = changeValue(activity.alu2,
					animation.getValue());
			changeAlu2.addListener(this);
			changeAlu2.start();
			break;
		case CHANGE_ALU_OUTPUT:
			AnimatorSet changeAluOutput = changeValue(activity.alu_out,
					animation.getValue());
			changeAluOutput.addListener(this);
			changeAluOutput.start();
			break;
		case ACC_TO_ALU_1:
			break;
		case ACC_TO_ALU_2:
			break;
		case ALU_TO_ACC:
			break;
		case IR_TO_MAR:
			break;
		case MAR_TO_MEMORY:
			break;
		case MBR_TO_ACC:
			break;
		case MBR_TO_IR_1:
			break;
		case MBR_TO_IR_2:
			break;
		case MEMORY_TO_MBR:
			AnimatorSet memoryToMbr = fromMemory(activity.to_pc_or_mbr,
					activity.to_mbr, animation.getValue());
			memoryToMbr.addListener(this);
			memoryToMbr.start();
			break;
		case PC_TO_MAR:
			break;
		default:
			break;
		}
	}

	public AndroidAnimator(SimulationActivity activity) {
		this.activity = activity;
	}

	public AndroidAnimator setOnAnimationEndListener(
			OnAnimationEndListener listener) {
		this.listener = listener;
		return this;
	}

	@Override
	public void onAnimationEnd(com.nineoldandroids.animation.Animator animator) {
		if (listener != null)
			listener.onAnimationEnd(animation);
	}

	@Override
	public void onAnimationCancel(
			com.nineoldandroids.animation.Animator animator) {
	}

	@Override
	public void onAnimationRepeat(
			com.nineoldandroids.animation.Animator animator) {
	}

	@Override
	public void onAnimationStart(com.nineoldandroids.animation.Animator animator) {
	}
}