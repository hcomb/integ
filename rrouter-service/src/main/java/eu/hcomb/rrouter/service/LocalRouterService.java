package eu.hcomb.rrouter.service;

import com.google.inject.Injector;

import eu.hcomb.rrouter.dto.RouteDTO;
import eu.hcomb.rrouter.pattern.InOut;

public interface LocalRouterService extends RouterService {

	public abstract InOut getAndRegisterPattern(Injector injector, RouteDTO route);
	
	public abstract void update(Long id, int x, int y);
}
