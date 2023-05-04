package stepanalyzer.repository.impl;

import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Id;
import jakarta.persistence.TypedQuery;
import org.hibernate.HibernateException;
import stepanalyzer.bean.AbstractSearchRequestBean;
import stepanalyzer.repository.AbstractRepository;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

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
    public <T> T find(Class<T> clazz, Integer id) {
        return findById(clazz, id);
    }

    // Long IDs

    @Override
    public <T> T find(Class<T> clazz, Long id) {
        return findById(clazz, id);
    }

    private <T, I> T findById(Class<T> clazz, I id) {
        return entityManager.find(clazz, id);
    }

    public <T> T getResultSingle(TypedQuery<T> query) {
        List<T> result = query.getResultList();
        if (result != null && result.size() == 1) {
            return result.get(0);
        } else if (result != null && result.isEmpty()) {
            return null;
        } else {
            throw new HibernateException("single result query returned more than one record");
        }
    }

    public <T> List<T> getResultList(TypedQuery<T> query) {
        return query.getResultList();
    }

    @Override
    public void delete(Object o) {
        entityManager.remove(o);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void save(Object o) {
        entityManager.persist(o);
    }

    protected <E> void createListWhereClause(String tableName, String fieldName, List<E> values,
                                             StringBuilder strQueryWhere, Map<String, Object> parameters) {
        createListWhereClauseGeneric(tableName, fieldName, values, strQueryWhere, parameters, false);
    }

    protected <E> void createListNotWhereClause(String tableName, String fieldName, List<E> values,
                                                StringBuilder strQueryWhere, Map<String, Object> parameters) {
        createListWhereClauseGeneric(tableName, fieldName, values, strQueryWhere, parameters, true);
    }

    private <E> void createListWhereClauseGeneric(String tableName, String fieldName, List<E> values,
                                                  StringBuilder strQueryWhere, Map<String, Object> parameters, boolean negate) {
        strQueryWhere.append(" AND ").append(tableName).append(".").append(fieldName).append(negate ? " NOT" : "").append(" IN ( ");

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

}
