#ifndef FILME_UNDO_H
#define FILME_UNDO_H

#include <utility>

#include "FilmRepo.h"


class ActiuneUndo {
public:
    virtual void doUndo() {};

    //destructorul e virtual pentru a ne asigura ca daca dau delete pe un
        // pointer se apeleaza destructorul din clasa care trebuie
    virtual ~ActiuneUndo() = default;
};


class UndoAdauga : public ActiuneUndo {
    Film filmAdaugat;
    FilmRepo& rep;
public:
    UndoAdauga(FilmRepo& rep, Film p) : rep{ rep }, filmAdaugat{ p } {}

    void doUndo() {
        rep.repo_sterge(filmAdaugat.getTitlu());
    }
};

class UndoSterge : public ActiuneUndo {
private:
    FilmRepo& rep;
    Film film_sters;
public:

    UndoSterge(FilmRepo& r, Film f) : rep{ r }, film_sters{ f } {}

    void doUndo() {
        //cout<<film_sters.getTitlu()<<" "<<film_sters.getGen()<<" "<<film_sters.getAn()<<" "<<film_sters.getActor_principal()<<endl;
        rep.repo_adauga(film_sters);
    }
};


class UndoModifica :public ActiuneUndo {
private:
    FilmRepo& repo; // Repository-ul filmelor
    Film a_inainte; // Film de dinainte de stergere
    Film a_dupa; // Film de dupa stergere
public:

    UndoModifica(FilmRepo& r, Film d1, Film d2) : repo{ r }, a_inainte{ d1 }, a_dupa{ d2 } {}

    void doUndo() {
        //repo.cauta_film(a_dupa.get_denumire()) = a_inainte;
        repo.repo_modifica(a_inainte.getTitlu(), a_inainte.getGen(), a_inainte.getActorPrincipal(), a_inainte.getAnulAparitiei());
    }
};
#endif //FILME_UNDO_H
