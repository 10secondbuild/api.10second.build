package build._10second.docker;

import build._10second.ContainerClient;
import build._10second.ContainerResponse;

import java.io.IOException;

import static build._10second.ContainerResponse.containerResponse;

public abstract class ContainerClientProcess implements ContainerClient {
    protected abstract String processName();

    @Override
    public ContainerResponse pull(String name) throws Exception {
        return containerResponse(process(processName(), "pull", name));
    }

    protected Process process(String... command) throws IOException {
        return new ProcessBuilder(command).
                redirectErrorStream(true).
                start();
    }
}
