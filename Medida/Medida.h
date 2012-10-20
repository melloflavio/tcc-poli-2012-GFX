struct Medida
{
    // each color is only 4 bits but stored as an 8-bit type
    float corrente;
    float tensao;
	long timeStamp;
    struct Medida *next; // Ponteiro para a próxima medida
};

void initMedida();

bool insertNewMedida(float corrente, float tensao, int timeStamp);

int listIsEmpty  ();

void freeList ();

void generateJSON();
