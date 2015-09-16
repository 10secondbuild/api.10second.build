package build._10second;

import java.util.Map;

import static com.googlecode.totallylazy.Maps.map;

public class Main {
    public static void main(String[] args) throws Exception {
        Packet packet = new Packet();
        Map<String, Object> project = packet.projects().find(m -> m.get("name").equals("10second.build")).get();
        Map<String, Object> newDevice = new Project(project, packet).createDevice(map(
                "hostname", "master.10second.build",
                "plan", "baremetal_1",
                "facility", "ewr1",
                "operating_system", "ubuntu_14_04"
        ));

        System.out.println("newDevice = " + newDevice);
    }
}

