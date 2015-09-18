package build._10second.cloudflare;

import com.googlecode.totallylazy.Sequence;
import org.junit.Before;
import org.junit.Test;

import static com.googlecode.totallylazy.Assert.assertThat;
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
            System.out.println(zone);
        }
    }

    @Test
    public void supportsZone() throws Exception {
        Zone zone = client.zone("10second.build");
        assertThat(zone.id, is(notNullValue()));
    }

    @Test
    public void supportsDnsRecords() throws Exception {
        Zone zone = client.zone("10second.build");
        Sequence<DnsRecord> records = client.dnsRecords(zone);
        for (DnsRecord record : records) {
            System.out.println(record);
        }
    }
}