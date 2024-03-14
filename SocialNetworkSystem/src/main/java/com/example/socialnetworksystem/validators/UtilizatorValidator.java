package com.example.socialnetworksystem.validators;

import com.example.socialnetworksystem.domain.Utilizator;

public class UtilizatorValidator implements Validator<Utilizator> {
    @Override
    public void validate(Utilizator entity) throws ValidationException {
        //System.out.println("Marius!");
        if(entity.getEmail()==""|| entity.getFirstName()=="" || entity.getLastName()==""){
            throw new ValidationException("EROARE LA VALIDARE : UTILIZATORUL NU POATE SA FIE NULL");
        }
    }
}
