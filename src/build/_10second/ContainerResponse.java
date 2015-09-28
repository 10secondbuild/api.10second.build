package build._10second;

import com.googlecode.totallylazy.functions.Lazy;

import java.io.InputStream;

import static com.googlecode.totallylazy.functions.Lazy.lazy;
import static java.lang.String.format;

public class ContainerResponse implements CommandResponse{
    private final Lazy<Integer> status;
    private final InputStream log;

    public ContainerResponse(Lazy<Integer> status, InputStream log) {
        this.status = status;
        this.log = log;
    }

    public static ContainerResponse containerResponse(final Process process) throws InterruptedException {
        return new ContainerResponse(lazy(process::waitFor), process.getInputStream());
    }

    public int status() {
        return status.value();
    }

    public InputStream log() {
        return log;
    }

    @Override
    public String toString() {
        return format("Status:%d\n%s", status(), log());
    }
}
