#ifndef __MEDICAO_H
#define __MEDICAO_H

#include "balanca.h"

void vMED_Init();

/* Mede o peso da balança da caixa peso e retorna o peso */
uint16_t ui16MED_LeituraPeso(uint8_t caixa);

#endif /* __MEDICAO_H */
