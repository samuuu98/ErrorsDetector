package errorsDetector.persistence;


import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import errorsDetector.model.Csv;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPABindingsException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPAPropertiesException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPAProtocolException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPASecurityException;
import it.unibo.arces.wot.sepa.commons.sparql.ARBindingsResults;

public class DataManager {
	private MyCsvReader csvReader;
	private HashMap<String, Csv> csvMap;
	private HashMap<String, SepaAggregator> aggregatorMap;
	
	public DataManager(MyCsvReader csvReader,HashMap<String, SepaAggregator> aggregatorMap) throws SEPAProtocolException, SEPASecurityException, SEPAPropertiesException, SEPABindingsException {
		this.csvReader = csvReader;
		this.csvMap = new HashMap<>();
		this.aggregatorMap = aggregatorMap;
		
		//Setup csvMap
		aggregatorMap.entrySet().forEach(entry -> csvMap.put(entry.getKey(), entry.getValue().getCsv()));
	}
	
	public DataManager(MyCsvReader csvReader) throws SEPAProtocolException, SEPASecurityException, SEPAPropertiesException, SEPABindingsException {
		this.csvReader = csvReader;
		this.csvMap = new HashMap<>();
		
	}
	
	public HashMap<String, Csv> getCsvMap(){
		return csvMap;
	}
	
	public Csv getObservation(String id) {
		
		return csvMap.containsKey(id) ? csvMap.get(id) : null;
	}
	
	public boolean addObservation(String id, String filePath) {
		if(!csvMap.containsKey(id))
			try {
				csvMap.put(id, getCsv(filePath));
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		return false;
	}
	
	private  Csv getCsv(String filePath) throws IOException{
		Reader reader= new FileReader(filePath);
		
		return new Csv(csvReader.read(reader));
	}
	
	/*public  Csv getCsv() throws IOException, BadFileFormatException{
		Reader reader= new FileReader("C:\\Users\\samuele_pc\\Documents\\tesi\\PioggiaCorreggio_1July_28Nov.csv");
		
		return new Csv(csvReader.read(reader));
	}*/

	public void readAll() throws IOException,SEPASecurityException, SEPAPropertiesException, SEPAProtocolException, SEPABindingsException {
		//synchronized (this) {
		//for(var value : aggregatorMap.values())
			//value.subscribe();
		//}
	}

}
