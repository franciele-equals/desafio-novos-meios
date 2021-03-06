# Bem vindo ? Equals
Somos uma empresa de tecnologia financeira especializada em Gest?o e Concilia??o de Vendas com cart?es de cr?dito, d?bito, boletos e outros meios de pagamentos online.

Neste desafio vamos precisar integrar um novo meio de pagamento chamado EqualsPay para o cliente Farm?cia P?o de A??car.

### Sobre o meio de pagamento
A EqualsPay ? um meio de pagamento novo que chegou no mercado com o objetivo de ajudar os pequenos lojistas 
oferecendo um meio de pagamento, seguro, confi?vel e acess?vel.

A EqualsPay se diferencia no mercado cobrando a menor taxa MDR (Merchant Discount Rate) para o processamento de pagamentos via cart?o de cr?dito.
O valor da taxa ? de 0,5%. Isso significa que para cada transa??o realizada via cart?o de cr?dito, ser? descontado um valor correspondente a taxa de 0,5% do valor bruto sendo
ent?o repassado para o lojista o valor l?quido que corresponde ao valor bruto - valor referente a taxa MDR (comiss?o).

### Sobre o lojista
A Farm?cia P?o de A??car, com CNPJ 43.117.665/0001-92, ? uma empresa brasileira de venda de produtos farmac?uticos com sede em Lavras, Minas Gerais.
A empresa tem pouco mais de 2 anos e com o aumento no fluxo das vendas optou por um servi?o de concilia??o financeira de cart?es
para verificar se os dados que seu meio de pagamento disponibilizaram sobre suas vendas via cart?o de cr?dito
possuem inconsist?ncias, falhas operacionais e at? mesmo fraude em rela??o aos valores que foram pagos e aos descontos que foram cobrados.

A Farm?cia P?o de A??car utiliza o meio de pagamento EqualsPay para processamento de seus pagamentos via cart?o de cr?dito cujo repasse (pagamento) dos valores
da EqualsPay ? Farm?cia P?o de A??car ? feito em D+30 ou seja ap?s 30 dias do dia em que a venda foi capturada pelo meio de pagamento.

Na situa??o de vendas parcelas o repasse tamb?m ? feito a cada 30 dias de acordo com o n?mero da parcela ou seja a parcela 1
ter? repasse em D+30, a parcela 2 em D+60, a parcela 3 em D+90 e assim por diante.

Por exemplo: Se um cliente vai na Farm?cia P?o de A??car e faz a compra de medicamentos no total de R$ 300 reais dividindo o pagamento em 3 vezes no cart?o de cr?dito,
a Farm?cia P?o de A??car receber? em D+30 o valor de R$100 - (valor MDR/3), em D+60 o valor de mais R$100 - (valor MDR/3) e em D+90
o valor de mais R$100 - (valor MDR/3).


### Funcionamento da integra??o

A EqualsPay disponibiliza as informa??es do processamento de pagamentos via cart?o de cr?dito do cliente Farm?cia P?o de ?c?car diariamente por meio do envio de um arquivo do tipo csv. 
O arquivo demostra todas as opera??es provenientes do uso do cart?o de cr?dito (previs?es de pagamento e pagamentos) realizados no dia anterior (D-1) que corresponde ? data de movimenta??o do arquivo. 
Assim as informa??es presentes no arquivo gerado no dia de hoje cont?m as opera??es do dia anterior (D-1).

Como o repasse ? feito a cada 30 dias, o arquivo apresenta registros referentes a previs?es de pagamentos (CV), que vamos chamar de comprovante de vendas 
porque s?o previs?es de pagamentos que foram realizados no dia. Tamb?m apresenta registros de pagamentos (CP) que corresponde ao pagamento propriamente dito realizado no dia em quest?o e por fim 
tamb?m apresenta um registro de cabe?alho (H) com informa??es referente a data de gera??o e movimenta??o do arquivo e informa??es sobre o cliente.

Para vendas ? vista ? enviado apenas um registro de CV no dia em que ocorreu a venda e um resitro de CP no arquivo que corresponde ao D+30. J? para vendas parceladas onde o repasse ser? feito a cada
30 dias de acordo com o numero da parcela, ser? enviado um Registro de CV, CP para cada parcela. Neste caso as informa??es referentes aos valores da venda ser?o obtidas
pelo somat?rio dos valores das parcelas.

Abaixo segue o layout dos arquivos enviados pela EqualsPay.

### Registro H - Cabe?alho
O registro H corresponde ao cabe?alho do arquivo e traz informa??es importante sobre a data de gera??o e movimenta??o do arquivo e dados do cliente

| Nome do campo            | Tipo     | Formato/<br/>Conte?do | Descri??o                                |
|--------------------------|----------|---------------------|--------------------------------------------|
| C?digo do registro       | Texto    | H                   | Cabe?alho do Arquivo                       |
| Data de gera??o          | Data     | AAAAMMDD            | Data em que o arquivo foi gerado           |
| Data de movimento        | Data     | AAAAMMDD            | Data de movimenta??o do arquivo            |
| CNPJ do lojista          | Texto    |                     | CNPJ desformatado do lojista               |
| Nome do lojista          | Texto    |                     | Nome do lojista                            |
| Identificador do arquivo | Num?rico |                     | N?mero que identifica o arquivo unicamente |

### Registro CV - Comprovante de Venda
O registro CV corresponde ? previs?o de pagamento das transa??es (vendas/parcelas) realizadas via cart?o de cr?dito.

| Nome do campo                | Tipo     | Formato/<br/>Conte?do | Descri??o                                  |
|------------------------------|----------|-----------------------|--------------------------------------------|
| C?digo do registro           | Texto    | CV                    | Registro de Comprovante de Venda           |
| Identificador da venda       | Num?rico |                       | N?mero sequencial ?nico da transa??o       |
| Data da venda                | Data     | AAAAMMDD              | Data em que a venda foi capturada          |
| Data do pagamento            | Data     | AAAAMMDD              | Data em que a venda/parcela ser? paga      |
| N?mero do cart?o             | Texto    |                       | N?mero do cart?o do cliente pagador        |
| N?mero da parcela            | Num?rico |                       | Indica o numero da parcela da venda        |
| N?mero total de parcela      | Num?rico |                       | Indica o numero total de parcelas da venda |
| Valor bruto venda/parcela    | Num?rico |                       | Valor bruto da venda                       |
| Valor comiss?o venda/parcela | Num?rico |                       | Valor de comissao cobrado pela adquirente  |

### Registro CP - Comprovante de Pagamento
O registro CP corresponde ao pagamento das transa??es (vendas ou parcelas) realizadas via cart?o de cr?dito

| Nome do campo                | Tipo     | Formato/<br/>Conte?do | Descri??o                                  |
|------------------------------|----------|-----------------------|--------------------------------------------|
| C?digo do registro           | Texto    | CP                    | Registro de Comprovante de Pagamento       |
| Identificador da venda       | Num?rico |                       | N?mero sequencial ?nico da transa??o       |
| Data da venda                | Data     | AAAAMMDD              | Data em que a venda foi capturada          |
| Data do pagamento            | Data     | AAAAMMDD              | Data em que a venda/parcela ser? paga      |
| N?mero do cart?o             | Texto    |                       | N?mero do cart?o do cliente pagador        |
| N?mero da parcela            | Num?rico |                       | Indica o numero da parcela da venda        |
| N?mero total de parcela      | Num?rico |                       | Indica o numero total de parcelas da venda |
| Valor bruto venda/parcela    | Num?rico |                       | Valor bruto da venda                       |
| Valor comiss?o venda/parcela | Num?rico |                       | Valor de comissao cobrado pela adquirente  |

## Sobre o desafio
A ideia do desafio ? desenvolver um microservi?o que seja capaz de extrair as informa??es de cabe?alho, vendas e pagamentos de um
arquivo do tipo CSV ou seja deve ser feito a extra??o de cada um dos tipos de registros (H, CV, CP). Este desafio n?o
envolve a parte de banco de dados logo sugerimos o uso de alguma estrutura para armazenamento em tempo de execu??o das
informa??es do arquivo que est? sendo processado.

Para facilitar o desenvolvimeto, o arquivo de exemplo para processamanento, ter? todas as informa??es de venda e de pagamentos no mesmo arquivo.
Vale ressaltar que num caso real receber?amos um arquivo di?rio contendo as informa??es de vendas e pagamentos que foram realizaodos no dia. Logo uma
venda que foi realizado em D+0 s? teria o registro de pagamento (CP) no arquivo do dia D+30 ou D+60, D+90 e assim por diante para vendas parceladas.

**DICA**: Se atente ao identificador da venda, ? data de pagamento e ao n?mero da parcela para saber qual parcela da venda j? foi paga.

### Requerimentos
* O projeto requer Java na vers?o 1.8 ou superior
* Maven 2.3.0 ou superior


### Sugest?es
Sugere-se o uso da Api Client Postman para faciliar o envio dos arquivos para processamento. Estaremos disponibilizando uma collection
com os endpoints a serem implementados.

### Sobre o microsservi?o
O microservi?o criado para o desafio se chama EqualPay. Ele ainda se econtra em faze inicial e abaixo s?o descritas as
atividade que ainda faltam ser implementadas.

**DICA**: Sinta-se a vontade em fazer uso de alguma biblioteca de seu conhecimento para auxiliar na execu??o das atividades 
do desafio.


### Atividades do desafio

#### 1. Deve-se extrair as informa??es presente no arquivo
O endpoint abaixo recebe um arquivo do tipo csv onde deve ser feito a leitura e a extra??o das informa??es referentes ao registro de cabe?alho (H), os registros
de venda (CV) e os registros de Pagamentos (CP). Essas informa??es devem ser armazenadas em alguma estrutura que mantenha as informa??o registradas
em tempo de execu??o.

```
POST /api/conciliacao/envio-arquivo
```

**DICA**: Deve-se fazer uso da classe Arquivo para representar as informa??es do arquivo

#### 2. Deve-se exibir as informa??es presente no arquivo
O endpoint abaixo recebe o identificador do arquivo onde deve ser retornado as informa??es presente no arquivo que possui o indentificador
enviado como par?metro

```
GET /api/conciliacao/obtem-arquivo/{identificadorArquivo}
```

#### 3. Deve-se extrair as informa??es de uma venda (? vista ou parcelada) a partir de seu identificador.
O endpoint abaixo recebe o identificador da venda onde deve ser retornado as informa??es da venda bem como seu valor bruto, 
valor de comiss?o cobrado pela adquirente e valor l?quido.

```
GET /api/conciliacao/obtem-venda/{identificadorVenda}
```

#### 4. Deve-se exibir uma listagem das vendas que foram totalmente pagas
O endpoint sugere que seja exibido uma listagem de todas as vendas presentes no arquivo que foram totalmente pagas ou seja
venda que possuem toda(s) a(s) sua(s) parcela(s) paga(s).

```
GET /api/conciliacao/obtem-vendas-pagas
```

#### 5. Deve-se exibir uma listagem das vendas que n?o foram totalmente pagas
O endpoint sugere que seja exibido uma listagem de todas as vendas presentes no arquivo que n?o foram totalmente pagas ou seja 
venda que ainda possuem alguma parcela pendente de pagamento

```
GET /api/conciliacao/obtem-vendas-pendentes
```

#### 6. Deve-se exibir a taxa praticada pelo meio de pagamento referente a venda
O endpoint sugere que seja calculada e exibida a taxa pratica pelo meio de pagamento sobre a venda informada com par?metro.
Conforme descrito no ?nicio deste documenta??o a taxa que a EqualPay cobra ? de 0,5% sobre o valor bruto da transa??o. 
Calculando a taxa que realmente foi praticado vai ser poss?vel observar se foi cobrado exatamente 0,5%.

DICA: O c?lculo da taxa praticada pode ser obtido por regra de 3 dividindo o valor da comiss?o pelo valor bruto e multiplicando por 100 para 
saber a porcentagem.

```
GET /api/conciliacao/obtem-taxa-praticada-venda/{identificadorVenda}
```

### Considera??es
#### O que ser? avaliado ?
* Qualidade de c?digo, respeitando os princ?pio de clean code e S.O.L.I.D.
* Capacidade em resolver problemas
* Capacidade de propor solu??es 
* Conhecimento em manipula??o de arquivos
* Conhecimento em orienta??o a objetos
* Conhecimento em desenvolvimento de microservi?o
* Conhecimento do framework spring no desenvolvimento do microservi?o como uso das anota??es, de inje??o de depend?ncia, invers?o de controle e etc.
* Capacidade de entendimento de regra de neg?cio

**DICA 1**: Implementem o m?ximo de coisas que conseguir

**DICA 2**: Em rela??o ?s coisas que n?o conseguir desenvolver fa?a um coment?rio no c?digo descrevendo como poderia ser feito

**DICA 3**: Fique a vontade para criar testes unit?rios

**DICA 4**: Fique a vontade em modificar o c?digo atual para deix?-lo mais leg?vel e limpo
