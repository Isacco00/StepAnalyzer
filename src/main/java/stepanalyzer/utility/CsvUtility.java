package stepanalyzer.utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import org.springframework.stereotype.Component;

@Component
public class CsvUtility {
	
	@Inject
	private DateUtility dateUtility;
	
	private static final CSVFormat FORMAT = CSVFormat.DEFAULT;

	// write data to csv
	public ByteArrayInputStream writeDataToCsv(final List<Object> forexSentimentList) {
		try (ByteArrayOutputStream stream = new ByteArrayOutputStream();
				CSVPrinter printer = new CSVPrinter(new PrintWriter(stream), FORMAT)) {
			for (Object entity : forexSentimentList) {
//				List<String> data = Arrays.asList(entity.getTokenForexSentiment().toString(),
//						entity.getShortPosition().toString(), entity.getLongPosition().toString(),
//						dateUtility.formatOffsetDateTime(entity.getUpdateTimestamp()));
//				printer.printRecord(data);
			}
			printer.flush();
			stream.close();
			return new ByteArrayInputStream(stream.toByteArray());
		} catch (final IOException e) {
			throw new RuntimeException("Csv writing error: " + e.getMessage());
		}
	}
	public static String changeExtension(String fileName, String newExtension) {
		int i = fileName.lastIndexOf('.');
		String name = fileName.substring(0, i);
		return name + newExtension;
	}
}
