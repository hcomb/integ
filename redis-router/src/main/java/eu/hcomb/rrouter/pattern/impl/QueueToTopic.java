package eu.hcomb.rrouter.pattern.impl;

import java.util.List;

import eu.hcomb.rrouter.pattern.InOut;

public class QueueToTopic extends InOut {
		
	Boolean running = true;
	
	public void startPattern() {
		setupJedis();
		
		running = true;
		
        while(running){
        	List<String> data = in.blpop(2, origin);
        	if(data!=null){
	            String payload = data.get(1);
	            for (String dest : destination)
					out.publish(dest, payload);
        	}
        }
        
	}
	
	public void stopPattern() {
		running = false;
		shutdownJedis();
	}
	
}
