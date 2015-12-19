package eu.hcomb.rrouter.pattern.impl;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import eu.hcomb.common.redis.SendHandler;
import eu.hcomb.rrouter.pattern.InOut;

public class TopicToQueue extends InOut {

	protected Subscriber subscriber;

	class Subscriber extends JedisPubSub implements SendHandler {

		public void sendPayload(Jedis out, String destination, String payload) {
			out.lpush(destination, payload);
		}

		public void onMessage(String channel, String payload) {
			
			meterIn.mark();
			
			boolean ret = redisService.send(poolOut, meterOut, destination, payload, this);
			
			if(!ret)
				redisService.handleFailure(poolOut, meterOut, destination, payload, this, maxRetry, waitTime);

		}

		public void onPMessage(String pattern, String channel, String message) {}
		public void onSubscribe(String channel, int subscribedChannels) {}
		public void onUnsubscribe(String channel, int subscribedChannels) {}
		public void onPUnsubscribe(String pattern, int subscribedChannels) {}
		public void onPSubscribe(String pattern, int subscribedChannels) {}

	}

	public void startPattern() {
		subscriber = new Subscriber();

		boolean ret = redisService.setupSubscription(poolIn, subscriber, origin);
		
		if(!ret)
			redisService.handleSubscriptionFailure(poolIn, subscriber, origin, maxRetry, waitTime);
	}
	
	public void stopPattern() {
		subscriber.unsubscribe(origin);
	}

}
