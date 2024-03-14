#pragma once
#include "Film.h"
#include <iostream>
#include <vector>
#include <string>
#include <ostream>

using std::vector;
using std::string;
using std::ostream;

class FilmRepo {
	vector<Film> all;
	/*
	* metoda privata verifica daca exista deja un f in repository
	*/
	bool exist(const Film& f) const;
public:
	FilmRepo() = default;
	//nu permitem copierea de obiecte
	FilmRepo(const FilmRepo& ot) = delete;
	/*
	* Salvare film
	* arunca exceptie daca mai exista un film cu acelasi titlu gen actor si an
	*/
	void repo_adauga(const Film& f);

	/*
	* Cauta
	* arunca exceptie daca nu exista animal
	*/
	const Film& find(const string& titlu) const;

	/*
	* returneaza toate filmele salvarte
	*/
	const vector<Film>& getAll() const noexcept;

	void repo_sterge(const string& titlu);

	void repo_modifica(const string& titlu_nou, const string& gen_nou, const string& actor_nou, int an_nou);

	int caut_poz(const string& titlu);
};

/*
* Folosit pentru a semnala situatii exceptionale care pot aparea in repo
*/
class FilmRepoException {
	string msg;
public:
	FilmRepoException(string m) :msg{ m } {}
	//functie friend (vreau sa folosesc membru privat msg)
	friend ostream& operator<<(ostream& out, const FilmRepoException& ex);

};

ostream& operator<<(ostream& out, const FilmRepoException& ex);

void testRepository();