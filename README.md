# Workout API

API REST para gerenciamento de usuários, exercícios e planos de treino personalizados. Projeto desenvolvido no contexto do curso **Santander Bootcamp 2026 - AI Java Back-end**, com foco em **Padrões de Projeto (Design Patterns)** aplicados na prática com **Java 21** e **Spring Boot**.

---

## Descrição do Desafio

Criar ou replicar um projeto e identificar os Padrões de Projeto (_Design Patterns_) empregados em seu funcionamento.

### O que é este projeto?

Este projeto é a evolução de uma API feita em caráter de aprendizado pessoal, devidamente aprimorado e pensado para o contexto do Santander Bootcamp 2026 - AI Java Back-end.



### Abordagem escolhida neste projeto

Este repositório busca **evoluir uma API**: partindo de um projeto de treinos de academia em Spring Boot, o mesmo foi aprimorado com:

- Autenticação **JWT** e criptografia de senhas com **BCrypt**
- Fluxo de **recuperação de senha** com envio de e-mail
- **Testes de integração** automatizados
- **Collection Postman** para validação manual dos endpoints
- Documentação interativa com **Swagger / OpenAPI**
- Arquitetura em camadas alinhada aos padrões ensinados no curso

---

## Sobre a API

A **Workout API** permite que usuários se cadastrem, façam login, gerenciem um catálogo de exercícios, montem planos de treino e gerem uma **ficha de treino** com repetições calculadas de acordo com o nível de condicionamento físico (`SEDENTARY`, `FIT`, `ATHLETE`).

### Funcionalidades principais

- Cadastro, atualização e exclusão de usuários
- Login com JWT
- Esqueci minha senha / reset de senha por e-mail
- CRUD completo de exercícios
- CRUD de planos de treino com associação de exercícios
- Geração de ficha de treino personalizada

---

## Tecnologias

| Tecnologia | Uso |
|---|---|
| Java 21 | Linguagem |
| Spring Boot 3.5 | Framework |
| Spring Security + JWT | Autenticação |
| Spring Data JPA | Persistência |
| H2 (in-memory) | Banco de dados |
| BCrypt | Hash de senhas |
| Spring Mail | Envio de e-mails |
| Springdoc OpenAPI | Documentação Swagger |
| JUnit + MockMvc | Testes de integração |
| Lombok | Redução de boilerplate |
| Maven | Build e dependências |

---

## Padrões de Projeto identificados

Os padrões abaixo aparecem de forma **explícita** no código ou **implicitamente** via convenções do Spring Framework.

### Padrões estruturais

| Padrão | Onde aparece | Papel |
|---|---|---|
| **Facade** | `UserService`, `ExerciseService`, `WorkoutPlanService`, `AuthService` | Simplificam operações complexas (persistência, regras de negócio, e-mail) atrás de uma interface coesa |
| **Repository** | `UserRepository`, `ExerciseRepository`, `WorkoutPlanRepository` | Abstraem o acesso a dados; o Spring Data gera a implementação |
| **DTO (Data Transfer Object)** | `*RequestDTO`, `*ResponseDTO` | Separam o contrato da API das entidades JPA |
| **Mapper** | `UserMapper`, `ExerciseMapper`, `WorkoutPlanMapper` | Convertem entre entidades e DTOs |

### Padrões comportamentais

| Padrão | Onde aparece | Papel |
|---|---|---|
| **Strategy (conceitual)** | `WorkoutPlanService.calculateReps()` com `switch` em `FitnessLevel` | A lógica de repetições muda conforme o nível do usuário |
| **Template Method (conceitual)** | `JwtAuthenticationFilter` estende `OncePerRequestFilter` | O Spring define o esqueleto do filtro; a lógica JWT fica na subclasse |
| **Chain of Responsibility (conceitual)** | Cadeia de filtros do Spring Security | Cada filtro decide se processa ou repassa a requisição |

### Padrões criacionais

| Padrão | Onde aparece | Papel |
|---|---|---|
| **Singleton** | Beans gerenciados pelo Spring (`@Service`, `@Component`, `@Configuration`) | O container garante uma única instância por escopo |
| **Factory Method (conceitual)** | `@Bean` em `SecurityConfig` e `OpenAPpiConfig` | Métodos factory criam objetos configurados (`SecurityFilterChain`, `OpenAPI`) |
| **Builder (conceitual)** | `Jwts.builder()` em `JwtService` | Construção fluente do token JWT |

### Outros conceitos relevantes

- **MVC** — Controllers recebem requisições, Services aplicam regras, Repositories persistem dados
- **Dependency Injection** — Injeção via construtor (`@RequiredArgsConstructor`) e `@Autowired`
- **Value Object** — Classe utilitária `Email` para validação de e-mail

---

## Pré-requisitos

- **Java 21**
- **Maven** (ou use o wrapper `./mvnw` incluso no projeto)
- **Postman** (opcional, para testes manuais)
- **Docker** (opcional, apenas para rodar o [Mailpit](https://github.com/axllent/mailpit) e testar e-mails localmente)

---

## Como executar

### 1. Clone o repositório

```bash
git clone <url-do-repositorio>
cd Workout-Api
```

### 2. Configure as variáveis de ambiente

Copie o arquivo de exemplo e ajuste os valores:

```bash
cp .env.example .env
```

| Variável | Descrição | Padrão |
|---|---|---|
| `JWT_SECRET` | Chave secreta do JWT (mín. 32 caracteres) | obrigatório em produção |
| `JWT_EXPIRATION` | Tempo de expiração do token em ms | `86400000` (24h) |
| `MAIL_HOST` | Servidor SMTP | `localhost` |
| `MAIL_PORT` | Porta SMTP | `1025` |
| `MAIL_FROM` | Remetente dos e-mails | `noreply@workout-api.local` |


### 3. Suba a aplicação

```bash
# Windows
.\mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```

A API ficará disponível em `http://localhost:8080`.

### 4. (Opcional) Suba o Mailpit para testar e-mails

```bash
docker run -d --name mailpit -p 1025:1025 -p 8025:8025 axllent/mailpit
```

Abra `http://localhost:8025` para visualizar os e-mails de reset de senha.

> A API **não depende de Docker** para funcionar. O Mailpit é apenas uma ferramenta de desenvolvimento para visualizar e-mails localmente.

---

## Documentação da API

Com a aplicação rodando, acesse:

| Recurso | URL |
|---|---|
| Swagger UI | http://localhost:8080/swagger-ui/index.html |
| OpenAPI JSON | http://localhost:8080/v3/api-docs |
| H2 Console | http://localhost:8080/h2-console |

Credenciais do H2: JDBC URL `jdbc:h2:mem:workoutdb`, usuário `sa`, senha vazia.

### Como testar pelo Swagger

1. Abra o **Swagger UI** no link acima
2. Execute `POST /auth/login` com e-mail e senha de um usuário cadastrado
3. Copie o `accessToken` da resposta
4. Clique em **Authorize** (cadeado no topo), cole o token e confirme
5. Teste os endpoints protegidos (Exercises, Workout Plans, etc.)

> **Dica:** se o Swagger não carregar ou exibir erro ao buscar `/v3/api-docs`, clique em **Authorize → Logout** para limpar um token expirado ou inválido e recarregue a página (`Ctrl + Shift + R`).

### Como testar pelo Swagger

1. Abra o **Swagger UI** no link acima
2. Execute `POST /auth/login` com e-mail e senha de um usuário cadastrado
3. Copie o `accessToken` da resposta
4. Clique em **Authorize** (cadeado no topo), cole o token e confirme
5. Teste os endpoints protegidos (Exercises, Workout Plans, etc.)

Endpoints públicos (cadastro, login, forgot/reset password) funcionam sem token. Os demais exigem o JWT no **Authorize**.

> **Dica:** se o Swagger não carregar ou exibir erro em `/v3/api-docs`, clique em **Authorize → Logout** para remover um token expirado ou inválido e recarregue a página (`Ctrl + Shift + R`).

---

## Endpoints

### Públicos (sem JWT)

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/users` | Cadastrar usuário |
| `POST` | `/auth/login` | Login (retorna JWT) |
| `POST` | `/users/forgot-password` | Solicitar reset de senha |
| `POST` | `/users/reset-password` | Alterar senha com token |

### Protegidos (requer `Authorization: Bearer <token>`)

| Método | Endpoint | Descrição |
|---|---|---|
| `PUT` | `/users/{id}` | Atualizar usuário |
| `DELETE` | `/users/{id}` | Excluir usuário |
| `GET` | `/exercises` | Listar exercícios |
| `GET` | `/exercises/{id}` | Buscar exercício |
| `POST` | `/exercises` | Criar exercício |
| `PUT` | `/exercises/{id}` | Atualizar exercício |
| `DELETE` | `/exercises/{id}` | Excluir exercício |
| `GET` | `/workout-plans` | Listar planos |
| `GET` | `/workout-plans/{id}` | Buscar plano |
| `POST` | `/workout-plans` | Criar plano |
| `PUT` | `/workout-plans/{id}` | Atualizar plano |
| `DELETE` | `/workout-plans/{id}` | Excluir plano |
| `POST` | `/workout-plans/{planId}/exercises` | Adicionar exercícios ao plano |
| `GET` | `/workout-plans/{planId}/sheet` | Gerar ficha de treino |

---

## Testes

### Testes automatizados (JUnit)

```bash
# Windows
.\mvnw.cmd test

# Linux / macOS
./mvnw test
```

Suíte de testes de integração:

- `AuthIntegrationTest` — login e proteção de rotas
- `UserIntegrationTest` — cadastro, update, forgot/reset password
- `ExerciseIntegrationTest` — CRUD de exercícios
- `WorkoutPlanIntegrationTest` — planos e ficha de treino

### Testes manuais (Postman)

Importe a collection em `postman/Workout-Api.postman_collection.json`.

**Ordem recomendada de execução:**

1. `01 - Auth`
2. `03 - Exercises`
3. `04 - Workout Plans`
4. `02 - Users` (cole o `resetToken` do Mailpit antes do reset de senha)

A collection gera automaticamente e-mail único no cadastro e encadeia variáveis (`token`, `userId`, `exerciseId`, `planId`).

---

## Estrutura do projeto

```
src/main/java/learning_api/workout_api/
├── config/              # Configurações (Security)
├── domain/
│   ├── user/            # Controller, DTOs, Entity, Mapper
│   ├── exercise/        # Controller, DTOs, Entity, Mapper
│   └── workoutplan/     # Controller, DTOs, Entity, Mapper
├── exception/           # Tratamento global de erros
├── repository/          # Interfaces JPA
├── security/            # JWT, filtros, UserDetails
└── service/             # Regras de negócio (Facade)
```

---

## Evoluções futuras (ideias)

- Implementar **Strategy** de forma explícita para cálculo de repetições
- Autorização por dono do recurso (usuário só acessa seus próprios dados)
- Endpoint `GET /users/me`
- Banco persistente (PostgreSQL) para ambiente de produção
- Refresh token

---

## Autor

Projeto desenvolvido como entrega do desafio de **Design Patterns** — Santander Bootcamp 2026 - AI Java Back-end.
