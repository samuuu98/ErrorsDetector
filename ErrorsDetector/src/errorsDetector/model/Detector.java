package errorsDetector.model;

import errorsDetector.utils.Helper;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.sql.Date;
import java.sql.Timestamp;

public class Detector {
	private Csv csvDistinct;
	private Csv csvMultiples;
	private Csv csvHoles;
	
	public Detector() {
	}
	
	public Csv getMissedPerDay(Csv csv) {
		List<CSVRow> csvRowList = getCsvHoles(csv).getCsvRows();
		ArrayList<CSVRow> csvMissedPerDayRowList = new ArrayList<>();
		LocalDateTime firstDay = csvRowList.get(0).getDate().toLocalDateTime();
		firstDay = LocalDateTime.of(firstDay.getYear(), firstDay.getMonth(), firstDay.getDayOfMonth(), 0, 0);
		LocalDateTime lastDay = csvRowList.get(csvRowList.size() - 1).getDate().toLocalDateTime();
		int count;
		for(LocalDateTime time = firstDay; time.getDayOfYear() <= lastDay.getDayOfYear(); time = time.plusDays(1)) {
			count = 0;
			for(var row : csvRowList) {
				if(row.getDate().toLocalDateTime().getDayOfYear() == time.getDayOfYear())
					count++;
			}
			csvMissedPerDayRowList.add(new CSVRow(Timestamp.valueOf(time), count));
		}
		return new Csv(csvMissedPerDayRowList);
	}
	
	public Csv getMultiplesPerDay(Csv csv) {
		List<CSVRow> csvRowList = getCsvMultiples(csv).getCsvRows();
		ArrayList<CSVRow> csvMissedPerDayRowList = new ArrayList<>();
		LocalDateTime firstDay = csvRowList.get(0).getDate().toLocalDateTime();
		firstDay = LocalDateTime.of(firstDay.getYear(), firstDay.getMonth(), firstDay.getDayOfMonth(), 0, 0);
		LocalDateTime lastDay = csvRowList.get(csvRowList.size() - 1).getDate().toLocalDateTime();
		int count;
		for(LocalDateTime time = firstDay; time.getDayOfYear() <= lastDay.getDayOfYear(); time = time.plusDays(1)) {
			count = 0;
			for(var row : csvRowList) {
				if(row.getDate().toLocalDateTime().getDayOfYear() == time.getDayOfYear())
					count++;
			}
			csvMissedPerDayRowList.add(new CSVRow(Timestamp.valueOf(time), count));
		}
		return new Csv(csvMissedPerDayRowList);
	}
	
	public Csv getCsvHoles(Csv csv) {
		detectCsv(csv, 1);
		
		return csvHoles;
	}
	
	public Csv getCsvDistinct(Csv csv) {
		detectCsv(csv, 1);
		
		return csvDistinct;
	}
	
	public Csv getCsvMultiples(Csv csv) {
		detectCsv(csv, 1);
		
		return csvMultiples;
	}
	
	private void detectCsv(Csv csv, double filler) {
		List<CSVRow> rowsList = csv.getCsvRows();
		ArrayList<CSVRow> HolesRows = new ArrayList<>();
		ArrayList<CSVRow> MultRows = new ArrayList<>();
		ArrayList<CSVRow> DistinctRows = new ArrayList<>();
		int dist = 0;
		Duration sampling = csv.getSampling();
		CSVRow currentRow = null;
		Iterator<CSVRow> iterator = rowsList.iterator();
		CSVRow previuosRow;
		
		if(iterator.hasNext()) {
			do {
				previuosRow = iterator.next();
			}while(previuosRow.getDate().toLocalDateTime().getHour() != 0 && iterator.hasNext());
		
			while(iterator.hasNext()) {
				currentRow = iterator.next();
				dist = Helper.distPosBetweenRows(previuosRow, currentRow, sampling);
				if(dist == 1) {
					DistinctRows.add(previuosRow);
				}else if(dist > 1) {
					for(int i=1;i<dist;i++) {
						Duration gap = sampling.multipliedBy(i);
						Timestamp t = Timestamp.valueOf(previuosRow.getDate().toLocalDateTime().plus(gap));
						HolesRows.add(new CSVRow(t, filler));
					}
				}else if(dist == 0) {
					MultRows.add(new CSVRow(previuosRow.getDate(), 1));
				}else
					throw new IllegalArgumentException("Errore: distanza tra righe negativa!");
				previuosRow = currentRow;
			}
			if(dist > 0)
				DistinctRows.add(currentRow);
			else if(dist == 0)
				MultRows.add(new CSVRow(currentRow.getDate(), 1));
		}
		csvDistinct = new Csv(DistinctRows);
		csvHoles = new Csv(HolesRows);
		csvMultiples = new Csv(MultRows);
		
	}
	
}
