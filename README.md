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

Na situação de compras (vendas) parcelas o repasse também é feito a cada 30 dias de acordo com o número da parcela ou seja a parcela 1
terá repasse em D+30, a parcela 2 em D+60, a parcela 3 em D+90 e assim por diante.

Por exemplo: Se um cliente vai na Farmácia Pão de Açúcar e faz a compra de medicamentos no total de R$ 300 reais dividindo o pagamento em 3 vezes no cartão de crédito,
a Farmácia Pão de Açúcar receberá em D+30 o valor de R$100 - (valor MDR/3), em D+60 o valor de mais R$100 - (valor MDR/3) e em D+90
o valor de mais R$100 - (valor MDR/3).


### Funcionamento da integração

A EqualsPay disponibiliza as informações das operações via cartão de crédito do cliente Farmácia Pão de Ácúcar diariamente por meio do envio de um arquivo do tipo CSV. 
O arquivo demostra todas as operações provenientes do uso do cartão de crédito realizados no dia anterior (D-1) que corresponde à data de movimento do arquivo ou seja o dia em que as
operações presente no arquivo foram realizadas. Assim as informações presentes no arquivo gerado no dia de hoje contém as operações do dia anterior (D-1).

As operações provenientes do uso do cartão de crédito são demonstradas no arquivo através de dois registros principais. O registro referente as vendas realizadas via cartão 
cujo repasse ainda será feito ao lojista (Registro iniciado com CV - que siginifica comprovante da venda) e o registro referente ao repasse do valor dessas vendas ao 
lojista (Registro iniciado com CP -  que significa comprovante do pagamento). No arquivo serão demonstradas todas as vendas realizadas (CV) e pagamentos recebidos (CP) pelo lojista 
referente ao dia anterior (D-1). Além disso será apresentado um registro de cabeçalho (H) com informações importantes sobre o lojista e sobre o arquivo como a data que o arquivo foi gerado (data de geração), 
data referente ao dia que ocorreu as operações de compra e pagamento (data de movimentação) e cnpj do lojista (identifica unicamente o cliente/loja).

Deve-se observar que como a EqualsPay disponibiliza os arquivos com as operações realizadas diariamente, o arquivo do dia D vai apresentar as vendas (CV) realizadas naquele dia e o repasse (CP) delas serão apresentados
apenas no arquivo do dia D+30, levando em consideração vendas à vista. Para vendas parcelas o arquivo do dia D vai apresentar um registro CV referente a cada uma das parcelas enquanto que os registros de repasse
referente a cada parcela serão apresentados no arquivo D+30 (parcela 1), D+60 (parcela 2), D+90 (parcela 3) e assim por diante de acorodo com o número de parcelas.
Neste caso de vendas parcelas as informações referentes aos valores totais da venda são obtidos através do somatório dos valores das parcelas.

Abaixo segue o layout dos arquivos enviados pela EqualsPay.

### Nomenclatura do Arquivo

Para facilitar a identificação dos arquivos de exemplo o mesmo tera algumas informações no seu nome separadas por underline (_), como:

```
(DATA DE GERAÇÃO)_(DATA DO MOVIMENTO)_(MEIO DE PAGAMENTO)_(CNPJ DO LOJISTA)_(IDENTIFICADOR DO ARQUIVO).csv
```

Exemplo:

```
20220102_20220101_EQUALSPAY_43117665000192_000001.csv
```

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
| Valor bruto venda/parcela    | Numérico |                       | Valor bruto da venda ou parcela            |
| Valor comissão venda/parcela | Numérico |                       | Valor de comissao da venda ou parcela      |

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
| Valor bruto venda/parcela    | Numérico |                       | Valor bruto da venda ou parcela            |
| Valor comissão venda/parcela | Numérico |                       | Valor de comissao da venda ou parcela      |

## Sobre o desafio
A ideia do desafio é desenvolver um microserviço que seja capaz de extrair as informações de cabeçalho, vendas e pagamentos de um
arquivo do tipo CSV ou seja deve ser feito a extração de cada um dos tipos de registros (H, CV, CP). Este desafio não
envolve a parte de banco de dados logo sugerimos o uso de alguma estrutura para armazenamento em tempo de execução das
informações do arquivo que está sendo processado.

**DICA**: Se atente ao identificador da venda, à data de pagamento e ao número da parcela para saber qual parcela da venda já foi paga.

### Requerimentos
* O projeto requer Java na versão 1.8 ou superior
* Maven 2.3.0 ou superior


### Sugestões
Sugere-se o uso da ferramenta Postman REST Client para facilitar a execução das requisições já predefinidas de serem implementadas. Estaremos disponibilizando uma collection
com os endpoints a serem implementados porém fique a vontade em usar qualquer outra ferramenta de sua preferência para executar as requisições.

### Sobre o microsserviço
O microserviço criado para o desafio se chama EqualsPay. Ele ainda se econtra em faze inicial e abaixo são descritas as
atividade que ainda faltam ser implementadas. Hoje os endpoints retorna apenas uma mensagem que indica qual o tipo de serviço que foi requisitado
porém devem passar a retornar, com a sua implementação, as informações solicitadas em cada uma das atividades.

**DICA**: Sinta-se a vontade em fazer uso de alguma biblioteca de seu conhecimento para auxiliar na execução das atividades 
do desafio.


### Atividades do desafio

#### 1. Deve-se processar as informações presente no arquivo
O endpoint abaixo recebe um arquivo do tipo csv onde deve ser feito a leitura das informações referentes ao registro de cabeçalho (H), os registros
de venda (CV) e os registros de Pagamentos (CP). Essas informações devem ser armazenadas em alguma estrutura que mantenha as informação registradas
em tempo de execução.

```
POST /api/conciliacao/processa-arquivo
```

**DICA 1**: Sugere-se o uso da classe Arquivo para representar as informações do arquivo

**DICA 2**: Sugere-se fazer uso do indetificador do arquivo como chave desse arquivo de modo a ser possível identitifica-lo

#### 2. Deve-se exibir as informações presente no arquivo
O endpoint abaixo recebe o identificador do arquivo onde deve ser retornado as informações presente no arquivo que possui o indentificador
enviado como parâmetro

```
GET /api/conciliacao/obtem-arquivo/{identificadorArquivo}
```

#### 3. Deve-se extrair as informações de uma venda (à vista ou parcelada) a partir do identificador da venda.
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
vendas que ainda possuem alguma parcela pendente de pagamento

```
GET /api/conciliacao/obtem-vendas-pendentes
```

#### 6. Deve-se exibir a taxa praticada pelo meio de pagamento referente a venda
O endpoint sugere que seja calculada e exibida a taxa praticada pela EqualsPay sobre a venda informada como parâmetro atráves do identificador da venda.
Observe que os registros CV apresentam valor bruto e valor comissão. Deve-se calcular a taxa referente ao valor cobrado de comissão sobre o valor bruto a fim 
de saber se a porcentagem cobrada pela EqualsPay é de 0,5% coforme previsto.

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
