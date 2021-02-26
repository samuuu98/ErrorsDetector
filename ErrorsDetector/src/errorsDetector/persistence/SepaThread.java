package errorsDetector.persistence;

import it.unibo.arces.wot.sepa.commons.exceptions.SEPABindingsException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPAPropertiesException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPAProtocolException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPASecurityException;
import it.unibo.arces.wot.sepa.commons.sparql.BindingsResults;

public class SepaThread extends Thread {
	private Thread t;
    private SepaAggregator sepaAggregator;
    private String name;
   
    public SepaThread(SepaAggregator sepaAggregator, String name) {
       this.sepaAggregator = sepaAggregator;
       this.name = name;
    }
   
    public void run() {
    	try {
			sepaAggregator.subscribe();
		} catch (SEPASecurityException | SEPAPropertiesException | SEPAProtocolException | SEPABindingsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    
    }
   
   public void start () {
      if (t == null) {
         t = new Thread (this, name);
         t.start ();
      }
   }

}
