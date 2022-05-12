#ifndef __APP_H
#define __APP_H

#include "conexao.h"
#include "serialdebug.h"
#include "medicao.h"
#include "gendefs.h"
#include "portas.h"

/* Defines */
#define MARGEM_BAIXA  0.75
#define MARGEM_MEDIA 0.5
#define MARGEM_ALTA 0.25


/* Superestados da máquina de estados */
typedef enum
{
  enEsperaConexao = 0,
  enLendoDados,
  enAdicionandoCreditos,
  enAguardandoRecipiente,
  enAguardandoRemoverRecipiente,
  enEntregandoProduto,
  enFechaConexao,
  enErro
}enVMGStates;

/* Erros possíveis */
typedef enum
{
  enErroNenhum = 0,
  enErroMedida,
  enErroEstoque,
  enErroPortas
}enVMGError;

/* Estrutura com as informacoes da aplicacao */
typedef struct
{
  enVMGStates superestado;
  enVMGStates ultimo_estado;
  enVMGError erro;
  uint16_t pesos[3];
}STRUCT_APP;

/* Funcao de polling da StateMachine */
void vAPP_Poll(void);

/* Inicializacao de variáveis */
void vAPP_Init(void);

/* Funcoes estaticas */

static void svRegistraErro(enVMGError erro);

static void svSwitchSuperstate(enVMGStates estadoDesejado);



#endif //__APP_H
