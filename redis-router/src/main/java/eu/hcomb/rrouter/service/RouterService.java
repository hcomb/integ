package eu.hcomb.rrouter.service;

import java.util.List;

import redis.clients.jedis.JedisPool;
import eu.hcomb.rrouter.dto.RedisInstanceDTO;
import eu.hcomb.rrouter.dto.RouteDTO;
import eu.hcomb.rrouter.pattern.InOut;

public interface RouterService {

	public abstract List<RouteDTO> getAllRoutes();
	
	public abstract List<RedisInstanceDTO> getAllInstances();

	public abstract JedisPool setPool(String key, JedisPool value);

	public abstract JedisPool getPool(String key);

	public abstract InOut getAndRegisterPattern(RouteDTO route);
}
