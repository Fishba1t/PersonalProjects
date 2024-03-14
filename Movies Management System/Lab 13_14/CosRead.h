#include "Observer.h"
#include "C:\Users\ovidi\source\repos\Lab 13_14\Lab 13_14\FilmService.h"
#include "C:\Users\ovidi\source\repos\Lab 13_14\Lab 13_14\CosCumparaturi.h"
#include <qwidget.h>
#include <qpainter.h>
#include <random>
#include <cmath>

#define RECTANGLE_MAX_DIM 256

class cosReadOnlyGUI : public QWidget, public Observer {
private:
    CosCumparaturi& cos;
    FilmService& serv;

public:
    cosReadOnlyGUI(FilmService& serv, CosCumparaturi& cos);

    void update() override;

    void paintEvent(QPaintEvent* event) override;

    ~cosReadOnlyGUI() {
        this->cos.removeObserver(this);
    }
};
