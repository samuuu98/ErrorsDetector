package errorsDetector.utils;

import java.time.Duration;

import errorsDetector.model.CSVRow;

public class Helper {
	
	private static Duration distanceBetweenRows(CSVRow row1, CSVRow row2){
		return Duration.between(row2.getDate().toLocalDateTime(), row2.getDate().toLocalDateTime());
	}
	
	public static int distPosBetweenRows(CSVRow row1, CSVRow row2, Duration sampling) {
		Duration duration = Duration.between(row1.getDate().toLocalDateTime(), row2.getDate().toLocalDateTime());

		return (int)Math.round(divisionBetweenDurations(duration, sampling));
	}
	
	public static boolean isSameDay(CSVRow row1, CSVRow row2) {
		return row2.getDate().toLocalDateTime().getDayOfYear() == row1.getDate().toLocalDateTime().getDayOfYear();
	}
	
	private static double divisionBetweenDurations(Duration d1, Duration d2) {
		return  (double)d1.getSeconds() / (double)d2.getSeconds();
	}



}
