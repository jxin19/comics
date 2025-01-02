INSERT INTO member (email, username, password)
SELECT 'test' || generate_series || '@test.com',
       'test' || generate_series,
       '$2a$10$uIW446bCsbJ4jwgEItSA8O2GW579seNiOJQ9/CBFGit3LgnDPEXde'
FROM generate_series(1, 30000);
