package com.rav.common.longtask;

import java.util.ArrayList;
import java.util.List;

public final class LongTask extends Thread {
	private final Integer max;
	private final Integer min;
	private int progress;
	private TaskState state;
	private boolean sthChanged;

	private List<ITaskListener> listeners;

	public enum TaskState {
		STOP, RUN, END, WARRNING, DISPOSE
	};

	public LongTask( int max ) {
		this( 0, max );
	}

	public LongTask( int min, int max ) {
		this.max = max;
		this.min = min;
		this.state = TaskState.STOP;
		this.progress = 0;
		this.sthChanged = true;
		this.listeners = new ArrayList<ITaskListener>();
	}

	public int getMax() {
		return this.max;
	}

	public int getMin() {
		return this.min;
	}

	public int getProgress() {
		return this.progress;
	}

	public TaskState get_State() {
		return this.state;
	}

	public int get_Id() {
		return this.hashCode();
	}

	public boolean isDisposed() {
		return this.state == TaskState.DISPOSE ? true : false;
	}

	public void setProgress( int newProgress ) {
		if (state != TaskState.RUN)
			throw new IllegalStateException(
					"Task is not running, you can change progress of running task only." );
		if (newProgress < 0 || newProgress > max)
			throw new IllegalArgumentException(
					"Progress of task has to be from [min, max]." );

		progress = newProgress;
		this.sthChanged();
	}

	public void endTask() {
		state = TaskState.END;
		this.sthChanged();
	}

	public void dispose() {
		state = TaskState.DISPOSE;
		this.sthChanged();
	}

	public void registerListener( ITaskListener listener ) {
		listeners.add( listener );
	}

	private void notifierListeners() {
		if (!sthChanged)
			return;
		LongTaskInfo info = new LongTaskInfo( this );
		for (ITaskListener i : listeners) {
			i.update( info );
		}
	}

	private void sthChanged() {
		sthChanged = true;
		synchronized (min) {
			min.notifyAll();
		}
	}

	@Override
	public void run() {
		synchronized (min) {
			this.state = TaskState.RUN;
			while (!isDisposed()) {
				try {
					min.wait( 1000 );
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				notifierListeners();
			}
		}
	}
}
