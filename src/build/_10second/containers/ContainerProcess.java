package build._10second.containers;

import com.googlecode.totallylazy.Strings;

import java.io.IOException;

public abstract class ContainerProcess implements ContainerClient {
    protected abstract String processName();

    @Override
    public Result<String> pull(String name) throws Exception {
        Process process = process(processName(), "pull", name);
        return Result.result(() -> process.waitFor() == 0, Strings.string(process.getInputStream()));
    }

    protected Process process(String... command) throws IOException {
        return new ProcessBuilder(command).
                redirectErrorStream(true).
                start();
    }

    @Override
    public Result<?> remove(String id) throws Exception {
        Process process = process(processName(), "rm", id);
        return Result.result(() -> process.waitFor()==0, null);
    }
}
