package eu.hcomb.rrouter.pattern;

import org.apache.commons.lang3.StringUtils;

import redis.clients.jedis.Jedis;

public abstract class InOut extends IntegrationPattern {

	protected String origin;
	protected String[] destination;

	protected Jedis in;
	protected Jedis out;

	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String[] getDestination() {
		return destination;
	}
	public void setDestination(String... destination) {
		this.destination = destination;
	}
	
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
			.append(StringUtils.join(destination, ','))
			.append("]")
			.toString();
	}

	public void setupJedis(){
		in = pool.getResource();
		out = pool.getResource();
	}

	public void shutdownJedis() {
		in.close();
		out.close();
	}

}
