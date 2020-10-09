package br.com.wellingtonmacedo.testsworkshop.repository;

import br.com.wellingtonmacedo.testsworkshop.events.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class EventRepositoryHttp implements EventRepository {

    private String uri;
    private final RestTemplate restTemplate;

    @Autowired
    public EventRepositoryHttp(@Value("${publisher.uri}") String uri, RestTemplate restTemplate) {
        this.uri = uri;
        this.restTemplate = restTemplate;
    }

    @Override
    public void publish(Event event) {
        restTemplate.postForEntity(uri, event, String.class);
    }
}
