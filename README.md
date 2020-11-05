"# poc_e-commerce" 


INSERT INTO test.`role` (id,name) VALUES (1,'ROLE_USER'), (2,'ROLE_ADMIN');

INSERT INTO test.`users` (password,username,role) VALUES
	 ('$2a$10$dvWuMcJddKu5i9g/rHyObORRziW3yteXotnwcJzD0oG7WCvVRxofW','ajifernandez','ROLE_USER'),
	 ('$2a$10$3T.6te2nLX4j3.FNbwis5OncbCao1e7B9aVvGCeoxwr/2FW901P0S','administrator','ROLE_ADMIN');
	 
INSERT INTO test.product (id, name,price) VALUES
	 (1,'product1',10.5),
	 (2,'product2',99.0),
	 (3,'product3',9.99),
	 (4,'product4',7.45),
	 (5,'product5',0.6);