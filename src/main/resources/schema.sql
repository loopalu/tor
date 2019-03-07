DROP SCHEMA public CASCADE;
DROP SEQUENCE IF EXISTS seq1;
DROP TABLE IF EXISTS orderrows;
DROP TABLE IF EXISTS orders;

CREATE TABLE orders (
   id INT NOT NULL PRIMARY KEY,
   orderNumber VARCHAR(255) NOT NULL
);

CREATE TABLE orderrows (
   id int,
   orderNumber VARCHAR(255) NOT NULL,
   itemName VARCHAR(255) NOT NULL,
   quantity int NOT NULL,
   price int NOT NULL,
   FOREIGN KEY (id) REFERENCES orders(id)
);

CREATE SEQUENCE seq1 START WITH 1;
