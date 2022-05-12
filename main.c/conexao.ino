#include "conexao.h"


/* Estrutura de controle de conexão */
STRUCT_CONN stConn = 
{
  .msgAvailable = FALSE,
  .conectado = 0
};

void vCONN_FechaConexao()
{
  
}

/* Função que pega os pesos do pacote bluetooth e põe no vetor pesos */
void vCONN_LeituraPesos(uint16_t* pesos)
{

  
}

void vCONN_DescartaMensagem(STRUCT_CONN* struct_conn)
{
  
}

/* Trata dos pacotes recebidos pela comunicação bluetooth */
void vCONN_Poll()
{
  
}

/* Envia um pacote de ACK para o aplicativo */
bool bCONN_SendAck()
{

  return false;
}

/* Envia um pacote de informações de crédito ao aplicativo */
bool bCONN_SendUserCredit(STRUCT_MSG* mensagem)
{
  return false;
}

/* Envia um pacote de requisição de ACK ao aplicativo */
bool bCONN_SendAckRequest()
{
  return false;
}
