package com.udemy.component;

import org.springframework.stereotype.Component;

import com.udemy.entity.Contact;
import com.udemy.model.ContactModel;

// TODO: Auto-generated Javadoc
/**
 * The Class ContactConverter.
 */
@Component("contactConverter")
public class ContactConverter {
    
    /**
     * Convert contact model 2 contact.
     *
     * @param contactModel the contact model
     * @return the contact
     */
    public Contact convertContactModel2Contact(
        ContactModel contactModel){
        return Contact.builder()
                .city(contactModel.getCity())
                .firstname(contactModel.getFirstName())
                .id(contactModel.getId())
                .lastname(contactModel.getLastName())
                .telephone(contactModel.getTelephone())
                .build();
    }
    
    /**
     * Convert contact 2 contact model.
     *
     * @param contact the contact
     * @return the contact model
     */
    public ContactModel convertContact2ContactModel(
        Contact contact){
        return ContactModel.builder()
                .city(contact.getCity())
                .firstName(contact.getFirstname())
                .id(contact.getId())
                .lastName(contact.getLastname())
                .telephone(contact.getTelephone())
                .build();
    }
}
