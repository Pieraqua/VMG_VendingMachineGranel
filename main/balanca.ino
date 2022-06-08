#include "HX711.h"                    // Biblioteca HX711 
#define DOUT1  34                      // HX711 DATA pin
#define DOUT2  25
#define DOUT3  26
#define DOUT4  27
#define CLK  13                       // HX711 SCK pin

struct calibration
{
  float calA;
  float calB;
} calibrations[4];

struct offset
{
  float offSetA;
  float offSetB;
} offSets[4];

struct peso
{
  float pesoA;
  float pesoB;
} pesos[4];

HX711 balancas[4];

int map_index_pin[4] = { 34, 25, 26, 27};

float getPesoCaixas(int index, bool comp);
void zeraBalanca(int index, bool comp);

void vBALANCA_Init()
{
  int i = 0;
  //init calibrations
  //for(int i = 0; i<3; i++)
  //{
    Serial.print("Abrindo balanca ");Serial.println(i);
    initBalanca(i, 58400, 116000, true);
    offSets[0].offSetA = 58100/2;
    offSets[0].offSetB = 58100/2;
  //}

  Serial.println("Abrindo balanca embalagem");
  initBalanca(3, 44600,0, false);
  
}


bool balanca_waitready(int index, int timeout, int delay_ms)
{
  return balancas[index].wait_ready_timeout(timeout, delay_ms);
}

void initBalanca(int index, int calA, int calB, bool comp)
{
  calibrations[index].calA = calA;
  if(comp)
    calibrations[index].calB = calB;
}

float getPesoCaixas(int index, bool comp)
{
  balancas[index].begin(map_index_pin[index], CLK, 64);
  if(!balanca_waitready(index, 5000, 0))
  {
    return 0;  
  }
  pesos[index].pesoA = (balancas[index].read() - offSets[index].offSetA)/calibrations[index].calA;
  if(comp){
    balancas[index].begin(map_index_pin[index], CLK, 32); //inicializa o canal B
    if(!balanca_waitready(index, 5000, 0))
    {
      return 0;
    }
    pesos[index].pesoB = (balancas[index].read() - offSets[index].offSetB)/calibrations[index].calB;
    return (pesos[index].pesoA + pesos[index].pesoB);
  }
  return pesos[index].pesoA;
}

void zeraBalanca(int index, bool comp)
{
  offSets[index].offSetA += pesos[index].pesoA*calibrations[index].calA;
  if(comp)
    offSets[index].offSetB += pesos[index].pesoB*calibrations[index].calB;
}
