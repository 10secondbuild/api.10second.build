package build._10second;

import com.googlecode.totallylazy.Sequence;

import java.math.BigDecimal;

import static com.googlecode.totallylazy.Sequences.sequence;

public class DeviceTest {
    public static void main(String[] args) throws Exception {
        Packet packet = new Packet();
        Sequence<Device> devices = packet.project("10second.build").devices(packet);

        for (Device device : devices) {
            System.out.println(device);
            IpAddress publicIp = sequence(device.ipAddresses()).
                    find(ip -> ip.address_family.equals(new BigDecimal("4")) && ip.Public).
                    get();
            System.out.println("device active on IP4 = " + publicIp.address);
        }

    }
}