package build._10second;

import build._10second.packet.Device;
import build._10second.packet.IpAddress;
import build._10second.packet.Packet;
import build._10second.packet.Project;

import java.math.BigDecimal;

import static com.googlecode.totallylazy.Maps.map;

public class BootStrap {
    public static void main(String[] args) throws Exception {
        Packet packet = new Packet();
        Project project = packet.project("10second.build");

        Device provisioned = project.provisionDevice(packet, map(
                "hostname", "master.10second.build",
                "plan", "baremetal_1",
                "facility", "ewr1",
                "operating_system", "ubuntu_14_04"
        ));

        Device active = provisioned.wait(packet, d -> d.state.equals("active"));

        IpAddress publicIp = active.ipAddresses().
                find(ip -> ip.address_family.equals(new BigDecimal("4")) && ip.Public).
                get();
        System.out.println("device active on IP4 = " + publicIp.address);
    }

}

