package build._10second.packet;

import build._10second.JsonRecord;

import java.math.BigDecimal;

public class IpAddress extends JsonRecord {
    public BigDecimal address_family;
    public String netmask;
    public BigDecimal cidr;
    public String address;
    public String gateway;
    public boolean Public;
}
