#pragma once
#include "Film.h"
#include "FilmRepo.h"
#include "UNDO.h"
#include "CosCumparaturi.h"
#include <string>
#include <map>
#include <stack>
#include <vector>

#include <functional>
#include "validator.h"

using std::vector;
using std::function;
using std::string;

class FilmService {
	FilmRepo& rep;
	FilmValidator& val;
	CosCumparaturi cos;
	vector<std::unique_ptr<ActiuneUndo>> undoList;

	/*
	 Functie de sortare generala
	 maiMareF - functie care compara 2 Pet, verifica daca are loc relatia mai mare
			  - poate fi orice functe (in afara clasei) care respecta signatura (returneaza bool are 2 parametrii Pet)
			  - poate fi functie lambda (care nu capteaza nimic in capture list)
	 returneaza o lista sortata dupa criteriu dat ca paramatru
	*/
	vector<Film> generalSort(bool(*maiMicF)(const Film&, const Film&));
	/*
	Functie generala de filtrare
	fct - poate fi o functie
	fct - poate fi lambda, am folosit function<> pentru a permite si functii lambda care au ceva in capture list
	returneaza doar animalele care trec de filtru (fct(pet)==true)
	*/
	vector<Film> filtreaza(function<bool(const Film&)> fct);
public:
	FilmService(FilmRepo& rep, FilmValidator& val) noexcept :rep{ rep }, val{ val } {}
	//nu permitem copierea de obiecte FilmService
	FilmService(const FilmService& ot) = delete;
	/*
	* returneaza toate animalele in ordinea in care au fost adaugate
	*/
	const vector<Film>& getAll() noexcept {
		return rep.getAll();
	}
	/*
	* Adauga un nou film
	* arunca exceptie daca: nu se poate salva <=> nu este valid
	*/
	void service_adauga(const string& titlu, const string& gen, const string& actor, int an);

	/*
	* Sortare dupa titlu
	*/
	vector<Film> sortByTitlu();

	/*
	* Sortare dupa gen
	*/
	vector<Film> sortByActor();

	/*
	* Sortare dupa gen+an aparitie
	*/
	vector<Film>sortByGenAn();

	vector<Film> filtrareTitlu(const string& titlu);

	vector<Film> filtrareAnAparitie(int an);

	void service_sterge(const string& titlu);

	void service_modifica(const string& titlu_nou, const string& gen_nou, const string& actor_nou, int an_nou);

	const Film& service_cauta(const string& titlu) const;

	void service_adauga_in_cos(string titlu_film);

	size_t service_adauga_random(int n);

	void service_goleste_cos();

	const vector<Film>& service_getAll_cumparaturi();

	void service_salveazaCos(string fisier);

	void undo();
};
void testService();