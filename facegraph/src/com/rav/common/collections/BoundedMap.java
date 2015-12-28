package com.rav.common.collections;

import java.util.Map;

public interface BoundedMap<K, V>
extends Map<K, V>
{
	/**
	 * @return True if Map is full, False if not.
	 */
	boolean isFull();
	/**
	 * @return Max capacity of map.
	 */
	int getMaxSize();
}
