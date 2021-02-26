package errorsDetector.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import errorsDetector.model.CSVRow;
import errorsDetector.model.Csv;
import errorsDetector.model.Detector;
import errorsDetector.persistence.DataManager;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class Controller {
	private DataManager dataManager;
	private Detector detector;
	
	public Controller(DataManager dataManager) {
		this.dataManager=dataManager;
		detector = new Detector();
	}
	
	public HashMap<String, Csv> getCsvMap(){
		return dataManager.getCsvMap();
	}
	
	public boolean addObservation(String id, String filePath) {
		return dataManager.addObservation(id, filePath);
	}
	
	public Csv getObservation(String id) {
		return dataManager.getObservation(id);
	}
	
	public Csv getMissingPerDay(String id) {
		return  detector.getMissedPerDay(dataManager.getObservation(id));
	}
	
	public Csv getMultiplesPerDay(String id) {
		return  detector.getMultiplesPerDay(dataManager.getObservation(id));
	}
	
	/*public Csv getCuttedCsv()  {
		return cutEdge(csv.getCsvList());
	}*/

	public List<TableColumn<CSVRow, Object>> getTableColumn() {
		List<TableColumn<CSVRow, Object>> columns = new ArrayList<>();
		
		Callback<CellDataFeatures<CSVRow, Object>, ObservableValue<Object>> property = new  PropertyValueFactory<>("Date");
		TableColumn<CSVRow, Object> tableCol = new TableColumn<>("Column1");
		tableCol.setCellValueFactory(property);
		
		Callback<CellDataFeatures<CSVRow, Object>, ObservableValue<Object>> property2 = new  PropertyValueFactory<>("Value");
		TableColumn<CSVRow, Object> tableCol2 = new TableColumn<>("Column2");
		tableCol2.setCellValueFactory(property2);
		
		columns.add(tableCol);
		columns.add(tableCol2);
        
		return columns;
	}
	
	/*public Csv cutEdge(List<String[]> csv) {
		boolean cut = false;
		List<String[]> newCsvList = csv;
		var previousRow = csv.iterator().next();
		var row = previousRow;
		
		while(!cut) {
			row = csv.iterator().next();
			cut = Timestamp.valueOf(row[0].replace("T", " ").replace("Z","")).toLocalDateTime().getHour() == 0 ;
			newCsvList.remove(previousRow);
			previousRow = row;
		}
		
		cut = false;
		Collections.reverse(csv);
		previousRow = csv.iterator().next();
		
		while(!cut) {
			row = csv.iterator().next();
			cut = Timestamp.valueOf(row[0].replace("T", " ").replace("Z","")).toLocalDateTime().getHour() == 23 ;
			newCsvList.remove(previousRow);
			previousRow = row;
		}
		
		Collections.reverse(newCsvList);
		
		return new Csv(newCsvList); 
	}*/
	

}
