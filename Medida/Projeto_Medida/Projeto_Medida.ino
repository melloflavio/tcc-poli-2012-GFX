/*
Projeto Medida
 */

#define BURST 300 //460 ta funcionando sem os maiores prints
#define SEC 5 // number os seconds to calculate power
#define MBLOCK 3
#define MINUTES 15
#define CONVCTE 476.19048 // conversion constant to get the real values in the input of the system

#include <Time.h>

//*********************************
//Configuração POST

#include <WiServer.h>

//Parametros globais a ser enviados
unsigned long postStartStamp;
unsigned long postLength;
float postActivePower;
float postFP;

#define WIRELESS_MODE_INFRA	1
#define WIRELESS_MODE_ADHOC	2

// Wireless configuration parameters ----------------------------------------
unsigned char local_ip[] = {192,168,1,201};	// IP address of WiShield
unsigned char gateway_ip[] = {192,168,1,1};	// router or gateway IP address
unsigned char subnet_mask[] = {255,255,255,0};	// subnet mask for the local network
const prog_char ssid[] PROGMEM = {"mellocel"};		// max 32 bytes

unsigned char security_type = 0;	// 0 - open; 1 - WEP; 2 - WPA; 3 - WPA2

// WPA/WPA2 passphrase
const prog_char security_passphrase[] PROGMEM = {};	// max 64 characters

// WEP 128-bit keys
// sample HEX keys
prog_uchar wep_keys[] PROGMEM = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d,	// Key 0
				  0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,	// Key 1
				  0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,	// Key 2
				  0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00	// Key 3
				};

// setup the wireless mode
// infrastructure - connect to AP
// adhoc - connect to another WiFi device
unsigned char wireless_mode = WIRELESS_MODE_INFRA;

uint8 ip[] = {192,168,1,143};

POSTrequest postToServer(ip, 80, "192.168.1.143", "/TCC/tcc-poli-2012-GFX/post.php", postBody);

// Fim Configuração POST
//***************************************

struct Medida
{
  // each color is only 4 bits but stored as an 8-bit type
  float corrente;
  float tensao;
  unsigned long curStamp;
  unsigned long volStamp;
  //struct Medida *next; // Ponteiro para a próxima medida
};

struct Power
{
  // each color is only 4 bits but stored as an 8-bit type
  float activePower;
  float fp;
  //struct Medida *next; // Ponteiro para a próxima medida
};



//Medida* head;
//Medida* tail;
//int listSize;

float fp;
Power instPow; // Instant power measurement
Power consPow[1]; // Consumed power per minute
unsigned long startStamp; // Moment of start of the power measurement
unsigned long endStamp; // Moment of end of the power measurement
int fpCount;
//unsigned maxCons; // Maximum consume
//int minFP; // Minimum fp
//int instant; // Instant of measurement(can very from 0 to RMS)

String response;

// the setup routine runs once when you press reset:
void setup() {                

  Serial.begin(57600);
  
  //Post
  // Initialize WiServer (we'll pass NULL for the page serving function since we don't need to serve web pages) 
  WiServer.init(NULL);
  // Enable Serial output and ask WiServer to generate log messages (optional)
  WiServer.enableVerboseMode(true);
  // Have the processData function called when data is returned by the server
  postToServer.setReturnFunc(printData);
//  Serial.print
  
}

boolean closedConnection = true;
// the loop routine runs over and over again forever:
void loop() {
//  insertNewMedida(0.1,0.1,1);
//  reponse = generateJSON();
//  delay(1000);
//  Serial.print(reponse);
//  Serial.print("\n");
//  while(true){Serial.print("\n"); Serial.print(CONVCTE, 6);}
if (closedConnection){
  closedConnection = false;
  getRMS();
  
  postStartStamp = startStamp;
  postLength = 1;
  postActivePower = consPow[0].activePower;
  postFP = consPow[0].fp;
  
  postToServer.submit();
}
  
  WiServer.server_task();
}


void generateJSON()
{
  //Medida* pAux = head;
  float tensao;

  //Inicio do json
  String jsonResponse = "{ \"medidas\" : [";

  //Primeira passada
  jsonResponse += "{ \"tensao\" : ";
  //jsonResponse += doubleToString(pAux->tensao);
  jsonResponse += ", \"corrente\" : ";
  //jsonResponse += doubleToString(pAux->corrente);
  jsonResponse += ", \"timestamp\" : ";
  //jsonResponse += pAux->timeStamp;
  jsonResponse += "}";

  //pAux = pAux->next;

  //Passadas intermediarias  
  //2000 maior numero possivel em teste (testado foi 2251)
//  while (pAux != NULL && jsonResponse.length < 2000)
//  {
//    jsonResponse += ",{ \"tensao\" : ";
//    jsonResponse += doubleToString(pAux->tensao);
//    jsonResponse += ", \"corrente\" : ";
//    jsonResponse += doubleToString(pAux->corrente);
//    jsonResponse += ", \"timestamp\" : ";
//    jsonResponse += pAux->timeStamp;
//    jsonResponse += "}";
//
//    pAux = pAux->next;
//  }

  //Todas as medidas foram colocadas no json
//  if(pAux == NULL){
//    //    freeList();
//  }

  //Nem todas as medidas foram colocadas no json, falta de espaco
//  else{
//    //    freeListUntilMarker(pAux);
//  }

  //Fechamento do objeto json
  jsonResponse += "]}";

  Serial.print(jsonResponse);
}


////Insere uma nova medida na lista ligada
//bool insertNewMedida(float corrente, float tensao, int timeStamp)
//{
//	Medida* ponteiroAux = (Medida *) malloc (sizeof(Medida));
//	if (ponteiroAux == NULL)
//	{
//		//Memória Insuficiente
//		//Erro
//		return false;
//	}
//	ponteiroAux->corrente = corrente;
//	ponteiroAux->tensao = tensao;
//	ponteiroAux->timeStamp = timeStamp;
//	ponteiroAux->next = NULL;
//	if(tail != NULL)
//	{
//		tail->next = ponteiroAux;
//	}
//	if(head == NULL 
//		|| listSize == 0 
//		|| tail == NULL)
//	{
//		head = ponteiroAux;
//	}
//	tail = ponteiroAux;
//	listSize++;
//	
//	return true;
//}

// Executes the burst measurements and than gets the rms values
void burstSample()
{
  int currentPin = 1;
  int voltagePin = 0;
  int numSamplesCur = BURST;
  int numSamplesVol = BURST;
 
  float currentValue = 0;
  float voltageValue = 0;
  int i;

  Medida sample[BURST];
  int maxCurrent = 0;
  int maxVoltage = 0;


  float corrente = 0;
  float tensao = 0;
  
  
  // analogRead() takes 100us(0.0001s) to read a aPin and have a resolution of 5V/1024units = 0.0049V/unit
  // acho q no fim das contas a melhor forma de ler ' dando burst na corrente e depois na tensao, falta verificar isso
  for(i = 0; i < numSamplesCur; i++) {
    sample[i].corrente = 0.0049*analogRead(currentPin);
    sample[i].curStamp = micros();
    if(sample[i].corrente == 0) {
      i--;
      numSamplesCur--;
    }
//    sample[i].corrente = analogRead(currentPin);
//    sample[i].tensao = analogRead(voltagePin);
//    delay(10);
//    sample[i].tensao = analogRead(voltagePin);
//    delay(10);
//    delayMicroseconds(500);
  } 
    
  for(i = 0; i < numSamplesVol; i++) {
    sample[i].tensao = 0.0049*analogRead(voltagePin);
    sample[i].volStamp = micros();
    if(sample[i].tensao == 0) {
      i--;
      numSamplesVol--;
    }
  }
    
  
//  for(i = 0; i < numSamplesCur; i++) {
//    Serial.print(numSamplesCur);
//    Serial.print("\t");
//    Serial.print(i);
//    Serial.print("\t");
//    Serial.print(sample[i].corrente, 5);
//    Serial.print("\n");    
//  }
//  
//  Serial.print("\n");
//  
//  for(i = 0; i < numSamplesVol; i++) {
//    Serial.print(numSamplesVol);
//    Serial.print("\t");
//    Serial.print(i);
//    Serial.print("\t");
//    Serial.print(sample[i].tensao, 5);
//    Serial.print("\n");    
//  }
    
//  while(true){}
    
  // Start the calculations of the Irms
  for(i = 0; i < numSamplesCur; i++) {
    currentValue += (sample[i].corrente * sample[i].corrente);   
    if(sample[i].corrente > sample[maxCurrent].corrente) {
      maxCurrent = i;
    }
  }
  
  // Start the calculations of the Vrms
  for(i = 0; i < numSamplesVol; i++) {
    voltageValue += (sample[i].tensao * sample[i].tensao);    
    if(sample[i].tensao > sample[maxVoltage].tensao) {
      maxVoltage = i;
    }  
  }  
  
  if(numSamplesCur > 0) {
    currentValue = sqrt(currentValue / numSamplesCur);
  }
  if(numSamplesVol > 0) {
    voltageValue = sqrt(voltageValue / numSamplesVol); 
  }
  
  i = 0;
  
//  Dessa forma se obtem um fator e potencia negativo, porem muito proximo do ideal.
//  instPow.fp = cos((sample[maxVoltage].volStamp - sample[maxCurrent].curStamp) * 0.000001 * 120 * M_PI);
  
  instPow.activePower = currentValue * voltageValue * CONVCTE;
  
  if(instPow.activePower != 0) {
    instPow.fp = cos(120 * M_PI * (sample[maxVoltage].volStamp - sample[maxCurrent].curStamp) * 0.001 * 0.001);
    if(instPow.fp < 0) {
      instPow.fp = -instPow.fp;
    }
    consPow[i].fp += instPow.fp;
  }
  else {
    instPow.fp = 0;
    fpCount--;
  }
  
  Serial.print("SamplesCur: ");
  Serial.print(numSamplesCur);
  Serial.print("\t CurStamp: ");
  Serial.print(sample[maxCurrent].curStamp);
  Serial.print("\t VolStamp: ");
  Serial.print(sample[maxVoltage].volStamp);
  Serial.print("\t Current: ");  
  Serial.print(currentValue, 5);
  Serial.print("\t Voltage: ");
  Serial.print(voltageValue, 5);
  Serial.print("\t ApPower: ");
  Serial.print(instPow.activePower);
  Serial.print("\t FP: ");
  Serial.print(instPow.fp);
  Serial.print("\t ActPower: ");
  
  instPow.activePower *= instPow.fp;
  
  Serial.print(instPow.activePower);
  Serial.print("\t SumActPower: ");    
  
  consPow[i].activePower += instPow.activePower;
  
  Serial.print(consPow[i].activePower);
  Serial.print("\t SumFP: ");  
  Serial.print(consPow[i].fp);
  Serial.print("\n");  
  
}




/* 
 *  Gets the rms values of current and tension of the minute, also gets de fp of the line
*/
void getRMS() {
  
  int i = 0;
  unsigned long milliSeconds;
  
  instPow.activePower = 0;
  instPow.fp = 0;
  fpCount = SEC;
  
  consPow[i].activePower = 0;
  consPow[i].fp = 0;
  
  Serial.print("\n");
  
  startStamp = now();
  
  // Chama o burstSample() RMS vezes, de 1 em 1 segundo
  for(i = 0; i < SEC; i++) {
    milliSeconds = millis();
    burstSample();
//    Serial.print(i);
    // holds the measurements to happen from second to second
    while(millis() - milliSeconds < 1000) {}
  }
  
  endStamp = now();
  
  i = 0;
  
  // Now we have the power medium value for the minute
  consPow[i].activePower /= SEC;
  consPow[i].fp /= fpCount;
    
  Serial.print("\n\n");
  Serial.print("Start: ");
  Serial.print(startStamp);  
  Serial.print("\t End: ");
  Serial.print(endStamp);  
  Serial.print("\t Active Power: ");
  Serial.print(consPow[i].activePower, 5);
  Serial.print("\t FP: ");
  Serial.print(consPow[i].fp, 5);
  Serial.print("\n");
  
  
  
//  encodeJSON(startStamp, endStamp, consPow[i].activePower, consPow[i].fp);
  
//  while(!sent) {WiServer.server_task();}
  
}

// Gets the max power consume and minimum fp values of the 15 minutes
void getConsume() {
  
  int i;
  //int j = 0;
  
//  maxCons = 0;
//  minFP = 1;
  
  startStamp = now();
  
  // TODO: alterar rotina para chamar o getRMS() MINUTES vezes, de 1 em 1 minuto
  for(i = 0; i < MINUTES; i++) {
    getRMS();
    
    
//    Serial.print(consPow[i].activePower);
//    Serial.print("\n");
//    Serial.print(consPow[i].fp);
//    Serial.print("\n");
    
//    if(i % 5 == 0 && i != 0) {
//      consPow[j].activePower = consPow[j].activePower / 5;
//      consPow[j].fp = consPow[j].fp / 5;
//      
//      if(consPow[j].activePower > maxCons) {
//        maxCons = consPow[j].activePower;
//      }
//      if(consPow[j].fp < minFP) {
//        minFP = consPow[j].fp;
//      }
//      
//      j++;
//    }
//    
//  }
//  
//  consPow[j].activePower = consPow[j].activePower / 5;
//  consPow[j].fp = consPow[j].fp / 5;
//  
//  if(consPow[j].activePower > maxCons) {
//    maxCons = consPow[j].activePower;
//  }
//  if(consPow[j].fp < minFP) {
//    minFP = consPow[j].fp;
  }
  
  endStamp = now();
 
}



// Inicializa a lista ligada de medidas
void initMedida()
{
//  head = NULL;
//  tail = NULL;
//  listSize = 0;
}

//Converte um valor float para um string
String doubleToString( double val){

  String placeholder = "";
  char tmp[25];
  dtostrf(val,1,10,tmp);//1 -> numero mnimo de casas, 10-> numero de casas depois da virgula
  String strOut = placeholder + tmp;
  return strOut;
}

//Libera a lista inteira
void freeList ()
{
//  Medida * pAux;
//  while (head != NULL)
//  {
//    pAux = head->next;
//    //Free pAux
//    head = pAux;
//    listSize --;
//  }
//
//  tail = NULL;	
}

//Libera a lista até o ponteiro indicado
void freeListUntilMarker (struct Medida *marker)
{
//  Medida * pAux;
//  while (head != NULL && head != marker)
//  {
//    pAux = head->next;
//    free(head);
//    head = pAux;
//    listSize --;
//  }
}

//Percorre a lista inteira, e retorna o número de nós. Usada para debug.
int getListSize(){
//  Medida * pAux = head;
//  int lSize = 0;
//  while (pAux != NULL){
//    lSize ++;
//    pAux = pAux->next;
//  }
//  return lSize;
}








////// Parte de POST

//Já adicionado acima
//#include <WiServer.h>

//Parametros globais a ser enviados
//unsigned long postStartStamp;
//unsigned long postLength;
//float postActivePower;
//float postFP;

/*
#define WIRELESS_MODE_INFRA	1
#define WIRELESS_MODE_ADHOC	2

// Wireless configuration parameters ----------------------------------------
unsigned char local_ip[] = {192,168,1,2};	// IP address of WiShield
unsigned char gateway_ip[] = {192,168,1,1};	// router or gateway IP address
unsigned char subnet_mask[] = {255,255,255,0};	// subnet mask for the local network
const prog_char ssid[] PROGMEM = {"mellocel"};		// max 32 bytes

unsigned char security_type = 0;	// 0 - open; 1 - WEP; 2 - WPA; 3 - WPA2

// WPA/WPA2 passphrase
const prog_char security_passphrase[] PROGMEM = {};	// max 64 characters

// WEP 128-bit keys
// sample HEX keys
prog_uchar wep_keys[] PROGMEM = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d,	// Key 0
				  0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,	// Key 1
				  0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,	// Key 2
				  0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00	// Key 3
				};

// setup the wireless mode
// infrastructure - connect to AP
// adhoc - connect to another WiFi device
unsigned char wireless_mode = WIRELESS_MODE_INFRA;
*/
unsigned char ssid_len;
unsigned char security_passphrase_len;
// End of wireless configuration parameters ----------------------------------------


// Function that prints data from the server
void printData(char* data, int len) {
    if(len == 0) {
      closedConnection = true;
    }

  while (len-- > 0) {
    Serial.print(*(data++));
  } 
}


// IP Address for www.weather.gov  
//uint8 ip[] = {192,168,1,143};

// A request that gets the latest METAR weather data for LAX
//GETrequest getWeather(ip, 80, "192.168.1.143", "/TCC/post.php");
//POSTrequest postToServer(ip, 80, "192.168.1.143", "/TCC/tcc-poli-2012-GFX/post.php", postBody);


// Time (in millis) when the data should be retrieved 
//long updateTime = 0;


void postBody ()
{
  WiServer.print(encodeJSON(postStartStamp, postLength, postActivePower, postFP));
}

String encodeJSON(unsigned long startStamp, unsigned long endStamp, float activePower,  float fp)
{
  //Medida* pAux = head;
  float tensao;

  //Inicio do json
  String jsonResponse = "{\"id\":\"1\",\"s\":";
  Serial.print ("Start = ");
  Serial.print (startStamp);
  Serial.print ("\n");
  jsonResponse += startStamp;
  jsonResponse +=",\"i\":"
  jsonResponse += endStamp;
  jsonResponse += ",\"c\":"+doubleToString(activePower)+",\"fp\":"+doubleToString(fp)+"}";
  Serial.print("Encoded");
  Serial.print(jsonResponse);
//  String jsonResponse = "{\"1\":\"2\"}";
  return jsonResponse;
}
