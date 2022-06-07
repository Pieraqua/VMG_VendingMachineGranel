#include "motoresVibra.h"



void vMOTOR_Init()
{
  
 /* Motor 1 enable */
 pinMode(MOTOR1, OUTPUT); 
 /* Motor 2 enable */
 pinMode(MOTOR2, OUTPUT);
 /* Motor 3 enable */
 pinMode(MOTOR3, OUTPUT);
 
}

/* Liga um motor de vibração. */
void vLigaMotor(uint8_t motor)
{
  switch(motor)
  {
    case enMotor1:
      digitalWrite(MOTOR1, HIGH);
    break;
    
    case enMotor2:
      digitalWrite(MOTOR2, HIGH);
    break;
    
    case enMotor3:
      digitalWrite(MOTOR3, HIGH);
    break;

    default:
    break;
    
  }
  
}

/* Desliga um motor de vibração. */
void vDesligaMotor(uint8_t motor)
{
  switch(motor)
  {
    case enMotor1:
     digitalWrite(MOTOR1, LOW);
    break;
    
    case enMotor2:
      digitalWrite(MOTOR2, LOW);
    break;
    
    case enMotor3:
      digitalWrite(MOTOR3, LOW);
    break;
    
    default:
    break;
  }
}
