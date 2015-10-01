package build._10second.containers;

import com.googlecode.totallylazy.Streams;

import java.io.IOException;

public abstract class ContainerProcess implements ContainerClient {
    protected abstract String processName();

    @Override
    public ContainerResponse pull(String name) throws Exception {
        return ContainerResponse.containerResponse(process(processName(), "pull", name));
    }

    protected Process process(String... command) throws IOException {
        return new ProcessBuilder(command).
                redirectErrorStream(true).
                start();
    }

    @Override
    public CommandResponse remove(String id) throws Exception {
        Process process = process(processName(), "rm", id);
        Streams.copy(process.getInputStream(), System.out);
        return CommandResponse.commandResponse(process);
    }
}
