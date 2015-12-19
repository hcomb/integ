package eu.hcomb.rrouter.pattern;

import redis.clients.jedis.JedisPool;

import com.codahale.metrics.Meter;
import com.google.inject.Inject;

import eu.hcomb.common.service.RedisService;

public abstract class InOut extends IntegrationPattern {

	protected int maxRetry = 4;
	protected int waitTime = 1000;

	protected String origin;
	protected String destination;
	
	protected Meter meterIn;
	protected Meter meterOut;

	protected JedisPool poolIn;
	protected JedisPool poolOut;

	@Inject
	protected RedisService redisService;
	
	@Override
	public String getName() {
		return new StringBuilder()
			.append(this.getClass().getName())
			.append("#")
			.append(this.getId())
			.append("[from=")
			.append(origin)
			.append("]")
			.append("[to=")
			.append(destination)
			.append("]")
			.toString();
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public void setPoolIn(JedisPool poolIn) {
		this.poolIn = poolIn;
	}

	public void setPoolOut(JedisPool poolOut) {
		this.poolOut = poolOut;
	}

	public void setMeterIn(Meter meterIn) {
		this.meterIn = meterIn;
	}

	public void setMeterOut(Meter meterOut) {
		this.meterOut = meterOut;
	}

	
}
