package errorsDetector.persistence;

import it.unibo.arces.wot.sepa.pattern.Aggregator;
import it.unibo.arces.wot.sepa.pattern.JSAP;

import java.sql.Timestamp;
import java.util.List;

import errorsDetector.model.CSVRow;
import errorsDetector.model.Csv;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPABindingsException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPAPropertiesException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPAProtocolException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPASecurityException;
import it.unibo.arces.wot.sepa.commons.response.ErrorResponse;
import it.unibo.arces.wot.sepa.commons.security.ClientSecurityManager;
import it.unibo.arces.wot.sepa.commons.sparql.Bindings;
import it.unibo.arces.wot.sepa.commons.sparql.BindingsResults;
import it.unibo.arces.wot.sepa.commons.sparql.RDFTermLiteral;
import it.unibo.arces.wot.sepa.commons.sparql.RDFTermURI;

public class SepaAggregator extends Aggregator {
	
	private String observationURI;
	private Csv csv;
	
	public SepaAggregator(String subscribeID, String updateID, String observationURI)
			throws SEPAProtocolException, SEPASecurityException, SEPAPropertiesException {
		super(new JSAPProvider().getJsap(), subscribeID, updateID, new JSAPProvider().getSecurityManager());
		
		this.observationURI = observationURI;
		this.csv = new Csv();
		
	}
	public Csv getCsv() {
		return csv;
	}

	
	@Override
	public void onAddedResults(BindingsResults results) {
		super.onAddedResults(results);
		 synchronized (this) {
		List<Bindings> bindingsList = results.getBindings();
		for(Bindings bindings : bindingsList) {
			double value = Double.parseDouble(bindings.getValue("value"));
			Timestamp time = Timestamp.valueOf(bindings.getValue("timestamp"));
			csv.addCsvRow(new CSVRow(time, value));
		}
		 }
	}
	
	
	public void updateResult(int value) {
		
		/*try {
			System.out.println(waterLevel);
			this.setUpdateBindingValue("observation", new RDFTermURI(map.get(bindings.getValue("observation"))));
			this.setUpdateBindingValue("waterLevel", new RDFTermLiteral(Integer.toString(waterLevel)));
		} catch (SEPABindingsException e1) {
			e1.printStackTrace();
		}
		
		
		boolean ret = false;
		while (!ret && retry > 0) {
			try {
				ret = update().isUpdateResponse();
				System.out.println("Update --> " +ret);
			} catch (SEPASecurityException | SEPAProtocolException | SEPAPropertiesException
					| SEPABindingsException e) {
				logger.error(e.getMessage());
				ret = false;
			}
			retry--;
		}*/
	}
	
}

