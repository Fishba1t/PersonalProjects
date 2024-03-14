#include "FilmRepo.h"
#include <iostream>
#include <sstream>
#include <assert.h>

using std::ostream;
using std::stringstream;

// Add a film to the repository
void FilmRepo::repo_adauga(const Film& f)
{
	if (exist(f)) {
		throw FilmRepoException("Exista deja un film cu titlul: " + f.getTitlu() + " genul:" + f.getGen() + " actorul:" + f.getActorPrincipal());
	}
	all.push_back(f);
}

// Remove a film from the repository based on its title
void FilmRepo::repo_sterge(const string& titlu)
{
	for (auto it = all.begin(); it != all.end(); ++it) {
		if (it->getTitlu() == titlu) {
			all.erase(it);
			return;
		}
	}
	throw FilmRepoException("Nu exista film cu titlul " + titlu);
}

// Find the position of a film in the repository based on its title
int FilmRepo::caut_poz(const string& titlu)
{
	for (int i = 0; i < all.size(); i++)
		if (all[i].getTitlu() == titlu)
			return i;
	return -1;
}

// Modify the details of a film in the repository
void FilmRepo::repo_modifica(const string& titlu, const string& gen_nou, const string& actor_nou, int an_nou)
{
	const int ind = caut_poz(titlu);
	if (ind == -1)
		throw FilmRepoException("Filmul pe care doriti sa il modificati nu exista!");
	all[ind].setGen(gen_nou);
	all[ind].setActorPrincipal(actor_nou);
	all[ind].setAnulAparitiei(an_nou);
}

// Check if a film already exists in the repository
bool FilmRepo::exist(const Film& f) const {
	try {
		find(f.getTitlu());
		return true;
	}
	catch (FilmRepoException&) {
		return false;
	}
}

// Find a film in the repository based on its title
const Film& FilmRepo::find(const string& titlu) const {
	for (const auto& f : all)
	{
		if (f.getTitlu() == titlu) {
			return f;
		}
	}

	throw FilmRepoException("Nu exista film cu titlul:" + titlu);
}

// Get all films in the repository
const vector<Film>& FilmRepo::getAll() const noexcept {
	return all;
}

// Overloaded output stream operator for FilmRepoException
ostream& operator<<(ostream& out, const FilmRepoException& ex) {
	out << ex.msg;
	return out;
}

// Test function for the FilmRepo class
void testRepository() {
	FilmRepo rep;
	Film f1{ "Titlu1", "Gen1", "Actor1", 1957 };
	Film f2{ "Ceva", "Altceva", "Altcevaaaa", 1961 };
	assert(rep.getAll().size() == 0);
	rep.repo_adauga(f1);
	rep.repo_adauga(f2);
	assert(rep.getAll().size() == 2);
	try {
		rep.repo_adauga(f1); assert(false);
	}
	catch (const FilmRepoException&) {
		assert(true);
	}
	assert(rep.find("Titlu1").getTitlu() == "Titlu1");

	assert(rep.getAll().size() == 2);
	rep.repo_modifica("Tnou", "Gnou", "Anou", 1879);
	Film f = rep.find("Tnou");
	assert(f.getTitlu() == "Tnou");
	assert(f.getGen() == "Gnou");
	assert(f.getActorPrincipal() == "Anou");
	assert(f.getAnulAparitiei() == 1879);

	try {
		rep.find("Titlu43"); assert(false);
	}
	catch (const FilmRepoException& e) {
		stringstream os;
		os << e;
		assert(os.str().find("exista") >= 0);
	}
	try {
		rep.repo_modifica("TitluNou", "GenulNou", "ActorulNou", 1895); assert(false);
	}
	catch (const FilmRepoException&) {
		assert(true);
	}
	rep.repo_sterge("Ceva");
	assert(rep.getAll().size() == 1);
	try {
		rep.repo_sterge("Inexistent"); assert(false);
	}
	catch (const FilmRepoException&) {
		assert(true);
	}
}
