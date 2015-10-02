package build._10second.containers;

import com.googlecode.totallylazy.Strings;

import java.io.IOException;
import java.io.InputStream;

public abstract class ContainerProcess implements ContainerClient {
    protected abstract String processName();

    @Override
    public Result<String> pull(ContainerConfig config) throws Exception {
        Process process = process(processName(), "pull", config.image + ":" + config.tag);
        return Result.result(() -> process.waitFor() == 0, Strings.string(process.getInputStream()));
    }

    @Override
    public Result<?> start(String id) throws Exception {
        Process process = process(processName(), "start", id);
        return Result.result(() -> process.waitFor()==0, null);
    }

    @Override
    public Result<?> stop(String id) throws Exception {
        Process process = process(processName(), "stop", id);
        return Result.result(() -> process.waitFor()==0, null);
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

    @Override
    public Result<InputStream> attach(String id) throws Exception {
        Process process = process(processName(), "attach", id);
        return Result.result(() -> process.waitFor()==0, process.getInputStream());
    }
}
