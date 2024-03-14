#include "Validator.h"
#include <assert.h>
#include <sstream>

// Function to validate a Film object
void FilmValidator::validare(const Film& f) {
    // Vector to store validation messages
    vector<string> msgs;

    // Check if the film's title is empty
    if (f.getTitlu().size() == 0) msgs.push_back("NEVALIDAT: Titlu vid!!!");

    // Check if the film's genre is empty
    if (f.getGen().size() == 0) msgs.push_back("NEVALIDAT: Gen vid!!!");

    // Check if the film's main actor is empty
    if (f.getActorPrincipal().size() == 0) msgs.push_back("NEVALIDAT: Actor vid!!!");

    // Check if the film's release year is outside the valid range [1860, 2023]
    if (f.getAnulAparitiei() < 1860 || f.getAnulAparitiei() > 2023)
        msgs.push_back("NEVALIDAT: Anul este invalid!!!");

    // If there are validation messages, throw a ValidateException
    if (msgs.size() > 0) {
        throw ValidateException(msgs);
    }
}

// Overloaded operator to output validation exception messages
ostream& operator<<(ostream& out, const ValidateException& ex) {
    for (const auto& msg : ex.msgs) {
        out << msg << " ";
    }
    return out;
}

// Test function for the FilmValidator
void testValidator() {
    // Create a FilmValidator instance
    FilmValidator v;

    // Create a Film object with invalid attributes
    Film f{ "", "", "", 1800 };

    try {
        // Attempt to validate the Film object, expecting a ValidateException
        v.validare(f);
    }
    catch (const ValidateException& ex) {
        // Create a stringstream to capture the exception messages
        std::stringstream sout;
        sout << ex;

        // Convert the stringstream to a string
        const auto mesaj = sout.str();

        // Assert that the validation messages contain expected substrings
        assert(mesaj.find("vid") >= 0);
        assert(mesaj.find("invalid") >= 0);
    }
}
