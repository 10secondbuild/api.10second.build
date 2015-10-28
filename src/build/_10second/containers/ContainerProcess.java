package build._10second.containers;

import com.googlecode.totallylazy.Files;
import com.googlecode.totallylazy.Functor;
import com.googlecode.totallylazy.Strings;
import com.googlecode.totallylazy.functions.Function1;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static build._10second.containers.Result.result;
import static com.googlecode.totallylazy.Sequences.sequence;

public abstract class ContainerProcess implements ContainerClient {
    protected abstract String processName();
    protected File temp = Files.temporaryDirectory("10second.build/" + processName());

    @Override
    public Result<String> pull(ContainerConfig config) throws Exception {
        Result<File> process = process(processName(), "pull", config.image + ":" + config.tag);
        return process.map(Strings::string);
    }

    @Override
    public Result<?> start(String id) throws Exception {
        return process(processName(), "start", id);
    }

    @Override
    public Result<?> stop(String id) throws Exception {
        return process(processName(), "stop", id);
    }

    protected Result<File> process(Iterable<? extends String> command) throws IOException {
        return process(sequence(command).toArray(String.class));
    }

    protected Result<File> process(String... command) throws IOException {
        File output = Files.file(temp, Files.randomFilename());
        Process process = new ProcessBuilder(command).
                redirectErrorStream(true).
                redirectOutput(output).
                start();
        return result(() -> process.waitFor() == 0, output);
    }

    @Override
    public Result<?> remove(String id) throws Exception {
        return process(processName(), "rm", id);
    }

    @Override
    public Result<InputStream> attach(String id) throws Exception {
        return process(processName(), "attach", id).map(FileInputStream::new);
    }
}
