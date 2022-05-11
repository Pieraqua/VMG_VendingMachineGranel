#ifndef __MOTORES_VIBRA_H
#define __MOTORES_VIBRA_H

typedef enum{
  enMotor1 = 0,
  enMotor2,
  enMotor3
}enMotor;

void vLigaMotor(enMotor motor);

void vDesligaMotor(enMotor motor);

#endif
