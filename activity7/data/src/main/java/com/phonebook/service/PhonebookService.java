package com.phonebook.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import com.phonebook.model.Contact;

public class PhonebookService {
    private HashMap<String, Contact> contacts = new HashMap<>();

    public void addContact(Contact c) {
        contacts.put(c.getName().toLowerCase(), c);
    }

    public Contact searchContact(String name) {
        return contacts.get(name.toLowerCase());
    }

    public void removeContact(String name) {
        contacts.remove(name.toLowerCase());
    }

    public void saveToCSV(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Contact contact : contacts.values()) {
                writer.write(contact.toCvsString());
                writer.newLine();
            }
        } catch (IOException) {
            System.err.println("Error saving to CVS: " + e.getMessage());
        }
    }

    public HashMap<String, Contact> getContacts() {
        return contacts;
    }
}
