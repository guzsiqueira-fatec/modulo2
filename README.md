# Microservices API - Auth & Products

Sistema de microserviços com autenticação JWT, API Gateway e serviço de produtos.

## 📋 Arquitetura

```
┌─────────────┐     ┌─────────────┐     ┌─────────────────┐
│   Cliente   │────▶│   Gateway   │────▶│  Auth Service   │
│             │     │   :8080     │     │                 │
└─────────────┘     └─────────────┘     └─────────────────┘
                          │                     │
                          │              ┌──────┴──────┐
                          │              │  PostgreSQL │
                          ▼              └─────────────┘
                   ┌─────────────────┐
                   │ Product Service │
                   └─────────────────┘
```

## 🚀 Como Executar

```bash
docker-compose up --build
```

O Gateway estará disponível em `http://localhost:8080`

---

## 🔐 Auth Service

Serviço de autenticação com registro e login de usuários.

### Endpoints

| Método | Rota | Descrição | Autenticação |
|--------|------|-----------|--------------|
| `POST` | `/api/v1/auth/register` | Registro de novo usuário | ❌ Pública |
| `POST` | `/api/v1/auth/login` | Login de usuário | ❌ Pública |

### POST `/api/v1/auth/register`

Registra um novo usuário e retorna o token JWT.

**Request Body:**
```json
{
  "name": "string",
  "email": "string",
  "password": "string"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIs..."
}
```

### POST `/api/v1/auth/login`

Autentica um usuário existente e retorna o token JWT.

**Request Body:**
```json
{
  "email": "string",
  "password": "string"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIs..."
}
```

---

## 📦 Product Service

Serviço de produtos com rotas públicas e protegidas.

### Endpoints

| Método | Rota | Descrição | Autenticação |
|--------|------|-----------|--------------|
| `GET` | `/api/v1/products` | Lista produtos | ❌ Pública |
| `GET` | `/api/v1/products/orders` | Lista pedidos do usuário | ✅ Requer JWT |

### GET `/api/v1/products`

Lista todos os produtos. **Rota pública**, não requer autenticação.

**Response:**
```
PUBLIC: List of products from API v1
```

### GET `/api/v1/products/orders`

Lista os pedidos do usuário autenticado. **Rota protegida**, requer token JWT.

**Headers:**
```
Authorization: Bearer <token>
```

**Response:**
```
PROTECTED: Hey <userName>, here are the list of product orders from API v1
```

---

## 🛡️ Gateway - Configuração de Rotas

O Gateway utiliza Spring Cloud Gateway e um filtro JWT para controlar o acesso às rotas.

### Rotas Públicas (sem autenticação)
- `/api/v1/auth/**` - Todas as rotas de autenticação
- `/api/v1/products` - Apenas a listagem de produtos

### Rotas Protegidas (requer JWT)
- `/api/v1/products/orders` - Pedidos do usuário
- Qualquer outra rota não listada como pública

### Fluxo de Autenticação

1. O cliente faz login/register e recebe um token JWT
2. Para rotas protegidas, envia o token no header `Authorization: Bearer <token>`
3. O Gateway valida o token e extrai as claims
4. Se válido, o header `userName` é adicionado à request para os serviços downstream

---

## 🔧 Variáveis de Ambiente

| Variável | Serviço | Descrição |
|----------|---------|-----------|
| `AUTH_SECRET_KEY` | Gateway, Auth | Chave secreta para assinatura JWT |
| `DATABASE_URL` | Auth | URL de conexão do PostgreSQL |
| `DATABASE_USERNAME` | Auth | Usuário do banco de dados |
| `DATABASE_PASSWORD` | Auth | Senha do banco de dados |
| `AUTH_SERVICE_URL` | Gateway | URL do serviço de autenticação |
| `PRODUCT_SERVICE_URL` | Gateway | URL do serviço de produtos |

---

## 🧪 Exemplo de Uso

```bash
# 1. Registrar usuário
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name": "Gustavo", "email": "gustavo@email.com", "password": "senha123"}'

# 2. Fazer login
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "gustavo@email.com", "password": "senha123"}'

# 3. Acessar rota pública
curl http://localhost:8080/api/v1/products

# 4. Acessar rota protegida (substitua <TOKEN> pelo token recebido)
curl http://localhost:8080/api/v1/products/orders \
  -H "Authorization: Bearer <TOKEN>"
```
