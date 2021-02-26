package errorsDetector.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CsvColumn {

	private List<String> columnValueProperty;
	
	public CsvColumn(){
		columnValueProperty = new ArrayList<>();
	}
	
	public ObservableList<String> columnProperty() { return FXCollections.observableList(columnValueProperty);}
	
	public void add(String string) {
		columnValueProperty.add(string);
	}
}
