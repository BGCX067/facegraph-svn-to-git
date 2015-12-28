package com.rav.common.collections;

import java.util.Map.Entry;

public class Tuple<T, K>
implements Entry<T, K>
{
	private final T key;
	private K value;
	
	public Tuple(T key, K value)
	{
		this.key = key;
		this.value = value;
	}
	
	public Tuple(T key)
	{
		this(key, null);
	}
	
	@Override
	public T getKey() {
		return this.key;
	}

	@Override
	public K getValue() {
		return this.value;
	}

	@Override
	public K setValue(K value) {
		K old = value;
		this.value = value;
		return old;
	}

}
