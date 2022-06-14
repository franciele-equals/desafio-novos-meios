# Bem vindo à Equals
Somos uma empresa de tecnologia financeira especializada em Gestão e Conciliação de Vendas com cartões de crédito, débito, boletos e outros meios de pagamentos online.

Neste desafio vamos precisar integrar um novo meio de pagamento chamado EqualsPay para o cliente Farmácia Pão de Açúcar.

### Sobre o meio de pagamento
A EqualsPay é um meio de pagamento novo que chegou no mercado com o objetivo de ajudar os pequenos lojistas 
oferecendo um meio de pagamento, seguro, confiável e acessível.

A EqualsPay se diferencia no mercado cobrando a menor taxa MDR (Merchant Discount Rate) para o processamento de pagamentos via cartão de crédito.
O valor da taxa é de 0,5%. Isso significa que para cada transação realizada via cartão de crédito, será descontado um valor correspondente a taxa de 0,5% do valor bruto sendo
então repassado para o lojista o valor líquido que corresponde ao valor bruto - valor referente a taxa MDR (comissão).

### Sobre o lojista
A Farmácia Pão de Açúcar, com CNPJ 43.117.665/0001-92, é uma empresa brasileira de venda de produtos farmacêuticos com sede em Lavras, Minas Gerais.
A empresa tem pouco mais de 2 anos e com o aumento no fluxo das vendas optou por um serviço de conciliação financeira de cartões
para verificar se os dados que seu meio de pagamento disponibilizaram sobre suas vendas via cartão de crédito
possuem inconsistências, falhas operacionais e até mesmo fraude em relação aos valores que foram pagos e aos descontos que foram cobrados.

A Farmácia Pão de Açúcar utiliza o meio de pagamento EqualsPay para processamento de seus pagamentos via cartão de crédito cujo repasse (pagamento) dos valores
da EqualsPay à Farmácia Pão de Açúcar é feito em D+30 ou seja após 30 dias do dia em que a venda foi capturada pelo meio de pagamento.

Na situação de vendas parcelas o repasse também é feito a cada 30 dias de acordo com o número da parcela ou seja a parcela 1
terá repasse em D+30, a parcela 2 em D+60, a parcela 3 em D+90 e assim por diante.

Por exemplo: Se um cliente vai na Farmácia Pão de Açúcar e faz a compra de medicamentos no total de R$ 300 reais dividindo o pagamento em 3 vezes no cartão de crédito,
a Farmácia Pão de Açúcar receberá em D+30 o valor de R$100 - (valor MDR/3), em D+60 o valor de mais R$100 - (valor MDR/3) e em D+90
o valor de mais R$100 - (valor MDR/3).


### Funcionamento da integração

A EqualsPay disponibiliza as informações do processamento de pagamentos via cartão de crédito do cliente Farmácia Pão de Ácúcar diariamente por meio do envio de um arquivo do tipo csv. 
O arquivo demostra todas as operações provenientes do uso do cartão de crédito (previsões de pagamento e pagamentos) realizados no dia anterior (D-1) que corresponde à data de movimentação do arquivo. 
Assim as informações presentes no arquivo gerado no dia de hoje contém as operações do dia anterior (D-1).

Como o repasse é feito a cada 30 dias, o arquivo apresenta registros referentes a previsões de pagamentos (CV), que vamos chamar de comprovante de vendas 
porque são previsões de pagamentos que foram realizados no dia. Também apresenta registros de pagamentos (CP) que corresponde ao pagamento propriamente dito realizado no dia em questão e por fim 
também apresenta um registro de cabeçalho (H) com informações referente a data de geração e movimentação do arquivo e informações sobre o cliente.

Para vendas à vista é enviado apenas um registro de CV no dia em que ocorreu a venda e um resitro de CP no arquivo que corresponde ao D+30. Já para vendas parceladas onde o repasse será feito a cada
30 dias de acordo com o numero da parcela, será enviado um Registro de CV, CP para cada parcela. Neste caso as informações referentes aos valores da venda serão obtidas
pelo somatório dos valores das parcelas.

Abaixo segue o layout dos arquivos enviados pela EqualsPay.

### Registro H - Cabeçalho
O registro H corresponde ao cabeçalho do arquivo e traz informações importante sobre a data de geração e movimentação do arquivo e dados do cliente

| Nome do campo            | Tipo     | Formato/<br/>Conteúdo | Descrição                                |
|--------------------------|----------|---------------------|--------------------------------------------|
| Código do registro       | Texto    | H                   | Cabeçalho do Arquivo                       |
| Data de geração          | Data     | AAAAMMDD            | Data em que o arquivo foi gerado           |
| Data de movimento        | Data     | AAAAMMDD            | Data de movimentação do arquivo            |
| CNPJ do lojista          | Texto    |                     | CNPJ desformatado do lojista               |
| Nome do lojista          | Texto    |                     | Nome do lojista                            |
| Identificador do arquivo | Numérico |                     | Número que identifica o arquivo unicamente |

### Registro CV - Comprovante de Venda
O registro CV corresponde à previsão de pagamento das transações (vendas/parcelas) realizadas via cartão de crédito.

| Nome do campo                | Tipo     | Formato/<br/>Conteúdo | Descrição                                  |
|------------------------------|----------|-----------------------|--------------------------------------------|
| Código do registro           | Texto    | CV                    | Registro de Comprovante de Venda           |
| Identificador da venda       | Numérico |                       | Número sequencial único da transação       |
| Data da venda                | Data     | AAAAMMDD              | Data em que a venda foi capturada          |
| Data do pagamento            | Data     | AAAAMMDD              | Data em que a venda/parcela será paga      |
| Número do cartão             | Texto    |                       | Número do cartão do cliente pagador        |
| Número da parcela            | Numérico |                       | Indica o numero da parcela da venda        |
| Número total de parcela      | Numérico |                       | Indica o numero total de parcelas da venda |
| Valor bruto venda/parcela    | Numérico |                       | Valor bruto da venda                       |
| Valor comissão venda/parcela | Numérico |                       | Valor de comissao cobrado pela adquirente  |

### Registro CP - Comprovante de Pagamento
O registro CP corresponde ao pagamento das transações (vendas ou parcelas) realizadas via cartão de crédito

| Nome do campo                | Tipo     | Formato/<br/>Conteúdo | Descrição                                  |
|------------------------------|----------|-----------------------|--------------------------------------------|
| Código do registro           | Texto    | CP                    | Registro de Comprovante de Pagamento       |
| Identificador da venda       | Numérico |                       | Número sequencial único da transação       |
| Data da venda                | Data     | AAAAMMDD              | Data em que a venda foi capturada          |
| Data do pagamento            | Data     | AAAAMMDD              | Data em que a venda/parcela será paga      |
| Número do cartão             | Texto    |                       | Número do cartão do cliente pagador        |
| Número da parcela            | Numérico |                       | Indica o numero da parcela da venda        |
| Número total de parcela      | Numérico |                       | Indica o numero total de parcelas da venda |
| Valor bruto venda/parcela    | Numérico |                       | Valor bruto da venda                       |
| Valor comissão venda/parcela | Numérico |                       | Valor de comissao cobrado pela adquirente  |

## Sobre o desafio
A ideia do desafio é desenvolver um microserviço que seja capaz de extrair as informações de cabeçalho, vendas e pagamentos de um
arquivo do tipo CSV ou seja deve ser feito a extração de cada um dos tipos de registros (H, CV, CP). Este desafio não
envolve a parte de banco de dados logo sugerimos o uso de alguma estrutura para armazenamento em tempo de execução das
informações do arquivo que está sendo processado.

Para facilitar o desenvolvimeto, o arquivo de exemplo para processamanento, terá todas as informações de venda e de pagamentos no mesmo arquivo.
Vale ressaltar que num caso real receberíamos um arquivo diário contendo as informações de vendas e pagamentos que foram realizaodos no dia. Logo uma
venda que foi realizado em D+0 só teria o registro de pagamento (CP) no arquivo do dia D+30 ou D+60, D+90 e assim por diante para vendas parceladas.

**DICA**: Se atente ao identificador da venda, à data de pagamento e ao número da parcela para saber qual parcela da venda já foi paga.

### Requerimentos
* O projeto requer Java na versão 1.8 ou superior
* Maven 2.3.0 ou superior


### Sugestões
Sugere-se o uso da Api Client Postman para faciliar o envio dos arquivos para processamento. Estaremos disponibilizando uma collection
com os endpoints a serem implementados.

### Sobre o microsserviço
O microserviço criado para o desafio se chama EqualPay. Ele ainda se econtra em faze inicial e abaixo são descritas as
atividade que ainda faltam ser implementadas.

**DICA**: Sinta-se a vontade em fazer uso de alguma biblioteca de seu conhecimento para auxiliar na execução das atividades 
do desafio.


### Atividades do desafio

#### 1. Deve-se extrair as informações presente no arquivo
O endpoint abaixo recebe um arquivo do tipo csv onde deve ser feito a leitura e a extração das informações referentes ao registro de cabeçalho (H), os registros
de venda (CV) e os registros de Pagamentos (CP). Essas informações devem ser armazenadas em alguma estrutura que mantenha as informação registradas
em tempo de execução.

```
POST /api/conciliacao/envio-arquivo
```

**DICA**: Deve-se fazer uso da classe Arquivo para representar as informações do arquivo

#### 2. Deve-se exibir as informações presente no arquivo
O endpoint abaixo recebe o identificador do arquivo onde deve ser retornado as informações presente no arquivo que possui o indentificador
enviado como parâmetro

```
GET /api/conciliacao/obtem-arquivo/{identificadorArquivo}
```

#### 3. Deve-se extrair as informações de uma venda (à vista ou parcelada) a partir de seu identificador.
O endpoint abaixo recebe o identificador da venda onde deve ser retornado as informações da venda bem como seu valor bruto, 
valor de comissão cobrado pela adquirente e valor líquido.

```
GET /api/conciliacao/obtem-venda/{identificadorVenda}
```

#### 4. Deve-se exibir uma listagem das vendas que foram totalmente pagas
O endpoint sugere que seja exibido uma listagem de todas as vendas presentes no arquivo que foram totalmente pagas ou seja
venda que possuem toda(s) a(s) sua(s) parcela(s) paga(s).

```
GET /api/conciliacao/obtem-vendas-pagas
```

#### 5. Deve-se exibir uma listagem das vendas que não foram totalmente pagas
O endpoint sugere que seja exibido uma listagem de todas as vendas presentes no arquivo que não foram totalmente pagas ou seja 
venda que ainda possuem alguma parcela pendente de pagamento

```
GET /api/conciliacao/obtem-vendas-pendentes
```

#### 6. Deve-se exibir a taxa praticada pelo meio de pagamento referente a venda
O endpoint sugere que seja calculada e exibida a taxa pratica pelo meio de pagamento sobre a venda informada com parâmetro.
Conforme descrito no ínicio deste documentação a taxa que a EqualPay cobra é de 0,5% sobre o valor bruto da transação. 
Calculando a taxa que realmente foi praticado vai ser possível observar se foi cobrado exatamente 0,5%.

DICA: O cálculo da taxa praticada pode ser obtido por regra de 3 dividindo o valor da comissão pelo valor bruto e multiplicando por 100 para 
saber a porcentagem.

```
GET /api/conciliacao/obtem-taxa-praticada-venda/{identificadorVenda}
```

### Considerações
#### O que será avaliado ?
* Qualidade de código, respeitando os princípio de clean code e S.O.L.I.D.
* Capacidade em resolver problemas
* Capacidade de propor soluções 
* Conhecimento em manipulação de arquivos
* Conhecimento em orientação a objetos
* Conhecimento em desenvolvimento de microserviço
* Conhecimento do framework spring no desenvolvimento do microserviço como uso das anotações, de injeção de dependência, inversão de controle e etc.
* Capacidade de entendimento de regra de negócio

**DICA 1**: Implementem o máximo de coisas que conseguir

**DICA 2**: Em relação às coisas que não conseguir desenvolver faça um comentário no código descrevendo como poderia ser feito

**DICA 3**: Fique a vontade para criar testes unitários

**DICA 4**: Fique a vontade em modificar o código atual para deixá-lo mais legível e limpo
