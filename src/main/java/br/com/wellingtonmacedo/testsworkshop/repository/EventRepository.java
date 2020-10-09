package br.com.wellingtonmacedo.testsworkshop.repository;

import br.com.wellingtonmacedo.testsworkshop.events.Event;

public interface EventRepository {
    void publish(Event event);
}
