#include <stdio.h>
#include <stdlib.h>
#include "Medida.h"

int main()
{
    printf("Inicio\n\n");
    initMedida();
    
    insertNewMedida(1.0, 1.0, 1000);
    insertNewMedida(2.0, 2.0, 1000);
    insertNewMedida(3.0, 3.0, 1000);
    insertNewMedida(4.0, 4.0, 1000);
    
    generateJSON();
    
    system("PAUSE");
    return 0;
}
