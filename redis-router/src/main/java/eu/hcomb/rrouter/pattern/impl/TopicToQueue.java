package eu.hcomb.rrouter.pattern.impl;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisConnectionException;
import eu.hcomb.rrouter.pattern.RetryForward;

public class TopicToQueue extends RetryForward {

	Boolean running = false;
	Subscriber subscriber;

	@Override
	public void sendPayload(Jedis out, String payload) {
		out.lpush(destination, payload);
	}
	
	public void startPattern() {
		subscriber = new Subscriber();

		boolean ret = setupSubscription();
		
		if(!ret)
			handleSubscriptionFailure();
	}
	
	public void handleSubscriptionFailure() {
		boolean ok = false;
		int max = maxRetry;
		while(!ok && max > 0){
			try{
				Thread.sleep(waitTime);
			}catch(Exception e){}
			max--;
			log.warn("retrying to subscribe, #"+max+", origin:"+origin);
			ok = setupSubscription();
		}
		
		if(!ok){
			log.error("cannot subscribe to destination: "+destination);
		}
		
	}

	
	public boolean setupSubscription(){
		
		Jedis in = null;
		try {
			in = poolIn.getResource();
			running = true;
			in.subscribe(subscriber, origin);
			return true;
		}catch(JedisConnectionException e){
			poolIn.returnBrokenResource(in);
			in = null;
			log.warn("exception while subscribing to origin:" + origin+", exception:"+e.getMessage());
			return false;
		}finally{
			if(in!=null)
				poolIn.returnResource(in);
		}
	}

	public void stopPattern() {

		subscriber.unsubscribe(origin);
		running = false;
	}

	class Subscriber extends JedisPubSub {

		public void onMessage(String channel, String payload) {
			
			meterIn.mark();
			
			boolean ret = forward(payload);
			
			if(!ret)
				handleFailure(payload);

		}

		public void onPMessage(String pattern, String channel, String message) {}
		public void onSubscribe(String channel, int subscribedChannels) {}
		public void onUnsubscribe(String channel, int subscribedChannels) {}
		public void onPUnsubscribe(String pattern, int subscribedChannels) {}
		public void onPSubscribe(String pattern, int subscribedChannels) {}

	}
}
