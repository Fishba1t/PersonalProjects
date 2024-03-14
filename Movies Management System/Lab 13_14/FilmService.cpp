#include "FilmService.h"
#include <functional>
#include <algorithm>
#include <fstream>
#include <assert.h>
#include <string.h>
#include <map>
#include <string>
using namespace std;

// For Shopping Cart

// Add a film to the shopping cart based on its title
void FilmService::service_adauga_in_cos(string titlu_film) {
	vector<Film> v{ rep.getAll() };
	for (int i = 0; i < v.size(); i++)
	{
		Film aux = v[i];
		if (aux.getTitlu() == titlu_film)
		{
			cos.adauga_in_cos(aux);
		}
	}
}

// Add a random number of films to the shopping cart
size_t FilmService::service_adauga_random(int n) {
	vector<Film> allFilms = rep.getAll();

	if (static_cast<size_t>(n) > allFilms.size()) {
		throw FilmRepoException("The number of random films to add is greater than the list length.");
	}

	cos.adauga_random(allFilms, n);
	return cos.getAll_cumparaturi().size();
}

// Clear the shopping cart
void FilmService::service_goleste_cos() {
	cos.goleste_cos();
}

// Get all films in the shopping cart
const vector<Film>& FilmService::service_getAll_cumparaturi() {
	if (cos.getAll_cumparaturi().size() == 0) throw exception();
	return cos.getAll_cumparaturi();
}

// Save the contents of the shopping cart to a file
void FilmService::service_salveazaCos(string fisier) {
	ofstream fout(fisier);
	vector<Film> lista = cos.cos_cumparaturi;
	for (auto& el : lista) {
		fout << el.getTitlu() << " | " << el.getGen() << " | " << el.getActorPrincipal() << " | " << el.getAnulAparitiei() << '\n';
	}
	fout.close();
}

// Main Program

// General sorting function for films based on a comparison function
vector<Film> FilmService::generalSort(bool(*maiMicF)(const Film&, const Film&)) {
	vector<Film> v = rep.getAll();
	for (auto i = v.begin(); i != v.end(); ++i) {
		for (auto j = i + 1; j != v.end(); ++j) {
			if (!maiMicF(*i, *j)) {
				auto temp = *i;
				*i = *j;
				*j = temp;
			}
		}
	}
	return v;
}

// Add a new film to the repository
void FilmService::service_adauga(const string& titlu, const string& gen, const string& actor, int an) {
	Film f{ titlu,gen,actor,an };
	val.validare(f);
	rep.repo_adauga(f);
	this->undoList.push_back(std::make_unique<UndoAdauga>(UndoAdauga{ rep,f }));
}

// Remove a film from the repository based on its title
void FilmService::service_sterge(const string& titlu) {
	Film f = rep.find(titlu);
	rep.repo_sterge(titlu);
	this->undoList.push_back(std::make_unique<UndoSterge>(UndoSterge{ rep,f }));
}

// Modify the details of a film in the repository
void FilmService::service_modifica(const string& titlu, const string& gen_nou, const string& actor_nou, int an_nou) {
	Film f = rep.find(titlu);
	Film f1{ titlu,gen_nou,actor_nou,an_nou };
	rep.repo_modifica(titlu, gen_nou, actor_nou, an_nou);
	this->undoList.push_back(std::make_unique<UndoModifica>(UndoModifica{ rep,f,f1 }));
}

// Sort films by title
vector<Film> FilmService::sortByTitlu() {
	return generalSort(cmpTitlu);
}

// Sort films by main actor
vector<Film> FilmService::sortByActor() {
	return generalSort(cmpActorPrincipal);
}

// Sort films by genre and release year
vector<Film> FilmService::sortByGenAn() {
	return generalSort(cmpGenAn);
}

// Filter films based on a given function
vector<Film> FilmService::filtreaza(function<bool(const Film&)> fct) {
	vector<Film> rez;
	for (const auto& film : rep.getAll()) {
		if (fct(film)) {
			rez.push_back(film);
		}
	}
	return rez;
}

// Filter films based on title
vector<Film> FilmService::filtrareTitlu(const string& titlu) {
	return filtreaza([titlu](const Film& f) {
		return f.getTitlu() == titlu;
		});
}

// Filter films based on release year
vector<Film> FilmService::filtrareAnAparitie(int an) {
	return filtreaza([an](const Film& f) noexcept {
		return f.getAnulAparitiei() == an;
		});
}

// Find a film in the repository based on its title
const Film& FilmService::service_cauta(const string& titlu) const {
	return rep.find(titlu);
}

// Undo the last operation
void FilmService::undo() {
	if (undoList.empty()) {
		throw FilmRepoException("No more operations to undo!");
	}

	this->undoList.back()->doUndo();
	this->undoList.pop_back();
}

// Test functions for the FilmService class
void testAdaugaCtr() {
	FilmRepo rep;
	FilmValidator val;
	FilmService ctr{ rep,val };
	ctr.service_adauga("Titlu", "Gen", "Actor", 1900);
	assert(ctr.getAll().size() == 1);

	//adaug ceva invalid
	try {
		ctr.service_adauga("", "", "", -1); assert(false);
	}
	catch (ValidateException&) {
		assert(true);
	}
}
void testFiltrare() {
	FilmRepo rep;
	FilmValidator val;
	FilmService ctr{ rep,val };
	ctr.service_adauga("Titlu1", "Gen1", "Actor1", 1957);
	ctr.service_adauga("Titlu2", "Gen2", "Actor2", 1961);
	ctr.service_adauga("Titlu3", "Gen3", "Actor3", 1957);
	assert(ctr.filtrareAnAparitie(1957).size() == 2);
	assert(ctr.filtrareTitlu("Titlu1").size() == 1);
	assert(ctr.filtrareAnAparitie(1870).size() == 0);
	assert(ctr.filtrareTitlu("Titlu55").size() == 0);
}
void testSortare()
{
	FilmRepo rep;
	FilmValidator val;
	FilmService ctr{ rep,val };
	ctr.service_adauga("Titlu1", "QGen1", "CActor1", 1957);
	ctr.service_adauga("CTitlu1", "AGen2", "QActor2", 1961);
	ctr.service_adauga("BTitlu1", "UGen2", "Actor2", 1967);

	auto firstP = ctr.sortByActor()[0];
	assert(firstP.getActorPrincipal() == "Actor2");

	firstP = ctr.sortByTitlu()[0];
	assert(firstP.getTitlu() == "BTitlu1");

	firstP = ctr.sortByGenAn()[0];
	assert(firstP.getGen() == "AGen2");

}
void testSortareAnGen()
{
	FilmRepo rep;
	FilmValidator val;
	FilmService ctr{ rep,val };
	ctr.service_adauga("Titlu1", "QGen1", "CActor1", 1957);
	ctr.service_adauga("CTitlu1", "AGen2", "QActor2", 1967);
	ctr.service_adauga("BTitlu1", "AGen2", "Actor2", 1961);

	auto firstP = ctr.sortByGenAn()[0];
	assert(firstP.getGen() == "AGen2");
}
void testCautaService()
{
	FilmRepo rep;
	FilmValidator val;
	const FilmService ctr{ rep,val };

	rep.repo_adauga(Film{ "The Godfather", "Crime","Marlon Brando", 1990 });
	rep.repo_adauga(Film{ "The Shawshank Redemption", "Drama", "Tim Robbins",1769 });
	rep.repo_adauga(Film{ "The Dark Knight","Action", "Christian Bale",2003 });

	Film film = ctr.service_cauta("The Godfather");
	assert(film.getGen() == "Crime");
	assert(film.getActorPrincipal() == "Marlon Brando");
	assert(film.getAnulAparitiei() == 1990);

}
void test_service_sterge()
{
	FilmRepo rep;
	FilmValidator val;
	FilmService ctr{ rep,val };

	ctr.service_adauga("The Godfather", "crime", "Marlon Brando", 1972);
	ctr.service_adauga("Forrest Gump", "drama", "Tom Hanks", 1994);
	ctr.service_adauga("The Shawshank Redemption", "drama", "Tim Robbins", 1994);

	assert(ctr.getAll().size() == 3);

	ctr.service_sterge("Forrest Gump");

	assert(ctr.getAll().size() == 2);
	try
	{
		ctr.service_sterge("INEXISTENT"); assert(false);
	}
	catch (const FilmRepoException&)
	{
		assert(true);
	}
}
void test_service_modifica()
{
	FilmRepo rep;
	FilmValidator val;
	FilmService ctr{ rep,val };

	ctr.service_adauga("The Godfather", "crime", "Marlon Brando", 1972);

	ctr.service_modifica("The Godfather: Part II", "crime", "Al Pacino", 1974);

	Film results = ctr.service_cauta("The Godfather: Part II");
	assert(results.getGen() == "crime");
	assert(results.getActorPrincipal() == "Al Pacino");
	assert(results.getAnulAparitiei() == 1974);
}

void testCosCumparaturi() {
	FilmRepo rep;
	FilmValidator val;
	FilmService serv{ rep, val };

	serv.service_adauga("Titlu1", "QGen1", "CActor1", 1957);
	serv.service_adauga("CTitlu1", "AGen2", "QActor2", 1961);
	serv.service_adauga("BTitlu1", "UGen2", "Actor2", 1967);

	serv.service_adauga_in_cos("Titlu1");
	try {
		serv.service_adauga_in_cos("inexistent");
	}
	catch (exception&) { assert(true); }

	serv.service_adauga_random(2);
	auto cumparaturi = serv.service_getAll_cumparaturi();
	assert(cumparaturi.size() == 3);
	serv.service_goleste_cos();
}

void test_Salveaza_in_Fisier() {
	FilmRepo rep;
	FilmValidator val;
	FilmService serv{ rep, val };

	serv.service_adauga("Titlu1", "QGen1", "CActor1", 1957);
	serv.service_adauga("CTitlu1", "AGen2", "QActor2", 1961);
	serv.service_adauga("BTitlu1", "UGen2", "Actor2", 1967);

	serv.service_adauga_in_cos("Titlu1");
	serv.service_adauga_in_cos("CTitlu1");

	serv.service_salveazaCos("testfisier.html");

	ifstream fin("testfisier.html");
	const Film f1("Titlu1", "QGen1", "CActor1", 1957);
	const Film f2("CTitlu1", "AGen2", "QActor2", 1961);

	string test;
	getline(fin, test);
	assert(test == "Titlu1 | QGen1 | CActor1 | 1957");
	getline(fin, test);
	assert(test == "CTitlu1 | AGen2 | QActor2 | 1961");

	fin.close();
}
void testService()
{
	testAdaugaCtr();
	testFiltrare();
	testSortare();
	testSortareAnGen();
	testCautaService();
	test_service_sterge();
	test_service_modifica();
	testCosCumparaturi();
	test_Salveaza_in_Fisier();
}