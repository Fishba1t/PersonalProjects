#pragma once
#include "CosCrud.h"
#include "C:\Users\ovidi\source\repos\Lab 13_14\Lab 13_14\Observer.h"

// GUI class for the shopping cart CRUD operations
void cosCRUDGUI::run() {
    this->cos_cumparaturi.addObserver(this); // Add the GUI as an observer to the shopping cart
    this->initComponents(); // Initialize GUI components
    this->connectSignals(); // Connect signals and slots for GUI interactions
    wnd->show(); // Display the GUI window
}

// Initialize GUI components
void cosCRUDGUI::initComponents() {
    wnd->setLayout(layout); // Set the layout for the main window

    table->setSelectionMode(QAbstractItemView::SingleSelection); // Set selection mode for the table
    layout->addWidget(table); // Add the table to the layout

    slider->setMinimum(0); // Set the minimum value for the slider
    slider->setMaximum(40); // Set the maximum value for the slider
    slider->setOrientation(Qt::Horizontal); // Set the orientation of the slider
    slider->setTickPosition(QSlider::TicksAbove); // Set the tick position for the slider
    layout->addWidget(slider); // Add the slider to the layout

    layout->addWidget(btnadd); // Add the "Add" button to the layout
    layout->addWidget(btnempty); // Add the "Empty Cart" button to the layout
}

// Connect signals and slots for GUI interactions
void cosCRUDGUI::connectSignals() {
    QObject::connect(btnadd, &QPushButton::clicked, [this]() {
        int number = slider->value(); // Get the value from the slider
        srv.service_adauga_random(number); // Add a random selection of films to the shopping cart
        cos_cumparaturi.notify(); // Notify observers about the change in the shopping cart
        });

    QObject::connect(btnempty, &QPushButton::clicked, [this]() {
        cos_cumparaturi.goleste_cos(); // Empty the shopping cart
        cos_cumparaturi.notify(); // Notify observers about the change in the shopping cart
        });
}
