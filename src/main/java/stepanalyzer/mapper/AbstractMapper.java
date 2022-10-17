package stepanalyzer.mapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractMapper<K, V> {

	public List<V> mapEntitiesToBeans(Collection<K> entities) {
		return entities.stream().map(this::mapEntityToBean).collect(Collectors.toList());
	}

	public V mapEntityToBean(K entity) {
		failOnNull(entity);
		return doMapping(entity);
	}

	protected abstract V doMapping(K entity);

	public V mapEntityToBean(V bean, K entity) {
		failOnNull(entity);
		return doMapping(bean, entity);
	}

	protected V doMapping(V bean, K entity) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	protected <E extends Enum<E>> E getOrNull(Class<E> enumClazz, String val) {
		return val != null ? Enum.valueOf(enumClazz, val) : null;
	}

	private void failOnNull(K entity) {
		if (entity == null) {
			throw new IllegalArgumentException("entity is null");
		}
	}

}
