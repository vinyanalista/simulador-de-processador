package br.com.vinyanalista.simulador.simulation;

public abstract class Animator {

	public interface AnimationEndListener {
		public void onAnimationEnd();
	}

	private AnimationEndListener listener = null;

	public abstract void animate(Animation animation);

	protected final void callAnimationEndListener() {
		if (listener != null)
			listener.onAnimationEnd();
	}

	public final void setAnimationEndListener(AnimationEndListener listener) {
		this.listener = listener;
	}

}