#include "Lab13_14.h"
#include <QtWidgets/QApplication>
#include "GUI.h"
#include "FilmService.h"
#include "C:\Users\ovidi\source\repos\Lab 13_14\Lab 13_14\CosCumparaturi.h"
#include "C:\Users\ovidi\source\repos\Lab 13_14\Lab 13_14\FilmService.h"
#include "C:\Users\ovidi\source\repos\Lab 13_14\Lab 13_14\FilmRepo.h"
#include "C:\Users\ovidi\source\repos\Lab 13_14\Lab 13_14\Validator.h"
#include "C:\Users\ovidi\source\repos\Lab 13_14\Lab 13_14\Film.h"
int main(int argc, char* argv[])
{
    QApplication aplicatie(argc, argv);

    FilmRepo rep;
    FilmValidator val;
    FilmService serv{ rep,val };
    CosCumparaturi cos;

    serv.getAll();
    FilmGUI interface { serv,cos };
    CosCumparaturiGUI{ serv,cos };
    interface.show();
    //Lab10_11GUI w;
   // w.show();
    return aplicatie.exec();
}

