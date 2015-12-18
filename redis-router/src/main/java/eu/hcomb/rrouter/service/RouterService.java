package eu.hcomb.rrouter.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import redis.clients.jedis.JedisPool;

import com.google.inject.Injector;

import eu.hcomb.rrouter.dto.EndpointDTO;
import eu.hcomb.rrouter.dto.RedisInstanceDTO;
import eu.hcomb.rrouter.dto.RouteDTO;
import eu.hcomb.rrouter.pattern.InOut;

public interface RouterService {

	public abstract List<RouteDTO> getAllRoutes();
	
	public abstract List<RedisInstanceDTO> getAllInstances();

	public abstract JedisPool setPool(String key, JedisPool value);

	public abstract JedisPool getPool(String key);

	public abstract InOut getAndRegisterPattern(Injector injector, RouteDTO route);
	
	public abstract List<EndpointDTO> getAllEndpoints();
	
	public abstract void update(Long id, int x, int y);
}
