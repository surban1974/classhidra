package it.classhidra.core.tool.util.v2;

import java.util.Collection;
import java.util.Map;

public class Util_generic_normalizer {
	@SuppressWarnings("unchecked")
	public static <K,V extends Object> Map<K, V> normalize(Map<?,?> source, Map<K, V> destination){
		if(source==null || destination==null)
			return null;
		destination.clear();
		destination.putAll(
				(Map<? extends K,? extends V>)source
			);
		return destination;
	}
	
	@SuppressWarnings("unchecked")
	public static <K extends Object> Collection<K> normalize(Collection<?> source, Collection<K> destination){
		if(source==null || destination==null)
			return null;
		destination.clear();
		destination.addAll(
				(Collection<? extends K>)source
			);
		return destination;
	}
}
