## Exemplo Função Azure

Exemplo criado com base no [quickstart](https://learn.microsoft.com/en-us/azure/azure-functions/create-first-function-cli-java?tabs=bash%2Cazure-cli%2Cbrowser) usando Java + Maven.

Implementação do algoritmo de [Hirschberg](https://en.wikipedia.org/wiki/Hirschberg%27s_algorithm)

Duas functions definidas para chamar o algoritmo. Ambas devem receber um POST HTTP com o corpo da função especificado em formatdo JSON:

```json
{
    "seq1": "ACTGTGGCATC",
    "seq2": "ATCTTCTGAGAAG",
    "gap": -1,
    "match": 1,
    "mismatch": -1
}
```

A pasta `seqs` contém alguns arquivos `.fasta` com sequências pequenas para serem usadas. O arquivo `teste.sh` é um script para chamar a função. Deve ser executado da seguite forma:

```bash
./teste.sh <path_para_arquivo_fasta> <path_para_arquivo_fasta> <url_funcao> <penalidade_gap> <score_match> <penalidade_mismatch>
```

Para executar localmente, seguir a orientação do [quickstart](https://learn.microsoft.com/en-us/azure/azure-functions/create-first-function-cli-java?) da Azure e instalar o [Azure Core Tools](https://learn.microsoft.com/en-us/azure/azure-functions/functions-run-local?tabs=linux%2Cportal%2Cv2%2Cbash&pivots=programming-language-java#v2) e o [Azure CLI](https://learn.microsoft.com/en-us/cli/azure/install-azure-cli). Em seguida, executar:

```bash
mvn clean package azure-functions:run
```

Testar executando o sctipt de teste:

```bash
./teste.sh seqs/NZ_JAFIMN010000088.fasta seqs/NZ_VCDC01000072.fasta http://localhost:7071/api/HirschbergScore -1 1 -1

./teste.sh seqs/NZ_JAFIMN010000088.fasta seqs/NZ_VCDC01000072.fasta http://localhost:7071/api/HirschbergAlignment -1 1 -1
```

Para fazer deploy na Azure:

```bash
az login
mvn azure-functions:deploy
```

Testar na Azure usando o script de teste com a URL da função na Azure 