#pragma once
#include <string>
#include <iostream>

using std::string;
using std::cout;

class Film {
	std::string titlu;
	std::string gen;
	std::string actor_principal;
	int anul_aparitiei;
public:
	Film(const string t, const string g, const string act, int an) :titlu{ t }, gen{ g }, actor_principal{ act }, anul_aparitiei{ an } {}

	Film(const Film& ot) :titlu{ ot.titlu }, gen{ ot.gen }, actor_principal{ ot.actor_principal }, anul_aparitiei{ ot.anul_aparitiei } {
	}

	string getTitlu()const {
		return titlu;
	}
	string getGen()const {
		return gen;
	}
	string getActorPrincipal()const {
		return actor_principal;
	}
	int getAnulAparitiei() const noexcept {
		return anul_aparitiei;
	}
	void setTitlu(const string& t) {
		titlu = t;
	}
	void setGen(const string& g) {
		gen = g;
	}
	void setActorPrincipal(const string& act) {
		actor_principal = act;
	}
	void setAnulAparitiei(int an) noexcept {
		anul_aparitiei = an;
	}
};

/*
* Compara dupa titlu
* returneaza true daca f1.titlu e mai mic decast f2.titlu
*/
bool cmpTitlu(const Film& f1, const Film& f2);

/*
* Compara dupa actor principal
* returneaza true daca f1.actorPrincipal e mai mic decast f2.actorPrincipal
*/
bool cmpActorPrincipal(const Film& f1, const Film& f2);

/*
* Compara dupa gen(daca genurile sunt echivalente compara dupa anul aparitiei)
*/
bool cmpGenAn(const Film& f1, const Film& f2);