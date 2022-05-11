#ifndef __MOTORES_VIBRA_H
#define __MOTORES_VIBRA_H

typedef enum{
  enMotor1 = 0,
  enMotor2,
  enMotor3
}enMotor;

/* Liga um motor de vibração. */
void vLigaMotor(enMotor motor);

/* Desliga um motor de vibração. */
void vDesligaMotor(enMotor motor);

#endif
