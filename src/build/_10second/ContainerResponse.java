package build._10second;

import static com.googlecode.totallylazy.Strings.string;
import static java.lang.String.format;

public class ContainerResponse {
    public int status;
    public String log;

    public static ContainerResponse containerResponse(final Process process) throws InterruptedException {
        return new ContainerResponse(){{
            status = process.waitFor();
            log = string(process.getInputStream());
        }};
    }

    @Override
    public String toString() {
        return format("Status:%d\n%s", status, log);
    }
}
