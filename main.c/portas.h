#ifndef __PORTAS_H
#define __PORTAS_H


typedef enum{
  enPorta_1 = 0,
  enPorta_2,
  enPorta_3,
  enPorta_Nenhuma
}enPorta;

typedef enum{
  portaFechada = 0,
  portaPoucoAberta,
  portaPoucoFechada,
  portaAberta
}enAberturaPorta;

typedef struct{
  enPorta numeroPorta;
  enAberturaPorta aberturaAtual;
  enAberturaPorta aberturaDesejada;
  bool portaAtivada;
}STRUCT_PORTA;

typedef struct{
  enPorta portaAtual;
  STRUCT_PORTA portas[3];
}STRUCT_CTL_PORTAS;

bool bPORTA_SetaAbertura(enPorta porta, enAberturaPorta abertura);

#endif __PORTAS_H
