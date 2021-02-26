package errorsDetector.ui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import errorsDetector.controller.Controller;
import errorsDetector.model.CSVRow;
import errorsDetector.model.Csv;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPABindingsException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPAPropertiesException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPAProtocolException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPASecurityException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MainPane extends BorderPane{
	 
	Controller controller;
	private Button buttonRead;
	private Button buttonLoad;
	private ToggleGroup radioGroup;
	private RadioButton rbMiss;
	private RadioButton rbMult;
	private ComboBox<String> comboBox;
	private TableView<CSVRow> csvTable;
	private XYChart.Series<String, Number> series1;
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	private ArrayList<String> results;
	private Set<String> users;
	
	public MainPane(Controller controller) {
		this.controller=controller;
		this.results = new ArrayList<>();
		initPane();
	}
	
	@SuppressWarnings("unchecked")
	public void initPane() {
		HBox centerPane = new HBox();
		HBox rightPane = new HBox();
		VBox leftPane = new VBox();
		Insets rect = new Insets(0,20,10,20);
		{
		
		leftPane.setSpacing(10);
		leftPane.setPadding(rect);
		Label lName = new Label ("Nome osservazione:");
		TextField name = new TextField();
		Label lFilePath = new Label ("Inserire Percorso:");
		TextField filepathField = new TextField();
		Label lComboBox = new Label ("Selezionare un'osservazione:");
		comboBox = new ComboBox<>();
		comboBox.setItems(FXCollections.observableList(new ArrayList<>(controller.getCsvMap().keySet())));
		csvTable = new TableView<>();
		buttonRead = new Button("Leggi");
		buttonLoad = new Button("Carica");
		radioGroup = new ToggleGroup();

		rbMiss = new RadioButton("Dati mancanti per giorno");
		rbMiss.setToggleGroup(radioGroup);
		rbMiss.setSelected(true);

		rbMult = new RadioButton("Dati multipli per giorno");
		rbMult.setToggleGroup(radioGroup);
		
		leftPane.getChildren().add(lName);
		leftPane.getChildren().add(name);
		leftPane.getChildren().add(lFilePath);
		leftPane.getChildren().add(filepathField);
		leftPane.getChildren().add(buttonRead);
		leftPane.getChildren().add(lComboBox);
		leftPane.getChildren().add(comboBox);
		leftPane.getChildren().add(rbMiss);
		leftPane.getChildren().add(rbMult);
		leftPane.getChildren().add(buttonLoad);
		LineChart<String, Number> chart = new LineChart<>(new CategoryAxis(), new NumberAxis());

	    series1 = new XYChart.Series<>();
	    chart.getData().addAll(series1);
	    chart.setCreateSymbols(false);
	    rightPane.getChildren().add(chart);
		
		centerPane.getChildren().add(csvTable);
		
		leftPane.setAlignment(Pos.BASELINE_RIGHT);
		setRight(rightPane);
		setCenter(centerPane);
		setLeft(leftPane);
		
		
		buttonRead.setOnAction(event -> {
			
			if(controller.addObservation(name.getText(), filepathField.getText()))
				updateComboBox();
				//setView(controller.getMissingPerDay(name.getText()));
			
			//Generatore di update
			/*Csv csv=controller.getCsv();
			List<CSVRow> _csvRowList = csv.getCsvRows();
			int count = 0;
			StringBuilder str = new StringBuilder();
			str.append("PREFIX sosa: <http://www.w3.org/ns/sosa/> " + 
					"PREFIX qudt: <http://qudt.org/schema/qudt#> ");
			for(CSVRow row : _csvRowList) {
				count++;
				str.append("INSERT {GRAPH <http://wot.arces.unibo.it/observation> {<http://soil_moisture> rdf:type sosa:Observation ; sosa:resultTime ");
				str.append("'" + row.getDate().toString() + "' ");
				str.append("; sosa:hasResult _:quantity . _:quantity rdf:type qudt:QuantityValue ; qudt:numericValue ");
				str.append("'" + row.getValue() + "' ");
				str.append("}} WHERE {} ; ");
				if(count == 49)
					break;
			}
			String path = "C:\\Users\\samuele_pc\\eclipse-workspace\\ErrorsDetector\\resources\\update_observation.txt";
			try {
			File file = new File(path);
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(str.toString());
			bw.flush();
			bw.close();
			}
			catch(IOException e) {
			e.printStackTrace();
			}*/
		});
		
		buttonLoad.setOnAction(event -> {
			if(controller.getObservation(comboBox.getSelectionModel().getSelectedItem()) != null)
				setView(comboBox.getSelectionModel().getSelectedItem());
		});
		
		}
	}
	
	private void setView(String id) {
		Csv csv;
		if(rbMiss.isSelected())
			csv = controller.getMissingPerDay(id);
		else 
			csv = controller.getMultiplesPerDay(id);
		
		if(csv != null) {
			series1.getData().clear();
			List<CSVRow> csvRowList = csv.getCsvRows();
			ObservableList<CSVRow> csvRowObList = FXCollections.observableList(csvRowList);
			csvTable.setItems(csvRowObList);
			csvTable.getColumns().setAll(controller.getTableColumn());
			 
			//dateFormat.format(row.getDate())
			csvRowList.forEach(row -> series1.getData().add(new Data<String, Number>(row.getDate().toString(),row.getValue())));
		}
	}
	
	private void updateComboBox() {
		comboBox.setItems(FXCollections.observableList(new ArrayList<>(controller.getCsvMap().keySet())));
	}
}
	
	


