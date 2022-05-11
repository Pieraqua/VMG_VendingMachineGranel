#ifndef __CONN_H
#define __CONN_H

#include "gendefs.h"

/* Estruturas de dados */

/* Enums */
typedef enum
{
  enMensagemVendaProdutos = 0,
  enMensagemAdicaoCreditos
}enTipoMensagem;

/* Structs */
/* Struct de controle de mensagem recebida */
typedef struct
{
  enTipoMensagem type;
  uint8_t payload[50];
}STRUCT_MSG;

/* Struct de controle de conexão */
typedef struct
{
  uint8_t msgAvailable;
  uint8_t conectado;
  STRUCT_MSG stMsg;
}STRUCT_CONN;

/* Variaveis globais */
extern STRUCT_CONN stConn;

/* Funcoes */
void vCONN_DescartaMensagem(STRUCT_CONN* struct_conn);
void vCONN_LeituraPesos(uint16_t* pesos);
void vCONN_FechaConexao();

bool bCONN_SendAck();
bool bCONN_SendUserCredit();
bool bCONN_SendAckRequest();

#endif /* __CONN_H */
