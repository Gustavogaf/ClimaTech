# ClimaTech - Sistema de Gestão e Automação de Ar Condicionado

## 📖 Sobre o Projeto

O **ClimaTech** é uma aplicação back-end robusta, desenvolvida como parte de um projeto para o **Bacharelado em Sistemas de Informação no Instituto Federal de Sergipe**.

O objetivo principal do sistema é gerenciar e automatizar de forma inteligente o sistema de ar condicionado do instituto, permitindo não apenas o controle remoto dos equipamentos, mas também a análise de dados de sensores em tempo real para otimização de consumo e manutenção proativa.

---

## ✨ Funcionalidades Principais

O sistema atualmente suporta as seguintes funcionalidades:

* **Gestão de Estrutura Física:** CRUD completo para `Pavilhões` e `Salas`.
* **Gestão de Ativos:** CRUD completo para `Equipamentos` de ar condicionado, incluindo dados de rede como Endereço MAC e IP.
* **Gestão de Utilizadores:** CRUD completo para `Utilizadores`, com distinção entre tipos `ADM` e `COMUM`.
* **Controle Remoto:** Endpoints para `Ligar` e `Desligar` equipamentos, com registo de auditoria de uso.
* **Recolha de Telemetria:** Endpoint para receber dados de sensores de `temperatura`, `voltagem` e `amperagem`.
* **Análise de Métricas:** Endpoint que processa dados brutos e retorna informações úteis como estado atual, temperatura e consumo de um equipamento.
* **Sistema de Alertas:** CRUD completo para gerir os tipos de `Alertas` e `Falhas`, e um sistema que dispara eventos de alerta com base na análise de métricas.

---

## 🛠️ Tecnologias Utilizadas

A API foi construída utilizando as seguintes tecnologias:

* **Linguagem:** Java 17
* **Framework:** Spring Boot 3.3.2
* **Persistência:** Spring Data JPA (Hibernate)
* **Base de Dados (Produção):** Microsoft SQL Server
* **Base de Dados (Testes):** H2 Database
* **Gestão de Dependências:** Apache Maven
* **Utilitários:** Lombok

---

## ⚙️ Configuração e Execução

### Pré-requisitos

* JDK (Java Development Kit) - Versão 17 ou superior.
* Apache Maven 3.9 ou superior.
* Uma instância do PostgreSQL acessível.

### Configuração da Base de Dados

1.  Crie uma base de dados no seu PostgreSQL com o nome `ClimaTech_DB`.
2.  No diretório `src/main/resources/`, renomeie o ficheiro `application-dev.properties.example` (se existir) para `application-dev.properties`.
3.  Abra o ficheiro `application-dev.properties` e insira as suas credenciais de acesso à base de dados:

    ```properties
    # Configuração para o ambiente de desenvolvimento (PostgreSQL)
    spring.datasource.url=jdbc:postgresql://<HOST_DO_SEU_BANCO>:5432/ClimaTech_DB
    spring.datasource.username=<SEU_UTILIZADOR_POSTGRES>
    spring.datasource.password=<SUA_SENHA_POSTGRES>
    spring.datasource.driver-class-name=org.postgresql.Driver
    spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    ```

### Executando a Aplicação

1.  Abra um terminal na raiz do projeto.
2.  Execute o seguinte comando Maven para compilar o projeto e executar os testes:
    ```bash
    mvn clean install
    ```
3.  Para iniciar a aplicação, execute:
    ```bash
    mvn spring-boot:run
    ```
4.  A API estará disponível em `http://localhost:8080`.

---

## 📡 Endpoints da API

Abaixo está a documentação detalhada dos endpoints da API.

### Pavilhões

* **`GET /api/pavilhoes`**
    * Descrição: Lista todos os pavilhões cadastrados.
    * Resposta de Sucesso: `200 OK`

* **`GET /api/pavilhoes/{id}`**
    * Descrição: Busca um pavilhão específico pelo seu ID.
    * Resposta de Sucesso: `200 OK`

* **`POST /api/pavilhoes`**
    * Descrição: Cria um novo pavilhão.
    * Exemplo de Body:
        ```json
        {
          "nome": "Pavilhão de Aulas IV"
        }
        ```
    * Resposta de Sucesso: `201 Created`

* **`PUT /api/pavilhoes/{id}`**
    * Descrição: Atualiza um pavilhão existente.
    * Exemplo de Body:
        ```json
        {
          "nome": "Pavilhão de Aulas V"
        }
        ```
    * Resposta de Sucesso: `200 OK`

* **`DELETE /api/pavilhoes/{id}`**
    * Descrição: Deleta um pavilhão.
    * Resposta de Sucesso: `204 No Content`

### Salas

* **`POST /api/salas`**
    * Descrição: Cria uma nova sala, associando-a a um pavilhão existente.
    * Exemplo de Body:
        ```json
        {
          "nome": "Sala 12",
          "pavilhaoId": 1
        }
        ```
    * Resposta de Sucesso: `201 Created`

* **`PUT /api/salas/{id}`**
    * Descrição: Atualiza uma sala existente.
    * Exemplo de Body:
        ```json
        {
          "nome": "Sala 13 - Laboratório",
          "pavilhaoId": 1
        }
        ```
    * Resposta de Sucesso: `200 OK`

### Equipamentos

* **`POST /api/equipamentos`**
    * Descrição: Cria um novo equipamento, associando-o a uma sala.
    * Exemplo de Body:
        ```json
        {
          "tag": "AC-PAIV-S12-01",
          "macAddress": "00:1A:2B:3C:4D:5E",
          "ipAddress": "192.168.1.50",
          "marca": "Springer",
          "modelo": "Inverter 12000 BTU",
          "salaId": 1
        }
        ```
    * Resposta de Sucesso: `201 Created`

* **`PUT /api/equipamentos/{id}`**
    * Descrição: Atualiza um equipamento existente.
    * Exemplo de Body:
        ```json
        {
          "tag": "AC-PAIV-S12-01",
          "macAddress": "00:1A:2B:3C:4D:5F",
          "ipAddress": "192.168.1.51",
          "marca": "Springer",
          "modelo": "Inverter 12000 BTU",
          "salaId": 1
        }
        ```
    * Resposta de Sucesso: `200 OK`

### Utilizadores

* **`POST /api/usuarios`**
    * Descrição: Cria um novo utilizador. `tipo` pode ser "COMUM" ou "ADM".
    * Exemplo de Body:
        ```json
        {
          "nome": "João da Silva",
          "email": "joao.silva@ifs.edu.br",
          "senha": "umaSenhaForte123",
          "tipo": "COMUM"
        }
        ```
    * Resposta de Sucesso: `201 Created`

### Leituras de Sensores

* **`POST /api/leituras`**
    * Descrição: Endpoint para os dispositivos IoT enviarem dados dos sensores.
    * Exemplo de Body:
        ```json
        {
          "tagEquipamento": "AC-PAIV-S12-01",
          "amperagem": 5.2,
          "voltagem": 218.7,
          "temperatura": 23.5
        }
        ```
    * Resposta de Sucesso: `202 Accepted`

### Controle Remoto

* **`POST /api/controle/ligar`**
    * Descrição: Liga um equipamento.
    * Exemplo de Body:
        ```json
        {
          "equipamentoId": 1,
          "usuarioId": 1
        }
        ```
    * Resposta de Sucesso: `200 OK`

* **`POST /api/controle/desligar`**
    * Descrição: Desliga um equipamento.
    * Exemplo de Body:
        ```json
        {
          "equipamentoId": 1,
          "usuarioId": 1
        }
        ```
    * Resposta de Sucesso: `200 OK`

### Métricas e Análise

* **`GET /api/metricas/equipamento/{id}`**
    * Descrição: Obtém o status e as métricas processadas de um equipamento.
    * Resposta de Sucesso: `200 OK`

* **`POST /api/analise/verificar`**
    * Descrição: Aciona a análise de um equipamento para verificar se algum alerta deve ser disparado. **Não necessita de body**, envie o ID como parâmetro na URL.
    * Exemplo de URL: `/api/analise/verificar?equipamentoId=1`
    * Resposta de Sucesso: `200 OK` (se um alerta for gerado) ou `204 No Content` (se tudo estiver normal).

### Alertas

* **`POST /api/alertas`**
    * Descrição: Cria um novo tipo de alerta. `tipo` pode ser "FALHA" ou "ALERTA".
    * Exemplo de Body:
        ```json
        {
          "codigoAlerta": "ALTA_TEMPERATURA",
          "descricao": "Temperatura do ambiente acima do limite aceitável.",
          "tipo": "ALERTA",
          "ativo": true
        }
        ```
    * Resposta de Sucesso: `201 Created`

---
