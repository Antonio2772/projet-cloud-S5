CREATE DATABASE gestion_user;
use gestion_user;

CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT true,
    nom VARCHAR(255),
    prenom VARCHAR(255),
    password VARCHAR(255) NOT NULL,
    pin_code VARCHAR(10),
    pin_expiration DATETIME
);


insert into user (nom,prenom,email,password) VALUES ('nomena','andria','nomenaandria800@gmail.com','nomena');