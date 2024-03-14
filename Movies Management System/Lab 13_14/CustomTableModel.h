#pragma once
#include <QAbstractTableModel>
#include <vector>
#include "Film.h"
using namespace std;

class CustomTableModel : public QAbstractTableModel {
private:
	vector <Film> lista_filme;

public:

	CustomTableModel(const vector<Film>& lista_filme) : lista_filme{ lista_filme } {}

	int rowCount(const QModelIndex& parent = QModelIndex()) const override {
		return int(lista_filme.size());
	}

	int columnCount(const QModelIndex& parent = QModelIndex()) const override {
		return 2;
	}

	QVariant headerData(int section, Qt::Orientation orientation, int role) const override;

	QVariant data(const QModelIndex& index, int role = Qt::DisplayRole) const override;

	void setProduct(const vector<Film>& new_list) {

		lista_filme = new_list;
		auto topLeft = createIndex(0, 0);
		auto bottomRight = createIndex(rowCount(), columnCount());
		emit dataChanged(topLeft, bottomRight);
	}
};

