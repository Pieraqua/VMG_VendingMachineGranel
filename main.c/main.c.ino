/* Programa da VMG para Oficinas de Integração 2 */
#include "app.h"

/* Systick timer */
hw_timer_t * timer = NULL;

/* Systick handler */
void IRAM_ATTR SysTickHandler(void)
{
  if(stPortas.portas[0].moveTimer)
  {
    stPortas.portas[0].moveTimer--;
  }
  if(stPortas.portas[1].moveTimer)
  {
    stPortas.portas[1].moveTimer--;
  }
  if(stPortas.portas[2].moveTimer)
  {
    stPortas.portas[2].moveTimer--;
  }
  
}

void setup() {
  // put your setup code here, to run once:
  Serial.begin(115200);
  vMED_Init();
  
  vAPP_Init();

  vPORTA_Init();

  //vMOTOR_Init();

  //vPROX_Init();

  vCONN_Init();

  /* Systick a cada milisegundo */
  timer = timerBegin(0, 80, true);
  timerAttachInterrupt(timer, &SysTickHandler, true);
  timerAlarmWrite(timer, 1000, true);
  timerAlarmEnable(timer);
}

void loop() {
  // put your main code here, to run repeatedly:

  while(1)
  { 
    #ifdef __DEBUG_MAIN
    Serial.println("Loop main!");
    #endif
    //vAPP_Poll();
    //vPORTA_Poll();
    vCONN_Poll();
    
  }

}
