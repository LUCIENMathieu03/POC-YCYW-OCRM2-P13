# POC Chat - Your Car Your Way

Preuve de concept d'une messagerie en direct entre un client et le service client, développée en Angular et Spring Boot. Objectif du POC : démontrer la faisabilité technique d'un chat en temps réel (WebSocket) respectant l'architecture déjà validée pour Your Car Your Way.

## Pré-requis

- **Angular 21**
- **Java 21**
- **Maven 3.8+**
- **PostgreSQL** (version 18 conseillée) installé et fonctionnel sur ta machine
- **Git**

## 1. Cloner le projet depuis GitHub

```
git clone <url-du-depot>
```

## 2. Installer le projet

Se rendre dans le dossier `front` puis installer :

```
npm install
```

Se rendre dans le dossier `back` puis installer :

```
mvn clean install
```

## 3. Créer la base de données

Crée une base PostgreSQL vide (le schéma des tables est généré automatiquement par Hibernate au premier démarrage du backend) :

```
createdb ycywDB
```

## 4. Configuration de l'environnement (.env)

Crée un fichier nommé `.env` à la racine du projet avec le contenu suivant :

```
DB_USERNAME=ton_nom_utilisateur_bdd
DB_PASSWORD=ton_mot_de_passe_bdd
DB_URL=jdbc:postgresql://localhost:5432/ycywDB
JWT_SECRET=une_chaine_de_caracteres_secrete_longue
SERVER_PORT=3001
BASE_URL=http://localhost:3001
```

| Variable      | Description                                                                                                        |
| ------------- | -------------------------------------------------------------------------------------------------------------------|
| `DB_USERNAME` | Identifiant de connexion à la base de données PostgreSQL.                                                          |
| `DB_PASSWORD` | Mot de passe de connexion à la base de données PostgreSQL.                                                         |
| `DB_URL`      | URL de connexion JDBC vers la base de données PostgreSQL.<br>Exemple : `jdbc:postgresql://localhost:5432/ycywDB`   |
| `JWT_SECRET`  | Clé secrète utilisée pour signer et vérifier les tokens JWT (garde-la confidentielle !).                           |
| `SERVER_PORT` | Port sur lequel le serveur backend écoute (ex. : `3001` pour accès à `http://localhost:3001`).                     |
| `BASE_URL`    | URL de base de l'API (doit correspondre au port configuré dans `SERVER_PORT`).                                     |

**Important** : ne commit jamais ce fichier `.env` dans le dépôt Git, car il contient des informations sensibles.

## 5. Lancer le backend

Depuis le dossier `back` :

```
mvn spring-boot:run
```

L'API sera disponible à : [http://localhost:3001](http://localhost:3001) (si tu utilises le port 3001).

## 6. Lancer le frontend

Depuis le dossier `front` :

```
ng serve
```

L'application sera disponible à : [http://localhost:4200](http://localhost:4200).

## 7. Créer des comptes de test

Le POC ne contient aucune donnée pré-remplie. Il faut créer au moins un compte client et un compte admin via l'API d'inscription, par exemple avec `curl` ou Postman :

```
curl -X POST http://localhost:3001/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"client@test.com","password":"test123","role":"client","name":"Client Test"}'

curl -X POST http://localhost:3001/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@test.com","password":"test123","role":"admin","name":"Admin Test"}'
```

Connecte-toi ensuite avec ces identifiants sur la page `/login` du frontend.

## 8. Tester le chat en direct

Pour observer le temps réel, ouvre deux **onglets** du navigateur (la session est stockée par onglet, pas besoin de deux navigateurs différents) :

- Un onglet connecté avec le compte **client** → redirige vers `/chat`.
- Un onglet connecté avec le compte **admin** → redirige vers `/admin/conversations`, clique sur le client pour ouvrir sa conversation.

Les messages envoyés d'un côté doivent apparaître instantanément de l'autre, sans recharger la page.

## Périmètre du POC

Ce projet est volontairement minimal, conformément à l'objectif du POC (démontrer la faisabilité du chat, pas livrer une application complète) :

- Pas d'inscription complète, de réservation, de gestion de profil, etc. — seul le compte minimal nécessaire au chat est géré.
- Pas d'interface graphique soignée — le style est volontairement basique.
- Pas de Docker/CI/CD/monitoring — hors scope du POC.
- Le rôle (`client` ou `admin`) est fixé en base à la création du compte, jamais choisi côté frontend.

## Notes supplémentaires

- Gère tes secrets (`JWT_SECRET`, `DB_PASSWORD`) avec précaution.
- Pour toute modification, vérifie que `.env` n'est jamais ajouté au commit.
