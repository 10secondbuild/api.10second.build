package build._10second.containers.docker;

import build._10second.containers.ContainerConfig;
import build._10second.containers.ContainerProcess;
import build._10second.containers.Result;
import com.googlecode.totallylazy.Strings;

import java.io.InputStream;

public class DockerProcess extends ContainerProcess {
    protected String processName() {
        return "docker";
    }

    @Override
    public Result<String> create(ContainerConfig config) throws Exception{
        Process process = process(processName(), "create", config.image);
        InputStream inputStream = process.getInputStream();
        return Result.result(() -> process.waitFor() == 0, Strings.string(inputStream).trim());
    }

}
