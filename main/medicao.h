#ifndef __MEDICAO_H
#define __MEDICAO_H

#include <Wire.h>

#define I2C_SDA1 34
#define I2C_SDA2 25
#define I2C_SDA3 26
#define I2C_SDA4 27
#define I2C_SCK 13

void vMED_Init();

/* Mede o peso da balança da caixa peso e retorna o peso */
uint16_t ui16MED_LeituraPeso(uint8_t caixa);

#endif /* __MEDICAO_H */
