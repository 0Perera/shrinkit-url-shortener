# ShrinkIt - Encurtador de URLs

Um projeto pessoal desenvolvido para colocar em prática e aprofundar os conhecimentos em desenvolvimento Back-end utilizando **Java** e o ecossistema **Spring Boot**.

Esta é uma aplicação web de encurtamento de URLs construída com foco em arquitetura em camadas, cache de alta performance para os links mais acessados e boas práticas de engenharia de software.

---

## Objetivo do Projeto

O principal objetivo desta aplicação não é apenas ser mais um encurtador de links, mas sim servir como um laboratório para a aplicação de conceitos de arquitetura, cache e segurança exigidos pelo mercado.

**Principais tópicos estudados e aplicados:**
- Criação de APIs RESTful estruturadas
- Cache em memória com política de expiração e eviction (Caffeine)
- Rate limiting por IP com algoritmo de token bucket
- Tratamento global de exceções
- Validação de entrada com Jakarta Validation
- Testes unitários e de integração
- Mapeamento robusto entre Entidades e DTOs (MapStruct)

---

## Tecnologias e Ferramentas

- **Linguagem:** Java 21
- **Framework Principal:** Spring Boot 4.0.6
- **Persistência de Dados:** Spring Data JPA, Hibernate, PostgreSQL (produção) e H2 (desenvolvimento/testes)
- **Cache:** Spring Cache + Caffeine
- **Segurança:** Spring Security (boilerplate) + Bucket4j (rate limiting por IP)
- **Mapeamento de Objetos:** MapStruct (Entity ↔ DTO)
- **Redução de Boilerplate:** Lombok
- **Validação:** Jakarta Validation (`@NotBlank`, `@URL`)
- **Frontend:** Thymeleaf + Tailwind CSS
- **Testes:** JUnit 5, Mockito e Spring MockMvc

---

## Arquitetura e Boas Práticas

- **Arquitetura em Camadas:** Divisão clara entre `Controller` (camada web), `Service` (regras de negócio) e `Repository` (acesso a dados).
- **Padrão DTO:** Implementado com `Records` do Java para imutabilidade e tráfego seguro de dados, sem expor entidades do banco na web.
- **Cache de leitura:** `ShortenedUrlService.findUrlByHash()` é anotado com `@Cacheable` no cache `urls`, evitando consultas repetidas ao banco para os hashes mais acessados (máximo 20 entradas, TTL de 10 minutos, eviction LRU).
- **Rate Limiting por IP:** O `IpRateLimitInterceptor` mantém um `Bucket` (Bucket4j) por endereço IP, com capacidade de 20 tokens e reabastecimento de 1 token a cada 40 segundos. Excedido o limite, responde `429 Too Many Requests`.
- **Tratamento Global de Exceções:** O `@RestControllerAdvice` (`GlobalExceptionHandler`) mapeia exceções de domínio para respostas HTTP corretas — `NotFoundException` → 404, falhas de validação (`MethodArgumentNotValidException`) → 400.
- **Conformidade REST:** Verbos HTTP corretos e status codes assertivos (`201 Created`, `302 Found`).
- **Cobertura de Testes:** `@WebMvcTest` para controllers, `@DataJpaTest` para repositórios e `@MockitoBean` para isolar a unidade testada nos testes de serviço, com H2 em memória.

---

## Endpoints da API

| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/shorten` | Recebe uma URL e retorna a versão encurtada |
| `GET` | `/{hash}` | Redireciona (`302`) para a URL original associada ao hash |

**Body de encurtamento (`POST /shorten`):**
```json
{ "url": "https://exemplo.com/caminho-bem-longo" }
```
**Resposta (`201 Created`):**
```json
{ "url": "https://exemplo.com/caminho-bem-longo", "hashUrl": "aB3xZ9" }
```

---

## Como rodar o projeto localmente

### Pré-requisitos
- Java 21+
- Maven (opcional — o projeto usa o wrapper `mvnw`)
- PostgreSQL (apenas para rodar em modo produção — o ambiente de desenvolvimento usa H2 em memória)

### Passos

**1.** Clone o repositório:
```bash
git clone https://github.com/0Perera/shrinkit-url-shortener
```

**2.** (Produção) Configure as variáveis de ambiente:

| Variável | Descrição |
|----------|-----------|
| `DB_URL` | URL JDBC do PostgreSQL (ex: `jdbc:postgresql://localhost:5432/shrinkit`) |
| `DB_USER` | Usuário do banco |
| `DB_PASSWORD` | Senha do banco |

**3.** Inicie a aplicação:
```bash
./mvnw spring-boot:run
```

**4.** A aplicação estará disponível em `http://localhost:8080` e o H2 Console em `http://localhost:8080/h2-console`.

---

## Como rodar os testes

Os testes usam o perfil `test` com H2 em memória — nenhuma variável de ambiente de banco é necessária.

```bash
# Todos os testes
./mvnw test

# Uma classe específica
./mvnw test -Dtest=ShortenedUrlServiceTest
```

---

Feito para aprimoramento técnico de Back-end com Java.