#include "app.h"

STRUCT_APP stAPP = 
{
  .superestado = enEsperaConexao,
  .ultimo_estado = enEsperaConexao,
  .erro = enErroNenhum
};

void vAPP_Init()
{
  memset(stAPP.pesos, 0, sizeof(uint16_t)*3);

  vPORTA_Init();
  
}

void vAPP_Poll()
{
  static uint8_t erro = 0;
  switch(stAPP.superestado)
  {
    case enEsperaConexao:
      if(stAPP.subestado == 0){
          #ifdef __DEBUG_APP
          Serial.println("APP: Estado enEsperaConexao");
          #endif
          stAPP.subestado = 1;
      }
        /* Tratamento estado enEsperaConexao */
        
        /* Caso tenha uma mensagem disponível no bluetooth, */
        if(stConn.msgAvailable)
        {
          /* e a mensagem seja uma requisicao de venda de produtos, */
          if(stConn.stMsg.type == enMensagemVendaProdutos)
          {
            /* Vai para o estado LendoDados e impede novas conexões */
            stConn.conectado = TRUE;
            svSwitchSuperstate(enLendoDados);
          }
          /* e a mensagem seja uma requisicao de adicao de creditos, */
          else if (stConn.stMsg.type == enMensagemAdicaoCreditos)
          {
            /* Adiciona créditos ao usuário */
            stConn.conectado = TRUE;
            svSwitchSuperstate(enAdicionandoCreditos);
          }
          /* Caso nao seja um pacote valido, descarta a mensagem. */
          else vCONN_DescartaMensagem(&stConn);
          
        }
        
        svSwitchSuperstate(enLendoDados);
        
    break;

    case enAdicionandoCreditos:
        if(stAPP.subestado == 0){
        #ifdef __DEBUG_APP
        Serial.println("APP: Estado enAdicionandoCreditos");
        #endif
        stAPP.subestado = 1;
        }
        /* Envia pedido de adição de créditos ao banco de dados */
        bCONN_SendUserCredit(&stConn.stMsg);
        vCONN_DescartaMensagem(&stConn);
        stConn.conectado = FALSE;
        svSwitchSuperstate(enEsperaConexao);
    break;

    case enLendoDados:
      if(stAPP.subestado == 0){
        #ifdef __DEBUG_APP
        Serial.println("APP: Estado enLendoDados");
        #endif
        stAPP.subestado = 1;
      }

        vCONN_LeituraPesos((uint16_t*)&(stAPP.pesos));

        /* Tratamento estado enLendoDados */
        svSwitchSuperstate(enEntregandoProduto);
    break;

    case enAguardandoRecipiente:
        if(stAPP.subestado == 0){
        #ifdef __DEBUG_APP
        Serial.println("APP: Estado enAguardandoRecipiente");
        #endif
        stAPP.subestado = 1;
        }

        /* Se detectar um recipiente volta a entregar o produto */
        if(bPROX_DetectaEmbalagem())
        {
        #ifdef __DEBUG_APP
        Serial.println("APP: Recipiente detectado");
        #endif
          svSwitchSuperstate(enEntregandoProduto);
        }

        
    break;

    case enEntregandoProduto:
        if(stAPP.subestado == 0){
        #ifdef __DEBUG_APP
        Serial.println("APP: Estado enEntregandoProduto");
        #endif
        stAPP.subestado = 1;
        }
        static uint8_t atual = 0;

        /* Se não detectar um recipiente */
        if(!bPROX_DetectaEmbalagem())
        {
        #ifdef __DEBUG_APP
        Serial.println("APP: Recipiente não detectado");
        #endif
          svSwitchSuperstate(enAguardandoRecipiente);
          break;
        }
        
        if(ui16MED_LeituraPeso(atual) < stAPP.pesos[atual] - MARGEM_BAIXA*stAPP.pesos[atual])
        {
          //Abrir a porta bastante
          if(bPORTA_SetaAbertura((enPorta)atual, portaAberta))
          {
            
          }
          else
          {
            svRegistraErro(enErroPortas);
          }
        }
        else if(ui16MED_LeituraPeso(atual) < stAPP.pesos[atual] - MARGEM_MEDIA*stAPP.pesos[atual])
        {
          //Abrir a porta não tanto
          if(bPORTA_SetaAbertura((enPorta)atual, portaPoucoFechada))
          {
            
          }
          else
          {
            svRegistraErro(enErroPortas);
          }
        }
        else if(ui16MED_LeituraPeso(atual) < stAPP.pesos[atual] - MARGEM_ALTA*stAPP.pesos[atual])
        {
          //Abrir a porta um pouco
          if(bPORTA_SetaAbertura((enPorta)atual, portaPoucoAberta))
          {
            
          }
          else
          {
            svRegistraErro(enErroPortas);
          }
        }
        else if(ui16MED_LeituraPeso(atual) > stAPP.pesos[atual])
        {
          //Fechar a porta
          if(bPORTA_SetaAbertura((enPorta)atual, portaFechada))
          {
            
          }
          else
          {
            svRegistraErro(enErroPortas);
          }
          
          //Ir para proxima caixa
          atual++;
        }
        
        /* Se fechou todas as caixas */
        if(atual == 3)
        {
          atual = 0;
          svSwitchSuperstate(enFechaConexao);
        }
    break;

    case enFechaConexao:
        if(stAPP.subestado == 0){
        #ifdef __DEBUG_APP
        Serial.println("APP: Estado enFechaConexao");
        #endif
        stAPP.subestado = 1;
        }
        /* Tratamento estado enFechaConexao */

        vCONN_FechaConexao();
        
    break;

    case enErro:
        if(stAPP.subestado == 0){
        #ifdef __DEBUG_APP
        Serial.println("APP: Estado enErro");
        #endif
        stAPP.subestado = 1;
        }
        /* Tratamento estado enErro */
        svSwitchSuperstate(enEsperaConexao);
    break;

    default:
        #ifdef __DEBUG_APP
        Serial.println("APP: Estado Inválido");
        #endif
        delay(1000);
      
    break;
    
  }
  
}

/* Funcao para registrar erros ocorridos */
static void svRegistraErro(enVMGError erro)
{
  stAPP.erro = erro;
  #ifdef __DEBUG_APP
    Serial.print("APP: Erro ");
  #endif
  switch(erro)
  {
    case enErroEstoque:
      #ifdef __DEBUG_APP
      Serial.println("falta de estoque");
      #endif
      stAPP.erro = enErroEstoque;
      break;
      
    case enErroMedida:
      #ifdef __DEBUG_APP
      Serial.println("de medida");
      #endif
      stAPP.erro = enErroMedida;
      break;
      
    case enErroPortas:
      #ifdef __DEBUG_APP
      Serial.println("de portas");
      #endif
      stAPP.erro = enErroPortas;
    break;
      
    default:
      #ifdef __DEBUG_APP
      snprintf(debugBuffer, 50, "inválido - %d\n", stAPP.erro);
      Serial.printf(debugBuffer);
      #endif
      stAPP.erro = enErroNenhum;
      break;
  }

  svSwitchSuperstate(enErro);
}

static void svSwitchSuperstate(enVMGStates estadoDesejado)
{
  stAPP.ultimo_estado = stAPP.superestado;
  stAPP.superestado = estadoDesejado;
  stAPP.subestado = 0;
}

static void svAdicionaCreditos(STRUCT_MSG msg)
{
  
}
