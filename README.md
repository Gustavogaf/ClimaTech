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
* Uma instância do Microsoft SQL Server acessível.

### Configuração da Base de Dados

1.  Crie uma base de dados no seu SQL Server com o nome `ClimaTech_DB`.
2.  No diretório `src/main/resources/`, renomeie o ficheiro `application-dev.properties.example` (se existir) para `application-dev.properties`.
3.  Abra o ficheiro `application-dev.properties` e insira as suas credenciais de acesso à base de dados:

    ```properties
    # Configuração para o ambiente de desenvolvimento (SQL Server)
    spring.datasource.url=jdbc:sqlserver://<HOST_DO_SEU_BANCO>:1433;databaseName=ClimaTech_DB;encrypt=true;trustServerCertificate=true;
    spring.datasource.username=<SEU_USUARIO_SQL_SERVER>
    spring.datasource.password=<SUA_SENHA_SQL_SERVER>
    spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver
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

Abaixo estão os principais endpoints disponíveis no sistema.

| Funcionalidade          | Método HTTP | URL                                | Descrição                                            |
| ----------------------- | ----------- | ---------------------------------- | ------------------------------------------------------ |
| **Pavilhões** | `GET`       | `/api/pavilhoes`                   | Lista todos os pavilhões.                              |
|                         | `POST`      | `/api/pavilhoes`                   | Cria um novo pavilhão.                                 |
| **Salas** | `GET`       | `/api/salas`                       | Lista todas as salas.                                  |
|                         | `POST`      | `/api/salas`                       | Cria uma nova sala associada a um pavilhão.            |
| **Equipamentos** | `GET`       | `/api/equipamentos`                | Lista todos os equipamentos.                           |
|                         | `POST`      | `/api/equipamentos`                | Cria um novo equipamento associado a uma sala.         |
| **Utilizadores** | `GET`       | `/api/usuarios`                    | Lista todos os utilizadores.                           |
|                         | `POST`      | `/api/usuarios`                    | Cria um novo utilizador.                               |
| **Leituras de Sensores** | `POST`      | `/api/leituras`                    | Endpoint para os sensores enviarem dados.              |
| **Controle Remoto** | `POST`      | `/api/controle/ligar`              | Liga um equipamento.                                   |
|                         | `POST`      | `/api/controle/desligar`             | Desliga um equipamento.                                |
| **Análise e Métricas** | `GET`       | `/api/metricas/equipamento/{id}`   | Obtém o status e métricas processadas de um equipamento. |
|                         | `POST`      | `/api/analise/verificar`           | Aciona a análise de um equipamento para gerar alertas.  |
| **Alertas** | `GET`       | `/api/alertas`                     | Lista todos os tipos de alerta configurados.           |
|                         | `POST`      | `/api/alertas`                     | Cria um novo tipo de alerta.                           |

---
_Este projeto foi desenvolvido com o auxílio do Gemini, um modelo de linguagem da Google._