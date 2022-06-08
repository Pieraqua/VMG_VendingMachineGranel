#include "teste.h"

typedef enum
{
  TesteServos = 0,
  TesteVibra,
  TesteLoop,
  testePortas,
  testePesos
}estadosTeste;

typedef enum
{
  subteste1 = 0,
  subteste2
}subtestes;

subtestes subtesteAtual = subteste1;
estadosTeste testeAtual = testePortas;

void vTESTE_Poll()
{
  int i = 0;
  switch(testeAtual)
  {
      case TesteLoop:
        Serial.println("Loopdieloop");
        delay(500);
      break;

      case testePortas:
        switch(subtesteAtual){
          case subteste1:
            if(!(i%500)){
              Serial.println("Subteste portas 1");
              Serial.println(getAberturaPorta(enPorta_1));
            }
            if(i == 0)
              bPORTA_SetaAbertura(enPorta_1, portaAberta);
            if(getAberturaPorta(enPorta_1) == portaAberta)
              {
                i = 0;
                subtesteAtual = subteste2;
              }
            break;
          case subteste2:
            if(!(i%50)){              
              Serial.println("Subteste portas 2");
              Serial.println(getAberturaPorta(enPorta_1));
            }
            if(i == 0)
              bPORTA_SetaAbertura(enPorta_1, portaFechada);
            if(getAberturaPorta(enPorta_1) == portaFechada)
              {
                i = 0;
                subtesteAtual = subteste1;
              }
            break;
          default:
            break;
        }

      case TesteVibra:
      //vLigaMotor(2);
      default:

      break;

      case testePesos:
        Serial.print("Caixa 1: "); Serial.println(ui16MED_LeituraPeso(0));
        //Serial.print("Caixa 2: "); Serial.println(ui16MED_LeituraPeso(1));
        //Serial.print("Caixa 3: "); Serial.println(ui16MED_LeituraPeso(2));
        Serial.print("Caixa 4: "); Serial.println(ui16MED_LeituraPeso(3));
        delay(200);
      break;

    
  }
  
}
