package build._10second.packet;

import build._10second.JsonRecord;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.predicates.Predicate;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.Sequences.sequence;
import static java.lang.Thread.sleep;

public class Device extends JsonRecord {
    public String id;
    public String hostname;
    public String href;
    public String state;
    private List<Map<String, Object>> ip_addresses;
    public Sequence<IpAddress> ipAddresses() {
        return sequence(ip_addresses).map(data -> create(IpAddress.class, data));
    }

    public Device wait(Packet packet, Predicate<Device> predicate) throws Exception {
        return repeat(() -> {
                sleep(Duration.ofSeconds(10).toMillis());
                return refesh(packet);
            }).find(predicate).get();
    }

    public Device refesh(Packet packet) throws Exception {
        return create(Device.class, packet.getJson(href));
    }
}
