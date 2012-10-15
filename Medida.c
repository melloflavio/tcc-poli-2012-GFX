# include <stdio.h>
# include <malloc.h>
# include "Medida.h"

#define TRUE 1
#define FALSE 0

Medida* head;

Medida* tail;

int listSize;

void initMedida()
{
	head = NULL;
	tail = NULL;
	listSize = 0;
}

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

int listIsEmpty  ()
{
	if (head == NULL 
		|| listSize == 0 
		|| tail == NULL)
	{
		return TRUE;
	}
	
	return FALSE;
}

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

void generateJSON()
{
	Medida* pAux = head;
	
	while (pAux != NULL)
	{
		printf("Entrou\n");
		printf("%f\n",pAux->tensao);
		pAux = pAux->next;
		printf("pointer %d\n\n\n",pAux);
	}
}
