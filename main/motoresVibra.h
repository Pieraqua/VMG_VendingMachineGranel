#ifndef __MOTORES_VIBRA_H
#define __MOTORES_VIBRA_H

#define MOTOR1 1
#define MOTOR2 19
#define MOTOR3 16

typedef enum{
  enMotor1 = 0,
  enMotor2,
  enMotor3
}enMotor;


void vMOTOR_Init();

/* Liga um motor de vibração. */
void vLigaMotor(enMotor motor);

/* Desliga um motor de vibração. */
void vDesligaMotor(enMotor motor);


#endif
