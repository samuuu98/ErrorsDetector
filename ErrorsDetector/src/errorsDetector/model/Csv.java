package errorsDetector.model;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Csv {
	private List<String[]> csvList;
	private List<CsvColumn> columnsList;
	private List<CSVRow> csvRows;
	private String colum1Name = null;
	private String colum2Name = null;
	private Duration sampling = Duration.ofHours(1);

	public Csv(List<String[]> csv) {
		this.csvList = csv;
		this.csvRows = setCsvRows();
	}
	
	public Csv(ArrayList<CSVRow> csvRows) {
		this.csvRows = csvRows;
		this.csvList = setCsvList();
	}
	
	public Csv() {
		this.csvRows = new ArrayList<>();
		this.csvList = new ArrayList<>();
	}
	
	private List<String[]> setCsvList(){
		List<String[]> csvList = new ArrayList<>();
		for(CSVRow row : csvRows)
			csvList.add(row.getRowValueProperty());
		
		return csvList;
	}
	
	public Duration getSampling(){
		return sampling;
	}
	
	public List<CSVRow> getCsvRows(){
		return csvRows;
	}
	
	public void addCsvRow(CSVRow row){
		csvRows.add(row);
		sortRowList();
		setCsvList();
	}

	private List<CSVRow> setCsvRows() {
		csvRows = new ArrayList<>();
		String[] header = csvList.iterator().next();
		try {
			Timestamp.valueOf(header[0].replace("T", " ").replace("Z",""));
		}catch(IllegalArgumentException e) {
			colum1Name = header[0];
			colum2Name = header[1];
			csvList.remove(header);
		}
		csvList.forEach(row -> csvRows.add(new CSVRow(row)));
		
		return csvRows;
	}

	public List<String[]> getCsvList() {
		return csvList;
	}
	
	public List<CsvColumn> getColumnsList(){
		CsvColumn[] columnArray = null;
		
		csvList.forEach(row -> 
			Arrays.asList(row).forEach(element ->
				columnArray[Arrays.asList(row).indexOf(element)].add(element)));
		
		return Arrays.asList(columnArray);
	}
	
	public void sortRowList() {
		Collections.sort((List<CSVRow>) this.getCsvRows(),  new Comparator<CSVRow>() {
	        @Override
	        public int compare(CSVRow r1, CSVRow r2) {
	            return r1.getDate().compareTo(r2.getDate());
	        }
		});
	}

}
