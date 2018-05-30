package vn.com.epi.gate.sentiment;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
//import java.util.Objects;
import java.util.Set;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;

/**
 * Posted from May 25, 2018 1:43 AM
 *
 * @author viquy (vi.quynh.31598@gmail.com)
 */
public class FrequencyMap<K extends Comparable<K>, V> implements Map<K, V> {

	private Multimap<K, FrequencyValue<V>> store = TreeMultimap.create(); 
	
	@Override
	public int size() {
		return store.size();
	}

	@Override
	public boolean isEmpty() {
		return store.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return store.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public V get(Object key) {
		@SuppressWarnings("unchecked")
		FrequencyValue<V> fValue = Iterables.getFirst(store.get((K) key), null);
		if (fValue == null) {
			return getMostFrequentValue();
		}
		return fValue.value;
	}

	private V getMostFrequentValue() {
		Map<V, Integer> counts = new HashMap<V, Integer>();
		for (Entry<K, FrequencyValue<V>> entry : store.entries()) {
			FrequencyValue<V> fValue = entry.getValue();
			int count = Objects.firstNonNull(counts.get(fValue.value), 0);
			count += fValue.frequency;
			counts.put(fValue.value, count);
		}
		int maxCount = 0;
		V winValue = null;
		for (Entry<V, Integer> entry : counts.entrySet()) {
			if (entry.getValue() > maxCount) {
				maxCount = entry.getValue();
				winValue = entry.getKey();
			}
		}
		return winValue;
	}

	@Override
	public V put(K key, V value) {
		return put(key, value, 1);
	}
	
	public V put(K key, V value, int count) {
		FrequencyValue<V> fValue = Iterables.getFirst(store.get((K) key), null);
		if (fValue == null) {
			fValue = new FrequencyValue<V>(value, count);
			store.put(key, fValue);
		} else {
			fValue.frequency += count;
			update(store.get(key), fValue);
		}
		return value;
	}

	private <T> void update(Collection<T> collection, T value) {
		if (collection.remove(value)) {
			collection.add(value);
		}
	}

	@Override
	public V remove(Object key) {
		return Iterables.getFirst(store.removeAll(key), getEmptyFrequencyValue()).value;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		store.clear();
	}

	@Override
	public Set<K> keySet() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<V> values() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		throw new UnsupportedOperationException();
	}

	private static class FrequencyValue<V> implements Comparable<FrequencyValue<V>> {
		private V value;
		private int frequency;
		public FrequencyValue() {
		}
		public FrequencyValue(V value, int frequency) {
			this.value = value;
			this.frequency = frequency;
		}
		@Override
		public int compareTo(FrequencyValue<V> o) {
			return o.frequency - frequency;
		}
	}

	@SuppressWarnings({ "rawtypes" })
	private static final FrequencyValue EMPTY_FREQUENCY_VALUE = new FrequencyValue();

	@SuppressWarnings("unchecked")
	private FrequencyValue<V> getEmptyFrequencyValue() {
		return EMPTY_FREQUENCY_VALUE;
	}

}
