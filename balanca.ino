#include "HX711.h"                    // Biblioteca HX711 
#define DOUT1  3                      // HX711 DATA pin
#define DOUT2  4
#define DOUT3  5
#define DOUTS  6
#define CLK  2                       // HX711 SCK pin

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

int map_index_pin[4] = { 3, 4, 5, 6};

float getPesoCaixas(int index, bool comp);
void zeraBalanca(int index, bool comp);

void setup()
{
  Serial.begin(9600);            // monitor serial 9600 Bps

  //init calibrations
  for(int i = 0; i<3; i++)
  {
    initBalanca(i, 44600, 83500, true);
  }

  initBalanca(3, 44600,0, false);
  
}

void loop()
{
  
}

void initBalanca(int index, int calA, int calB, bool comp)
{
  calibrations[index].calA = calA;
  if(comp)
    calibrations[index].calB = calB;
}

float getPesoCaixas(int index, bool comp)
{
  balancas[index].begin(map_index_pin[index], CLK, 64);//inicializa o canal A
  pesos[index].pesoA = (balancas[index].read() - offSets[index].offSetA)/calibrations[index].calA;
  if(comp){
    balancas[index].begin(map_index_pin[index], CLK, 32); //inicializa o canal B
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
