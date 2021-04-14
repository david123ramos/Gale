package messages;

/**
 *
 * @author David
 * 
 * 
 * 
 * a classe Message é um empacotador genérico a ser enviado ao front
 * em qualquer evento do websocket;
 */
public class Message<T> {
    
    private String eventName;
    private T data;
    
    public Message(){}
    public Message(String eventName, T data) {
        this.eventName = eventName;
        this.data = data;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
