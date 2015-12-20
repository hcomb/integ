package eu.hcomb.rrouter.pattern;

import io.dropwizard.lifecycle.Managed;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class IntegrationPattern implements Managed {

	protected Log log = LogFactory.getLog(this.getClass());
	
	protected String id;
	
	public abstract void startPattern();
	public abstract void stopPattern();
	
	protected Thread innerThread;
	
	public void start() throws Exception {
		innerThread = new Thread(new Runnable() {
			public void run() {
				try{
					startPattern();
				}catch(Exception e){
					throw new RuntimeException(e);
				}
			}
		});
		innerThread.start();
		log.debug("started pattern " + getName());
	}
	
	public void stop() throws Exception {
		log.debug("stopping pattern " + getName());
		innerThread.interrupt();
		stopPattern();
		log.debug("stopped pattern " + getName());
	}
	
	public abstract String getName();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
