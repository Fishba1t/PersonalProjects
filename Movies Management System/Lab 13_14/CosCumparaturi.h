#pragma once
#include "Film.h"
#include "Observer.h"
#include <vector>
#include <algorithm>
#include <random> 
#include <chrono>
#include <map>
#include <string>
using std::map;
using std::vector;

class CosCumparaturi:public Observable {

	friend class FilmService;

private:
	vector<Film> cos_cumparaturi;
public:
	CosCumparaturi() = default;

	void adauga_in_cos(const Film& f);

	void goleste_cos() noexcept;

	void adauga_random(vector<Film> filme, int n);

	const vector<Film>& getAll_cumparaturi() noexcept;

};