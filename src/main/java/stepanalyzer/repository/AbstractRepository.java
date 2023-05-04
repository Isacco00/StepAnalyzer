package stepanalyzer.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import jakarta.persistence.TypedQuery;

public interface AbstractRepository {

	public static final String format = "dd/MM/uuuu"; // set the format to whatever you need

	public static final String HYPHENED_FORMAT = "uuuu-MM-dd";

	// Long ID should be used for almost every Entity
	<T> T find(Class<T> clazz, Long id);

	// Integer ID should only be used for edoc
	<T> T find(Class<T> clazz, Integer id);

	<T> T getResultSingle(TypedQuery<T> query);

	<T> List<T> getResultList(TypedQuery<T> query);

	void delete(Object o);

	void save(Object o);

	// This contains both Hypened and Slashed parser
	LocalDate fromString(String value);

	// reverse of fromString method
	String toBeDate(LocalDate date);

}
