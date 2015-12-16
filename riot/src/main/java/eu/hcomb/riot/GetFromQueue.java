package eu.hcomb.riot;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

public class GetFromQueue {

	public static void main(String[] args) {
		
		JedisPoolConfig poolConfig = new JedisPoolConfig();

		final AtomicLong counter = new AtomicLong(0);

		String host = args[1];
		Integer port = Integer.parseInt(args[2]);
		String queue = args[3];
		
		System.out.println(" * setting up connection: "+host+":"+port);
		JedisPool pool = new JedisPool(poolConfig, host, port, Protocol.DEFAULT_TIMEOUT, null);
		final Jedis jedis = pool.getResource();

		new Thread(new Runnable() {
			public void run() {
				while(true){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
					System.out.println(" * current size: " + counter.get());
				}
			}
		}).start();

		
		List<String> messages = null;
        while(true){
          messages = jedis.blpop(0,queue);
          String payload = messages.get(1);
          System.out.println(" * got msg from "+messages.get(0)+":" + payload);
          counter.incrementAndGet();
        }
        
	}
}
