/* Programa da VMG para Oficinas de Integração 2 */
#include "app.h"

void setup() {
  // put your setup code here, to run once:
  Serial.begin(115200);
  vAPP_Init();
}

void loop() {
  // put your main code here, to run repeatedly:

  while(1)
  { 
    vAPP_Poll();
    
  }

}
