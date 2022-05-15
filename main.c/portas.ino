#include "portas.h"


STRUCT_CTL_PORTAS stPortas = 
{
  .portaAtual = enPorta_Nenhuma,
  .portaAtivada = false,
  
};

/* Tem certeza de que todas as portas inicializam fechadas */
void vPORTA_Init()
{
  stPortas.portas[0].aberturaAtual = portaAberta;
  stPortas.portas[1].aberturaAtual = portaAberta;
  stPortas.portas[2].aberturaAtual = portaAberta;
  stPortas.portas[0].aberturaDesejada = portaFechada;
  stPortas.portas[1].aberturaDesejada = portaFechada;
  stPortas.portas[2].aberturaDesejada = portaFechada;
  stPortas.portas[0].numeroPorta = enPorta_1;
  stPortas.portas[1].numeroPorta = enPorta_2;
  stPortas.portas[2].numeroPorta = enPorta_3;

  stPortas.portas[0].servo.attach(23);
  stPortas.portas[0].servo.attach(3);
  stPortas.portas[0].servo.attach(18);


  /* Configura os servos para suas posições iniciais (fechado) */
  while(stPortas.portas[0].aberturaAtual != stPortas.portas[0].aberturaDesejada &&
          stPortas.portas[1].aberturaAtual != stPortas.portas[1].aberturaDesejada &&
            stPortas.portas[2].aberturaAtual != stPortas.portas[2].aberturaDesejada){
    /* Para cada porta, */
    for(int i = 0; i < 3; i++){
      /* Se a porta terminou seu último movimento, */
      if(stPortas.portas[i].moveTimer == 0)
      {
        /* Mova a porta para cima ou para baixo, de acordo com a abertura atual e desejada. */
        if(stPortas.portas[i].aberturaAtual < stPortas.portas[i].aberturaDesejada)
        { 
          stPortas.portas[i].aberturaAtual++;
          stPortas.portas[i].servo.write(stPortas.portas[i].aberturaAtual);
        }
        else if(stPortas.portas[i].aberturaAtual > stPortas.portas[i].aberturaDesejada)
        {
          stPortas.portas[i].aberturaAtual--;
          stPortas.portas[i].servo.write(stPortas.portas[i].aberturaAtual);
        }
      }
    }
    delay(15);
  }
}

/* Deixa a porta *porta* *abertura* aberta. */
bool bPORTA_SetaAbertura(enPorta porta, enAberturaPorta abertura)
{
  stPortas.portas[porta].aberturaDesejada = abertura;

  
  return true;
}

/* Função de polling que controla as portas */
void vPORTA_Poll()
{
  /* Para cada porta, */
  for(int i = 0; i < 3; i++){
    /* Se a porta terminou seu último movimento, */
    if(stPortas.portas[i].moveTimer <= 0)
    {
      /* Mova a porta para cima ou para baixo, de acordo com a abertura atual e desejada. */
      if(stPortas.portas[i].aberturaAtual < stPortas.portas[i].aberturaDesejada)
      { 
        stPortas.portas[i].aberturaAtual++;
        stPortas.portas[i].servo.write(stPortas.portas[i].aberturaAtual);
      }
      else if(stPortas.portas[i].aberturaAtual > stPortas.portas[i].aberturaDesejada)
      {
        stPortas.portas[i].aberturaAtual--;
        stPortas.portas[i].servo.write(stPortas.portas[i].aberturaAtual);
      }

      /* Seta o timer da porta para 15ms */
      stPortas.portas[i].moveTimer = 15;
    }
  }
}
