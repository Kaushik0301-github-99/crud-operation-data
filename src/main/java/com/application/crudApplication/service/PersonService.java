package com.application.crudApplication.service;

import com.application.crudApplication.entity.Person;
import com.application.crudApplication.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;

public class PersonService {
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public Person createPerson(Person person) {
        Person savedPerson = personRepository.save(person);
        kafkaTemplate.send("person_topic", "Created person with ID: " + savedPerson.getId());
        return savedPerson;
    }

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public Person updatePerson(Long id, Person personDetails) {
        Person person = personRepository.findById(id).orElseThrow(() -> new RuntimeException("Person not found"));
        person.setName(personDetails.getName());
        Person updatedPerson = personRepository.save(person);
        kafkaTemplate.send("person_topic", "Updated person with ID: " + updatedPerson.getId());
        return updatedPerson;
    }

    public void deletePerson(Long id) {
        personRepository.deleteById(id);
        kafkaTemplate.send("person_topic", "Deleted person with ID: " + id);
    }
}
