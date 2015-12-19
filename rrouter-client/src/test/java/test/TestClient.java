package test;

import java.util.List;

import javax.ws.rs.client.Client;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.glassfish.jersey.client.JerseyClientBuilder;

import eu.hcomb.rrouter.client.RouterClient;
import eu.hcomb.rrouter.dto.RedisInstanceDTO;
import eu.hcomb.rrouter.dto.RouteDTO;

public class TestClient {

	public static void main(String[] args) throws Exception {
	
		Client jerseyClient = new JerseyClientBuilder().build();
		String targetUrl = "http://localhost:8480/rrouter/api";
		
		RouterClient client = new RouterClient();
		client.setJerseyClient(jerseyClient);
		client.setTargetUrl(targetUrl);

		List<RedisInstanceDTO> instances = client.getAllInstances();
		for (RedisInstanceDTO dto : instances) {
			System.out.println(ToStringBuilder.reflectionToString(dto,ToStringStyle.MULTI_LINE_STYLE));
		}
		System.out.println("===");
		List<RouteDTO> routes = client.getAllRoutes();
		for (RouteDTO dto : routes) {
			System.out.println("on('"+dto.getEventName()+"').from("
					+ToStringBuilder.reflectionToString(dto.getFrom(),ToStringStyle.SIMPLE_STYLE)
					+ ").to("+
					ToStringBuilder.reflectionToString(dto.getTo(),ToStringStyle.SIMPLE_STYLE)
					+ ")");
		}
		
	}
	
}
