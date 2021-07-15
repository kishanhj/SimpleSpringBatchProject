package com.perficient.springbatchapp.model;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

public class PersonFieldSetMapper implements FieldSetMapper<Person> {

    @Override
    public Person mapFieldSet(FieldSet fieldSet) {
        return new Person(fieldSet.readString("firstName"), fieldSet.readString("lastName"));
    }
}
