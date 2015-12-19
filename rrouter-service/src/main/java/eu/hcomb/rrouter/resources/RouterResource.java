package eu.hcomb.rrouter.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;

import eu.hcomb.rrouter.dto.RouteDTO;
import eu.hcomb.rrouter.service.RouterService;

@Path("/routes")
@Produces(MediaType.APPLICATION_JSON)
public class RouterResource {

	@Inject
	protected RouterService routerService;
	
	@GET
	public List<RouteDTO> get(){
		return routerService.getAllRoutes();
	}



}
