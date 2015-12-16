package eu.hcomb.riot;

import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicLong;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

public class WriteToQueue {

	public static void main(String[] args) throws InterruptedException {

		JedisPoolConfig poolConfig = new JedisPoolConfig();

		SecureRandom random = new SecureRandom();
		
		final AtomicLong counter = new AtomicLong(0);
		
		String host = args[1];
		Integer port = Integer.parseInt(args[2]);
		String queue = args[3];
		String msg = args[4];
		Integer num = Integer.parseInt(args[5]);
		Integer timeout = Integer.parseInt(args[6]);

		System.out.println(" * setting up connection: " + host + ":" + port);
		JedisPool pool = new JedisPool(poolConfig, host, port, Protocol.DEFAULT_TIMEOUT, null);
		final Jedis jedis = pool.getResource();

		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
				System.out.println(" * current size: " + counter.get());
			}
		}).start();
		
		for (int i = 0; i < num; i++) {
			Thread.sleep(timeout);
			String data = msg.replace("RAND", "" + random.nextLong());
			jedis.rpush(queue, data);
			counter.incrementAndGet();
			System.out.println(" * written to " + queue + ":" + data);
		}
		System.out.println(" * final size: " + counter.get());

		jedis.close();
		pool.close();
		
		System.exit(0);

	}
}
