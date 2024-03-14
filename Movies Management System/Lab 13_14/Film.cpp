#include "Film.h"

// Comparison function for sorting films based on title
bool cmpTitlu(const Film& f1, const Film& f2) {
    return f1.getTitlu() < f2.getTitlu(); // Compare films based on their titles
}

// Comparison function for sorting films based on the main actor
bool cmpActorPrincipal(const Film& f1, const Film& f2) {
    return f1.getActorPrincipal() < f2.getActorPrincipal(); // Compare films based on their main actors
}

// Comparison function for sorting films based on genre and release year
bool cmpGenAn(const Film& f1, const Film& f2) {
    if (f1.getGen() == f2.getGen())
        return f1.getAnulAparitiei() < f2.getAnulAparitiei(); // If genres are the same, compare based on release year
    else
        return f1.getGen() < f2.getGen(); // Otherwise, compare based on genre
}
