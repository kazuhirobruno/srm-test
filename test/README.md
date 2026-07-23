# SRM Asset Test

## Visão Geral

Este projeto implementa uma API REST para gestão de recebíveis financeiros com precificação baseada em regras de risco via Strategy Pattern, cálculo de câmbio e liquidação transacional.

A solução foi construída com Spring Boot 4, Java 21, PostgreSQL, Flyway, Spring Data JPA e Springdoc OpenAPI.

## Requisitos Funcionais Principais

### 1. Gestão de Câmbio (Currency Engine)

- Armazenamento de taxas de câmbio entre moedas.
- Endpoint para cadastro manual de taxas de câmbio.
- Conversão aplicável no fluxo de precificação e liquidação.

### 2. Motor de Precificação (Strategy Pattern)

- Cada tipo de recebível aplica uma regra de risco diferente.
- Fórmula base utilizada:

  Valor Presente = Valor Face / (1 + Taxa Base + Spread)^Prazo

- Regras implementadas:
  - Duplicata Mercantil: spread de 1,5% a.m.
  - Cheque Pré-datado: spread de 2,5% a.m.
- Quando a moeda de pagamento é diferente da moeda original, a conversão cambial é aplicada ao valor final.

### 3. Persistência e Integridade

- Uso de PostgreSQL com Flyway para versionamento do schema.
- Liquidação financeira executada dentro de transação com lock pessimista para evitar inconsistências e race conditions.

### 4. API RESTful

- Endpoints organizados sob versionamento de API.
- Respostas com status HTTP semânticos.
- Documentação automática via Swagger/OpenAPI.

### 5. Consultas Analíticas

- Endpoint de extrato de liquidação com filtros por período, cedente e moeda.
- Paginação implementada para consultas de grande volume.

### 6. Arquitetura em Camadas

- Camada de controller: exposição de endpoints REST.
- Camada de service: regras de negócio e orquestração.
- Camada de repository: acesso a dados e consultas.

## Stack Tecnológica

- Java 21
- Spring Boot 4.1.0
- Spring Web MVC
- Spring Data JPA
- Flyway
- PostgreSQL
- Springdoc OpenAPI
- Lombok
- Maven

## Como Executar

### Pré-requisitos

- JDK 21
- Docker e Docker Compose
- Maven Wrapper

### Subindo o ambiente

```bash
docker compose up -d
./mvnw spring-boot:run
```

### Testes

```bash
./mvnw test
```

## Endpoints da API

Base URL: http://localhost:8080/api/v1

### 1. Recebíveis

#### Cadastrar recebível

- Método: POST
- URL: /api/v1/recebiveis/
- Descrição: Cadastra um novo recebível com status inicial PENDENTE.

Exemplo de corpo:

```json
{
  "valorOriginal": 1000.0,
  "prazo": 3,
  "tipo": "DUPLICATA",
  "moedaOriginal": "BRL",
  "cedente": "Empresa ABC"
}
```

#### Liquidar recebível

- Método: PATCH
- URL: /api/v1/recebiveis/{id}/liquidar
- Descrição: Liquida um recebível e aplica câmbio quando necessário.

Exemplo de corpo:

```json
{
  "moedaPagamento": "USD",
  "taxaBase": 0.08
}
```

#### Simular precificação

- Método: POST
- URL: /api/v1/recebiveis/simular
- Descrição: Simula a precificação de um recebível sem persistir dados.

Exemplo de corpo:

```json
{
  "valorOriginal": 1000.0,
  "prazo": 3,
  "tipo": "CHEQUE",
  "moedaOriginal": "BRL",
  "moedaPagamento": "USD",
  "taxaBase": 0.08,
  "cedente": "Empresa ABC"
}
```

### 2. Taxas de Câmbio

#### Cadastrar taxa de câmbio

- Método: POST
- URL: /api/v1/taxas-cambio
- Descrição: Registra uma nova cotação para um par de moedas.

Exemplo de corpo:

```json
{
  "moedaOrigem": "BRL",
  "moedaDestino": "USD",
  "fatorConversao": 0.18
}
```

### 3. Relatórios

#### Extrato de liquidação

- Método: GET
- URL: /api/v1/relatorios/extrato
- Descrição: Retorna um extrato paginado com filtros opcionais.

Parâmetros disponíveis:

- cedente: filtro parcial por nome do cedente
- moeda: filtro por moeda
- dataInicio: data inicial do período
- dataFim: data final do período
- page: número da página (início em 0)
- size: quantidade de registros por página

Exemplo:

```bash
curl "http://localhost:8080/api/v1/relatorios/extrato?cedente=Empresa&moeda=USD&page=0&size=10"
```

## Documentação Swagger

A documentação interativa da API pode ser acessada em:

- http://localhost:8080/swagger-ui.html
- http://localhost:8080/api-docs

## Modelo de Dados e Banco

O projeto utiliza Flyway para versionar o schema do banco. A estrutura inicial contempla tabelas para recebíveis e taxas de câmbio.

## Diagrama ER

```mermaid
er diagram
  recebiveis ||--o{ taxas_cambio : "usa"
  recebiveis {
    bigint id
    decimal valor_original
    int prazo
    string tipo
    string moeda_original
    string cedente
    string status
    decimal valor_liquidado
    string moeda_liquidacao
    timestamp created_at
    timestamp updated_at
  }

  taxas_cambio {
    bigint id
    string moeda_origem
    string moeda_destino
    decimal fator_conversao
    timestamp created_at
  }
```

## Scripts DDL

Arquivo de migração inicial:

- [src/main/resources/db/migration/V1\_\_criar_tabelas_iniciais.sql](src/main/resources/db/migration/V1__criar_tabelas_iniciais.sql)

```sql
CREATE TABLE recebiveis (
    id BIGSERIAL PRIMARY KEY,
    valor_face NUMERIC(18, 4) NOT NULL,
    prazo INT NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    moeda_original VARCHAR(10) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE taxas_cambio (
    id BIGSERIAL PRIMARY KEY,
    moeda_origem VARCHAR(10) NOT NULL,
    moeda_destino VARCHAR(10) NOT NULL,
    fator_conversao NUMERIC(18, 6) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_par_moedas UNIQUE (moeda_origem, moeda_destino)
);
```

Migração para adequação da tabela de recebíveis:

- [src/main/resources/db/migration/V2\_\_alterar_tabela_recebivel.sql](src/main/resources/db/migration/V2__alterar_tabela_recebivel.sql)

```sql
ALTER TABLE recebiveis RENAME COLUMN valor_face TO valor_original;

ALTER TABLE recebiveis
ADD COLUMN valor_liquidado NUMERIC(18, 4),
ADD COLUMN moeda_liquidacao VARCHAR(10),
ADD COLUMN updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP;
```

Migração complementar para o cedente:

- [src/main/resources/db/migration/V3\_\_adicionar_cedente_recebiveis.sql](src/main/resources/db/migration/V3__adicionar_cedente_recebiveis.sql)

```sql
ALTER TABLE recebiveis ADD COLUMN cedente VARCHAR(255);

UPDATE recebiveis
SET cedente = 'Cedente Não Informado'
WHERE cedente IS NULL;

ALTER TABLE recebiveis ALTER COLUMN cedente SET NOT NULL;
```

## Observações de Implementação

- A precificação faz uso do padrão Strategy para desacoplar a regra de risco do cálculo.
- A liquidação é transacional e utiliza lock pessimista para evitar inconsistências.
- O endpoint de relatório foi modelado para suportar filtragem e paginação em cenários com grande volume de dados.
