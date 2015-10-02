package build._10second.containers.docker;

import build._10second.*;
import build._10second.containers.*;
import com.googlecode.totallylazy.Strings;
import com.googlecode.totallylazy.functions.Lazy;
import com.googlecode.totallylazy.io.Uri;
import com.googlecode.totallylazy.json.Json;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.handlers.ClientHttpHandler;
import com.googlecode.utterlyidle.handlers.HttpClient;

import java.io.InputStream;
import java.util.Map;

import static com.googlecode.totallylazy.Maps.map;
import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.io.Uri.uri;
import static com.googlecode.utterlyidle.MediaType.APPLICATION_JSON;
import static com.googlecode.utterlyidle.RequestBuilder.*;

public class DockerClient implements ContainerClient {
    private final HttpClient http;

    public DockerClient(Uri baseUrl, HttpClient http) {
        this.http = new ModifyRequest(new AuditFailures(http), request ->
                modify(request).
                        uri(baseUrl.mergePath(request.uri().path()).query(request.uri().query())).
                        build());
    }

    public DockerClient() {
        this(host(), new ClientHttpHandler());
    }

    public static Uri host() {
        String docker_host = Environment.getMandatory("DOCKER_HOST");
        if(Environment.get("DOCKER_TLS_VERIFY").contains("1")) throw new UnsupportedOperationException("Not yet implemented TLS handling");
        return uri(docker_host.replace("tcp://", "http://"));
    }

    @Override
    public Result<String> pull(ContainerConfig config) throws Exception {
        Response response = http.handle(post("images/create").query("fromImage", config.image).query("tag", config.tag).build());
        InputStream inputStream = response.entity().inputStream();
        String log = Strings.string(inputStream);
        return Result.result(() -> !log.contains("error"), log);
    }

    @Override
    public Result<String> create(ContainerConfig config) throws Exception {
        Map<String, String> map = map("Image", config.image + ":" + config.tag);
        Response response = http.handle(post("containers/create").
                contentType(APPLICATION_JSON).
                entity(Json.json(map)).build());
        String id = cast(Json.map(response.entity().toString()).get("Id"));
        return Result.result(Lazy.lazy(() -> response.status().isSuccessful()), id);
    }

    @Override
    public Result<?> remove(String id) throws Exception {
        Response response = http.handle(delete("containers/" + id).build());
        return Result.result(() -> response.status().isSuccessful(), null);
    }
}
