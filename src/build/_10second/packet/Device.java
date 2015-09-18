package build._10second.packet;

import build._10second.JsonRecord;
import com.googlecode.totallylazy.Sequence;

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
}
