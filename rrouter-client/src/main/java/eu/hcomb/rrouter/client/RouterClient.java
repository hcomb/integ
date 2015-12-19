package eu.hcomb.rrouter.client;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import eu.hcomb.rrouter.dto.RedisInstanceDTO;
import eu.hcomb.rrouter.dto.RouteDTO;

@Singleton
public class RouterClient {

	@Inject
	private Client jerseyClient;
	
	@Inject
	@Named("rrouter.url")
	private String targetUrl;

	public void setJerseyClient(Client jerseyClient) {
		this.jerseyClient = jerseyClient;
	}

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
	
	public List<RedisInstanceDTO> getAllInstances(){
		Response response = jerseyClient.target(targetUrl).path("/instances").request().get();

        expect(response, new int[]{200,204});

        if(response.getStatus() == 204)
        	return new ArrayList<RedisInstanceDTO>();
        else
        	return response.readEntity(new GenericType<List<RedisInstanceDTO>>(){});
	}

	private void expect(Response response, int[] statusCode) {
		boolean found = false;
		for (int i = 0; i < statusCode.length; i++) {
			if(response.getStatus() == statusCode[i])
				found = true;
		}
		if(!found)
			throw new RuntimeException("expecting response status "+statusCode+" but got "+ response.getStatus());

	}
}
