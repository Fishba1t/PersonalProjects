#pragma once

#include <QtWidgets/QMainWindow>
#include "ui_Lab13_14.h"

class Lab13_14 : public QMainWindow
{
    Q_OBJECT

public:
    Lab13_14(QWidget *parent = nullptr);
    ~Lab13_14();

private:
    Ui::Lab13_14Class ui;
};
