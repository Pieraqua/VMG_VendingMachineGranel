#ifndef __APP_H
#define __APP_H

/* Superestados da máquina de estados */
typedef enum
{
  enEsperaConexao = 0,
  enLendoDados,
  enAguardandoRecipiente,
  enEntregandoProduto,
  enFechaConexao,
  enErro
}enVMGStates;

typedef struct
{
  uint8_t superestado;
  
}stAPP;

/* Funcao de polling da StateMachine */
void vAPP_Poll();

/* Inicializacao de variáveis */
void vAPP_Init();

#endif //__APP_H
