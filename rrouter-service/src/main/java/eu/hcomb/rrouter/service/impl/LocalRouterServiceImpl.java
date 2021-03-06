package eu.hcomb.rrouter.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import redis.clients.jedis.Jedis;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import eu.hcomb.common.service.RedisPoolContainer;
import eu.hcomb.rrouter.dto.EndpointDTO;
import eu.hcomb.rrouter.dto.RedisInstanceDTO;
import eu.hcomb.rrouter.dto.RouteDTO;
import eu.hcomb.rrouter.pattern.InOut;
import eu.hcomb.rrouter.pattern.impl.QueueToQueue;
import eu.hcomb.rrouter.pattern.impl.QueueToTopic;
import eu.hcomb.rrouter.pattern.impl.TopicToQueue;
import eu.hcomb.rrouter.service.LocalRouterService;
import eu.hcomb.rrouter.service.mapper.RouterMapper;

@Singleton
public class LocalRouterServiceImpl implements LocalRouterService {

	protected Log log = LogFactory.getLog(this.getClass());
	
	@Inject
	protected RouterMapper routerMapper;
	
	@Inject
	protected MetricRegistry registry;
	
	@Inject
	protected RedisPoolContainer redisPools;
	
	protected Map<RouteDTO,InOut> patterns = new HashMap<RouteDTO, InOut>();
	
	protected Map<String,Gauge<Long>> gauges = new HashMap<String,Gauge<Long>>();
	
	public List<EndpointDTO> getAllEndpoints() {
		return routerMapper.getAllEndpoints();
	}
	
	public void update(Long id, int x, int y) {
		routerMapper.update(id, x, y);
	}
	
	public InOut getAndRegisterPattern(Injector injector, RouteDTO route) {

		InOut pattern = getPatternInstance(injector, route);
		if(pattern == null)
			return null;
		
		pattern.setId(""+route.getId());
		
		pattern.setOrigin(route.getFrom().getKey());
		pattern.setPoolIn(redisPools.getPool(route.getFrom().getInstance()));

		pattern.setDestination(route.getTo().getKey());
		pattern.setPoolOut(redisPools.getPool(route.getTo().getInstance()));
		
		pattern.setMeterIn(registry.meter("router.route."+route.getId()+"."+route.getFrom().getInstance() + "." + route.getFrom().getKey()));
		pattern.setMeterOut(registry.meter("router.route."+route.getId()+"."+route.getTo().getInstance() + "." + route.getTo().getKey()));

		registerQueueGauges(route);
		

		return pattern;
	}
	
	
	private void registerQueueGauges(final RouteDTO route) {
		registerQueueGauge(route.getId(), route.getFrom());
		registerQueueGauge(route.getId(), route.getTo());
	}	
	
	private void registerQueueGauge(Long routeId, final EndpointDTO endpoint) {
		if("queue".equals(endpoint.getType())){
			String key = "router.queue."+routeId+"."+endpoint.getInstance() + "." + endpoint.getKey() + ".size";
			Gauge<Long> gauge = gauges.get(key);
			final Jedis jedis = redisPools.getPool(endpoint.getInstance()).getResource();
			if(gauge == null){
				log.info("registering gauge metric on queue "+endpoint.getInstance() + ":" + endpoint.getKey());
				gauge = new Gauge<Long>() {
					public Long getValue() {
						return jedis.llen(endpoint.getKey());
					}
				};
				registry.register(key, gauge);
				gauges.put(key, gauge);
			}
		}
	}



	private InOut getPatternInstance(Injector injector, RouteDTO route) {
		if(route.getFrom().getType().equals("queue") && route.getTo().getType().equals("queue"))
			return injector.getInstance(QueueToQueue.class);
		if(route.getFrom().getType().equals("queue") && route.getTo().getType().equals("topic"))
			return injector.getInstance(QueueToTopic.class);
		if(route.getFrom().getType().equals("topic") && route.getTo().getType().equals("queue"))
			return injector.getInstance(TopicToQueue.class);
		return null;
	}

	public List<RouteDTO> getAllRoutes() {
		List<RouteDTO> list = routerMapper.getAllRoutes();
		for (RouteDTO dto : list)
			dto.fix();
		return list;
	}
	
	public List<RedisInstanceDTO> getAllInstances() {
		return routerMapper.getAllInstances();
	}
	
}
