#include "conexao.h"


/* Estrutura de controle de conexão */
STRUCT_CONN stConn = 
{
  .msgAvailable = FALSE,
  .conectado = 0
};

/* Função que pega os pesos do pacote bluetooth e põe no vetor pesos */
void vCONNLeituraPesos(uint16_t* pesos)
{

  
}

/* Trata dos pacotes recebidos pela comunicação bluetooth */
void vCONN_Poll()
{
  
}

/* Envia um pacote de ACK para o aplicativo */
bool bCONN_SendAck()
{
  
}

/* Envia um pacote de informações de crédito ao aplicativo */
bool bCONN_SendUserCredit()
{
  
}

/* Envia um pacote de requisição de ACK ao aplicativo */
bool bCONN_SendAckRequest()
{
  
}
