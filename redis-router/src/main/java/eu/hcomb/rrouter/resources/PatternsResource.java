package eu.hcomb.rrouter.resources;

import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import eu.hcomb.rrouter.pattern.IntegrationPattern;

@Path("/patterns")
@Produces(MediaType.APPLICATION_JSON)
public class PatternsResource {

	protected List<Map<String,Object>> config;
	protected List<IntegrationPattern> patterns;
	
	public PatternsResource setPatterns(List<IntegrationPattern> patterns) {
		this.patterns = patterns;
		return this;
	}

	public PatternsResource setConfig(List<Map<String, Object>> config) {
		this.config = config;
		return this;
	}

	private IntegrationPattern getPattern(String id) {
		for(IntegrationPattern p : patterns)
			if(id.equals(p.getId()))
				return p;
		return null;
	}
	
	@GET
	public List<Map<String,Object>> get(){
		return config;
	}

	@GET
	@Path("{id}/stop")
	public boolean stop(@PathParam("id") String id) throws Exception {
		IntegrationPattern pattern = getPattern(id);
		if(pattern == null)
			return false;
		pattern.stop();
		return true;
	}

	@GET
	@Path("{id}/start")
	public boolean start(@PathParam("id") String id) throws Exception {
		IntegrationPattern pattern = getPattern(id);
		if(pattern == null)
			return false;
		pattern.start();
		return true;
	}

}
