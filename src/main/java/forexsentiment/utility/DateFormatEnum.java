package forexsentiment.utility;

import java.util.Arrays;
import java.util.List;

public enum DateFormatEnum {

  QUARTER_LOWERCASE_N_YYYY_SLASH("'q'Q/yyyy"),
  QUARTER_LOWERCASE_N_YYYY_HYPHEN("'q'Q-yyyy"),
  QUARTER_LOWERCASE_N_YYYY_SPACE("'q'Q yyyy"),

  QUARTER_UPPERCASE_N_YYYY_SLASH("'Q'Q/yyyy"),
  QUARTER_UPPERCASE_N_YYYY_HYPHEN("'Q'Q-yyyy"),
  QUARTER_UPPERCASE_N_YYYY_SPACE("'Q'Q yyyy"),

  DDMMYYYY_SLASH("dd/MM/yyyy"),
  DDMMYYYY_HYPHEN("dd-MM-yyyy"),
  DDMMYYYY_SPACE("dd MM yyyy"),

  MMDDYYYY_SLASH("MM/dd/yyyy"),
  MMDDYYYY_HYPHEN("MM-dd-yyyy"),
  MMDDYYYY_SPACE("MM dd yyyy"),
  MMDDYYYY("MMddyyyy"),

  MMYYYY_SLASH("MM/yyyy"),
  MMYYYY_HYPHEN("MM-yyyy"),
  MMYYYY_SPACE("MM yyyy"),

  YYYYMMDD_HYPHEN("yyyy-MM-dd");

  private String value;

  DateFormatEnum(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static List<DateFormatEnum> getAllowedExcelFormat() {
    return Arrays.asList(DDMMYYYY_SLASH, DDMMYYYY_HYPHEN, DDMMYYYY_SPACE, MMDDYYYY_SLASH, MMDDYYYY_HYPHEN, MMDDYYYY_SPACE, MMYYYY_SLASH, MMYYYY_HYPHEN,
        MMYYYY_SPACE);
  }

  public static List<DateFormatEnum> getNOTAllowedExcelFormat() {
    return Arrays.asList(QUARTER_LOWERCASE_N_YYYY_SLASH, QUARTER_LOWERCASE_N_YYYY_HYPHEN, QUARTER_LOWERCASE_N_YYYY_SPACE, QUARTER_UPPERCASE_N_YYYY_SLASH,
        QUARTER_UPPERCASE_N_YYYY_HYPHEN, QUARTER_UPPERCASE_N_YYYY_SPACE);
  }

  public static String getDatePatternDescription(DateFormatEnum pattern) {

    switch (pattern) {

      case QUARTER_LOWERCASE_N_YYYY_SLASH:
        return "qN/yyyy";

      case QUARTER_LOWERCASE_N_YYYY_HYPHEN:
        return "qN-yyyy";

      case QUARTER_LOWERCASE_N_YYYY_SPACE:
        return "qN yyyy";

      case QUARTER_UPPERCASE_N_YYYY_SLASH:
        return "QN/yyyy";

      case QUARTER_UPPERCASE_N_YYYY_HYPHEN:
        return "QN-yyyy";

      case QUARTER_UPPERCASE_N_YYYY_SPACE:
        return "QN yyyy";

      default:
        return pattern.getValue();
    }
  }
}
