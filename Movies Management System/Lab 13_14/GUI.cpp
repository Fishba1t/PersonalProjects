#include "GUI.h"
#include <sstream>
#include <list>
#include "GUI.h"
#include <QLabel>
#include <QPushButton>
#include <QVBoxLayout>

void CosCumparaturiGUI::create_cart_interface()
{
    QHBoxLayout* main_layout = new QHBoxLayout{};
    setLayout(main_layout);

    auto left_part = new QVBoxLayout;
    left_part->addWidget(cos_layout);

    main_layout->addLayout(left_part);

    auto middle_part = new QVBoxLayout;

    auto form_layout = new QFormLayout;
    form_layout->addRow("Titlu", text_titlu_cos);
    form_layout->addRow("File Name", editFile);
    form_layout->addRow("Numar Filme Random", editNr);
    middle_part->addLayout(form_layout);

    auto button_layout = new QHBoxLayout{};
    button_layout->addWidget(butonAddinCos);
    button_layout->addWidget(butonExitCos);
    button_layout->addWidget(butonGolesteCos);
    button_layout->addWidget(butonFileEdit);
    button_layout->addWidget(butonRandom);
    middle_part->addLayout(button_layout);

    main_layout->addLayout(middle_part);
    main_layout->addLayout(new_button_layout);
}

void FilmGUI::create_interface()
{
    QHBoxLayout* main_layout = new QHBoxLayout{};
    setLayout(main_layout);

    auto left_part = new QVBoxLayout;
    left_part->addWidget(list_layout);
    left_part->addWidget(tabel_layout);



    ///

    QStringList tblHeaderList;
    tblHeaderList << "Titlu" << "Gen" << "Actor Principal" << "An Aparitie";
    tabel_layout->setHorizontalHeaderLabels(tblHeaderList);





    ///

    left_part->addWidget(butonSortTitlu);
    left_part->addWidget(butonSortActor);
    left_part->addWidget(butonSortGenAn);
    left_part->addWidget(butonFiltrareTitlu);
    left_part->addWidget(butonFiltrareAnAparitie);
    left_part->addWidget(butonRefresh);
    left_part->addWidget(butonShowCart);

    main_layout->addLayout(left_part);

    auto middle_part = new QVBoxLayout;

    auto form_layout = new QFormLayout;
    form_layout->addRow("Titlu", text_titlu);
    form_layout->addRow("Gen", text_gen);
    form_layout->addRow("Actor Principal", text_actor);
    form_layout->addRow("Anul Aparitiei", text_an);
    middle_part->addLayout(form_layout);

    auto button_layout = new QHBoxLayout{};
    button_layout->addWidget(butonAdd);
    button_layout->addWidget(butonSterge);
    button_layout->addWidget(butonModifica);
    button_layout->addWidget(butonUndo);
    button_layout->addWidget(butonExit);
    middle_part->addLayout(button_layout);

    main_layout->addLayout(middle_part);
    main_layout->addLayout(new_button_layout);
}

void FilmGUI::interface_connect()
{
    QObject::connect(butonSortTitlu, &QPushButton::clicked, [&]() { load_data(serv.sortByTitlu()); });

    QObject::connect(butonSortActor, &QPushButton::clicked, [&]() { load_data(serv.sortByActor());  });

    QObject::connect(butonSortGenAn, &QPushButton::clicked, [&]() { load_data(serv.sortByGenAn()); });

    QObject::connect(butonFiltrareTitlu, &QPushButton::clicked, [&]() { GUIFiltruTitlu(); });

    QObject::connect(butonFiltrareAnAparitie, &QPushButton::clicked, [&]() { GUIFiltruAnAparitie(); });

    QObject::connect(butonRefresh, &QPushButton::clicked, [&]() { GUIRefresh(); });

    QObject::connect(butonExit, &QPushButton::clicked, [&]() { close(); });

    QObject::connect(butonAdd, &QPushButton::clicked, [&]() { GUIadd(); });

    QObject::connect(list_layout, &QListWidget::itemSelectionChanged, [&]() { GUIselect(); });

    QObject::connect(butonModifica, &QPushButton::clicked, [&]() { GUImodifica(); });

    QObject::connect(butonUndo, &QPushButton::clicked, [&]() { GUIundo(); });

    QObject::connect(butonSterge, &QPushButton::clicked, [&]() { GUIsterge(); });

    connect(butonShowCart, &QPushButton::clicked, this, &FilmGUI::showCartWindow);

}

void CosCumparaturiGUI::interface_cart_connect()
{
    cos.addObserver(this);

    QObject::connect(butonExitCos, &QPushButton::clicked, [&]() { close(); });

    QObject::connect(butonAddinCos, &QPushButton::clicked, [&]() { GUIaddCos(); });

    QObject::connect(butonGolesteCos, &QPushButton::clicked, [&]() { GUIgolesteCos(); });

    QObject::connect(butonFileEdit, &QPushButton::clicked, this, &CosCumparaturiGUI::GUI_export);

    QObject::connect(butonRandom, &QPushButton::clicked, this, &CosCumparaturiGUI::GUI_adaugaRandom);
}


void FilmGUI::load_data(const vector<Film>& lista)
{
    list_layout->clear();;
    for (const auto& film : lista)
    {
        QListWidgetItem* item = new QListWidgetItem();
        item->setText(QString::fromStdString(film.getTitlu()));
        item->setData(Qt::UserRole, QString::fromStdString(film.getActorPrincipal()));
        list_layout->addItem(item);
    }
}

void FilmGUI::GUIadd()
{
    auto titlu = text_titlu->text();
    auto gen = text_gen->text();
    auto actor = text_actor->text();
    auto anul = text_an->text();
    int an = 0;
    try {
        int an = std::stoi(anul.toStdString());
        try {
            serv.service_adauga(titlu.toStdString(), gen.toStdString(), actor.toStdString(), an);
            GUIaddGen();
            ReloadListTabel(serv.getAll());
            load_data(serv.getAll());
        }
        catch (FilmRepoException)
        {
            QMessageBox::information(nullptr, "Eroare!", "Filmul exista deja!");
        }
        catch (ValidateException)
        {
            QMessageBox::information(nullptr, "Eroare!", "Film invalid!");
        }
    }
    catch (std::invalid_argument)
    {
        QMessageBox::information(nullptr, "Eroare!", "Film invalid!");
    }
    text_titlu->clear();
    text_gen->clear();
    text_actor->clear();
    text_an->clear();
}

void FilmGUI::GUIsterge()
{
    auto titlu = text_titlu->text();
    try {
        try {
            serv.service_sterge(titlu.toStdString());
            load_data(serv.getAll());
            ReloadListTabel(serv.getAll());
            GUIaddGen();
        }
        catch (FilmRepoException)
        {
            QMessageBox::information(nullptr, "Eroare!", "Filmul pe care doriti sa-l stergeti nu exista!");
        }
    }
    catch (std::invalid_argument)
    {
        QMessageBox::information(nullptr, "Eroare!", "Titlu invalid!");
    }
}

void FilmGUI::GUIselect()
{
    auto selectie = list_layout->selectedItems();
    if (selectie.isEmpty())
    {
        text_titlu->setText("");
        text_gen->setText("");
        text_actor->setText("");
        text_an->setText("");
    }
    else {
        auto item_selected = selectie.at(0);
        auto titlu = item_selected->text();
        auto actor = item_selected->data(Qt::UserRole).toString();
        text_titlu->setText(titlu);
        auto film = serv.service_cauta(titlu.toStdString());
        text_an->setText(QString::number(film.getAnulAparitiei()));
        text_gen->setText(QString::fromStdString(film.getGen()));
        text_actor->setText(QString::fromStdString(film.getActorPrincipal()));
    }
}

void FilmGUI::GUImodifica()
{
    auto titlu = text_titlu->text();
    auto gen = text_gen->text();
    auto actor = text_actor->text();
    auto anul = text_an->text();
    int an = 0;
    try {
        an = stoi(anul.toStdString());
        try {
            serv.service_modifica(titlu.toStdString(), gen.toStdString(), actor.toStdString(), an);
            load_data(serv.getAll());
            ReloadListTabel(serv.getAll());
            GUIaddGen();
        }
        catch (FilmRepoException)
        {
            QMessageBox::information(nullptr, "Eroare!", "Filmul pe care doriti sa-l modificati nu exista!");
        }
    }
    catch (std::invalid_argument)
    {
        QMessageBox::information(nullptr, "Eroare!", "Date invalide!");
    }
}

void FilmGUI::GUIundo()
{
    try
    {
        serv.undo();
        GUIaddGen();
        load_data(serv.getAll()); // Reload data into the table view
        ReloadListTabel(serv.getAll()); // Assuming this method updates the table view
    }
    catch (FilmRepoException)
    {
        QMessageBox::information(nullptr, "Eroare!", "Nu mai exista undo!");
    }
}


void FilmGUI::GUIaddGen()
{
    vector<Film> movies_list = serv.getAll();
    vector<std::pair<string, int>> tipuri;
    for (auto film : movies_list) {
        bool ok = false;
        int i = 0;
        for (auto type : tipuri) {
            if (type.first == film.getGen()) {
                ok = true;
                break;
            }
            i++;
        }
        if (ok == true)
            tipuri[i].second++;
        else
            tipuri.emplace_back(film.getGen(), 1);
    }
    QLayoutItem* item;
    while ((item = new_button_layout->takeAt(0)) != NULL)
    {
        delete item->widget();
        delete item;
    }

    for (auto t : tipuri) {
        string tip = t.first;
        int nr = t.second;
        auto item = new QRadioButton(QString::fromStdString(tip));

        QObject::connect(item, &QRadioButton::clicked, [nr] {
            string n = std::to_string(nr);
            auto* lbl = new QLabel(QString::fromStdString(n));
            lbl->show();
            });
        new_button_layout->addWidget(item);
    }
}


void FilmGUI::GUIFiltruTitlu()
{
    auto titlu = text_titlu->text();
    try {
        auto filteredFilms = serv.filtrareTitlu(titlu.toStdString());
        load_data(filteredFilms);
        GUIaddGen();
    }
    catch (FilmRepoException)
    {
        QMessageBox::information(nullptr, "Eroare!", "Filmul nu exista!");
    }
    catch (std::invalid_argument)
    {
        QMessageBox::information(nullptr, "Eroare!", "Titlu invalid!");
    }
}

void FilmGUI::GUIFiltruAnAparitie()
{
    auto an = text_an->text();
    try {
        int anInt = an.toInt(); // Convert the QString to int

        auto filteredFilms = serv.filtrareAnAparitie(anInt);
        load_data(filteredFilms);
        GUIaddGen();
    }
    catch (FilmRepoException)
    {
        QMessageBox::information(nullptr, "Eroare!", "Filmul nu exista!");
    }
    catch (std::invalid_argument)
    {
        QMessageBox::information(nullptr, "Eroare!", "An invalid!");
    }
}

void FilmGUI::GUIRefresh()
{
    try {

        vector<Film> filme = serv.getAll();
        load_data(filme);
        GUIaddGen();
    }
    catch (FilmRepoException)
    {
        QMessageBox::information(nullptr, "Eroare!", "Lista e goala!");
    }
}

void FilmGUI::showCartWindow()
{
    CosCumparaturiGUI* cosGUI = new CosCumparaturiGUI(serv,cos);  // Create a dynamic instance of CosCumparaturiGUI
    cosGUI->setAttribute(Qt::WA_DeleteOnClose);  // Set the attribute to delete the object when the window is closed
    cosGUI->show();  // Display the CosCumparaturiGUI window
}


void CosCumparaturiGUI::load_cart_data(const vector<Film>& lista)
{
    cos_layout->clear();
    for (const auto& film : lista)
    {
        QListWidgetItem* item = new QListWidgetItem();
        item->setText(QString::fromStdString(film.getTitlu()));
        item->setData(Qt::UserRole, QString::fromStdString(film.getActorPrincipal()));
        cos_layout->addItem(item);
    }
}

void CosCumparaturiGUI::GUIaddCos()
{
    auto titlu = text_titlu_cos->text();
    try {
        // Check if the movie exists in the main movie list
        if (serv.service_cauta(titlu.toStdString()).getTitlu() == titlu.toStdString())
        {
            serv.service_adauga_in_cos(titlu.toStdString());
            load_cart_data(serv.service_getAll_cumparaturi());
        }
        else {
            QMessageBox::information(nullptr, "Eroare!", "Filmul nu exista!");
        }
    }
    catch (FilmRepoException)
    {
        QMessageBox::information(nullptr, "Eroare!", "Filmul nu exista!");
    }
    text_titlu_cos->clear();
}

void CosCumparaturiGUI::GUIgolesteCos()
{
    try {
        serv.service_goleste_cos();
        vector<Film> gol;
        load_cart_data(gol);
    }
    catch (FilmRepoException)
    {
        QMessageBox::information(nullptr, "Eroare!", "Cosul este gol!");
    }
}

void CosCumparaturiGUI::GUI_export()
{
    try {
        string fileName = editFile->text().toStdString();

        editFile->clear();

        serv.service_salveazaCos(fileName);

        QMessageBox::information(this, "Info", QString::fromStdString("Filmele au fost exportate in fisier!"));
    }
    catch (exception&) {
        QMessageBox::warning(this, "Warning", QString::fromStdString("Fisierul nu se poate deschide"));
    }
}

void CosCumparaturiGUI::GUI_adaugaRandom() {
    try {
        string n = editNr->text().toStdString();
        int nr;

        editNr->clear();

        nr = stoi(n);

        // Check if the number entered by the user exceeds the available films
        size_t availableFilms = serv.getAll().size();
        if (static_cast<size_t>(nr) > availableFilms) {
            // Show a message box indicating that the number entered is greater than the available films
            QMessageBox::warning(this, "Warning", QString::fromStdString("Numarul adaugat este mai mare decat numarul total de filme existente!"));
            return; // Return without further execution
        }

        // If the number entered is valid, add random films
        serv.service_adauga_random(nr);
        load_cart_data(serv.service_getAll_cumparaturi());

        QMessageBox::information(this, "Info", QString::fromStdString("Filme adaugate random cu succes!"));
    }
    catch (const std::invalid_argument&) {
        QMessageBox::warning(this, "Warning", QString::fromStdString("Trebuie sa introduceti un numar valid!"));
    }
}


void FilmGUI::ReloadListTabel(const vector<Film>& lista)
{

    tabel_layout->clearContents();
    tabel_layout->setRowCount(serv.getAll().size());
    int lineNumber = 0;
    for (const auto& activitate : lista)
    {
        this->tabel_layout->setItem(lineNumber, 0, new QTableWidgetItem(QString::fromStdString(activitate.getTitlu())));
        this->tabel_layout->setItem(lineNumber, 1, new QTableWidgetItem(QString::fromStdString(activitate.getGen())));
        this->tabel_layout->setItem(lineNumber, 2, new QTableWidgetItem(QString::fromStdString(activitate.getActorPrincipal())));
        this->tabel_layout->setItem(lineNumber, 3, new QTableWidgetItem(QString::fromStdString(to_string(activitate.getAnulAparitiei()))));

        ++lineNumber;
    }

}