package com.udemy.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.udemy.component.ContactConverter;
import com.udemy.entity.Contact;
import com.udemy.model.ContactModel;
import com.udemy.repository.ContactRepository;
import com.udemy.service.ContactService;

// TODO: Auto-generated Javadoc
/**
 * The Class ContactServiceImpl.
 */
@Service("contactServiceImpl")
public class ContactServiceImpl implements ContactService {
    
    /** The contact repository. */
    @Autowired
    @Qualifier("contactRepository")
    private ContactRepository contactRepository;
    
    /** The contact converter. */
    @Autowired
    @Qualifier("contactConverter")
    private ContactConverter contactConverter;
    
    /*
     * (non-Javadoc)
     * @see
     * com.udemy.service.ContactService#addContact(com.udemy.model.ContactModel)
     */
    @Override
    public ContactModel addContact(
        ContactModel contactModel){
        Contact contact = contactRepository.save(contactConverter.convertContactModel2Contact(contactModel));
        return contactConverter.convertContact2ContactModel(contact);
    }
    
    /*
     * (non-Javadoc)
     * @see com.udemy.service.ContactService#listAllContacts()
     */
    @Override
    public List<ContactModel> listAllContacts(){
        List<Contact> contacts = contactRepository.findAll();
        List<ContactModel> contactsModel = new ArrayList<>();
        
        for (Contact contact : contacts) {
            contactsModel.add(contactConverter.convertContact2ContactModel(contact));
        }
        return contactsModel;
    }
    
    /*
     * (non-Javadoc)
     * @see com.udemy.service.ContactService#findContactById(int)
     */
    @Override
    public Contact findContactById(
        int id){
        return contactRepository.findById(id);
    }
    
    /*
     * (non-Javadoc)
     * @see com.udemy.service.ContactService#findContactByIdModel(int)
     */
    @Override
    public ContactModel findContactByIdModel(
        int id){
        return contactConverter.convertContact2ContactModel(findContactById(id));
    }
    
    /*
     * (non-Javadoc)
     * @see com.udemy.service.ContactService#removeContact(int)
     */
    @Override
    public void removeContact(
        int id){
        Contact contact = contactRepository.findById(id);
        
        if (null != contact) {
            contactRepository.delete(contact);
        }
    }
    
}
