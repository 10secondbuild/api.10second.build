package build._10second.docker;

import build._10second.ContainerConfig;
import build._10second.ContainerProcess;
import build._10second.CreateResponse;
import com.googlecode.totallylazy.Strings;

import java.io.InputStream;

import static com.googlecode.totallylazy.functions.Lazy.lazy;

public class DockerProcess extends ContainerProcess {
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
