package errorsDetector;

import java.io.IOException;
import java.util.HashMap;

import com.sun.javafx.logging.Logger;

import errorsDetector.controller.Controller;
import errorsDetector.persistence.DataManager;
import errorsDetector.persistence.MyCsvReader;
import errorsDetector.persistence.SepaAggregator;
import errorsDetector.persistence.SepaThread;
import errorsDetector.ui.MainPane;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPABindingsException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPAPropertiesException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPAProtocolException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPASecurityException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ErrorsDetectorApplication extends Application {
	
	@Override
	public void init(){
		// do nothing
	}

	@Override
	public void start(Stage stage) throws SEPAProtocolException, SEPASecurityException, SEPAPropertiesException {
		DataManager dataManager = null;
		try {
			dataManager = createDataManager();
		} catch (SEPAProtocolException | SEPASecurityException | SEPAPropertiesException | SEPABindingsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!readData(dataManager))
			return;

		Controller controller = new Controller(dataManager);
		stage.setTitle("Error's Detector");
		
		BorderPane root = new MainPane(controller);
		Scene scene = new Scene(root, 980, 480);
		stage.setScene(scene);
		stage.show();
	}

	private boolean readData(DataManager dataManager) {
		try {
				dataManager.readAll();
			return true;
		} catch (IOException | SEPASecurityException | SEPAPropertiesException | SEPAProtocolException
				| SEPABindingsException e) {
			showAlert("Errore: " + e.toString());
			e.printStackTrace();
		} 
		return false;
	}

	private void showAlert(String text) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Errore");
		alert.setHeaderText("Impossibile leggere i dati");
		alert.setContentText(text);
		alert.showAndWait();
	}

	protected DataManager createDataManager() throws SEPAProtocolException, SEPASecurityException, SEPAPropertiesException, SEPABindingsException {
		MyCsvReader csvReader = new MyCsvReader();
		HashMap<String, SepaAggregator> aggregatorMap = new HashMap<>();
		SepaAggregator sepaAggregator = new SepaAggregator("SOIL_MOISTURE", "UPDATEVALUE", "http://soilMoisture");
		aggregatorMap.put("http://soilMoisture", sepaAggregator);
		new SepaThread(sepaAggregator, "http://soilMoisture").start();

		DataManager dataManager = new DataManager(csvReader, aggregatorMap);
		return dataManager;
	}

	public static void main(String[] args) {
		launch(args);
	}

}
