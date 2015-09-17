package build._10second.packet;

import build._10second.JsonRecord;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.predicates.Predicate;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.utterlyidle.PathParameters.pathParameters;
import static com.googlecode.utterlyidle.UriTemplate.uriTemplate;
import static java.lang.Thread.sleep;

public class Project extends JsonRecord {
    public String id;
    public String name;

    public Device provisionDevice(Packet packet, Map<String, Object> device) throws Exception {
        String path = devicesPath();
        return JsonRecord.create(Device.class, packet.postJson(path, device));
    }

    private String devicesPath() {
        return uriTemplate("/projects/{id}/devices").generate(pathParameters().add("id", id));
    }

    public Sequence<Device> devices(Packet packet) throws Exception {
        return packet.list("devices", devicesPath()).map(data -> JsonRecord.create(Device.class, data));
    }

}

