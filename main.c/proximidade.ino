#include "proximidade.h"

void vPROX_Init()
{
  pinMode(SENSOR1, INPUT); 
}

bool bPROX_DetectaEmbalagem()
{
  #warning TESTE
  return true;

  if(digitalRead(SENSOR1))
  {
    return true;
  }

  return false;
}
