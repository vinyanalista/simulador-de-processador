package br.com.vinyanalista.simulador.simulation;

import java.util.ArrayList;
import java.util.List;

public abstract class Animator {

	public interface AnimationListener {
		public void onAnimationEnd();
	}

	private List<AnimationListener> listeners = new ArrayList<AnimationListener>();

	public abstract void animate(Animation animation);

	protected final void callAnimationEndListener() {
		for (AnimationListener listener : listeners)
			listener.onAnimationEnd();
	}

	public final void addAnimationListener(AnimationListener listener) {
		listeners.add(listener);
	}

	public final void removeAnimationListener(AnimationListener listener) {
		listeners.remove(listener);
	}

}