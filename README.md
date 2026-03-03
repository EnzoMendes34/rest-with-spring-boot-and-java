
# 🚀 REST API with Spring Boot and Java

<p>
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white"/>
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white"/>
  <img src="https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white"/>
  <img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white"/>
  <img src="https://img.shields.io/badge/Flyway-CC0200?style=for-the-badge&logo=flyway&logoColor=white"/>
</p>

API RESTful desenvolvida com Spring Boot para explorar os conceitos essenciais do REST, com foco em boas práticas, segurança e qualidade de código.

---

## 📋 Funcionalidades

- ✅ CRUD completo de Pessoas e Livros
- ✅ Autenticação e autorização com **JWT**
- ✅ Paginação, filtros e busca por nome
- ✅ Upload e download de arquivos (single e múltiplos)
- ✅ Exportação de dados em **.CSV**, **.XLSX** e **.PDF**
- ✅ Importação de pessoas via **.CSV** ou **.XLSX**
- ✅ Envio de e-mails simples e com anexo
- ✅ Disable de entidade sem exclusão (PATCH)
- ✅ Testes unitários e de integração
- ✅ Documentação com **Swagger / OpenAPI**
- ✅ Suporte a JSON, XML e YAML nos endpoints

---

## 🛠️ Tecnologias

| Tecnologia | Versão |
|------------|--------|
| Java | 17+ |
| Spring Boot | 3.x |
| Spring Security | 6.x |
| Hibernate / JPA | - |
| MySQL | 8.x |
| JWT (jjwt) | - |
| Flyway | - |
| Swagger / OpenAPI | 3.x |
| Maven | - |

---

## ⚙️ Como rodar o projeto

### Pré-requisitos

- Java 17+
- Maven
- MySQL rodando localmente

### Passo a passo

```bash
# Clone o repositório
git clone https://github.com/EnzoMendes34/rest-with-spring-boot-and-java.git

# Entre na pasta
cd rest-with-spring-boot-and-java
```

Configure o arquivo `src/main/resources/application.yml` com suas informações:

```yaml
cors:
  originPatterns: http://localhost:8080,http://localhost:3000

file:
  upload-dir: /caminho/para/seu/diretorio/upload

security:
  jwt:
    token:
      secret-key: sua_chave_secreta
      expire-lenght: 3600000

email:
  subject: "Default Subject"
  message: "Default Message"

spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/rest_with_spring_boot_and_java?useTimezone=true&serverTimezone=UTC
    username: seu_usuario
    password: sua_senha
  jpa:
    hibernate:
      ddl-auto: none
    show-sql: false
  mail:
    host: smtp.gmail.com
    port: 587
    username: ${EMAIL_USERNAME}
    password: ${EMAIL_PASSWORD}
    properties:
      mail.smtp.auth: true
      mail.smtp.starttls.enabled: true
  servlet:
    multipart:
      enabled: true
      max-file-size: 200MB
      max-request-size: 215MB
  flyway:
    baseline-on-migrate: true
```

> ⚠️ As variáveis `EMAIL_USERNAME` e `EMAIL_PASSWORD` devem ser configuradas como variáveis de ambiente na sua máquina. Nunca commite credenciais reais no repositório.

```bash
# Rode o projeto
mvn spring-boot:run
```

A API estará disponível em `http://localhost:8080`

A documentação Swagger estará disponível em `http://localhost:8080/swagger-ui/index.html`

---

## 🔐 Autenticação

A API utiliza **JWT** para autenticação. Para acessar os endpoints protegidos:

1. Crie um usuário em `POST /auth/signup`
2. Faça login em `POST /auth/signin` e copie o token retornado
3. Envie o token no header de todas as requisições:

```
Authorization: Bearer {seu_token}
```

---

## 📡 Endpoints

### 🔑 Auth
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/auth/signin` | Login e geração do token JWT |
| POST | `/auth/refresh/{username}` | Refresh do token |
| POST | `/auth/signup` | Cadastro de novo usuário |

### 👤 Pessoas
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/person/v1` | Lista todas as pessoas (paginado) |
| GET | `/api/person/v1/{id}` | Busca pessoa por ID |
| GET | `/api/person/v1/findPeopleByName/{firstName}` | Busca pessoas por nome (paginado) |
| GET | `/api/person/v1/exportPage` | Exporta lista de pessoas (.CSV, .XLSX ou .PDF) |
| GET | `/api/person/v1/export/{id}` | Exporta dados de uma pessoa (.PDF) |
| POST | `/api/person/v1` | Cria nova pessoa |
| POST | `/api/person/v1/createMultiplePeople` | Importa pessoas via arquivo .CSV ou .XLSX |
| PUT | `/api/person/v1` | Atualiza pessoa |
| PATCH | `/api/person/v1/{id}` | Desativa uma pessoa |
| DELETE | `/api/person/v1/{id}` | Remove pessoa |

### 📚 Livros
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/book/v1` | Lista todos os livros (paginado) |
| GET | `/api/book/v1/{id}` | Busca livro por ID |
| POST | `/api/book/v1` | Cria novo livro |
| PUT | `/api/book/v1` | Atualiza livro |
| DELETE | `/api/book/v1/{id}` | Remove livro |

### 📁 Arquivos
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/file/v1/uploadFile` | Upload de um arquivo |
| POST | `/api/file/v1/uploadMultipleFiles` | Upload de múltiplos arquivos |
| GET | `/api/file/v1/downloadFile/{fileName}` | Download de arquivo |

### 📧 E-mail
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/email/v1` | Envia e-mail simples |
| POST | `/api/email/v1/withAttachment` | Envia e-mail com anexo |

---

## 🧪 Testes

```bash
# Rodar todos os testes
mvn test
```

---

## 👨‍💻 Autor

Feito por **Enzo Mendes**

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/enzo-mendes-49896b285)
[![GitHub](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/EnzoMendes34)
