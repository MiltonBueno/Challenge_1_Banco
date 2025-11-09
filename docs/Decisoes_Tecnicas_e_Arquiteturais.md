Arquitetura do Sistema:

O projeto segue uma arquitetura em camadas bem definida: Domain (entidades de negócio), Services (lógica de negócio), Repositories (acesso a dados com implementação InMemory), DTOs (transferência de dados), UI (interface com usuário), Utils (utilitários) e Config (configurações). Essa separação facilita a manutenção, testes e futura migração para persistência real (banco de dados).

Foi criada a classe BankingConfig para centralizar todas as constantes e configurações do sistema (limites de conta, taxas, horários, etc), facilitando a manutenção e possíveis ajustes futuros sem necessidade de alterar múltiplas classes.

Foi implementada a classe DataSeeder no pacote config para popular o sistema com dados iniciais de teste (agências, clientes, contas e transações), facilitando o desenvolvimento e demonstração da aplicação.


Hierarquia de Exceções:

A hierarquia de exceções customizadas foi estruturada da seguinte forma: DomainException (abstrata) como exceção base, da qual derivam BusinessException (para violações de regras de negócio, como saldo insuficiente) e ValidationException (para erros de validação e entidades não encontradas). Todas estendem RuntimeException, evitando a necessidade de tratamento obrigatório em toda a cadeia de chamadas.


Classes de Domínio:

O id das classes Client e Transaction são definidos automaticamente como "final" na classe abstrata BaseIdEntity, este parâmetro é o único utilizado para comparação no equals e hashcode dessas classes. A classe Account utiliza o accountNumber e a classe Agency utiliza o agencyNumber para comparação no equals e hashcode.

A escolha do tipo String para os parâmetros AgencyNumber e AccountNumber se deve à possibilidade de os números se iniciarem com 0 (0001-...)

Parâmetro "password" no cliente para permitir realizar transações, ver informações sobre a conta, etc, com mais segurança


Imutabilidade dos Parâmetros:

As classes Client e Transaction possuem um getter pro Id mas não um setter, visto que uma vez definido ele é final. A mesma ideia se aplica a:

Client - Os parâmetros name, password e accounts são definidos como final e não possuem setters, visto que uma vez definidos eles não devem ser mutáveis. A lista accounts possui o método auxiliar addAccount() para adicionar itens.

Account - Os parâmetros accountNumber, client, agency e transactions são definidos como final e não possuem setters, visto que uma vez definidos eles não devem ser mutáveis. A lista transactions possui o método auxiliar addTransaction() para adicionar itens.

Agency - Os parâmetros agencyNumber e accounts são definidos como final e não possuem setters, visto que uma vez definidos eles não devem ser mutáveis. A lista accounts possui o método auxiliar addAccount() para adicionar itens.

Transaction - Os parâmetros transactionTime, amount, type, sourceAccount e targetAccount são definidos como final e não possuem setters, visto que uma vez definidos eles não devem ser mutáveis.


Estrutura das Transações:

Os parâmetro sourceAccount e targetAccount nem sempre vão estar presentes em todas as transactions visto que depende do tipo de transaction, conforme abaixo:

TransactionType.WITHDRAW - vai conter apenas sourceAccount (conta de onde houve o saque).

TransactionType.DEPOSIT - vai conter apenas targetAccount (conta onde o valor foi depositado).

TransactionType.TRANSFERENCE - vai conter tanto targetAccount quanto sourceAccount (conta de onde o valor saiu e pra onde foi).


Tipos de Conta:

A ideia utilizada pra diferenciar a conta corrente (CheckingAccount), a conta poupança (SavingsAccount) e a conta empresarial (BusinessAccount) foi a seguinte:

Conta corrente - Possui um limite inicial pré-definido de 1000 reais.

Conta poupança - Possui um método "applyInterest" que ao ser chamado incrementa 0.5% ao saldo (correspondente ao rendimento de uma conta poupança). O limite inicial é zero.

Conta empresarial - Possui um limite inicial pré-definido de 50000 reais e cobra taxa de 0.75% do valor em cada transação (saque, depósito ou transferência).

A cobrança de 0.75% nas transações de contas empresariais é definida da seguinte forma:

No caso de depósito, o valor depositado é 0.75% menor do que o inserido no terminal pelo usuário.

No caso de saque, 0.75% do valor sacado é descontado adicionalmente do saldo.

No caso de transferência, 0.75% do valor transferido é descontado adicionalmente do saldo da conta de origem se a conta de origem for empresarial.

Para o recebimento de transferências não é cobrada nenhuma taxa da conta que está recebendo, independente do tipo.


Regras de Negócio:

A aplicação possui uma regra de negócio que limita transferências realizadas no período noturno (entre 20h e 6h) a um valor máximo de 1000 reais, visando simular uma maior segurança das transações.

O valor possível de ser "retirado" da conta, ou seja, sacado ou transferido, é o do saldo + limite. Ou seja, o máximo de negativo que uma conta vai poder chegar será sempre o valor do limite negativo. No caso do BusinessAccount a conta feita é considerada com a taxa, ou seja, o valor do saldo + limite + taxa é o valor máximo que poderá ser transferido ou retirado da conta.

É possível perceber que na classe Account há duas funções que parecem executar a mesma função de "adicionar" um valor ao saldo da conta, o método "receiveTransfer" e o método "depositValue". Isso é porque apesar de na classe abstrata as funções serem as mesmas, decidi fazer uma distinção de responsabilidades, de forma que se em algum momento o depósito possuir uma regra de negócio diferente do recebimento via transferência é fácil de alterar o código sem gerar problemas.


Decisões de Implementação:

Escolha de BigDecimal na classe Account pela sua maior precisão (mais recomendado ao se trabalhar com dinheiro)

Ao realizar contas utilizando BigDecimal, de acordo com minhas pesquisas faz sentido receber o valor como String e então converter diretamente para BigDecimal sem utilizar double, garante mais segurança e precisão. Para facilitar essa conversão e validação de entradas do usuário, foi criada a classe InputHandler que possui métodos para receber e converter automaticamente os valores em BigDecimal, além de validar entradas de String, int e confirmações (boolean).

O uso de números nos Enums (TransactionType, LocaleFormat e CsvDelimiter) facilita potencial integração futura com banco de dados/serialização, minimizando inclusive a chance de erros no desenvolvimento, alterando as posições do enum por exemplo.


Formatação e Localização:

A aplicação implementa um sistema de formatação baseado em localização (LocaleFormat) que permite exibir valores monetários tanto no formato brasileiro (R$ 1.234,56) quanto no formato americano ($1,234.56), utilizando a classe NumberFormatter para a conversão.

O enum CsvDelimiter permite ao usuário escolher o delimitador usado na exportação de transações para CSV, oferecendo as opções COMMA (vírgula) e SEMICOLON (ponto e vírgula), facilitando a compatibilidade com diferentes configurações regionais do Excel (inglês e português).