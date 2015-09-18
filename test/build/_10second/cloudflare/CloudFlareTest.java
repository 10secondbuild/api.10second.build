package build._10second.cloudflare;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.json.Json;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.predicates.Predicates.is;
import static com.googlecode.totallylazy.predicates.Predicates.notNullValue;

public class CloudFlareTest {
    private CloudFlare cloudFlare;

    @Before
    public void setUp() throws Exception {
        cloudFlare = new CloudFlare();
    }

    @Test
    public void supportsUser() throws Exception {
        User user = cloudFlare.user();
        assertThat(user.email, is(CloudFlare.email()));
    }

    @Test
    public void supportsZones() throws Exception {
        Sequence<Zone> zones = cloudFlare.zones();
        for (Zone zone : zones) {
            System.out.println(zone);
        }
    }

    @Test
    public void supportsZone() throws Exception {
        Zone zone = cloudFlare.zone("10second.build");
        assertThat(zone.id, is(notNullValue()));
    }

    @Test
    public void supportsDnsRecords() throws Exception {
        Zone zone = cloudFlare.zone("10second.build");
        Sequence<DnsRecord> records = cloudFlare.dnsRecords(zone);
        for (DnsRecord record : records) {
            System.out.println(record);
        }
    }
}