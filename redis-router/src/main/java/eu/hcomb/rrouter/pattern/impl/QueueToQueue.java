package eu.hcomb.rrouter.pattern.impl;

import redis.clients.jedis.Jedis;
import eu.hcomb.rrouter.pattern.RetryForward;

public class QueueToQueue extends RetryForward {

	Boolean running = false;

	public void startPattern() {
		running = true;

		while (running) {

			String payload = getPayload();
						
			boolean ret = forward(payload);
			
			if(!ret)
				handleFailure(payload);
		}

	}
	
	@Override
	public void sendPayload(Jedis out, String payload) {
		out.lpush(destination, payload);
	}

	public void stopPattern() {
		running = false;
	}

}
