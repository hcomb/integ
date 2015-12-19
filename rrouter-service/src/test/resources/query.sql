-- list all endpoints
SELECT 
	instance.name AS instance,
	endpt.id AS id, endpt.type AS TYPE, endpt.rkey AS `key`
FROM
	routes_endpoints AS endpt
	LEFT OUTER JOIN redis_instances AS instance ON endpt.instance_id = instance.id
	
-- get routes
SELECT
	from_instance.name AS from_instance_name,
	from_endpoint.id AS from_id, from_endpoint.rkey AS from_key, from_endpoint.type AS from_type,
	routes_links.active AS to_active, routes_links.event AS event_name,
	to_endpoint.id AS to_id, to_endpoint.rkey AS to_key, to_endpoint.type AS to_type,
	to_instance.name AS to_instance_name
FROM 	routes_endpoints AS from_endpoint
	LEFT OUTER JOIN redis_instances AS from_instance ON from_endpoint.instance_id = from_instance.id
	
	LEFT OUTER JOIN routes_links ON routes_links.id_from = from_endpoint.id
	
	LEFT OUTER JOIN routes_endpoints AS to_endpoint ON routes_links.id_to = to_endpoint.id
	LEFT OUTER JOIN redis_instances AS to_instance ON to_endpoint.instance_id = to_instance.id
WHERE 
	routes_links.active IS NOT NULL

INSERT INTO routes_links (id_from, id_to) VALUES(
	(SELECT id FROM routes_endpoints WHERE rkey = 'queue.debug'),
	(SELECT id FROM routes_endpoints WHERE rkey = 'log-collector')
);


INSERT INTO routes_links (id_from, id_to, `event`) VALUES(
	(SELECT id FROM routes_endpoints WHERE rkey = 'base-queue'),
	(SELECT id FROM routes_endpoints WHERE rkey = 'queue.debug'),
	'debug-message'
);

INSERT INTO routes_links (id_from, id_to) VALUES(
	(SELECT id FROM routes_endpoints WHERE rkey = 'log.topic'),
	(SELECT id FROM routes_endpoints WHERE rkey = 'log-collector')
);

INSERT INTO routes_links (id_from, id_to, `event`) VALUES(
	(SELECT id FROM routes_endpoints WHERE rkey = 'base-service'),
	(SELECT id FROM routes_endpoints WHERE rkey = 'log.topic'),
	'log'
);

INSERT INTO routes_links (id_from, id_to, `event`) VALUES(
	(SELECT id FROM routes_endpoints WHERE rkey = 'authn-service'),
	(SELECT id FROM routes_endpoints WHERE rkey = 'authz-service'),
	'login'
);

INSERT INTO routes_links (id_from, id_to) VALUES(
	(SELECT id FROM routes_endpoints WHERE rkey = 'error.alert'),
	(SELECT id FROM routes_endpoints WHERE rkey = 'mail-service')
);

INSERT INTO routes_links (id_from, id_to) VALUES(
	(SELECT id FROM routes_endpoints WHERE rkey = 'report-service'),
	(SELECT id FROM routes_endpoints WHERE rkey = 'error.alert')
);

INSERT INTO routes_links (id_from, id_to) VALUES(
	(SELECT id FROM routes_endpoints WHERE rkey = 'error.queue'),
	(SELECT id FROM routes_endpoints WHERE rkey = 'report-service')
);

INSERT INTO routes_links (id_from, id_to) VALUES(
	(SELECT id FROM routes_endpoints WHERE rkey = 'exception.queue'),
	(SELECT id FROM routes_endpoints WHERE rkey = 'report-service')
);


INSERT INTO routes_links (id_from, id_to, `event`) VALUES(
	(SELECT id FROM routes_endpoints WHERE rkey = 'base-service'),
	(SELECT id FROM routes_endpoints WHERE rkey = 'error.queue'),
	'throws-error'
);


INSERT INTO routes_links (id_from, id_to, `event`) VALUES(
	(SELECT id FROM routes_endpoints WHERE rkey = 'base-service'),
	(SELECT id FROM routes_endpoints WHERE rkey = 'exception.queue'),
	'throws-exception'
);

INSERT INTO routes_links (id_from, id_to) VALUES(
	(SELECT id FROM routes_endpoints WHERE rkey = 'status.topic'),
	(SELECT id FROM routes_endpoints WHERE rkey = 'redis-router')
);

INSERT INTO routes_links (id_from, id_to) VALUES(
	(SELECT id FROM routes_endpoints WHERE rkey = 'status.topic'),
	(SELECT id FROM routes_endpoints WHERE rkey = 'zookeeper-service')
);

INSERT INTO routes_links (id_from, id_to, `event`) VALUES(
	(SELECT id FROM routes_endpoints WHERE rkey = 'authn-service'),
	(SELECT id FROM routes_endpoints WHERE rkey = 'service.status'),
	'status.change'
);

INSERT INTO routes_links (id_from, id_to, `event`) VALUES(
	(SELECT id FROM routes_endpoints WHERE rkey = 'authz-service'),
	(SELECT id FROM routes_endpoints WHERE rkey = 'service.status'),
	'status.change'
);



INSERT INTO routes_links (id_from, id_to) VALUES(
	(SELECT id FROM routes_endpoints WHERE rkey = 'service.status'),
	(SELECT id FROM routes_endpoints WHERE rkey = 'status.topic')
);

INSERT INTO routes_links (id_from, id_to) VALUES(
	(SELECT id FROM routes_endpoints WHERE rkey = 'route.topic'),
	(SELECT id FROM routes_endpoints WHERE rkey = 'authn-service')
);

INSERT INTO routes_links (id_from, id_to) VALUES(
	(SELECT id FROM routes_endpoints WHERE rkey = 'route.topic'),
	(SELECT id FROM routes_endpoints WHERE rkey = 'authz-service')
);

INSERT INTO routes_links (id_from, id_to) VALUES(
	(SELECT id FROM routes_endpoints WHERE rkey = 'route.sync'),
	(SELECT id FROM routes_endpoints WHERE rkey = 'route.topic')
);

INSERT INTO routes_links (id_from, id_to, `event`) VALUES(
	(SELECT id FROM routes_endpoints WHERE rkey = 'redis-router'),
	(SELECT id FROM routes_endpoints WHERE rkey = 'route.sync'),
	'route.status'
);

INSERT INTO routes_links (id_from, id_to, `event`) VALUES(
	(SELECT id FROM routes_endpoints WHERE rkey = 'authn-service'),
	(SELECT id FROM routes_endpoints WHERE rkey = 'login.success'),
	'login.ok'
);

INSERT INTO routes_links (id_from, id_to) VALUES(
	(SELECT id FROM routes_endpoints WHERE rkey = 'exception.mapper'),
	(SELECT id FROM routes_endpoints WHERE rkey = 'log.event')
);

INSERT INTO routes_links (id_from, id_to) VALUES(
	(SELECT id FROM routes_endpoints WHERE rkey = 'log.dispatcher.1'),
	(SELECT id FROM routes_endpoints WHERE rkey = 'log.analyzer.1')
);
INSERT INTO routes_links (id_from, id_to) VALUES(
	(SELECT id FROM routes_endpoints WHERE rkey = 'log.dispatcher.1'),
	(SELECT id FROM routes_endpoints WHERE rkey = 'log.analyzer.2')
);
INSERT INTO routes_links (id_from, id_to) VALUES(
	(SELECT id FROM routes_endpoints WHERE rkey = 'log.dispatcher.2'),
	(SELECT id FROM routes_endpoints WHERE rkey = 'log.analyzer.3')
);
INSERT INTO routes_links (id_from, id_to) VALUES(
	(SELECT id FROM routes_endpoints WHERE rkey = 'log.dispatcher.2'),
	(SELECT id FROM routes_endpoints WHERE rkey = 'log.analyzer.4')
);


INSERT INTO routes_links (id_from, id_to) VALUES(
	(SELECT id FROM routes_endpoints WHERE rkey = 'log.event'),
	(SELECT id FROM routes_endpoints WHERE rkey = 'log.dispatcher.1')
);

INSERT INTO routes_links (id_from, id_to) VALUES(
	(SELECT id FROM routes_endpoints WHERE rkey = 'log.event'),
	(SELECT id FROM routes_endpoints WHERE rkey = 'log.dispatcher.2')
);

INSERT INTO routes_links (id_from, id_to) VALUES(
	(SELECT id FROM routes_endpoints WHERE rkey = 'role.svc'),
	(SELECT id FROM routes_endpoints WHERE rkey = 'role.new')
);

INSERT INTO routes_links (id_from, id_to) VALUES(
	(SELECT id FROM routes_endpoints WHERE rkey = 'role.new'),
	(SELECT id FROM routes_endpoints WHERE rkey = 'role.topic.new')
);

INSERT INTO routes_links (id_from, id_to) VALUES(
	(SELECT id FROM routes_endpoints WHERE rkey = 'user.update'),
	(SELECT id FROM routes_endpoints WHERE rkey = 'user.topic.update')
);


INSERT INTO routes_links (id_from, id_to) VALUES(
	(SELECT id FROM routes_endpoints WHERE rkey = 'user.delete'),
	(SELECT id FROM routes_endpoints WHERE rkey = 'user.topic.delete')
);
	
	
INSERT INTO routes_links (id_from, id_to) VALUES(
	(SELECT id FROM routes_endpoints WHERE rkey = 'user.svc'),
	(SELECT id FROM routes_endpoints WHERE rkey = 'user.delete')
);

INSERT INTO routes_links (id_from, id_to) VALUES(
	(SELECT id FROM routes_endpoints WHERE rkey = 'user.svc'),
	(SELECT id FROM routes_endpoints WHERE rkey = 'user.update')
);

INSERT INTO routes_links (id_from, id_to) VALUES(
	(SELECT id FROM routes_endpoints WHERE rkey = 'user.svc'),
	(SELECT id FROM routes_endpoints WHERE rkey = 'user.new')
);	

INSERT INTO routes_links (id_from, id_to) VALUES(
	(SELECT id FROM routes_endpoints WHERE rkey = 'user.new'),
	(SELECT id FROM routes_endpoints WHERE rkey = 'user.topic')
);

INSERT INTO routes_links (id_from, id_to) VALUES(
	(SELECT id FROM routes_endpoints WHERE rkey = 'user.topic'),
	(SELECT id FROM routes_endpoints WHERE rkey = 'welcome.mail')
);


INSERT INTO routes_links (id_from, id_to) VALUES(
	(SELECT id FROM routes_endpoints WHERE rkey = 'welcome.mail'),
	(SELECT id FROM routes_endpoints WHERE rkey = 'mail.svc')
);

INSERT INTO routes_links (id_from, id_to) VALUES(
	(SELECT id FROM routes_endpoints WHERE rkey = 'user.topic'),
	(SELECT id FROM routes_endpoints WHERE rkey = 'user.provision')
);

INSERT INTO routes_links (id_from, id_to) VALUES(
	(SELECT id FROM routes_endpoints WHERE rkey = 'user.provision'),
	(SELECT id FROM routes_endpoints WHERE rkey = 'provision.svc')
);

INSERT INTO routes_links (id_from, id_to) VALUES(
	(SELECT id FROM routes_endpoints WHERE rkey = 'user.topic'),
	(SELECT id FROM routes_endpoints WHERE rkey = 'user.hello')
);

INSERT INTO routes_links (id_from, id_to) VALUES(
	(SELECT id FROM routes_endpoints WHERE rkey = 'user.hello'),
	(SELECT id FROM routes_endpoints WHERE rkey = 'chat.svc')
);