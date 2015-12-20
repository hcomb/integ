package eu.hcomb.rrouter;

import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.List;

import org.mybatis.guice.datasource.helper.JdbcHelper;

import com.codahale.metrics.MetricRegistry;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import eu.hcomb.common.jdbc.DatasourceHealthCheck;
import eu.hcomb.common.jdbc.PersistenceModule;
import eu.hcomb.common.resources.WhoAmI;
import eu.hcomb.common.service.EventEmitter;
import eu.hcomb.common.service.RedisPoolContainer;
import eu.hcomb.common.service.RedisService;
import eu.hcomb.common.service.impl.RedisEventEmitter;
import eu.hcomb.common.service.impl.RedisPoolContainerImpl;
import eu.hcomb.common.service.impl.RedisServiceJedisImpl;
import eu.hcomb.common.web.BaseApp;
import eu.hcomb.rrouter.dto.RedisInstanceDTO;
import eu.hcomb.rrouter.dto.RouteDTO;
import eu.hcomb.rrouter.pattern.InOut;
import eu.hcomb.rrouter.resources.EndpointResource;
import eu.hcomb.rrouter.resources.InstanceResource;
import eu.hcomb.rrouter.resources.RouterResource;
import eu.hcomb.rrouter.service.LocalRouterService;
import eu.hcomb.rrouter.service.RouterService;
import eu.hcomb.rrouter.service.impl.LocalRouterServiceImpl;
import eu.hcomb.rrouter.service.mapper.RouterMapper;

public class RRouterApp extends BaseApp<RRouterConfig> {
	
	public static void main(String[] args) throws Exception {
		new RRouterApp().run(args);
	}
	
	@Override
	public String getName() {
        return "rrouter-service";
    }

	public void configure(Binder binder) {
		configureSecurity(binder);
		configureEventEmitter(binder);

		binder
			.bind(LocalRouterService.class)
			.to(LocalRouterServiceImpl.class);

		binder
			.bind(RouterService.class)
			.to(LocalRouterServiceImpl.class);

		binder
			.bind(EventEmitter.class)
			.to(RedisEventEmitter.class);

		binder
			.bind(RedisService.class)
			.to(RedisServiceJedisImpl.class);

	}	

	@Override
	public void initialize(Bootstrap<RRouterConfig> bootstrap) {
		super.initialize(bootstrap);
		bootstrap.addBundle(new AssetsBundle("/assets", "/app", "index.html"));
	}
	
	@Override
	public void run(RRouterConfig configuration, Environment environment) {
		init(environment, configuration);
		
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
		environment.jersey().register(injector.getInstance(EndpointResource.class));
		environment.jersey().register(injector.getInstance(InstanceResource.class));
		
		environment.healthChecks().register("mysql", injector.getInstance(DatasourceHealthCheck.class));

		setUpSwagger(configuration, environment);
		
		setupExceptionMappers();
	}

	private void setupConfigurableJedis(RRouterConfig configuration, Environment environment) {
		LocalRouterService routerSvc = injector.getInstance(LocalRouterService.class);
		RedisPoolContainer redisPools = injector.getInstance(RedisPoolContainerImpl.class);
		List<RedisInstanceDTO> instances = routerSvc.getAllInstances();
		for (RedisInstanceDTO instance : instances) {
			redisPools.register(environment, instance);
		}
		
		List<RouteDTO> routes = routerSvc.getAllRoutes();
		for (RouteDTO route : routes) {
			InOut pattern = routerSvc.getAndRegisterPattern(injector, route);
			if(pattern==null){
				//log.info("ignoring route: " + route.getFrom().getType() + " -> " + route.getTo().getType());
			}else{
				environment.lifecycle().manage(pattern);
			}

		}
	}

	@Provides
	@Singleton
	public MetricRegistry getMetricsRegistry(){
		return environment.metrics();
	}
}