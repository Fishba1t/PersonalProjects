#pragma once
#pragma once
#include <QtWidgets/QApplication>
#include <QtWidgets/qradiobutton.h>
#include <QtWidgets/qlabel.h>
#include <QtWidgets/qpushbutton.h>
#include <QtWidgets/qboxlayout.h>
#include <QtWidgets/qlineedit.h>
#include <QtWidgets/qformlayout.h>
#include <QtWidgets/qlistwidget.h>
#include <QtWidgets/qtablewidget.h>
#include <qmessagebox.h>
#include <qdebug.h>
#include "FilmService.h"
#include <vector>
#include <string>
#include "CosRead.h"
#include "CosCrud.h"
using namespace std;

class FilmGUI : public QWidget {
private:
    FilmService& serv;
    CosCumparaturi& cos;

    QListWidget* list_layout = new QListWidget;
    QTableWidget* tabel_layout = new QTableWidget{ 0,4 };
    QHBoxLayout* new_button_layout = new QHBoxLayout{};
    QPushButton* butonExit = new QPushButton{ "&Exit" };
    QPushButton* butonAdd = new QPushButton{ "&Adauga" };
    QPushButton* butonModifica = new QPushButton{ "&Modifica" };
    QPushButton* butonSterge = new QPushButton{ "&Sterge" };
    QPushButton* butonUndo = new QPushButton{ "&Undo" };
    QPushButton* butonFiltrareTitlu = new QPushButton{ "&Filtru Titlu" };
    QPushButton* butonFiltrareAnAparitie = new QPushButton{ "&Filtru An Aparitie" };
    QPushButton* butonRefresh = new QPushButton{ "&Refresh" };
    QPushButton* butonSortTitlu = new QPushButton{ "&Sort Titlu" };
    QPushButton* butonSortActor = new QPushButton{ "&Sort Actor" };
    QPushButton* butonSortGenAn = new QPushButton{ "&Sort Gen An" };
    QPushButton* butonShowCart = new QPushButton{ "&Show Cart" }; //cart crud

    QLineEdit* text_titlu = new QLineEdit;
    QLineEdit* text_gen = new QLineEdit;
    QLineEdit* text_actor = new QLineEdit;
    QLineEdit* text_an = new QLineEdit;

    void create_interface();

    void load_data(const vector<Film>& lista);

    void interface_connect();

    void GUIadd();

    void GUIselect();

    void GUImodifica();

    void GUIundo();

    void GUIaddGen();

    void GUIsterge();

    void GUIFiltruTitlu();

    void GUIFiltruAnAparitie();

    void GUIRefresh();

    void ReloadListTabel(const vector<Film>& lista);

    void load_cart_data(const vector<Film>& lista);

public:
    explicit FilmGUI(FilmService& serv,CosCumparaturi& cos) : serv{ serv }, cos{cos}
    {

        create_interface();
        load_data(serv.getAll());
        interface_connect();
        ReloadListTabel(serv.getAll());

    }


private slots:
    void showCartWindow();
};

class CosCumparaturiGUI :public QWidget, public Observer {
private:
    FilmService& serv;
    CosCumparaturi& cos;

    QListWidget* list_layout = new QListWidget;
    QListWidget* cos_layout = new QListWidget;

    QHBoxLayout* new_button_layout = new QHBoxLayout{};
    QPushButton* butonExitCos = new QPushButton{ "&Exit" };
    QPushButton* butonAddinCos = new QPushButton{ "&Adauga in Cos" };
    QPushButton* butonGolesteCos = new QPushButton{ "&Goleste Cos" };
    QPushButton* butonFileEdit = new QPushButton{ "&Export" };
    QPushButton* butonRandom = new QPushButton{ "&Adauga Random" };


    QLineEdit* text_titlu_cos = new QLineEdit;
    QLineEdit* editFile = new QLineEdit;
    QLineEdit* editNr = new QLineEdit;

    void create_cart_interface();

    void load_cart_data(const vector<Film>& lista);

    void interface_cart_connect();

    void GUIaddCos();

    void GUIgolesteCos();

    void GUI_export();

    void GUI_adaugaRandom();

public:
    explicit CosCumparaturiGUI(FilmService& serv,CosCumparaturi& cos) : serv{ serv } ,cos {cos}
    {
        create_cart_interface();
        interface_cart_connect();

    }
    void update() override {
        load_cart_data(serv.service_getAll_cumparaturi());
    }
};