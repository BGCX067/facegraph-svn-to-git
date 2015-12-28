package com.rav.common.progress;

public interface Watchable {
	/**
	 * @return Maximum progress of task.
	 */
	int getMax();
	/**
	 * @return Current progress of task.
	 */
	int getProgress();
	/**
	 * @return Current message of task.
	 */
	String getMsg();
}
