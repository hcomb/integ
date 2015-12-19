package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

public class TestYAMLSerializer {

	public static void main(String[] args) throws Exception {
		
		YAMLMapper mapper = new YAMLMapper();
		List<Map<String,Object>> data = new ArrayList<Map<String,Object>>(){{
			add(new HashMap<String,Object>(){{
				put("type", "QueueToTopic");
				put("origin", "queue.q1");
				put("destination", new ArrayList<String>(){{
					add("topic.t1");
				}});
			}});
			
			add(new HashMap<String,Object>(){{
				put("type", "TopicToQueue");
				put("origin", "topic.t1");
				put("destination", new ArrayList<String>(){{
					add("queue.q2");
					add("queue.q3");
					add("queue.q4");
				}});
			}});
			
		}};
		
		mapper.writeValue(System.out, data);
		
	}
}
