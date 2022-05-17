#ifndef __MEDICAO_H
#define __MEDICAO_H

#include <Wire.h>

#define I2C_SDA 34
#define I2C_SCK 14

void vMED_Init();

/* Mede o peso da balança da caixa peso e retorna o peso */
uint16_t ui16MED_LeituraPeso(uint8_t peso);

#endif /* __MEDICAO_H */
