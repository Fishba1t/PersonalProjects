package com.example.socialnetworksystem.validators;

import com.example.socialnetworksystem.domain.Message;

public class MessagesValidator implements Validator<Message>{

    @Override
    public void validate(Message entity) throws ValidationException {
        if(entity.getId()==null){
            throw  new ValidationException("ID CAN'T BE NULL!");
        }
    }
}
