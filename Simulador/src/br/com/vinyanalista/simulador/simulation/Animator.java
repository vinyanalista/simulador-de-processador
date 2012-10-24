package br.com.vinyanalista.simulador.simulation;

public abstract class Animator {

	interface AnimationEndListener {
		public void onAnimationEnd();
	}

	AnimationEndListener listener = null;

	public abstract void animate(Animation animation);

	protected final void callAnimationEndListener() {
		if (listener != null)
			listener.onAnimationEnd();
	}

	final void setAnimationEndListener(AnimationEndListener listener) {
		this.listener = listener;
	}

}