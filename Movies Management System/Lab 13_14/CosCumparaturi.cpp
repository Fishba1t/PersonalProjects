#include "CosCumparaturi.h"
#include <exception>
#include <iostream>
#include <map>
#include <string>

using namespace std;
using std::shuffle;

// Class implementation for the shopping cart
void CosCumparaturi::adauga_in_cos(const Film& f) {
    cos_cumparaturi.push_back(f); // Add a film to the shopping cart
    notify(); // Notify observers about the change in the shopping cart
}

// Clear the shopping cart
void CosCumparaturi::goleste_cos() noexcept {
    cos_cumparaturi.clear(); // Remove all items from the shopping cart
    notify(); // Notify observers about the change in the shopping cart
}

// Add a random selection of films to the shopping cart
void CosCumparaturi::adauga_random(vector<Film> filme, int n) {
    shuffle(filme.begin(), filme.end(), std::default_random_engine(std::random_device{}())); // Shuffle the vector of films
    const size_t init_size = cos_cumparaturi.size();
    while (cos_cumparaturi.size() < n + init_size && filme.size() > 0) {
        cos_cumparaturi.push_back(filme.back()); // Add films randomly to the shopping cart
        filme.pop_back();
    }
    notify(); // Notify observers about the change in the shopping cart
}

// Get all films in the shopping cart
const vector<Film>& CosCumparaturi::getAll_cumparaturi() noexcept {
    return cos_cumparaturi; // Return the vector containing all films in the shopping cart
}
