package build._10second;

import java.util.Map;

import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.utterlyidle.PathParameters.pathParameters;
import static com.googlecode.utterlyidle.UriTemplate.uriTemplate;

public class Project {
    private final Map<String, Object> data;
    private final Packet packet;

    public Project(Map<String, Object> data, Packet packet) {
        this.data = data;
        this.packet = packet;
    }

    public String name() {
        return get("name");
    }

    public String id() {
        return get("id");
    }

    private <T> T get(String key) {
        return cast(data.get(key));
    }

    public Map<String, Object> createDevice(Map<String, Object> device) throws Exception {
        String path = uriTemplate("/projects/{id}/devices").generate(pathParameters().add("id", id()));
        return packet.postJson(path, device);
    }
}
