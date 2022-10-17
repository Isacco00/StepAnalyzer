package stepanalyzer.repository.impl;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Id;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.jpa.HibernateEntityManager;

import stepanalyzer.bean.AbstractSearchRequestBean;
import stepanalyzer.repository.AbstractRepository;

@SuppressWarnings("deprecation")
public abstract class AbstractRepositoryImpl implements AbstractRepository {

	@Inject
	protected EntityManager entityManager;

	public void setEntityManager(EntityManager arg) {
		this.entityManager = arg;
	}

	private static final String QRY_GENERIC = "FROM %s e WHERE e.%s = :id";

	private <T> String extractIdField(Class<T> clazz) {
		for (Field fld : clazz.getDeclaredFields()) {
			if (fld.isAnnotationPresent(Id.class)) {
				return fld.getName();
			}
		}
		throw new HibernateException("No @Id field defined for entity " + clazz.getName());
	}

	// Integer IDs

	@Override
	public <T> T find(Class<T> clazz, Integer id, String... filters) {
		return findById(clazz, id, filters);
	}

	@Override
	public <T> T find(Class<T> clazz, Integer id) {
		return findById(clazz, id);
	}

	@Override
	public <T> Boolean exists(Class<T> clazz, Integer id) {
		return existsById(clazz, id);
	}

	// Long IDs

	@Override
	public <T> T find(Class<T> clazz, Long id, String... filters) {
		return findById(clazz, id, filters);
	}

	@Override
	public <T> T find(Class<T> clazz, Long id) {
		return findById(clazz, id);
	}

	@Override
	public <T> T retrieve(Class<T> clazz, Long id) {
		T entity = find(clazz, id);

		if (entity == null) {
			throw new EntityNotFoundException(
					"Entity not found for class=[" + clazz.toString() + "] and token=[" + id + "]");
		}

		return entity;
	}

	@Override
	public <T> Boolean exists(Class<T> clazz, Long id) {
		return existsById(clazz, id);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private <T, I> T findById(Class<T> clazz, I id, String... filters) {
		Session session = entityManager.unwrap(Session.class);
		if (filters != null) {
			for (String filter : filters) {
				session.enableFilter(filter);
			}
		}
		String idField = extractIdField(clazz);
		Query qry = session.createQuery(String.format(QRY_GENERIC, clazz.getSimpleName(), idField));
		qry.setParameter("id", id);

		T result = (T) qry.uniqueResult();
		// entityManager.detach(result);
		return result;
	}

	private <T, I> T findById(Class<T> clazz, I id) {
		T result = entityManager.find(clazz, id);
		// entityManager.detach(result);
		return result;
	}

	private <T, I> Boolean existsById(Class<T> clazz, I id) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<T> rootEntity = cq.from(clazz);

		String idName = extractIdField(clazz);

		cq.select(cb.count(rootEntity));
		cq.where(cb.equal(rootEntity.get(idName), id));
		return entityManager.createQuery(cq).getSingleResult() > 0;
	}

	@Override
	public <T> Boolean exists(Class<T> clazz, Map<String, ?> paramList) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<T> rootEntity = cq.from(clazz);
		cq.select(cb.count(rootEntity));

		if (paramList != null) {
			paramList.forEach((k, v) -> {
				cq.where(cb.equal(rootEntity.<String>get(k), v));
			});
		}

		return entityManager.createQuery(cq).getSingleResult() > 0;
	}

	@Override
	public <T> List<T> findBy(Class<T> clazz, Map<String, ?> params, String... filters) {
		return findBy(clazz, params, false, filters);
	}

	@Override
	public <T> List<T> findBy(Class<T> clazz, Map<String, ?> params, boolean distinct, String... filters) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(clazz);
		Root<T> rootEntity = cq.from(clazz);
		cq.select(rootEntity);
		cq.distinct(distinct);

		if (params != null) {
			params.forEach((k, v) -> {
				if (v instanceof String) {
					if (v != null) {
						cq.where(cb.like(rootEntity.<String>get(k), (String) v));
					}
				} else if (v instanceof Integer) {
					if (v != null) {
						cq.where(cb.equal(rootEntity.<String>get(k), (Integer) v));
					}
				} else {
					if (v != null) {
						cq.where(cb.equal(rootEntity.<String>get(k), (Object) v));
					}
				}
			});
		}

		if (filters != null) {
			Session session = entityManager.unwrap(Session.class);
			for (String filter : filters) {
				session.enableFilter(filter);
			}
		}

		TypedQuery<T> query = entityManager.createQuery(cq);
		return getResultList(query);

	}

	public <T> T getResultSingle(TypedQuery<T> query) {
		List<T> result = query.getResultList();
		if (result != null && result.size() == 1) {
			T singleResult = result.get(0);
			// entityManager.detach(singleResult);
			return singleResult;
		} else if (result != null && result.isEmpty()) {
			return null;
		} else {
			throw new HibernateException("single result query returned more than one record");
		}
	}

	public <T> List<T> getResultList(TypedQuery<T> query) {
		List<T> result = query.getResultList();
		// result.forEach(x -> entityManager.detach(x));
		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> List<T> getResultList(Query query) {
		List<T> result = query.list();
		// result.forEach(x -> entityManager.detach(x));
		return result;
	}

	public <T> List<T> getResultList(javax.persistence.Query query, Class<T> clazz) {
		return castList(query.getResultList(), clazz);
	}

	private static <T> List<T> castList(Collection<?> c, Class<? extends T> clazz) {
		List<T> r = new ArrayList<T>(c.size());
		for (Object o : c) {
			r.add(clazz.cast(o));
		}
		return r;
	}

	@Override
	public void delete(Object o) {
		entityManager.remove(o);
	}

	@Override
	public <T> void deleteById(long tokenEntity, Class<T> clazz) {
		String idField = extractIdField(clazz);
		javax.persistence.Query q = entityManager
				.createQuery("DELETE " + clazz.getSimpleName() + " WHERE " + idField + " = " + tokenEntity);
		q.executeUpdate();
	}

	@Override
	public <T> void deleteByIds(List<Long> tokens, Class<T> clazz) {
		if (tokens != null && !tokens.isEmpty()) {
			String idField = extractIdField(clazz);
			javax.persistence.Query q = entityManager
					.createQuery("DELETE " + clazz.getSimpleName() + " WHERE " + idField + " IN "
							+ tokens.stream().map(n -> String.valueOf(n)).collect(Collectors.joining(",", "(", ")")));
			q.executeUpdate();
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void save(Object o) {
		entityManager.persist(o);
	}

	@Override
	public void saveOrUpdate(Object o) {
		entityManager.merge(o);
	}

	@Override
	public void refresh(Object o) {
		entityManager.refresh(o);
	}

	@Override
	public void flush() {
		entityManager.flush();
	}

	protected <E extends Object> void createListWhereClause(String tableName, String fieldName, List<E> values,
			StringBuilder strQueryWhere, Map<String, Object> parameters) {
		createListWhereClauseGeneric(tableName, fieldName, values, strQueryWhere, parameters, false);
	}

	protected <E extends Object> void createListNotWhereClause(String tableName, String fieldName, List<E> values,
			StringBuilder strQueryWhere, Map<String, Object> parameters) {
		createListWhereClauseGeneric(tableName, fieldName, values, strQueryWhere, parameters, true);
	}

	private <E extends Object> void createListWhereClauseGeneric(String tableName, String fieldName, List<E> values,
			StringBuilder strQueryWhere, Map<String, Object> parameters, boolean negate) {
		strQueryWhere.append(" AND " + tableName + "." + fieldName + (negate ? " NOT" : "") + " IN ( ");

		String whereClause = values.stream().map(x -> {
			parameters.put("par" + x.toString().replace("-", "_"), x);
			return " :" + "par" + x.toString().replace("-", "_");
		}).collect(Collectors.joining(", "));

		strQueryWhere.append(whereClause);
		strQueryWhere.append(") ");
	}

	// TODO: manage LocalDate QueryParam

	@Override
	public LocalDate fromString(String value) {
		LocalDate date = parseShortDateToLocalDateHyphened(value);
		if (date == null) {
			date = parseShortDateToLocalDateSlashed(value);
		}
		return date;
	}

	@Override
	public String toBeDate(LocalDate date) {
		if (date == null) {
			return null;
		}
		return date.format(DateTimeFormatter.ofPattern(format, Locale.ENGLISH));
	}

	public OffsetDateTime parseShortDateToOffsetDateTime(String dateString) {
		LocalDate localdate = this.fromString(dateString);
		if (localdate != null) {
			return OffsetDateTime.of(localdate, java.time.LocalTime.MIDNIGHT, java.time.ZoneOffset.UTC);
		} else {
			return null;
		}
	}

	private LocalDate parseShortDateToLocalDateSlashed(String dateString) {
		return parseShortDateToLocalDateByFormat(dateString, format);
	}

	private LocalDate parseShortDateToLocalDateHyphened(String dateString) {
		return parseShortDateToLocalDateByFormat(dateString, HYPHENED_FORMAT);
	}

	private LocalDate parseShortDateToLocalDateByFormat(String dateString, String dateFormat) {
		return this.parseDate(dateString, DateTimeFormatter.ofPattern(dateFormat, Locale.ENGLISH));
	}

	private LocalDate parseDate(String dateString, DateTimeFormatter dateFormat) {
		try {
			if (dateString != null) {
				return LocalDate.parse(dateString, dateFormat.withResolverStyle(ResolverStyle.STRICT));
			} else {
				return null;
			}

		} catch (DateTimeParseException e) { // NOSONAR
			return null;
		}
	}

	public long getSequence(String sequenceId) {
		return this.entityManager.unwrap(HibernateEntityManager.class).getSession()
				.doReturningWork(new ReturningWork<Long>() {
					@Override
					public Long execute(Connection connection) throws SQLException {
						ResultSet result = null;
						try (PreparedStatement statement = connection.prepareStatement("SELECT nextval(?) as Id")) {
							statement.setString(1, sequenceId);

							result = statement.executeQuery();

							if (!result.next()) {
								throw new RuntimeException("nextval('" + sequenceId + "' failed");
							}

							long sequence = result.getLong(1);

							statement.close();

							return sequence;
						} finally {
							if (result != null) {
								result.close();
							}
						}
					}
				});
	}

	public Map<String, Boolean> orderParsing(String orderByUserCriteria) {
		Map<String, Boolean> orderByUserCriteriaMap = new HashMap<>();

		List<String> orderCriteriaList = Arrays.asList(orderByUserCriteria.trim().split(","));
		orderCriteriaList.stream().forEach(x -> {
			List<String> orderCriteriaWithDirection = Arrays.asList(x.trim().split(" "));
			if (!orderCriteriaWithDirection.isEmpty()) {
				if (orderCriteriaWithDirection.size() == 1) { // DEFAULT ASC sorting
					orderByUserCriteriaMap.put(orderCriteriaWithDirection.get(0), false);
				} else if (orderCriteriaWithDirection.size() == 2) { // CUSTOM sorting
					boolean isDesc = false;
					if (orderCriteriaWithDirection.get(1).equalsIgnoreCase("DESC")) {
						isDesc = true;
					}
					orderByUserCriteriaMap.put(orderCriteriaWithDirection.get(0), isDesc);
				}
			}
		});
		return orderByUserCriteriaMap;
	}

	public <T> TypedQuery<T> setFinalParameters(boolean retrieveTokens, StringBuilder strQueryFrom,
			StringBuilder strQueryWhere, Map<String, Object> parameters, Class<T> outputClazz,
			AbstractSearchRequestBean request) {

		String strQueryFinal = (strQueryFrom.append(strQueryWhere.toString())).toString();
		TypedQuery<T> query = entityManager.createQuery(strQueryFinal, outputClazz);
		parameters.forEach(query::setParameter);

		if (retrieveTokens && request != null && request.getFirstResult() != null) {
			query.setFirstResult(request.getFirstResult());
		}
		if (retrieveTokens && request != null && request.getMaxResult() != null) {
			query.setMaxResults(request.getMaxResult());
		}

		return query;
	}

	public <T> StringBuilder getFromQuery(boolean isCount, boolean retrieveTokens, String entityAlias, String idName,
			Class<T> clazz) {
		String selectQuery = "";
		if (isCount) {
			selectQuery = " SELECT COUNT(DISTINCT " + entityAlias + "." + idName + ")";
		} else if (!retrieveTokens) {
			selectQuery = " SELECT DISTINCT " + entityAlias;
		} else {
			selectQuery = " SELECT DISTINCT " + entityAlias + "." + idName;
		}
		selectQuery += " FROM " + clazz.getSimpleName() + " " + entityAlias + " ";
		return new StringBuilder(selectQuery);
	}
}
