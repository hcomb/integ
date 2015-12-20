package eu.hcomb.rrouter.client;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import eu.hcomb.common.client.BaseClient;
import eu.hcomb.rrouter.dto.EndpointDTO;
import eu.hcomb.rrouter.dto.RedisInstanceDTO;
import eu.hcomb.rrouter.dto.RouteDTO;
import eu.hcomb.rrouter.service.RouterService;

@Singleton
public class RouterClient extends BaseClient implements RouterService {
	
	@Inject
	@Named("rrouter.url")
	private String targetUrl;

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	public List<RouteDTO> getAllRoutes(){
        
		Response response = jerseyClient.target(targetUrl).path("/routes").request().get();

        expect(response, new int[]{200,204});

        if(response.getStatus() == 204)
        	return new ArrayList<RouteDTO>();
        else
        	return response.readEntity(new GenericType<List<RouteDTO>>(){});

	}

	public List<EndpointDTO> getAllEndpoints() {
		Response response = jerseyClient.target(targetUrl).path("/endpoints").request().get();

        expect(response, new int[]{200,204});

        if(response.getStatus() == 204)
        	return new ArrayList<EndpointDTO>();
        else
        	return response.readEntity(new GenericType<List<EndpointDTO>>(){});
	}
	
	public List<RedisInstanceDTO> getAllInstances(){
		Response response = jerseyClient.target(targetUrl).path("/instances").request().get();

        expect(response, new int[]{200,204});

        if(response.getStatus() == 204)
        	return new ArrayList<RedisInstanceDTO>();
        else
        	return response.readEntity(new GenericType<List<RedisInstanceDTO>>(){});
	}

}
