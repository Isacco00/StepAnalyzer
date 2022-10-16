package forexsentiment.utility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectionUtils {
	public static final String SEPARATOR = "_#_";

	public static <T> boolean isNullOrEmpty(Collection<T> elements) {
		return elements == null || elements.isEmpty();
	}

	public static <T> boolean notNullNotEmpty(Collection<T> elements) {
		return !isNullOrEmpty(elements);
	}

	public static <T> T getSingleElement(Collection<T> elements) {
		if (isNullOrEmpty(elements)) {
			throw new RuntimeException("Cannot retrieve single element from empty list");
		}
		if (elements.size() != 1) {
			throw new RuntimeException("Was expecting exactly one element, but found [" + elements.size() + "]");
		}
		return elements.iterator().next();
	}

	public static <T> T getSingleElementOrNull(Collection<T> elements) {
		Collection<T> safeCollection = nullToEmpty(elements);
		return safeCollection.size() == 1 ? safeCollection.iterator().next() : null;
	}

	public static <T> List<T> nullToEmpty(List<T> elements) {
		return elements != null ? elements : new ArrayList<T>();
	}

	public static <T> Collection<T> nullToEmpty(Collection<T> elements) {
		return elements != null ? elements : new ArrayList<T>();
	}

	public static <T> Set<T> nullToEmpty(Set<T> elements) {
		return elements != null ? elements : new HashSet<T>();
	}

	public static <T> boolean containsDuplicates(List<T> elements) {
		List<T> list = nullToEmpty(elements);
		Set<T> set = list.stream().collect(Collectors.toSet());
		return list.size() > set.size();
	}

	public static <T> Stream<T> toSafeStream(List<T> elements) {
		return nullToEmpty(elements).stream();
	}

	public static <T> Stream<T> toSafeStream(Set<T> elements) {
		return nullToEmpty(elements).stream();
	}

	public static <K, T> Map<K, T> forceOneElementPerKey(Map<K, List<T>> map) {
		Map<K, T> result = new HashMap<K, T>();
		map.entrySet().stream().forEach(x -> result.put(x.getKey(), getSingleElement(x.getValue())));
		return result;
	}
}
