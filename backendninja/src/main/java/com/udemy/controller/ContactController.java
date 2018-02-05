package com.udemy.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.udemy.constant.ViewConstant;
import com.udemy.model.ContactModel;
import com.udemy.service.ContactService;

// TODO: Auto-generated Javadoc
/**
 * The Class ContactController.
 */
@Controller
@RequestMapping("/contacts")
public class ContactController {
    
    /** The Constant LOG. */
    private static final Log LOG = LogFactory.getLog(ContactController.class);
    
    /** The contact service. */
    @Autowired
    @Qualifier("contactServiceImpl")
    private ContactService contactService;
    
    /**
     * Cancel.
     *
     * @return the string
     */
    @GetMapping("/cancel")
    public String cancel(){
        return "redirect:/contacts/showcontacts";
    }
    
    /**
     * Redirect contact form.
     *
     * @param id the id
     * @param model the model
     * @return the string
     */
    @GetMapping("/contactform")
    public String redirectContactForm(
        @RequestParam(name = "id", required = false) int id,
        Model model){
        ContactModel contact = new ContactModel();
        if (id != 0) {
            contact = contactService.findContactByIdModel(id);
        }
        model.addAttribute("contactModel", contact);
        return ViewConstant.CONTACT_FORM;
    }
    
    /**
     * Adds the contact.
     *
     * @param contactModel the contact model
     * @param model the model
     * @return the string
     */
    @PostMapping("/addcontact")
    public String addContact(
        @ModelAttribute(name = "contact") ContactModel contactModel,
        Model model){
        LOG.info("METHOD: addContact() -- PARAMS: " + contactModel.toString());
        
        if (null != contactService.addContact(contactModel)) {
            model.addAttribute("result", 1);
        } else {
            model.addAttribute("result", 0);
        }
        
        return "redirect:/contacts/showcontacts";
    }
    
    /**
     * Show contacts.
     *
     * @return the model and view
     */
    @GetMapping("/showcontacts")
    public ModelAndView showContacts(){
        ModelAndView mav = new ModelAndView(ViewConstant.CONTACTS);
        
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        mav.addObject("username", user.getUsername());
        mav.addObject("contacts", contactService.listAllContacts());
        return mav;
    }
    
    /**
     * Rmove contact.
     *
     * @param id the id
     * @return the model and view
     */
    @GetMapping("/removecontact")
    public ModelAndView rmoveContact(
        @RequestParam(name = "id", required = true) int id){
        contactService.removeContact(id);
        return showContacts();
    }
}
