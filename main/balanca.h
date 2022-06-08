#ifndef __BALANCA_H
#define __BALANCA_H

void vBALANCA_Init();
void initBalanca(int index, int calA, int calB, bool comp);
float getPesoCaixas(int index, bool comp);
void zeraBalanca(int index, bool comp);
bool balanca_waitready(int index, int timeout, int delay_ms);

#endif //__BALANCA_H
