#include "CosRead.h"

// Constructor for the read-only GUI of the shopping cart
cosReadOnlyGUI::cosReadOnlyGUI(FilmService& serv, CosCumparaturi& cos) : serv{ serv }, cos{ cos } {
    this->cos.addObserver(this); // Add the GUI as an observer to the shopping cart
    this->repaint(); // Repaint the GUI to display the initial state of the shopping cart
}

// Paint event handler for the read-only GUI
void cosReadOnlyGUI::paintEvent(QPaintEvent* event) {
    QPainter g{ this }; // Create a QPainter object for painting on the GUI
    g.setPen(Qt::blue); // Set the pen color to blue for drawing shapes

    std::mt19937 mt{ std::random_device{}() }; // Initialize a random number generator
    std::uniform_int_distribution<> x_coord(0, this->width()); // Define a uniform distribution for x coordinates
    std::uniform_int_distribution<> y_coord(0, this->height()); // Define a uniform distribution for y coordinates
    std::uniform_int_distribution<> width(15, RECTANGLE_MAX_DIM); // Define a uniform distribution for rectangle width
    std::uniform_int_distribution<> height(15, RECTANGLE_MAX_DIM); // Define a uniform distribution for rectangle height

    int z_coord = 1000; // Set a constant value for the z coordinate
    int p_coord = 150; // Set a constant value for the p coordinate

    int x = 10; // Initial x coordinate for drawing rectangles
    int y = 10; // Initial y coordinate for drawing rectangles
    int w = 10; // Initial width for drawing rectangles

    // Iterate through each film in the shopping cart and draw a rectangle for each
    for (auto f : serv.service_getAll_cumparaturi()) {
        g.drawRect(x, y + 5, w, f.getAnulAparitiei() % 100); // Draw a rectangle for the film
        x += 50; // Update x coordinate for the next rectangle
    }
}

// Update method called when the shopping cart is modified
void cosReadOnlyGUI::update() {
    this->repaint(); // Repaint the GUI to reflect the changes in the shopping cart
}
