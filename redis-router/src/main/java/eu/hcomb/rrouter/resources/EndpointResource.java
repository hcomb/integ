package eu.hcomb.rrouter.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Inject;

import eu.hcomb.rrouter.dto.EndpointDTO;
import eu.hcomb.rrouter.service.RouterService;

@Path("/endpoints")
@Produces(MediaType.APPLICATION_JSON)
public class EndpointResource {

	protected Log log = LogFactory.getLog(this.getClass());
	
	@Inject
	protected RouterService routerService;
	
	@GET
	public List<EndpointDTO> get(){
		return routerService.getAllEndpoints();
	}

	@POST
	@Path("/{nodeId}/{x}/{y}")
	public void updateCoords(@PathParam("nodeId") Long id, @PathParam("x") Integer x, @PathParam("y") Integer y){
		log.info("updateCoords("+id+","+x+","+y+")");
		routerService.update(id, x, y);
	}


}
