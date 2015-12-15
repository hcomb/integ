package eu.hcomb.rrouter.pattern.impl;

import redis.clients.jedis.JedisPubSub;
import eu.hcomb.rrouter.pattern.InOut;

public class TopicToQueue extends InOut {
	
	Subscriber subscriber;
	
	public void startPattern() {
		setupJedis();

		subscriber = new Subscriber();
		
		in.subscribe(subscriber, origin);
	}
	
	public void stopPattern() {
		
		subscriber.unsubscribe(origin);
		
		shutdownJedis();
	}
	
	class Subscriber extends JedisPubSub {

		public void onMessage(String channel, String message) {
			for (String dest : destination)
				out.rpush(dest, message);
		}

		public void onPMessage(String pattern, String channel, String message) {}
		public void onSubscribe(String channel, int subscribedChannels) {}
		public void onUnsubscribe(String channel, int subscribedChannels) {}
		public void onPUnsubscribe(String pattern, int subscribedChannels) {}
		public void onPSubscribe(String pattern, int subscribedChannels) {}
		
	}
}
