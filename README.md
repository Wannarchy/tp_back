#  tp_back — API REST Spring Boot

API REST de gestion de produits, commandes et utilisateurs avec authentification JWT.

---

vidéo : https://youtu.be/QoPwEdIPvto

##  Prérequis

Avant de commencer, assure-toi d'avoir installé :

- [Java JDK 17]
- [MySQL 8.0+]
- [IntelliJ IDEA]
- [Postman]

---

## Lancer le projet

### 1. Cloner le projet
```bash
git clone https://github.com/wannarchy/tp_back.git
```
Ouvre le dossier `tp_back` dans **IntelliJ IDEA**.

---

### 2. Démarrer MySQL
Ouvre **XAMPP** et démarre le service **MySQL**.

---

### 3. Configurer la base de données
Ouvre `src/main/resources/application.properties` et renseigne ton mot de passe MySQL 

> La base de données `tp_back` sera créée automatiquement au premier lancement.

---

### 4. Lancer l'application
Ouvre `TpBackApplication.java` et clique sur le bouton **▶️** en haut à droite.

Tu dois voir dans la console :
```
Tomcat started on port(s): 8080
Started TpBackApplication
```
ou autre cela sera ton PORT

L'API est prête sur **http://localhost:8080** 

---

## Utiliser l'API avec Postman

### Étape 1 — Créer un compte
```
POST http://localhost:8080/api/auth/register


---

### Étape 2 — Se connecter et récupérer le token
```
POST http://localhost:8080/api/auth/login

```
> 📋 Copie le `token` — tu en auras besoin pour les requêtes suivantes.

---

### Étape 3 — Utiliser le token
Dans **chaque requête protégée**, ajoute dans l'onglet **Headers** de Postman :
```
Authorization: Bearer <colle_ton_token_ici>
```

---

### Étape 4 — Passer en mode ADMIN (optionnel)
Par défaut, les comptes créés ont le rôle `USER`.
Pour accéder aux routes d'administration, exécute cette requête SQL :
```sql
UPDATE users SET roles = 'ADMIN' WHERE username = 'john';
```
Puis **reconnecte-toi** pour obtenir un nouveau token ADMIN.

---

##  Endpoints disponibles

###  Public (sans token)
| Méthode | URL | Description |
|---|---|---|
| POST | `/api/auth/register` | Créer un compte |
| POST | `/api/auth/login` | Se connecter |
| GET | `/api/products` | Liste des produits |
| GET | `/api/products/{id}` | Détail d'un produit |
| GET | `/api/products/search?name=xxx` | Rechercher un produit |

###  Authentifié (token requis)
| Méthode | URL | Description |
|---|---|---|
| POST | `/api/orders` | Créer une commande |
| GET | `/api/orders/my-orders` | Voir ses commandes |

###  ADMIN uniquement (token admin requis)
| Méthode | URL | Description |
|---|---|---|
| POST | `/api/admin/products` | Créer un produit |
| PUT | `/api/admin/products/{id}` | Modifier un produit |
| DELETE | `/api/admin/products/{id}` | Supprimer un produit |
| GET | `/api/admin/users` | Liste des utilisateurs |
| PUT | `/api/admin/users/{id}` | Modifier un utilisateur |
| DELETE | `/api/admin/users/{id}` | Supprimer un utilisateur |


====================================================
STRUCTURE DE LA BASE DE DONNÉES — tp_back
====================================================

BASE DE DONNÉES : tp_back

----------------------------------------------------
TABLE : users
----------------------------------------------------
| Colonne    | Type         | Contraintes          |
|------------|--------------|----------------------|
| id         | INT          | PK, AUTO_INCREMENT   |
| username   | VARCHAR(255) | NOT NULL, UNIQUE     |
| email      | VARCHAR(255) | NOT NULL, UNIQUE     |
| password   | VARCHAR(255) | NOT NULL             |
| roles      | ENUM         | 'USER', 'ADMIN'      |
| enabled    | BOOLEAN      | DEFAULT true         |
| created_at | DATETIME     |                      |

----------------------------------------------------
TABLE : categories
----------------------------------------------------
| Colonne | Type         | Contraintes           |
|---------|--------------|-----------------------|
| id      | INT          | PK, AUTO_INCREMENT    |
| name    | VARCHAR(255) | NOT NULL, UNIQUE      |

----------------------------------------------------
TABLE : products
----------------------------------------------------
| Colonne       | Type         | Contraintes          |
|---------------|--------------|----------------------|
| id            | INT          | PK, AUTO_INCREMENT   |
| name          | VARCHAR(255) | NOT NULL             |
| description   | TEXT         |                      |
| price         | FLOAT        | NOT NULL             |
| categorie_id  | INT          | FK -> categories(id) |
| stock_quantity| INT          | NOT NULL             |
| lien_image    | VARCHAR(255) |                      |
| created_at    | DATETIME     |                      |

----------------------------------------------------
TABLE : orders
----------------------------------------------------
| Colonne      | Type         | Contraintes          |
|--------------|--------------|----------------------|
| id           | INT          | PK, AUTO_INCREMENT   |
| user_id      | INT          | FK -> users(id)      |
| product_id   | INT          | FK -> products(id)   |
| order_date   | DATETIME     |                      |
| total_amount | FLOAT        | NOT NULL             |
| status       | ENUM         | 'PENDING'            |
|              |              | 'PROCESSING'         |
|              |              | 'SHIPPED'            |
|              |              | 'DELIVERED'          |
| quantite     | INT          | NOT NULL             |

----------------------------------------------------
RELATIONS
----------------------------------------------------

users ──────────────< orders
(1 utilisateur      (plusieurs commandes)
peut avoir
plusieurs commandes)

products ───────────< orders
(1 produit          (plusieurs commandes)
peut être dans
plusieurs commandes)

categories ─────────< products
(1 catégorie        (plusieurs produits)
peut avoir
plusieurs produits)

----------------------------------------------------
DONNÉES INITIALES (à insérer manuellement)
----------------------------------------------------

-- Créer une catégorie
INSERT INTO categories (name) VALUES ('Boulangerie');
INSERT INTO categories (name) VALUES ('Boissons');
INSERT INTO categories (name) VALUES ('Epicerie');

-- Passer un utilisateur en ADMIN
UPDATE users SET roles = 'ADMIN' WHERE username = 'john';
sauf si un admin est déjà existant 

====================================================