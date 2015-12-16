-- list all endpoints
SELECT 
	instance.name AS instance_name,
	endpt.type AS endpoint_type, endpt.rkey AS endpoint_key	
FROM
	routes_endpoints AS endpt
	LEFT OUTER JOIN redis_instances AS instance ON endpt.instance_id = instance.id
	
-- get routes
SELECT
	from_instance.name AS from_instance_name,
	from_endpoint.rkey AS from_key, from_endpoint.type AS from_type,
	routes_links.active AS to_active,
	to_endpoint.rkey AS to_key, to_endpoint.type AS to_type,
	to_instance.name AS to_instance_name
FROM 	routes_endpoints AS from_endpoint
	LEFT OUTER JOIN redis_instances AS from_instance ON from_endpoint.instance_id = from_instance.id
	
	LEFT OUTER JOIN routes_links ON routes_links.id_from = from_endpoint.id
	
	LEFT OUTER JOIN routes_endpoints AS to_endpoint ON routes_links.id_to = to_endpoint.id
	LEFT OUTER JOIN redis_instances AS to_instance ON to_endpoint.instance_id = to_instance.id
WHERE 
	routes_links.active IS NOT NULL
	
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