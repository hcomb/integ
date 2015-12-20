package eu.hcomb.rrouter.service;

import java.util.List;

import eu.hcomb.rrouter.dto.EndpointDTO;
import eu.hcomb.rrouter.dto.RedisInstanceDTO;
import eu.hcomb.rrouter.dto.RouteDTO;

public interface RouterService {

	public abstract List<RouteDTO> getAllRoutes();
	
	public abstract List<RedisInstanceDTO> getAllInstances();
	
	public abstract List<EndpointDTO> getAllEndpoints();
	
}
