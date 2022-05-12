#ifndef __PORTAS_H
#define __PORTAS_H


typedef enum{
  enPorta_Nenhuma = 0,
  enPorta_1,
  enPorta_2,
  enPorta_3,
  
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
  
}STRUCT_PORTA;

typedef struct{
  enPorta portaAtual;
  STRUCT_PORTA portas[3];
  bool portaAtivada;
}STRUCT_CTL_PORTAS;

extern STRUCT_CTL_PORTAS stPortas;

/* Deixa a porta *porta* *abertura* aberta. */
bool bPORTA_SetaAbertura(enPorta porta, enAberturaPorta abertura);

void vPORTA_Init();

#endif /* __PORTAS_H */
