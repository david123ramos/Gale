package messages;

import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbException;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

/**
 *
 * @author David
 */
public class MessageDecoder implements Decoder.Text<Message>{

    @Override
    public Message decode(String string) throws DecodeException {
        return JsonbBuilder.create().fromJson(string, Message.class);
    }

    @Override
    public boolean willDecode(String string) {
        try {
            JsonbBuilder.create().fromJson(string, Message.class);
            return true;
        }catch(JsonbException ex) {
            return false;
        }
    }

    @Override
    public void init(EndpointConfig ec) {}

    @Override
    public void destroy() {}
}
