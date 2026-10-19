package com.raichess.websocket;

import com.raichess.event.PersonChanged;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class PersonChangeNotifier {

    private final PersonWebSocketHandler handler;

    public PersonChangeNotifier(PersonWebSocketHandler handler) {
        this.handler = handler;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onChange(PersonChanged event) {
        handler.broadcast("{\"type\":\"" + event.getType() + "\",\"id\":" + event.getId() + "}");
    }
}
