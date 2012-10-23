/*
Projeto Medida
*/

struct Medida
{
    // each color is only 4 bits but stored as an 8-bit type
    float corrente;
    float tensao;
    long timeStamp;
    struct Medida *next; // Ponteiro para a próxima medida
};

Medida* head;
Medida* tail;
int listSize;

String response;

// the setup routine runs once when you press reset:
void setup() {                
  
  Serial.begin(57600);
  initMedida();
}

// the loop routine runs over and over again forever:
void loop() {
  insertNewMedida(0.1,0.1,1);
  reponse = generateJSON();
  delay(1000);
  Serial.print(reponse);
  Serial.print("\n");
}


void generateJSON()
{
  Medida* pAux = head;
  float tensao;
  
  //Inicio do json
  String jsonResponse = "{ \"medidas\" : [";
  
  //Primeira passada
  jsonResponse += "{ \"tensao\" : ";
  jsonResponse += doubleToString(pAux->tensao);
  jsonResponse += ", \"corrente\" : ";
  jsonResponse += doubleToString(pAux->corrente);
  jsonResponse += ", \"timestamp\" : ";
  jsonResponse += pAux->timeStamp;
  jsonResponse += "}";
  
  pAux = pAux->next;

  //Passadas intermediarias  
  //2000 maior numero possivel em teste (testado foi 2251)
  while (pAux != NULL && jsonResponse.length < 2000)
  {
    jsonResponse += ",{ \"tensao\" : ";
    jsonResponse += doubleToString(pAux->tensao);
    jsonResponse += ", \"corrente\" : ";
    jsonResponse += doubleToString(pAux->corrente);
    jsonResponse += ", \"timestamp\" : ";
    jsonResponse += pAux->timeStamp;
    jsonResponse += "}";
    
    pAux = pAux->next;
  }
  
  //Todas as medidas foram colocadas no json
  if(pAux == NULL){
//    freeList();
  }
  
  //Nem todas as medidas foram colocadas no json, falta de espaco
  else{
//    freeListUntilMarker(pAux);
  }
  
  //Fechamento do objeto json
  jsonResponse += "]}";
  
  Serial.print(jsonResponse);
}


//Insere uma nova medida na lista ligada
bool insertNewMedida(float corrente, float tensao, int timeStamp)
{
	Medida* ponteiroAux = (Medida *) malloc (sizeof(Medida));
	if (ponteiroAux == NULL)
	{
		//Memória Insuficiente
		//Erro
		return false;
	}
	ponteiroAux->corrente = corrente;
	ponteiroAux->tensao = tensao;
	ponteiroAux->timeStamp = timeStamp;
	ponteiroAux->next = NULL;
	if(tail != NULL)
	{
		tail->next = ponteiroAux;
	}
	if(head == NULL 
		|| listSize == 0 
		|| tail == NULL)
	{
		head = ponteiroAux;
	}
	tail = ponteiroAux;
	listSize++;
	
	return true;
}

// Inicializa a lista ligada de medidas
void initMedida()
{
	head = NULL;
	tail = NULL;
	listSize = 0;
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
	Medida * pAux;
	while (head != NULL)
	{
		pAux = head->next;
		//Free pAux
		head = pAux;
		listSize --;
	}
	
	tail = NULL;	
}

//Libera a lista até o ponteiro indicado
void freeListUntilMarker (struct Medida *marker)
{
	Medida * pAux;
	while (head != NULL && head != marker)
	{
            pAux = head->next;
            free(head);
	    head = pAux;
	    listSize --;
	}
}

//Percorre a lista inteira, e retorna o número de nós. Usada para debug.
int getListSize(){
  Medida * pAux = head;
  int lSize = 0;
  while (pAux != NULL){
    lSize ++;
    pAux = pAux->next;
  }
  return lSize;
}
