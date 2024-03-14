#pragma once

#include "Observer.h"
#include <qwidget.h>
#include <qpushbutton.h>
#include <qtablewidget.h>
#include <qslider.h>
#include <qlayout>
#include <vector>
#include "C:\Users\ovidi\source\repos\Lab 13_14\Lab 13_14\CosCumparaturi.h"
#include "C:\Users\ovidi\source\repos\Lab 13_14\Lab 13_14\FilmService.h"

using std::vector;

class cosCRUDGUI : public QWidget, public Observer {
private:
    CosCumparaturi& cos_cumparaturi;
    FilmService& srv;
    QWidget* wnd;
    QHBoxLayout* layout;
    QTableView* table;
    QSlider* slider;
    QPushButton* btnadd;
    QPushButton* btnempty;

    // Initializes the GUI components
    void initComponents();

    // Connects signals to slots
    void connectSignals();


public:
    // Constructor taking references to CosCumparaturi and FilmService
    explicit cosCRUDGUI(CosCumparaturi& cos_cumparaturi, FilmService& srv) :cos_cumparaturi{ cos_cumparaturi }, srv{ srv } {
        wnd = new QWidget;
        layout = new QHBoxLayout;
        btnadd = new QPushButton("Adauga Random"); // Button to add random films to the cart
        btnempty = new QPushButton("Goleste Cos"); // Button to empty the cart
        slider = new QSlider;
        table = new QTableView;

    };

    // Runs the GUI
    void run();


    // Destructor
    ~cosCRUDGUI() {
        this->cos_cumparaturi.removeObserver(this);
    }
};
