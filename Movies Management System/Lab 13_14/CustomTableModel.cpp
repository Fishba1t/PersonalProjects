#include "CustomTableModel.h"

// Retrieve data for a specific cell in the table
QVariant CustomTableModel::data(const QModelIndex& index, int role) const {
    int row = index.row();
    int col = index.column();

    if (role == Qt::DisplayRole) {
        if (col == 0)
            return QString::fromStdString(lista_filme[row].getTitlu()); // Display the film title in the first column
        else if (col == 1)
            return QString::fromStdString(lista_filme[row].getGen()); // Display the film genre in the second column
    }
    return QVariant(); // Return an empty QVariant for other roles or cells
}

// Retrieve header data for a specific section (column or row) in the table
QVariant CustomTableModel::headerData(int section, Qt::Orientation orientation, int role) const {
    if (role == Qt::DisplayRole && orientation == Qt::Horizontal) {
        switch (section) {
        case 0:
            return QString("Titlu"); // Display "Titlu" as the header for the first column
        case 1:
            return QString("Gen"); // Display "Gen" as the header for the second column
        }
    }

    return QVariant(); // Return an empty QVariant for other roles or sections
}
