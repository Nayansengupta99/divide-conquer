package com.example.demo.util;

import java.util.Map;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class Util {

	public <K, V extends Comparable<V>> Map<K, V> sortByValues(final Map<K, V> map) {
		Comparator<K> valueComparator = new Comparator<K>() {
			public int compare(K k1, K k2) {
				int compare = map.get(k1).compareTo(map.get(k2));
				if (compare == 0)
					return 1;
				else
					return compare;
			}
		};

		Map<K, V> sortedByValues = new TreeMap<K, V>(valueComparator);
		sortedByValues.putAll(map);
		return sortedByValues;
	}

	public double returnUptoTwoDecimalPlaces(Double num) {
		BigDecimal bd = new BigDecimal(num).setScale(2, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

}
