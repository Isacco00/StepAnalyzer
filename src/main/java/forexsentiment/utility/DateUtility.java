package forexsentiment.utility;

import java.text.DateFormat;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.Locale;

import org.springframework.stereotype.Component;

@Component
public class DateUtility {

	public static final DateFormatEnum DEFAULT_DATE_FORMAT = DateFormatEnum.YYYYMMDD_HYPHEN;

	public static final String DEFAULT_DATE_DELIMITER = "-";

	public String formatDate(Date date) {
		return formatDate(date, DEFAULT_DATE_FORMAT.getValue());
	}

	public String formatDate(Date date, String pattern) {
		// TODO: validator.checkNotNull(date);
		DateFormat dateFormat = new SimpleDateFormat(pattern);

		return dateFormat.format(date);
	}

	public LocalDate convertToLocalDate(Date dateToConvert) {
		return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public LocalDate parseDate(String unparsedDate) {
		return parseDate(unparsedDate, DEFAULT_DATE_FORMAT);
	}

	public String formatDate(LocalDate date) {
		final String delimiter = DEFAULT_DATE_DELIMITER;

		String formattedDay = addZeroPrefixIfNeeded(date.getDayOfMonth());
		String formattedMonth = addZeroPrefixIfNeeded(date.getMonthValue());
		String formattedYear = date.getYear() + "";

		StringBuilder result = new StringBuilder();
		result.append(formattedDay);
		result.append(delimiter);
		result.append(formattedMonth);
		result.append(delimiter);
		result.append(formattedYear);

		return result.toString();
	}

	public String formatDate(LocalDate date, DateFormatEnum pattern) {
		String resultDate = date.format(DateTimeFormatter.ofPattern(pattern.getValue()));
		return resultDate;
	}

	private String addZeroPrefixIfNeeded(int value) {
		return (value < 10 ? "0" : "") + value;
	}

	public LocalDate parseDate(String unparsedDate, String pattern) {
		try {
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
			TemporalAccessor temporalAccessor = dateFormatter.parse(unparsedDate);

			int day = temporalAccessor.isSupported(ChronoField.DAY_OF_MONTH)
					? temporalAccessor.get(ChronoField.DAY_OF_MONTH)
					: 1;

			int month = retrieveMonthValue(temporalAccessor);

			int year = temporalAccessor.get(ChronoField.YEAR);

			return LocalDate.of(year, month, day);
		} catch (Exception e) {
			throw new RuntimeException("Cannot parse date [" + unparsedDate + "] with pattern [" + pattern + "]", e);
		}
	}

	public LocalDate parseDate(String unparsedDate, DateFormatEnum pattern) {
		return parseDate(unparsedDate, pattern.getValue());
	}

	private int retrieveMonthValue(TemporalAccessor temporalAccessor) {

		if (temporalAccessor.isSupported(ChronoField.MONTH_OF_YEAR)) {
			return temporalAccessor.get(ChronoField.MONTH_OF_YEAR);
		} else if (temporalAccessor.isSupported(IsoFields.QUARTER_OF_YEAR)) {
			int quarter = temporalAccessor.get(IsoFields.QUARTER_OF_YEAR);
			return 1 + 3 * (quarter - 1);
		} else {
			throw new RuntimeException("Cannot parse month value!");
		}

	}

	public boolean isDate(String string) {
		return isDateInGivenFormat(string, DEFAULT_DATE_FORMAT);
	}

	public boolean isDateInGivenFormat(String string, DateFormatEnum pattern) {
		try {
			parseDate(string, pattern);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isDateInGivenFormat(String date, String pattern) {
		try {
			parseDate(date, pattern);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public LocalDate convertToLocalDate(OffsetDateTime offsetDateTime) {
		if (offsetDateTime == null) {
			return null;
		}
		return offsetDateTime.withOffsetSameInstant(ZoneOffset.UTC).toLocalDate();
	}

	public LocalDate plusOneYearLeapSafe(LocalDate date) {
		LocalDate newDate = date.plusDays(1).plusYears(1).minusDays(1);
		return newDate;
	}

	public String convertDateFormat(String date, DateFormatEnum from, DateFormatEnum to) {
		try {
			TemporalAccessor temporalAccessor = DateTimeFormatter.ofPattern(from.getValue(), Locale.ENGLISH)
					.parse(date);
			return DateTimeFormatter.ofPattern(to.getValue(), Locale.ENGLISH).format(temporalAccessor);
		} catch (Exception e) {
			return "";
		}
	}

	public OffsetDateTime getDateInitialMonth(OffsetDateTime currentDate) {
		return currentDate.with(TemporalAdjusters.firstDayOfMonth());
	}

	public OffsetDateTime getDateEndMonth(OffsetDateTime currentDate) {
		return currentDate.with(TemporalAdjusters.lastDayOfMonth());
	}

	public LocalDate getDateInitialMonthLocalDate(LocalDate currentDate) {
		return currentDate.with(TemporalAdjusters.firstDayOfMonth());
	}

	public LocalDate getDateEndMonthLocalDate(LocalDate currentDate) {
		return currentDate.with(TemporalAdjusters.lastDayOfMonth());
	}

	public boolean checkDateAfterOrEqual(LocalDate shouldBeBefore, LocalDate shouldBeAfter) {
		if (shouldBeAfter == null || shouldBeBefore == null) {
			return false;
		}
		if (shouldBeAfter.isAfter(shouldBeBefore) || shouldBeAfter.isEqual(shouldBeBefore)) {
			return true;
		}
		return false;
	}

	public boolean checkDateBeforeOrEqual(LocalDate shouldBeBefore, LocalDate shouldBeAfter) {
		if (shouldBeAfter == null || shouldBeBefore == null) {
			return false;
		}
		if (shouldBeBefore.isBefore(shouldBeAfter) || shouldBeBefore.isEqual(shouldBeAfter)) {
			return true;
		}
		return false;
	}

	public String formatOffsetDateTime(OffsetDateTime dateTime) {
		return DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss").format(dateTime);
	}
}
