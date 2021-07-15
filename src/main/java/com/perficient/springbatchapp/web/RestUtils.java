package com.perficient.springbatchapp.web;

import com.perficient.springbatchapp.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class RestUtils {

     RestTemplate restTemplate;

     @Value("${person.url}")
     String personUrl;

    public RestUtils(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Async
    public  void sendPostRequest(Person person){
        restTemplate.postForEntity(personUrl, person, String.class);
        log.info("Sent request for :"+ person);
    }
}
