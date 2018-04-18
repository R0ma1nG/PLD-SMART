# Serveur

### Installation
1. Installer NodeJS dernière version **LTS**.
2. Aller dans le répertoire ```/server```, lancer la commande ```npm install``` pour installer les modules node.
### Lancement
1. ```node server.js``` pour lancer le serveur.
2. ```http://localhost:8080/``` adresse du serveur.

# MongoDB

1. Installer MongoDB, voir doc officielle : https://docs.mongodb.com/manual/administration/install-community/
2. **Penser à ajouter mongod et les autres commandes au path.**
3. ```mongod --dbpath <path to data folder>``` pour lancer le processus principal MongoDB. Succès si ```[initandlisten] waiting for connections on port 27017``` est la dernière ligne.
4. ```mongo --host localhost:27017``` pour lancer un terminal mongo.  Toutes les commandes sont ensuite tapées dans ce terminal (insert, update, find, ...).
