#ifndef __PORTAS_H
#define __PORTAS_H

#include <ESP32Servo.h>
#include <analogWrite.h>
#include <ESP32PWM.h>

typedef enum{
  enPorta_Nenhuma = 6,
  enPorta_1 = 0,
  enPorta_2,
  enPorta_3,
  
}enPorta;

typedef enum{
  portaFechada = 40,
  portaPoucoAberta = 25,
  portaPoucoFechada = 10,
  portaAberta = 0
}enAberturaPorta;

typedef struct{
  enPorta numeroPorta;
  uint8_t aberturaAtual;
  uint8_t aberturaDesejada;
  Servo servo;
  volatile int16_t moveTimer;
}STRUCT_PORTA;

typedef struct{
  enPorta portaAtual;
  STRUCT_PORTA portas[3];
  bool portaAtivada;
}STRUCT_CTL_PORTAS;

extern STRUCT_CTL_PORTAS stPortas;

/* Deixa a porta *porta* *abertura* aberta. */
bool bPORTA_SetaAbertura(enPorta porta, enAberturaPorta abertura);

enAberturaPorta getAberturaPorta(enPorta porta);

void vPORTA_Init();

void vPORTA_Poll();

#endif /* __PORTAS_H */
