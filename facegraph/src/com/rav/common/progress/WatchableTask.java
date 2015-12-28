package com.rav.common.progress;

public class WatchableTask extends Thread implements Watchable {
	private int n_max;
	private int max;
	private int progress;
	private String msg;

	public WatchableTask( int max, String msg ) {
		super();
		this.n_max = max;
		resetTask( max, msg );
	}

	@Override
	public int getMax() {
		return this.n_max;
	}

	@Override
	public int getProgress() {
		return (this.progress * this.n_max) / this.max;
	}

	protected void setMsg(String msg)
	{
		this.msg = msg;
	}
	protected void incProgress( String msg ) {
		this.incProgress();
		this.setMsg( msg );
	}
	protected void incProgress(){
		this.setProgress( this.progress + 1);
	}
	protected void setProgress( int progress, String msg ) {
		this.setProgress( progress );
		this.setMsg( msg );
	}
	protected void setProgress( int progress ) {
		if (progress < 0 || progress > this.max)
			throw new IllegalArgumentException(
					"Progress has to be from <0, max>." );
		if (progress < this.progress)
			throw new IllegalArgumentException(
					"New progress can NOT be less the current." );
		this.progress = progress;
	}

	protected void resetTask( int max, String msg ) {
		this.max = max;
		this.progress = 0;
		this.msg = msg;
	}

	@Override
	public String getMsg() {
		return msg;
	}
}
