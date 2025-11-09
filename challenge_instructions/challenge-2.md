# Renew your career - Spring Boot - desafio 2 - Operações bancárias RESTful

## Solicitação

Agora que já temos uma aplicação bancária com operações e funções disponíveis via linha de comando (CLI terminal), 
vamos expô-la para ser consumida por um frontend através de endpoints REST. 
**[As operações mínimas necessárias estão na
descrição do Desafio 1.](challenge-1.md)**

Como estamos expondo nossos serviços para um outro time desenvolver, seria interessante utilizar um formato de 
documentação do mercado para interagir com os consumidores. Para isso, precisaremos documentar nossos endpoints com 
**OpenAPI**, através do **Swagger** como uma dependência do projeto.

## Requisitos mandatórios:

* O projeto deve estar em repositório público no GitHub.
* A aplicação deve persistir informações em banco de dados (qualquer um utilizado durante o curso - PostgreSQL, MongoDB 
* ou até mesmo o in-memory H2), através de JPA/Hibernate
* A documentação da aplicação com Swagger é mandatória - https://swagger.io/
  * Aqui temos um guia de como utilizar com Maven/Spring Boot: https://www.treinaweb.com.br/blog/documentando-uma-api-spring-boot-com-o-swagger

## Desejável

* O projeto deve estar deployado e executando em algum serviço cloud. O curso ensina como fazer o deploy utilizando o 
Heroku (vide seção 23 do curso). Se isso foi feito, enviar o link de acesso à página principal do Swagger da aplicação
junto com o link do repositório no GitHub.

## Considerações:
 
* Caso queiram aumentar as operações com novas funcionalidades (que talvez não fizessem sentido no desafio 1 com um CLI
), fiquem à vontade.
* O padrão de comunicação será de API Restful, com entradas e saídas no formato JSON, como utilizado no curso.
* Como temos mais classes com responsabilidades específicas agora no Spring Boot (@Controller, @Service, @Entity, etc), 
organizem os pacotes conforme utilizado no curso pelo instrutor. Não há necessidade de, agora, ficarem preocupados 
com padrões como arquitetura hexagonal (ports and adapters), como vi muitos com dúvidas durante todo o curso.
* **Para quem não fez o desafio 1, essa é a chance de fazê-lo, utilizando REST ao invés de um CLI para interação. 
 Aproveitem!**

## Datas:

Para este segundo desafio, estou divulgando um pouco antes e vou extender a data de entrega em mais uma semana. 
Sendo agora a data final para envio **14/05/2024 23:59**.

O link do repositório (**e link da documentação Swagger deployada no Heroku ou outro serviço cloud, se houver**) 
deve ser enviado para:

* [frederico.x.silva@accenture.com](mailto:frederico.x.silva@accenture.com)
* [carlos.h.kafka@accenture.com](mailto:carlos.h.kafka@accenture.com)
* [julio.cesar.maciel@accenture.com](mailto:julio.cesar.maciel@accenture.com)

## Bom desafio à todos!