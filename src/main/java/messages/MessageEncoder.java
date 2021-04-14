package messages;

import javax.json.bind.JsonbBuilder;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 *
 * @author David
 */
public class MessageEncoder implements Encoder.Text<Message>{

    @Override
    public String encode(Message t) throws EncodeException {
        return JsonbBuilder.create().toJson(t);
    }

    @Override
    public void init(EndpointConfig ec) {}

    @Override
    public void destroy() {}
    
}
