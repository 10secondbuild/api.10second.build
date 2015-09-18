package build._10second.cloudflare;

import com.googlecode.totallylazy.Sequence;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.net.InetAddress;
import java.util.Map;
import java.util.UUID;

import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.Maps.map;
import static com.googlecode.totallylazy.predicates.Predicates.instanceOf;
import static com.googlecode.totallylazy.predicates.Predicates.is;
import static com.googlecode.totallylazy.predicates.Predicates.notNullValue;

public class CloudFlareClientTest {
    private CloudFlareClient client;

    @Before
    public void setUp() throws Exception {
        client = new CloudFlareClient();
    }

    @Test
    public void supportsUser() throws Exception {
        User user = client.user();
        assertThat(user.email, is(CloudFlareClient.email()));
    }

    @Test
    public void supportsZones() throws Exception {
        Sequence<Zone> zones = client.zones();
        for (Zone zone : zones) {
            assertThat(zone.id, is(instanceOf(String.class)));
            assertThat(zone.name, is(instanceOf(String.class)));
            assertThat(zone.status, is(instanceOf(String.class)));
        }
    }

    @Test
    public void supportsZone() throws Exception {
        Zone zone = client.zone("10second.build");
        assertThat(zone.id, is(instanceOf(String.class)));
        assertThat(zone.name, is(instanceOf(String.class)));
        assertThat(zone.status, is(instanceOf(String.class)));
    }

    @Test
    public void supportsDnsRecords() throws Exception {
        Zone zone = client.zone("10second.build");
        Sequence<DnsRecord> records = client.dnsRecords(zone);
        for (DnsRecord record : records) {
            assertThat(record.id, is(instanceOf(String.class)));
            assertThat(record.name, is(instanceOf(String.class)));
            assertThat(record.type, is(instanceOf(String.class)));
            assertThat(record.content, is(instanceOf(String.class)));
        }
    }

    @Test
    @Ignore("Manual test")
    public void supportsCreatingDnsRecord() throws Exception {
        Zone zone = client.zone("10second.build");
        String name = getClass().getSimpleName().toLowerCase();
        String ipAddress = "10.0.0.1";
        DnsRecord record = client.createDnsRecord(zone, map("type", "A", "name", name, "content", ipAddress));
        client.delete(zone, record);
    }
}