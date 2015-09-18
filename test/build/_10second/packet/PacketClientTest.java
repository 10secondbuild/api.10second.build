package build._10second.packet;

import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Some;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static com.googlecode.totallylazy.Assert.assertFalse;
import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.Assert.assertTrue;
import static com.googlecode.totallylazy.Maps.map;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.predicates.Predicates.instanceOf;
import static com.googlecode.totallylazy.predicates.Predicates.is;
import static com.googlecode.totallylazy.predicates.Predicates.notNullValue;

public class PacketClientTest {
    private PacketClient client;

    @Before
    public void setUp() throws Exception {
        client = new PacketClient();
    }

    @Test
    public void supportsFacilites() throws Exception {
        Sequence<Facility> facilities = client.facilities();
        for (Facility facility : facilities) {
            assertThat(facility.id, is(instanceOf(String.class)));
            assertThat(facility.name, is(instanceOf(String.class)));
            assertThat(facility.code, is(instanceOf(String.class)));
        }
    }

    @Test
    public void supportsProjects() throws Exception {
        Sequence<Project> projects = client.projects();
        for (Project project : projects) {
            assertThat(project.id, is(instanceOf(String.class)));
            assertThat(project.name, is(instanceOf(String.class)));
            assertThat(project.devicesPath().toString(), is("projects/" + project.id + "/devices"));
        }
    }

    @Test
    public void supportsDevicesAndIPAddresses() throws Exception {
        Project project = client.project("10second.build");
        Sequence<Device> devices = client.devices(project);

        for (Device device : devices) {
            assertThat(device.id, is(instanceOf(String.class)));
            assertThat(device.hostname, is(instanceOf(String.class)));
            assertThat(device.href, is(instanceOf(String.class)));
            assertThat(device.state, is(instanceOf(String.class)));

            for (IpAddress ipAddress : device.ipAddresses()) {
                assertThat(ipAddress.address, is(instanceOf(String.class)));
                assertThat(ipAddress.address_family, is(instanceOf(Number.class)));
                assertThat(ipAddress.cidr, is(instanceOf(Number.class)));
                assertThat(ipAddress.gateway, is(instanceOf(String.class)));
                assertThat(ipAddress.netmask, is(instanceOf(String.class)));
            }
        }
    }

    @Test
    @Ignore("Manual test - takes 7 minutes")
    public void supportsCreatingADeviceAndRemovingIt() throws Exception {
        String testHost = getClass().getSimpleName().toLowerCase();

        Project project = client.project("10second.build");

        assertFalse(client.devices(project).exists(d -> d.hostname.equals(testHost)));

        Device provisioned = client.provisionDevice(project, map(
            "hostname", testHost,
            "plan", "baremetal_1",
            "facility", "ewr1",
            "operating_system", "ubuntu_14_04"
        ));

        Device active = client.pollUntil(provisioned, d -> d.state.equals("active"));

        Option<IpAddress> publicIp4 = active.ipAddresses().find(ip -> ip.address_family.equals(new BigDecimal("4")) && ip.Public);
        assertThat(publicIp4, is(instanceOf(Some.class)));

        assertTrue(client.devices(project).exists(d -> d.hostname.equals(testHost)));

        client.deprovisionDevice(project, active);

        assertFalse(client.devices(project).exists(d -> d.hostname.equals(testHost)));
    }
}