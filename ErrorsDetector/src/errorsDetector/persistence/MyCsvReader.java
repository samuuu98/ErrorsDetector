 package errorsDetector.persistence;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.io.Reader;
import java.util.List;


public class MyCsvReader {

	public List<String[]> read(Reader reader) throws IOException{
		try {
			CSVParser parser = new CSVParserBuilder()
				    .withSeparator(',')
				    .withIgnoreQuotations(true)
				    .build();

				CSVReader csvReader = new CSVReaderBuilder(reader)
				    .withSkipLines(0)
				    .withCSVParser(parser)
				    .build();
			//CSVReader csvReader = new CSVReader(reader);
			List<String[]> csv = csvReader.readAll();
			csvReader.close();
			return csv;
			
		} catch (CsvException e) {
			e.printStackTrace();
		}
		return null;
	}

}
