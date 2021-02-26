package errorsDetector.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class CSVRow{
	
	private String[] rowValueProperty;
	private Timestamp Date;
	private double Value;
	
	public CSVRow(String[] rowValueProperty){
		this.rowValueProperty = rowValueProperty;
		setDate();
		setValue();
	}
	
	public CSVRow(Timestamp Date, double value){
		this.Date = Date;
		this.Value = value;
		setRowValueProperty();
	}
	
	@SuppressWarnings("null")
	private String[] setRowValueProperty() {
		String[] rowValueProperty = {Date.toString(), String.valueOf(Value)};
		//rowValueProperty[0] = Date.toString();
		//rowValueProperty[1] = String.valueOf(Value);
		
		return rowValueProperty;
	}
	
	public String[] getRowValueProperty() {
		return rowValueProperty;
	}
	
	public String getColumnValue(int col) {
		String value = null;
		int index = col-1;
		if(rowValueProperty[index] != null)
			value = index == 0 ? rowValueProperty[index].replace("T", " ").replace("Z", "") : rowValueProperty[index];
		else
			value = "";
		
		return value;
	}
	
	public Timestamp getDate() {
		return Date;
	}
	
	public void setDate() {
		try {
			Date = Timestamp.valueOf(getColumnValue(1));
		}catch(IllegalArgumentException e) {
			e.printStackTrace();
			Date = Timestamp.valueOf(LocalDateTime.now());
		}
	}
	
	public void setValue() {
		Value = Double.parseDouble(getColumnValue(2));
	}

	
	public double getValue() {
		return Value;
	}

	
	public void add(String string) {
		rowValueProperty[rowValueProperty.length] = string;
	}
	
}
