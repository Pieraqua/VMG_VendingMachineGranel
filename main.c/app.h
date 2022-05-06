#ifndef __APP_H
#define __APP_H

#include "serialdebug.h"

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

/* Erros possíveis */
typedef enum
{
  enErroNenhum = 0,
  enErroMedida,
  enErroEstoque
}enVMGError;

/* Estrutura com as informacoes da aplicacao */
typedef struct
{
  enVMGStates superestado;
  enVMGStates ultimo_estado;
  enVMGError erro;
}STRUCT_APP;

/* Funcao de polling da StateMachine */
void vAPP_Poll();

/* Inicializacao de variáveis */
void vAPP_Init();

/* Funcoes estaticas */

static void svRegistraErro(enVMGError erro);

static void svSwitchSuperstate(enVMGStates estadoDesejado);



#endif //__APP_H
