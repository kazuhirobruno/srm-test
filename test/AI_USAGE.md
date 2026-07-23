### Prompt 1

## Leia essa definição e me auxilie a entender o problema proposto

1.  **Gestão de Câmbio (Currency Engine):**
    - Sistema capaz de armazenar e prover taxas de câmbio (ex: USD para BRL).
    - Endpoint para atualização manual ou integração (mockada) de taxas.

2.  **Motor de Precificação (Strategy Pattern):**
    - Cada tipo de recebível possui uma regra de risco (Spread) diferente. Aplique o padrão **Strategy** para desacoplar a regra do cálculo.
    - _Fórmula Base:_ `Valor Presente = Valor Face / (1 + Taxa Base + Spread)^Prazo`
    - _Variações de Risco (Exemplo):_
      - Duplicata Mercantil: Spread de 1.5% a.m.
      - Cheque Pré-datado: Spread de 2.5% a.m.
    - Se a operação for cross-currency (Título em BRL, Pagamento em USD), aplicar a conversão cambial no final.

3.  **Persistência e Integridade:**
    - Uso de Banco de Dados Relacional (preferencialmente).
    - Transações financeiras devem respeitar as propriedades **ACID**. Nenhuma liquidação pode ficar "pela metade" (cuidado com _race conditions_).

4.  **API RESTful (API First):**
    - Design de APIs claro, seguindo verbos HTTP corretos e códigos de status semânticos.
    - Documentação via OpenAPI/Swagger.

5.  **Consultas Analíticas:**
    - Implementar uma rota de "Extrato de Liquidação" que permita filtrar grandes volumes de dados por período, cedente e tipo de moeda.
    - _Diferencial:_ Uso de Query Builders ou SQL nativo otimizado para performance em vez de ORMs puros para relatórios.

6.  **Arquitetura em camadas para o backend:**
    - Separação das lógicas de aplicação, de negócio e de persistência em 3 camadas.
    - Relatórios podem ser organizados em duas camadas apenas sem necessidade de passar pela de negócios.

Justificativa: não tenho conhecimento completo na área de finanças. Utilizar a IA para me ajudar a entender o problema proposto e as melhores estratégias para dividir em partes e conseguir evoluir.

### Prompt 2

## Com base no texto que enviei, me ajude a definir a estrutura de pastas. Se possível, nomear cada diretório e explicar quais partes devem ser colocadas em cada um.

Justificativa: Facilitar o atendimento e a implementação da arquitetura em camadas.

### Prompt 3

## Definir os scripts utilizando a migrations do Flyway e as entidades no diretório domain

Justificativa: acelerar o desenvolvimento. Porém a IA focou apenas nos campos tradicionais e se esqueceu do created_at, sendo este implementado manualmente nos scripts. Adicionalmente, utilizei as annotations do Lombok como Data, All Args e No Args constructor.

### Prompt 4

## Em repository, acredito ser necessário identificar em alguma das entities, suponho que seja o Recebivel, o lock pessimista para impedir alterações enquanto um recurso estiver sendo utilizado. Minha dúvida está correta?

Resposta: sim.

### Prompt 5

## Seguindo o padrão strategy, gostaria de uma ideia de implementação para esta especificação. Acredito que apenas a interface e as classes implementadoras serão necessárias. Vê mais um ponto importante a ser considerado?

Resposta: Adicionar o nome no @Component, assim permitindo a criação de uma Factory responsável pela gestão das estratégias implementadas. Caso seja necessário adicionar mais uma strategy futuramente, o sistema não precisará sofrer uma grande refatoração.

### Prompt 6

## Em RecebivelService, gostaria de implementar os transactional para evitar race condiction.

Resposta: Método Liquidar e Cadastrar.

### Prompt 7

## Percebi que em Liquidar, o valor final não estava sendo salvo em lugar nenhum. Acredito que é necessário salvar em Recebível.

Resposta: Apontamento correto, então foi necessário atualizar a entity de Recebível, a Migration (resultando no script V2) e alterar a linha para salvar este item.

### Prompt 8

## Quero implementar as validações utilizando Valid e criando um global exception

Justificativa: tratar os erros de input. Fui passando cada entity e DTO a fim de concluir este tema

Resposta: validacão em cada DTO, entity e a criação da classe global para tratamento desses erros.

### Prompt 9

## Em cada controller, adicionar um try catch para segmentar a resposta ao usuário de acordo com o problema encontrado, caso ocorra exception

Justificativa: Atender o requisito de tratamento de exceptions.

Resposta: Refatoração de cada uma das funções nas controllers.

### Prompt 10

## Gostaria de um auxilio para implementar a parte de report

Resposta: criação de uma interface para o DTO, ajuste no repository adicionando paginação (visando melhorar a performance).

### Prompt 11

## Quero entender esse script SQL adicionado no repository

Justificativa: entender o motivo do script SQL ser implementado como foi mostrado.

### Prompt 12

## Quero documentar esse controller/DTO

Justificativa: explicar os parâmetros, requests e responses da aplicação.

### Prompt 13

## Ajudar na estruturação e criação das classes de testes nos Strategies

Justificativa: manter o core da aplicação documentado.
