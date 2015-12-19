package eu.hcomb.rrouter.pattern.impl;

import redis.clients.jedis.Jedis;
import eu.hcomb.common.redis.SendHandler;
import eu.hcomb.rrouter.pattern.InOut;

public class QueueToTopic extends InOut implements SendHandler {

	Boolean running = false;

	public void sendPayload(Jedis out, String destination, String payload) {
		out.publish(destination, payload);
	}

	public void startPattern() {
		running = true;

		while (running) {

			String payload = redisService.brpop(poolIn, meterIn, origin);
						
			boolean ret = redisService.send(poolOut, meterOut, destination, payload, this);
			
			if(!ret)
				redisService.handleFailure(poolOut, meterOut, destination, payload, this, maxRetry, waitTime);
		}

	}
	
	public void stopPattern() {
		running = false;
	}
}
