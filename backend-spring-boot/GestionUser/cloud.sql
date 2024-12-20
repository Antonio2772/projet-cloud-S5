CREATE TABLE role(
   id INT AUTO_INCREMENT,
   libelle VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id)
);

CREATE TABLE utilisateur(
   id INT AUTO_INCREMENT,
   nom VARCHAR(255)  NOT NULL,
   prenom VARCHAR(255)  NOT NULL,
   email VARCHAR(255)  NOT NULL,
   mot_de_passe VARCHAR(255)  NOT NULL,
   token VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id)
);

CREATE TABLE pin(
   id INT AUTO_INCREMENT,
   pin INT NOT NULL,
   id_1 INT NOT NULL,
   PRIMARY KEY(id),
   UNIQUE(id_1),
   FOREIGN KEY(id_1) REFERENCES utilisateur(id)
);

CREATE TABLE tentative(
   id INT AUTO_INCREMENT,
   nombre INT NOT NULL,
   id_1 INT NOT NULL,
   PRIMARY KEY(id),
   UNIQUE(id_1),
   FOREIGN KEY(id_1) REFERENCES utilisateur(id)
);

CREATE TABLE nombre_tentative(
   nombre_max INT,
   PRIMARY KEY(nombre_max)
);

CREATE TABLE duree_session(
   duree_max INT,
   PRIMARY KEY(duree_max)
);

CREATE TABLE duree_confirmation_pin(
   duree_max INT,
   PRIMARY KEY(duree_max)
);

insert into utilisateur (nom,prenom,email,mot_de_passe,token) VALUES ('nomena','andria','nomenaandria800@gmail.com','nomena','aaa');