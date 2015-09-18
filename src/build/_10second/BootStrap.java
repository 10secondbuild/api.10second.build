package build._10second;

import build._10second.packet.Device;
import build._10second.packet.IpAddress;
import build._10second.packet.PacketClient;
import build._10second.packet.Project;

import java.math.BigDecimal;

public class BootStrap {
    public static void main(String[] args) throws Exception {
        PacketClient packetClient = new PacketClient();
        Project project = packetClient.project("10second.build");

        Device provisioned = packetClient.provisionDevice(project, new Device() {{
            hostname = "master.10second.build";
            put("plan", "baremetal_1");
            put("facility", "ewr1");
            put("operating_system", "ubuntu_14_04");
        }});

        Device active = packetClient.pollUntil(provisioned, d -> d.state.equals("active"));

        IpAddress publicIp = active.ipAddresses().
                find(ip -> ip.address_family.equals(new BigDecimal("4")) && ip.Public).
                get();
        System.out.println("device active on IP4 = " + publicIp.address);
    }

}

