package eu.hcomb.rrouter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import eu.hcomb.common.cors.CorsConfig;
import eu.hcomb.common.cors.CorsConfigurable;
import eu.hcomb.common.jdbc.JdbcConfig;
import eu.hcomb.common.jdbc.JdbcConfigurable;
import eu.hcomb.common.redis.JedisConfig;
import eu.hcomb.common.redis.JedisConfigurable;
import eu.hcomb.common.swagger.SwaggerConfig;
import eu.hcomb.common.swagger.SwaggerConfigurable;
import eu.hcomb.common.web.BaseConfig;

public class RRouterConfig extends BaseConfig implements CorsConfigurable, SwaggerConfigurable, JedisConfigurable, JdbcConfigurable {
	
	protected CorsConfig corsConfig = new CorsConfig();

	protected SwaggerConfig swaggerConfig = new SwaggerConfig();

    protected JedisConfig redis = new JedisConfig();

    protected JdbcConfig jdbcConfig = new JdbcConfig();

    public JdbcConfig getJdbcConfig() {
		return jdbcConfig;
	}

	protected List<Map<String,Object>> routerConfig = new ArrayList<Map<String,Object>>();
    
	public List<Map<String, Object>> getRouterConfig() {
		return routerConfig;
	}

	public JedisConfig getRedis() {
		return redis;
	}

	public SwaggerConfig getSwaggerConfig() {
		return swaggerConfig;
	}

	public CorsConfig getCorsConfig() {
		return corsConfig;
	}
	
}