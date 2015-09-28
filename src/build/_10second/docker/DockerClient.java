package build._10second.docker;

import build._10second.CommandResponse;
import build._10second.ContainerConfig;
import build._10second.CreateResponse;
import com.googlecode.totallylazy.Streams;
import com.googlecode.totallylazy.Strings;

import java.io.InputStream;

import static com.googlecode.totallylazy.functions.Lazy.lazy;

public class DockerClient extends ContainerClientProcess {
    protected String processName() {
        return "docker";
    }

    @Override
    public CreateResponse create(ContainerConfig config) throws Exception{
        Process process = process(processName(), "create", config.image);
        return createResponse(process);
    }

    public static CreateResponse createResponse(final Process process) throws InterruptedException {
        InputStream inputStream = process.getInputStream();
        return new CreateResponse(lazy(process::waitFor), Strings.string(inputStream).trim());
    }
}
