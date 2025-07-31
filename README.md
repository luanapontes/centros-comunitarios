# API REST - Gerenciamento de Centros Comunitários

Este projeto é uma API REST desenvolvida em **Java + Spring Boot**, com persistência em **MongoDB**, criada para gerenciar centros comunitários em situações de emergência. A API permite o controle de ocupação, troca de recursos entre centros, geração de relatórios e alertas.

## ✅ Funcionalidades Implementadas

-  CRUD completo de **Centros Comunitários**
-  Atualização de **recursos** de um centro
-  Atualização da **ocupação atual**
-  Relatório de **ocupação geral** (`/centers/report/occupancy`)
-  Alerta de centros com **alta ocupação** (`/centers/notifications/occupancy-alert`)
-  Registro de **troca de recursos** entre centros (`/exchanges`)
-  Histórico de **trocas realizadas** (`/exchanges/history`)

## 🛠️ Tecnologias Utilizadas

- Java 17  
- Spring Boot 
- Maven  
- Swagger  
- JUnit + Mockito  
- Lombok  
- MongoDB
- Docker Compose

## 🧠 Ferramentas de Desenvolvimento
Este projeto foi acelerado com:

💡 GitHub Copilot: para sugestões de código inteligentes.

💬 ChatGPT: para auxílio em lógica.

## 🧪 Testes Unitários

O projeto possui testes unitários para os **services** e de integração para os **controllers** garantindo o funcionamento correto das funcionalidades principais.

## 🚀 Como Executar o Projeto

1. Clone o repositório:

git clone https://github.com/luanapontes/centros-comunitarios.git

2. Configure o MongoDB no arquivo **application.properties**:

spring.data.mongodb.uri=mongodb://localhost:27017/centrosdb

3. Rode o projeto com Maven:

mvnw spring-boot:run

## 🐳 Executando com Docker

1. Clone o repositório:

git clone https://github.com/luanapontes/centros-comunitarios.git

2. Suba os containers com Docker Compose:

docker-compose up --build

3. Para parar os containers, use:

docker-compose down

## 📄 Documentação da API

A documentação da API está disponível através do Swagger:

> 👉 [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
