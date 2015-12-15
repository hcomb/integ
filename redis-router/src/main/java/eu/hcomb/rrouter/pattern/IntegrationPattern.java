package eu.hcomb.rrouter.pattern;

import io.dropwizard.lifecycle.Managed;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import redis.clients.jedis.JedisPool;

import com.google.inject.Inject;

public abstract class IntegrationPattern implements Managed{

	protected Log log = LogFactory.getLog(this.getClass());
	
	protected String id;
	
	@Inject
	protected JedisPool pool;

	public abstract void startPattern();
	public abstract void stopPattern();
	
	protected Thread innerThread;
	
	public void start() throws Exception {
		log.debug("starting pattern " + getName());
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

	public JedisPool getPool() {
		return pool;
	}

	public void setPool(JedisPool pool) {
		this.pool = pool;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
