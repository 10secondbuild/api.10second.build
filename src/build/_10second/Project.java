package build._10second;

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

class Project extends JsonRecord {
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

class Device extends JsonRecord {
    public String id;
    public String hostname;
    public String href;
    public String state;
    private List<Map<String, Object>> ip_addresses;
    public Sequence<IpAddress> ipAddresses() {
        return sequence(ip_addresses).map(data -> JsonRecord.create(IpAddress.class, data));
    }

    public Device wait(Packet packet, Predicate<Device> predicate) throws Exception {
        return repeat(() -> {
                sleep(Duration.ofSeconds(10).toMillis());
                return refesh(packet);
            }).find(predicate).get();
    }

    public Device refesh(Packet packet) throws Exception {
        return JsonRecord.create(Device.class, packet.getJson(href));
    }
}

class IpAddress extends JsonRecord {
    public BigDecimal address_family;
    public String netmask;
    public BigDecimal cidr;
    public String address;
    public String gateway;
    public boolean Public;
}
