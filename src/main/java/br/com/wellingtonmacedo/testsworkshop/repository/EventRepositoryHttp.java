package br.com.wellingtonmacedo.testsworkshop.repository;

import br.com.wellingtonmacedo.testsworkshop.component.UUIDGenerator;
import br.com.wellingtonmacedo.testsworkshop.events.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class EventRepositoryHttp implements EventRepository {

    private String uri;

    private final RestTemplate restTemplate;
    private final UUIDGenerator uuidGenerator;

    @Autowired
    public EventRepositoryHttp(@Value("${publisher.uri}") String uri, RestTemplate restTemplate, UUIDGenerator uuidGenerator) {
        this.uri = uri;
        this.restTemplate = restTemplate;
        this.uuidGenerator = uuidGenerator;
    }

    @Override
    public void publish(Event event) {
        restTemplate.postForEntity(uri, event.withId(uuidGenerator.generate()), String.class);
    }
}
