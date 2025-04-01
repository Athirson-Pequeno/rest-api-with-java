# REST API for Production Flow Management

[![Java](https://img.shields.io/badge/Java-17-blue.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.1-green.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

API RESTful para gerenciamento do fluxo de produção industrial, desenvolvida com Java Spring Boot. Oferece operações CRUD para diferentes entidades do processo produtivo com autenticação JWT.

## 📋 Sumário
- [Funcionalidades Principais](#-funcionalidades-principais)
- [Pré-requisitos](#-pré-requisitos)
- [Instalação](#-instalação)
- [Autenticação](#-autenticação)
- [Endpoints da API](#-endpoints-da-api)
- [Modelos de Dados](#-modelos-de-dados)
- [Exemplos de Uso](#-exemplos-de-uso)
- [Contribuição](#-contribuição)
- [Licença](#-licença)
- [Contato](#-contato)

## 🚀 Funcionalidades Principais
- Autenticação JWT com refresh token
- CRUD completo para:
  - Tipos de produtos
  - Setores de produção
  - Partes/components de produtos
  - Ordens de produção
  - Usuários com diferentes níveis de acesso
- Paginação e ordenação de resultados
- HATEOAS para navegação entre recursos
- Gestão de status de pedidos (Em Progresso, Recebido, Finalizado)

## 🛠️ Pré-requisitos
- Java 17+
- Maven 3.8+
- MySQL 8.0+
- Docker (opcional para ambiente de desenvolvimento)

## 📥 Instalação

# Clonar repositório
git clone https://github.com/Athirson-Pequeno/rest-api-with-java.git

# Configurar banco de dados (Docker)
docker-compose up -d


# Compilar e executar
mvn spring-boot:run

## 🔒 Autenticação
A API usa JWT para autenticação. Para obter um token:

1. Faça login na rota /auth/signin

```bash
curl -X POST "http://localhost:8080/auth/signin" \
-H "Content-Type: application/json" \
-d '{"email": "user@example.com", "password": "senha123"}
```
2. Use o token retornado no header das requisições:

```http
Authorization: Bearer <seu-token-jwt>
```

## 📡 Endpoints da API
### 🔧 Tipos de Produtos

Método|Endpoint|Descrição
| ------------- | ------------- |------------- |
GET	|/api/types/v1	|Listar todos tipos
POST|	/api/types/v1|	Criar novo tipo
GET|	/api/types/v1/{id}|	Buscar tipo por ID
PUT	|/api/types/v1/{id}	|Atualizar tipo
DELETE|	/api/types/v1/{id}|	Excluir tipo

### 🏭 Setores de Produção

Método|Endpoint|Descrição
| ------------- | ------------- |------------- |
GET|	/api/sectors/v1	|Listar todos setores
POST|	/api/sectors/v1	|Criar novo setor
GET|	/api/sectors/v1/{id}|	Buscar setor por ID
PUT	|/api/sectors/v1/{id}|	Atualizar setor
DELETE|	/api/sectors/v1/{id}|	Excluir setor

### 📦 Ordens de Produção
Método|Endpoint|Descrição
| ------------- | ------------- |------------- |
GET	|/api/orders/v1	|Listar todas ordens
POST|	/api/orders/v1|	Criar nova ordem
GET|	/api/orders/v1/{id}|	Buscar ordem por ID
PUT|	/api/orders/v1/{id}|	Atualizar ordem
DELETE	|/api/orders/v1/{id}|	Excluir ordem

Nota: Consulte a documentação Swagger completa para todos os endpoints e detalhes.

### 🗄️ Modelos de Dados Principais
Product: Representa um produto final com código, cores e partes componentes

Part: Componente individual de um produto, associado a setores e tipos

Order: Ordem de produção com status e itens associados

User: Usuário do sistema com diferentes níveis de acesso

Sector: Setor de produção responsável por partes específicas

### 💡 Exemplos de Uso
Criar novo produto:

```bash

curl -X POST "http://localhost:8080/api/products/v1" \
-H "Authorization: Bearer $TOKEN" \
-H "Content-Type: application/json" \
-d '{
  "name": "Smartphone X",
  "productCode": "SMX-2023",
  "colorCode": "#FF5733",
  "colorName": "Sunset Orange",
  "parts": [1, 2, 3]
}'
```

Criar ordem de produção:

```bash

curl -X POST "http://localhost:8080/api/orders/v1" \
-H "Authorization: Bearer $TOKEN" \
-H "Content-Type: application/json" \
-d '[{
  "productID": 1,
  "amount": 50
}]'
```

📄 Licença
Distribuído sob licença MIT. Veja LICENSE para mais informações.

📧 Contato
Athirson Pequeno - athirsondev@gmail.com


[![Repositório](https://img.shields.io/badge/Repositório-GitHub-black.svg)](https://github.com/Athirson-Pequeno/rest-api-with-java)
