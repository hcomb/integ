package eu.hcomb.rrouter.pattern;

import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

public abstract class RetryForward extends InOut {
	protected int maxRetry = 4;
	protected int waitTime = 1000;

	public void handleFailure(String payload) {
		boolean ok = false;
		int max = maxRetry;
		while(!ok && max > 0){
			try{
				Thread.sleep(waitTime);
			}catch(Exception e){}
			max--;
			log.warn("retrying to forward, #"+max+", destination:"+destination+", payload:"+payload);
			ok = forward(payload);
		}
		
		if(!ok){
			log.error("cannot forward message, destination: "+destination+", payload:"+payload);
		}
		
	}

	public boolean forward(String payload){
		if(payload == null)
			return true;
		
		Jedis out = null;
		try{
			out = poolOut.getResource();
			sendPayload(out, payload);
			meterOut.mark();
			return true;
		}catch(JedisConnectionException e){
			if(out!=null)
				poolOut.returnBrokenResource(out);
			out = null;
			log.warn("exception while sending message destination:" + destination+", exception:"+e.getMessage());
			return false;
		} finally {
			if(out!=null)
				poolOut.returnResource(out);
		}
	}
	
	public abstract void sendPayload(Jedis out, String payload);

	public String getPayload(){
		Jedis in = null;
		String payload = null;
		try{
			in = poolIn.getResource();
			List<String> data = in.brpop(2, origin);
			if (data != null) {
				meterIn.mark();
				payload = data.get(1);
			}
		}catch(JedisConnectionException e){
			poolIn.returnBrokenResource(in);
			in = null;
			log.warn("exception while getting message from origin:" +origin+", exception:"+e.getMessage());
		} finally {
			if(in!=null)
				poolIn.returnResource(in);
		}
		return payload;
	}
	
}
