package build._10second;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static com.googlecode.totallylazy.Maps.map;
import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Unchecked.cast;
import static java.lang.Thread.sleep;
import static java.time.Duration.ofMinutes;

public class BootStrap {
    public static void main(String[] args) throws Exception {
        Packet packet = new Packet();
        Map<String, Object> project = packet.projects().find(m -> m.get("name").equals("10second.build")).get();
        System.out.println("project id = " + project.get("id"));

        Map<String, Object> createdDevice = new Project(project, packet).createDevice(map(
                "hostname", "master.10second.build",
                "plan", "baremetal_1",
                "facility", "ewr1",
                "operating_system", "ubuntu_14_04"
        ));

        String deviceHref = cast(createdDevice.get("href"));
        System.out.println("created device = " + deviceHref);

        Map<String, Object> activeDevice = repeat(() -> {
            sleep(Duration.ofSeconds(10).toMillis());
            return packet.getJson(deviceHref);
        }).find(device -> {
            Object state = device.get("state");
            System.out.println("state = " + state);
            return state.equals("active");
        }).get();

        List<Map<String, Object>> ipAddresses = cast(activeDevice.get("ip_addresses"));

        Map<String, Object> publicIp = sequence(ipAddresses).find(ip -> ip.get("address_family").equals("4") && ip.get("public").equals(true)).get();
        System.out.println("device active on IP4 = " + publicIp.get("address"));

    }
}

