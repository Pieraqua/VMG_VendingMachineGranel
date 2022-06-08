#include "proximidade.h"

void vPROX_Init()
{
  pinMode(SENSOR1, INPUT); 
  pinMode(SENSOR2, INPUT);
}

bool bPROX_DetectaEmbalagem()
{
  #warning TESTE
  return true;

  if(digitalRead(SENSOR1) == HIGH)
  {
    Serial.println("Sensor 1 detected something");
    return true;    
  }

  if(digitalRead(SENSOR2) == HIGH)
  {
    Serial.println("Sensor 2 detected something");
    return true;
  }

  return false;
}
