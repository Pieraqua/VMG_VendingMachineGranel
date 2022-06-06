#include "conexao.h"

#if !defined(CONFIG_BT_ENABLED) || !defined(CONFIG_BLUEDROID_ENABLED)
#error Bluetooth is not enabled! Please run `make menuconfig` to and enable it
#endif
/* Estrutura de controle de conexão */
STRUCT_CONN stConn = 
{
  .msgAvailable = FALSE,
  .conectado = FALSE,
  .receivingMsg = FALSE
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

static int hexToInt(uint8_t* bytes, uint8_t tamanho)
{
  int i, result;
  for(i = 0; i < tamanho; i++)
  {
    //if(bytes[i] >= '0' && bytes[i] <= '9')
    //  result += (bytes[i] - '0')*16^(tamanho - i);
    //else if(bytes[i] >= 'A' && bytes[i] <= 'F')
    //  result += (bytes[i] - 'A' + 9)*16^(tamanho - i);
    //else if(bytes[i] >= 'a' && bytes[i] <= 'f')
    //  result += (bytes[i] - 'a' + 9)*16^(tamanho - i);
    result += (bytes[i])*16^(tamanho - i);
  }
  return result;
}

/* Trata dos pacotes recebidos pela comunicação bluetooth */
void vCONN_Poll()
{
  //Recebe mensagem
  while (SerialBT.available() && !stConn.msgAvailable) {
    char msg = (char)SerialBT.read();
    Serial.println("Byte recebido");
    Serial.println((byte)msg);
    Serial.print("Current: ");
    Serial.println(stConn.stMsg.current);
    

    if(!stConn.msgAvailable){
      uint8_t current = stConn.stMsg.current;
      stConn.stMsg.payload[current++] = (uint8_t)msg;
      stConn.stMsg.current++;
      if(current >= 3){
        //Finalizador de mensagem
        if(stConn.stMsg.payload[current-3] == 18 && stConn.stMsg.payload[current-2] == 52 && stConn.stMsg.payload[current-1] == 18 && stConn.stMsg.payload[current] == 52)
        {
          stConn.stMsg.current = 0;
          stConn.receivingMsg = 1;
        }
        if(stConn.stMsg.payload[current-3] == 255 && stConn.stMsg.payload[current-2] == 255 && stConn.stMsg.payload[current-1] == 255 && stConn.stMsg.payload[current] == 255)
        {
          stConn.msgAvailable = 1;
          stConn.receivingMsg = 0;
        }
      }
    }
  }

  /* Tratamento de mensagens */
  if(stConn.msgAvailable)
  {
    //ACK
    if(stConn.stMsg.payload[0] == 3){
      Serial.println("Ack received");
    }

    //ACKREQ
    if(stConn.stMsg.payload[0] == 4){
      Serial.println("Ackreq received");
    }

    if(stConn.stMsg.payload[0] == 2){
      //Pedido
      Serial.println("Pedido recebido");
      Serial.print("Pesos:"); Serial.print(hexToInt(&stConn.stMsg.payload[1], 2));Serial.print(hexToInt(&stConn.stMsg.payload[3], 2));Serial.print(hexToInt(&stConn.stMsg.payload[5], 2));
      Serial.println("Checksum:");
      Serial.print(stConn.stMsg.payload[7]);
    }
    if(stConn.stMsg.payload[0] == 5){
      //Credito
      
    }

    stConn.stMsg.current = 0;
    stConn.msgAvailable = 0;
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
