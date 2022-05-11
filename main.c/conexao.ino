#include "conexao.h"


/* Estrutura de controle de conexão */
STRUCT_CONN stConn = 
{
  .msgAvailable = FALSE,
  .conectado = 0
};

/* Função que pega os pesos do pacote bluetooth e põe no vetor pesos */
void vCONNLeituraPesos(uint8_t* pesos)
{

  
}

/* Trata dos pacotes recebidos pela comunicação bluetooth */
void vCONN_Poll()
{
  
}

bool bCONN_SendAck()
{
  
}

bool bCONN_SendUserCredit()
{
  
}

bool bCONN_SendAckRequest()
{
  
}
