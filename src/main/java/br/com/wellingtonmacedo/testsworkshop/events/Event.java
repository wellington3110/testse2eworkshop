package br.com.wellingtonmacedo.testsworkshop.events;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.With;

import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode
public abstract class Event {

    @With
    private final UUID id;
    private final String type;

    protected Event(UUID id, String type) {
        this.id = id;
        this.type = type;
    }
}
