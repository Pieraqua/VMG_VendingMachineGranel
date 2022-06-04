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
  SerialBT.disconnect();
}

/* Função que pega os pesos do pacote bluetooth e põe no vetor pesos */
void vCONN_LeituraPesos(uint16_t* pesos)
{
  
  
}

void vCONN_DescartaMensagem(STRUCT_CONN* struct_conn)
{
  struct_conn->stMsg.current = 0;
}

/* Trata dos pacotes recebidos pela comunicação bluetooth */
void vCONN_Poll()
{
  //Recebe mensagem
  while (SerialBT.available() && !stConn.msgAvailable) {
    char msg = (char)SerialBT.read();
    Serial.println(msg);
    uint8_t current = stConn.stMsg.current;
    stConn.stMsg.payload[current++] = (uint8_t)msg;
    stConn.stMsg.current++;
    if(current > 4){
      //Finalizador de mensagem
      if(stConn.stMsg.payload[current-4] == 0xFF && stConn.stMsg.payload[current-3] == 0xFF && stConn.stMsg.payload[current-2] == 0xFF && stConn.stMsg.payload[current-1] == 0xFF)
      {
        stConn.msgAvailable = 1;
      }
    }
  }
  //Se ja tem uma mensagem no buffer, ignora as proximas.
  if(SerialBT.available() && stConn.msgAvailable)
  {
    //Se nao esta enviando nada, envia um ack para avisar que descartou a mensagem
    bCONN_SendAck();    
    SerialBT.read();
  }
  
}

/* Envia um pacote de ACK para o aplicativo */
bool bCONN_SendAck()
{
  //OPCODE
  SerialBT.write(3);
  //Payload
  SerialBT.write(0x12);
  SerialBT.write(0x34);
  SerialBT.write(0x56);
  SerialBT.write(0x78);
  //Checksum
  SerialBT.write((3 + 0x12 + 0x34 + 0x56 + 0x78)%256);
  //Packet ender
  SerialBT.write(0xFF);
  SerialBT.write(0xFF);
  
  return true;
}

/* Envia um pacote de informações de crédito e da maquina ao aplicativo */
bool bCONN_SendUserCredit(STRUCT_MSG* mensagem)
{
  //OPCODE
  SerialBT.write(5);
 
  //peso disponivel item 1
  SerialBT.write(0xfe);
  //peso disponivel item 1
  SerialBT.write(0xfe);
  //peso disponivel item 2
  SerialBT.write(0xfe);
  //peso disponivel item 2
  SerialBT.write(0xfe);  
  //peso disponivel item 3
  SerialBT.write(0xfe);  
  //peso disponivel item 3
  SerialBT.write(0xfe);  
  //dinheiros disponiveis MSB
  SerialBT.write(0xfe);  
  //dinheiros disponiveis middle byte
  SerialBT.write(0xfe);  
  //dineheiros disponiveis LSB
  SerialBT.write(0xfe);
 
  //checksum
  SerialBT.write((uint8_t)((0xff * 9 + 5)%256));

  //packet ender
  SerialBT.write(0xff);
  SerialBT.write(0xff);

  return true;
}

/* Envia um pacote de requisição de ACK ao aplicativo */
bool bCONN_SendAckRequest()
{
  //OPCODE
  SerialBT.write(4);
  //Payload
  SerialBT.write(0x78);
  SerialBT.write(0x56);
  SerialBT.write(0x34);
  SerialBT.write(0x12);
  //Checksum
  SerialBT.write((4 + 0x12 + 0x34 + 0x56 + 0x78)%256);

  //Packet ender
  SerialBT.write(0xFF);
  SerialBT.write(0xFF);
  
  return false;
}
