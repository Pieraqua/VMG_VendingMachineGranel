#include "conexao.h"

#if !defined(CONFIG_BT_ENABLED) || !defined(CONFIG_BLUEDROID_ENABLED)
#error Bluetooth is not enabled! Please run `make menuconfig` to and enable it
#endif
/* Estrutura de controle de conexão */
STRUCT_CONN stConn = 
{
  .msgAvailable = FALSE,
  .conectado = 0
};

void vCONN_Init()
{
  SerialBT.begin("VMGaGranel");
  Serial.println("Bluetooth iniciado. ID: VMGaGranel");
}

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
  if (SerialBT.available()) {
    int msg = SerialBT.read();
    Serial.println(msg);
  }
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
