package build._10second.containers;

import build._10second.JsonRecord;

import java.util.List;

import static com.googlecode.totallylazy.Lists.list;

public class ContainerConfig extends JsonRecord {
    public String image;
    public String tag = "latest";
    public List<String> command = list();
}
