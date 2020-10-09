package br.com.wellingtonmacedo.testsworkshop.service;

import br.com.wellingtonmacedo.testsworkshop.component.UUIDGenerator;
import br.com.wellingtonmacedo.testsworkshop.events.Event;
import br.com.wellingtonmacedo.testsworkshop.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    private final UUIDGenerator uuidGenerator;
    private final EventRepository repository;

    @Autowired
    public EventService(UUIDGenerator uuidGenerator, EventRepository repository) {
        this.uuidGenerator = uuidGenerator;
        this.repository = repository;
    }

    public void publish(Event event) {
        repository.publish(event.withId(uuidGenerator.generate()));
    }
}
