/*
 * regain/Thumbnailer - A file search engine providing plenty of formats (Plugin)
 * Copyright (C) 2011  Come_IN Computerclubs (University of Siegen)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Contact: Come_IN-Team <come_in-team@listserv.uni-siegen.de>
 */
package ch.ivyteam.ivy.addons.filemanager.thumbnailer.util;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Hashtable that can contain several entries per key. (Helper Class)
 * 
 * Contract: <li>It is possible to put several identical key-value pairs (i.e.
 * where key and value is equal) <li>entrySet is not supported. Instead, it can
 * be iterated over all entries.
 * 
 * @param <K>
 *            Key
 * @param <V>
 *            Value
 */
public class ChainedHashMap<K, V> implements Map<K, V>,
		Iterable<Map.Entry<K, V>> {

	private static final int DEFAULT_HASHTABLE_SIZE = 20;

	private static final int DEFAULT_LIST_SIZE = 10;
	private int listSize;

	HashMap<K, List<V>> hashtable;
	int size;

	public ChainedHashMap() {
		this(DEFAULT_HASHTABLE_SIZE);
	}

	public ChainedHashMap(int hashtableSize) {
		this(hashtableSize, DEFAULT_LIST_SIZE);
	}

	public ChainedHashMap(int hashtableSize, int chainSize) {
		hashtable = new HashMap<K, List<V>>(hashtableSize);
		listSize = chainSize;

		size = 0;
	}

	public ChainedHashMap(Map<? extends K, ? extends V> map) {
		this();

		if (map instanceof ChainedHashMap) {
			// Copy-constructor
			ChainedHashMap<? extends K, ? extends V> hashtable = (ChainedHashMap<? extends K, ? extends V>) map;
			for (K key : hashtable.keySet()) {
				for (V value : hashtable.getList(key)) {
					put(key, value);
				}
			}
		} else
			putAll(map);
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public boolean containsKey(Object key) {
		return hashtable.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		if (isEmpty())
			return false;

		Collection<List<V>> elements = hashtable.values();

		for (List<V> list : elements) {
			if (list.contains(value))
				return true;
		}
		return false;
	}

	@Override
	/**
	 * Get first of the linked objects by this key.
	 */
	public V get(Object key) {
		List<V> list = hashtable.get(key);
		if (list == null)
			return null;
		else
			return list.get(0);
	}

	/**
	 * Get all objects linked by this key as an Iterable usable an foreach loop.
	 * 
	 * @param key
	 * @return Iterable
	 * @throws NullPointerException
	 *             (if key null)
	 */
	public Iterable<V> getIterable(Object key) {
		final List<V> list = hashtable.get(key);

		if (list == null) {
			// Empty Iterator
			return new Iterable<V>() {
				public Iterator<V> iterator() {
					return new Iterator<V>() {
						public boolean hasNext() {
							return false;
						}

						public V next() {
							throw new NoSuchElementException("Empty");
						}

						public void remove() {
						}
					};
				}
			};
		} else {
			// Iterator of list
			return new Iterable<V>() {
				public Iterator<V> iterator() {
					return list.iterator();
				}
			};
		}
	}

	public List<V> getList(Object key) {
		List<V> list = hashtable.get(key);
		if (list == null)
			list = new ArrayList<V>();
		return list;
	}

	/**
	 * Iterate over all elements in the table. Note that this currently copies
	 * them into a collection, so concurrent modification will not be taken into
	 * account (there will be no ConcurrentModificationException, either).
	 */
	@Override
	public Iterator<Map.Entry<K, V>> iterator() {
		if (size == 0) {
			return new Iterator<Map.Entry<K, V>>() {
				public boolean hasNext() {
					return false;
				}

				public Map.Entry<K, V> next() {
					throw new NoSuchElementException("Empty");
				}

				public void remove() {
				}
			};
		} else {
			Collection<Map.Entry<K, V>> entries = new ArrayList<Map.Entry<K, V>>();
			for (K key : hashtable.keySet()) {
				List<V> values = hashtable.get(key);
				for (V value : values)
					entries.add(new AbstractMap.SimpleEntry<K, V>(key, value));
			}
			return entries.iterator();
		}
	}

	@Override
	/**
	 * Add this Value at the end of this key.
	 * 
	 * @return As the value is never replaced, this will always return null.
	 */
	public V put(K key, V value) {
		boolean success;

		List<V> list = hashtable.get(key);
		if (list == null) {
			list = new ArrayList<V>(listSize);
			success = list.add(value);
			hashtable.put(key, list);
		} else {
			success = list.add(value);
		}

		if (success)
			size++;

		return null;
	}

	@Override
	/**
	 * Remove all objects linked to this key.
	 * 
	 * @param key	Key
	 * @return First of linked objects (or null).
	 */
	public V remove(Object key) {
		List<V> list = hashtable.remove(key);
		if (list == null)
			return null;
		else {
			V element = list.get(0);
			size -= list.size();
			return element;
		}
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> map) {
		for (Entry<? extends K, ? extends V> entry : map.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public void clear() {
		hashtable.clear();
		size = 0;
	}

	@Override
	public Set<K> keySet() {
		return hashtable.keySet();
	}

	@Override
	// "The set is backed by the map, so changes to the map are reflected in the set, and vice-versa."
	public Collection<V> values() {
		List<V> newList = new ArrayList<V>();

		if (isEmpty())
			return newList;

		Collection<List<V>> values = hashtable.values();

		for (List<V> list : values)
			newList.addAll(list);

		return newList;
	}

	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		throw new UnsupportedOperationException(
				"entrySet is not implemented, as identical entries are allowed (conflict with Set contract). Instead, use .iterator() to iterate through all entries.");
	}

	public String toString() {
		StringBuffer str = new StringBuffer(200);

		for (K key : hashtable.keySet()) {
			str.append(key).append(":\n");

			List<V> values = hashtable.get(key);
			for (V value : values)
				str.append("\t").append(value).append("\n");
			str.append("\n");
		}

		return str.toString();
	}

}
