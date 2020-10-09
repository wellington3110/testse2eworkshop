package br.com.wellingtonmacedo.testsworkshop.telemetry;

import br.com.wellingtonmacedo.testsworkshop.config.Loggable;
import br.com.wellingtonmacedo.testsworkshop.events.Event;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
public class UserTelemetry implements Loggable {

    public void errorToSaveEvent(Event event, Exception error) {
        logger().error(format("Error to save event: [%s]", event), error);
    }
}
