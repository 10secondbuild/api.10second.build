package build._10second.packet;

import com.googlecode.totallylazy.Sequence;
import org.junit.Test;

import java.math.BigDecimal;

import static com.googlecode.totallylazy.Sequences.sequence;

public class DeviceTest {
    @Test
    public void canGetDevices() throws Exception {
        Packet packet = new Packet();
        Sequence<Device> devices = packet.project("10second.build").devices(packet);

        for (Device device : devices) {
            IpAddress publicIp = sequence(device.ipAddresses()).
                    find(ip -> ip.address_family.equals(new BigDecimal("4")) && ip.Public).
                    get();
            System.out.println("device active on IP4 = " + publicIp.address);
        }
    }
}