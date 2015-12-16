package eu.hcomb.rrouter.service.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import eu.hcomb.rrouter.dto.RedisInstanceDTO;
import eu.hcomb.rrouter.dto.RouteDTO;

public interface RouterMapper {

	@Select("SELECT "
				+ "routes_links.id AS id, "
				+ "from_instance.name AS fromInstanceName, "
				+ "from_endpoint.rkey AS fromKey, from_endpoint.type AS fromType, "
				+ "routes_links.active AS active, "
				+ "to_endpoint.rkey AS toKey, to_endpoint.type AS toType, "
				+ "to_instance.name AS toInstanceName "
			+ "FROM routes_endpoints AS from_endpoint "
				+ "LEFT OUTER JOIN redis_instances AS from_instance ON from_endpoint.instance_id = from_instance.id "
				+ "LEFT OUTER JOIN routes_links ON routes_links.id_from = from_endpoint.id "
				+ "LEFT OUTER JOIN routes_endpoints AS to_endpoint ON routes_links.id_to = to_endpoint.id "
				+ "LEFT OUTER JOIN redis_instances AS to_instance ON to_endpoint.instance_id = to_instance.id "
			+ "WHERE "
				+ "routes_links.active IS NOT NULL")
	List<RouteDTO> getAllRoutes();

	@Select("SELECT * FROM redis_instances")
	List<RedisInstanceDTO> getAllInstances();
}
