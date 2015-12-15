package eu.hcomb.rrouter;

import io.dropwizard.lifecycle.Managed;
import io.dropwizard.setup.Environment;

import java.util.List;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Module;

import eu.hcomb.common.redis.JedisModule;
import eu.hcomb.common.resources.WhoAmI;
import eu.hcomb.common.web.BaseApp;
import eu.hcomb.rrouter.pattern.IntegrationPattern;
import eu.hcomb.rrouter.pattern.Parser;
import eu.hcomb.rrouter.resources.PatternsResource;

public class RRouterApp extends BaseApp<RRouterConfig> {
	
	public static void main(String[] args) throws Exception {
		new RRouterApp().run(args);
	}
	
	public void configure(Binder binder) {
		configureSecurity(binder);
		
	}	

	@Override
	public void run(RRouterConfig configuration, Environment environment) {
		
		Module jedis = new JedisModule(configuration, environment);
		
		injector = Guice.createInjector(this, jedis);
		
		defaultConfig(environment, configuration);
        
		environment.jersey().register(injector.getInstance(WhoAmI.class));
		
		List<IntegrationPattern> patterns = Parser.parse(injector, configuration);
		environment.jersey().register(
				injector.getInstance(PatternsResource.class)
					.setConfig(configuration.getRouterConfig())
					.setPatterns(patterns)
			);

		for (Managed pattern : patterns)
			environment.lifecycle().manage(pattern);
		
		setUpSwagger(configuration, environment);
	}

}