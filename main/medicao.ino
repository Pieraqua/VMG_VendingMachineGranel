#include "medicao.h"

void vMED_Init()
{
  //Wire.begin(I2C_SDA, I2C_SCK);
  
}
/* Mede o peso da balança da caixa peso e retorna o peso */
uint16_t ui16MED_LeituraPeso(uint8_t caixa)
{
  float peso = 0;
  switch(caixa){
    case 0:
        peso = getPesoCaixas(0, true);
    break;

    case 1:
      peso = getPesoCaixas(1, true);
    break;

    case 2:
      peso = getPesoCaixas(2, true);
    break;  

    case 3:
      peso = getPesoCaixas(3, false);
    break;
  }
  return 1000;
  return (uint16_t)(peso*1000);
}
