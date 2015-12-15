package eu.hcomb.rrouter.pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.inject.Injector;

import eu.hcomb.rrouter.RRouterConfig;
import eu.hcomb.rrouter.pattern.impl.QueueToTopic;
import eu.hcomb.rrouter.pattern.impl.TopicToQueue;

public abstract class Parser {

	@SuppressWarnings("unchecked")
	public static List<IntegrationPattern> parse(Injector injector, RRouterConfig config){
		List<IntegrationPattern> ret = new ArrayList<IntegrationPattern>();
		
		List<Map<String,Object>> list = config.getRouterConfig();
		for (int i = 0; i < list.size(); i++) {
			Map<String,Object> item = list.get(i);
			String type = (String)item.get("type");
			if("QueueToTopic".equals(type)){
				InOut el = injector.getInstance(QueueToTopic.class);
				el.setId((String)item.get("id"));
				el.setOrigin((String)item.get("origin"));
				List<String> destination = (List<String>)item.get("destination");
				el.setDestination(destination.toArray(new String[]{}));
				ret.add(el);
			}
			if("TopicToQueue".equals(type)){
				InOut el = injector.getInstance(TopicToQueue.class);
				el.setId((String)item.get("id"));
				el.setOrigin((String)item.get("origin"));
				List<String> destination = (List<String>)item.get("destination");
				el.setDestination(destination.toArray(new String[]{}));
				ret.add(el);
			}
		}
				
		return ret;
		
	}
}
