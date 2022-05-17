#include "medicao.h"

void vMED_Init()
{
  Wire.begin(I2C_SDA, I2C_SCK);
}

/* Mede o peso da balança da caixa peso e retorna o peso */
uint16_t ui16MED_LeituraPeso(uint8_t peso)
{
  return 0;
}
