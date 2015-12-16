package eu.hcomb.rrouter;

import io.dropwizard.setup.Environment;

import java.util.List;

import org.mybatis.guice.datasource.helper.JdbcHelper;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.codahale.metrics.MetricRegistry;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import eu.hcomb.common.jdbc.DatasourceHealthCheck;
import eu.hcomb.common.jdbc.PersistenceModule;
import eu.hcomb.common.redis.ManagedJedisPool;
import eu.hcomb.common.redis.RedisHealthCheck;
import eu.hcomb.common.resources.WhoAmI;
import eu.hcomb.common.web.BaseApp;
import eu.hcomb.rrouter.dto.RedisInstanceDTO;
import eu.hcomb.rrouter.dto.RouteDTO;
import eu.hcomb.rrouter.pattern.InOut;
import eu.hcomb.rrouter.resources.RouterResource;
import eu.hcomb.rrouter.service.RouterService;
import eu.hcomb.rrouter.service.impl.RouterServiceImpl;
import eu.hcomb.rrouter.service.mapper.RouterMapper;

public class RRouterApp extends BaseApp<RRouterConfig> {
	
	public static void main(String[] args) throws Exception {
		new RRouterApp().run(args);
	}
	
	public void configure(Binder binder) {
		configureSecurity(binder);
		
		binder
			.bind(RouterService.class)
			.to(RouterServiceImpl.class);
		
	}	

	@Override
	public void run(RRouterConfig configuration, Environment environment) {
		this.environment = environment;
		
		Module persistence = new PersistenceModule(configuration, environment) {
			@Override
			protected void initialize() {
				install(JdbcHelper.MySQL);
				setup();
		        addMapperClass(RouterMapper.class);
			}
		};

		injector = Guice.createInjector(this, persistence);
		
		defaultConfig(environment, configuration);
		
		setupConfigurableJedis(configuration, environment);
		
		environment.jersey().register(injector.getInstance(WhoAmI.class));
		environment.jersey().register(injector.getInstance(RouterResource.class));

		environment.healthChecks().register("mysql", injector.getInstance(DatasourceHealthCheck.class));

		setUpSwagger(configuration, environment);
		
	}

	private void setupConfigurableJedis(RRouterConfig configuration, Environment environment) {
		RouterService routerSvc = injector.getInstance(RouterService.class);
		List<RedisInstanceDTO> instances = routerSvc.getAllInstances();
		for (RedisInstanceDTO instance : instances) {
			
			JedisPoolConfig poolConfig = new JedisPoolConfig();
			poolConfig.setMinIdle(instance.getMinIdle());
			poolConfig.setMaxIdle(instance.getMaxIdle());
			poolConfig.setMaxTotal(instance.getMaxTotal());
	
			JedisPool pool = new JedisPool(poolConfig, instance.getHost(), instance.getPort(), 2000);

			environment.lifecycle().manage(new ManagedJedisPool(pool));
			environment.healthChecks().register("redis-"+instance.getName(), new RedisHealthCheck(pool));
			
			routerSvc.setPool(instance.getName(), pool);
		}
		
		List<RouteDTO> routes = routerSvc.getAllRoutes();
		for (RouteDTO route : routes) {
			InOut pattern = routerSvc.getAndRegisterPattern(route);
			if(pattern==null){
				log.info("ignoring route: " + route.getId() +" " + route.getFrom().getType() + " -> " + route.getTo().getType());
			}else{
				environment.lifecycle().manage(pattern);
			}

		}
	}

	Environment environment;
	
	@Provides
	@Singleton
	public MetricRegistry getMetricsRegistry(){
		return environment.metrics();
	}
}