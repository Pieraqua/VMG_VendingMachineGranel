#include "portas.h"


STRUCT_CTL_PORTAS stPortas = 
{
  .portaAtual = enPorta_Nenhuma,
  .portaAtivada = false,
  
};

/* Tem certeza de que todas as portas inicializam fechadas */
void vPORTA_Init()
{
  stPortas.portas[0].aberturaAtual = portaFechada;
  stPortas.portas[1].aberturaAtual = portaFechada;
  stPortas.portas[2].aberturaAtual = portaFechada;
  stPortas.portas[0].aberturaDesejada = portaFechada;
  stPortas.portas[1].aberturaDesejada = portaFechada;
  stPortas.portas[2].aberturaDesejada = portaFechada;
  stPortas.portas[0].numeroPorta = enPorta_1;
  stPortas.portas[1].numeroPorta = enPorta_2;
  stPortas.portas[2].numeroPorta = enPorta_3;
}

/* Deixa a porta *porta* *abertura* aberta. */
bool bPORTA_SetaAbertura(enPorta porta, enAberturaPorta abertura)
{
  return false;
}
