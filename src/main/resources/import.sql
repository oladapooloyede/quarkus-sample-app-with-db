-- Sample data for products table
INSERT INTO products (id, name, description, price, quantity, created_at, updated_at) VALUES (nextval('products_seq'), 'Laptop', 'High-performance laptop with 16GB RAM', 1299.99, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO products (id, name, description, price, quantity, created_at, updated_at) VALUES (nextval('products_seq'), 'Wireless Mouse', 'Ergonomic wireless mouse with precision tracking', 29.99, 50, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO products (id, name, description, price, quantity, created_at, updated_at) VALUES (nextval('products_seq'), 'Mechanical Keyboard', 'RGB mechanical keyboard with cherry MX switches', 149.99, 25, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO products (id, name, description, price, quantity, created_at, updated_at) VALUES (nextval('products_seq'), 'USB-C Hub', '7-in-1 USB-C hub with HDMI and ethernet', 59.99, 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO products (id, name, description, price, quantity, created_at, updated_at) VALUES (nextval('products_seq'), 'External SSD', '1TB portable SSD with USB 3.2', 189.99, 15, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
