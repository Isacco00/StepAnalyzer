package stepanalyzer.merger;

import jakarta.inject.Inject;
import stepanalyzer.repository.GenericRepository;

import java.lang.reflect.InvocationTargetException;

public abstract class AbstractMerger<K, V> {
	@Inject
	protected GenericRepository repo;
	public V mapNew(K bean, Class<V> clazz) {
		try {
			V v = clazz.getDeclaredConstructor().newInstance();
			doMerge(bean, v);
			return v;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException | InstantiationException e) {
			throw new RuntimeException(e);
		}
	}

	public void merge(K bean, V entity) {
		failOnNull(bean, "bean is null");
		failOnNull(entity, "entity is null");
		doMerge(bean, entity);
	}

	protected abstract void doMerge(K bean, V entity);

	private <T> void failOnNull(T object, String message) {
		if (object == null) {
			throw new IllegalArgumentException(message);
		}
	}
}
