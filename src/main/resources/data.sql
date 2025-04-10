INSERT INTO app_user (name, email, password, phone) VALUES
('Test Rider', 'testRider@gmail.com', 'Password', '+918085643273'),
('Test Driver', 'testDriver@gmail.com', '$2a$10$examplePasswordHash2', '+911234567890');


INSERT INTO user_entity_roles (user_entity_id, roles) VALUES
(1, 'RIDER'),
(2, 'RIDER'),
(2, 'DRIVER');

INSERT INTO rider (user_id, rating) VALUES
(1, 4.9);

INSERT INTO wallet (user_id, balance) VALUES
(1, 100),
(2, 500);

INSERT INTO driver (user_id, rating, available, current_location) VALUES
(2, 4.7, true, ST_GeomFromText('POINT(77.1025 28.7041)', 4326));
