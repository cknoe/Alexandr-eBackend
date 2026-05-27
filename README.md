# Backend Alexandr-e

API REST développée avec Java et Spring Boot permettant la gestion d’utilisateurs, de cartes et de collections de cartes.

L’application utilise une authentification JWT afin que chaque utilisateur puisse uniquement accéder à ses propres ressources.

# Fonctionnalités

- Authentification JWT avec refresh token
- Gestion des utilisateurs
- Accès sécurisé aux ressources utilisateur
- Gestion de cartes
- Gestion de collections
- Extraction des métadonnées OpenGraph depuis une URL
- Intégration avec l’API Logo.dev

# Stack Technique

## Backend

- Java 17
- Spring Boot
- Spring Security (JWT Authentication)
- Spring Data JPA
- Hibernate
- Spring Web (REST API)

## Base de Données

- PostgreSQL
- H2 (tests)
- Hibernate
- Flyway (Versioning de BDD)

## Infrastructure

- Docker
- Docker Compose

## API Externes

- Logo.dev API (logo des sites)
- OpenGraph metadata extraction (Jsoup)

## Outils

- Maven
- Caffeine (cache)
- Jsoup (HTML parsing)

# Architecture

Le projet est structuré de la sorte :

- `config/` – configuration application (Caffeine cache, WebClient)
- `controller/` – endpoints REST
- `dto/` – objets request/response
- `entity/` – entité JPA
- `exception/` - exceptions personnalisées et gestion des exception (@ControllerAdvice)
- `service/` – logique métier
- `repository/` – Accès BDD (Spring Data JPA)
- `security/` – logique d'authentification et d'authorisation JWT, CORS

# Installation

## Pré-requis

- Docker + Docker Compose

Docker daemon doit être executé pour faire tourner la base de données :
- Mac/Windows: Docker Desktop
- Linux: Docker Engine (`systemctl start docker`)

## Variables d'environnement

Créer un fichier .env à la racine du projet sur le modèle de `.env.example` :

```sh
cp .env.example .env
```

(Optionnel) Remplir `LOGO_DEV_KEY` avec votre clef API
[Logo.dev](https://www.logo.dev)

# Run

```sh
docker compose up --build -d
```

# API documentation

Une fois l'application lancée :

Swagger UI : [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

# Authentification

## Login

Les utilisateurs peuvent s’authentifier via :

```http
POST /login
Content-Type: application/json

{
  "username": "<USERNAME>",
  "password": "<PASSWORD>"
}
```

Réponse :
- Retourne un token d'accès (JWT) nommé "token"
- Un cookie `refresh-token` est créé automatiquement

## Routes protégées

Toutes les routes protégées nécessitent un token JWT dans l’en-tête HTTP :

```http
Authorization: Bearer <access_token>
```

## Refresh-Token

Si le refresh-token (cookie HTTP-only) est présent et valide, il permet de générer un nouveau token d’accès sans se reconnecter.

```http
POST /refresh-token
```

Réponse :
- Retourne un nouveau token d’accès (JWT) nommé "token"

## Ajouts futurs

- Tests unitaires et intégration