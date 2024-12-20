# Étape 1 : Image de base
FROM openjdk:17-jdk-slim

# Étape 2 : Répertoire de travail dans le conteneur
WORKDIR /app

# Étape 3 : Copier le JAR généré
COPY target/GestionUser-0.0.1-SNAPSHOT.jar application.jar

# Étape 4 : Exposer le port utilisé par votre application
EXPOSE 8080

# Étape 5 : Commande pour lancer l'application
ENTRYPOINT ["java", "-jar", "application.jar"]
