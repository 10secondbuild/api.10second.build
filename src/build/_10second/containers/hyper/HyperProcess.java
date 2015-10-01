package build._10second.containers.hyper;

import build._10second.containers.ContainerConfig;
import build._10second.containers.CreateResponse;
import build._10second.containers.ContainerProcess;
import build._10second.containers.Result;
import com.googlecode.totallylazy.*;
import com.googlecode.totallylazy.json.Json;
import com.googlecode.totallylazy.regex.Regex;

import java.io.File;

import static com.googlecode.totallylazy.Bytes.bytes;
import static com.googlecode.totallylazy.Files.temporaryFile;
import static com.googlecode.totallylazy.Files.write;
import static com.googlecode.totallylazy.Lists.list;
import static com.googlecode.totallylazy.Maps.map;
import static com.googlecode.totallylazy.functions.Lazy.lazy;

public class HyperProcess extends ContainerProcess {
    protected String processName() {
        return "hyper";
    }

    @Override
    public Result<String> create(ContainerConfig config) throws Exception {
        String json = Json.json(map("containers", list(map("image", config.image))));
        System.out.println("json = " + json);
        File pod = temporaryFile();
        write(bytes(json), pod);
        return createResponse(process(processName(), "create", pod.getAbsolutePath()));
    }

    private static final Regex podId = Regex.regex("pod-\\w+");

    public static Result<String> createResponse(final Process process) throws InterruptedException {
        String value = Strings.string(process.getInputStream());
        return Result.result(() -> process.waitFor() == 0, podId.match(value).group());
    }
}
