package com.perficient.springbatchapp.config;

import com.perficient.springbatchapp.model.Person;
import com.perficient.springbatchapp.model.PersonFieldSetMapper;
import com.perficient.springbatchapp.web.RestUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class JobConfiguration {

    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Autowired
    JobBuilderFactory jobBuilderFactory;

    @Autowired
    RestUtils restUtils;

    @Bean
    public FlatFileItemReader<Person> csvPersonReader(){
        FlatFileItemReader<Person> personReader = new FlatFileItemReader<>();

        personReader.setLinesToSkip(1);
        personReader.setResource(new ClassPathResource("data/person_data.csv"));

        DefaultLineMapper<Person> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("id","firstName","lastName","email","gender","ipAddress");

        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(new PersonFieldSetMapper());
        lineMapper.afterPropertiesSet();

        personReader.setLineMapper(lineMapper);
        return personReader;
    }

    @Bean
    public ItemWriter<Person> personItemWriter(){
        return people -> people.forEach(restUtils::sendPostRequest);
    }

    @Bean
    public Step step1(){
        return stepBuilderFactory.get("step1")
                .<Person, Person>chunk(100)
                .reader(csvPersonReader())
                .writer(personItemWriter())
                .build();
    }

    @Bean
    public Job job1(){
        return jobBuilderFactory.get("job1")
                .start(step1())
                .build();
    }
}
