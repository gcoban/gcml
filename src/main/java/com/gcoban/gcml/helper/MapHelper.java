package com.gcoban.gcml.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class MapHelper {

	public static <K, V extends Comparable<? super V>> List<Map.Entry<K, V>> sortMapByValuesDecreasing(final Map<K, V> mapToSort) {

		List<Map.Entry<K, V>> entries = new ArrayList<>(mapToSort.size());
		entries.addAll(mapToSort.entrySet());

		Collections.sort(entries, new Comparator<Map.Entry<K, V>>() {

			@Override
			public int compare(final Map.Entry<K, V> entry1, final Map.Entry<K, V> entry2) {

				return entry2.getValue().compareTo(entry1.getValue());
			}
		});

		return entries;
	}

	public static <K, V extends Comparable<? super V>> List<Map.Entry<K, V>> sortMapByValuesIncreasing(final Map<K, V> mapToSort) {

		List<Map.Entry<K, V>> entries = new ArrayList<>(mapToSort.size());
		entries.addAll(mapToSort.entrySet());

		Collections.sort(entries, new Comparator<Map.Entry<K, V>>() {

			@Override
			public int compare(final Map.Entry<K, V> entry1, final Map.Entry<K, V> entry2) {

				return entry1.getValue().compareTo(entry2.getValue());
			}
		});

		return entries;
	}
}