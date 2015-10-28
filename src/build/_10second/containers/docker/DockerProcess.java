package build._10second.containers.docker;

import build._10second.containers.ContainerConfig;
import build._10second.containers.ContainerProcess;
import build._10second.containers.Result;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.Strings;

import java.io.File;
import java.io.InputStream;

import static com.googlecode.totallylazy.Sequences.cons;
import static com.googlecode.totallylazy.Sequences.sequence;

public class DockerProcess extends ContainerProcess {
    protected String processName() {
        return "docker";
    }

    @Override
    public Result<String> create(ContainerConfig config) throws Exception{
        Result<File> result = process(cons(processName(), cons("create", cons(config.image, sequence(config.command)))));
        result.success(); // force wait
        return result.map(f -> Strings.string(f).trim());
    }

}
