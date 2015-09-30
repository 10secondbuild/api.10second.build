package build._10second.docker;

import build._10second.*;
import com.googlecode.totallylazy.functions.Lazy;
import com.googlecode.totallylazy.io.Uri;
import com.googlecode.totallylazy.json.Json;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.handlers.ClientHttpHandler;
import com.googlecode.utterlyidle.handlers.HttpClient;

import java.util.Map;

import static com.googlecode.totallylazy.Maps.map;
import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.utterlyidle.MediaType.APPLICATION_JSON;
import static com.googlecode.utterlyidle.RequestBuilder.*;

public class DockerClient implements ContainerClient {
    private final HttpClient http;

    public DockerClient(Uri baseUrl, HttpClient http) {
        this.http = new ModifyRequest(new AuditFailures(http), request ->
                modify(request).
                        uri(baseUrl.mergePath(request.uri().path())).
                        accepting(APPLICATION_JSON).
                        build());
    }

    public DockerClient() {
        this(Uri.uri("http://localhost:2376/"), new ClientHttpHandler());
    }

    @Override
    public ContainerResponse pull(String name) throws Exception {
        Response response = http.handle(post("images/create").query("fromImage", name).build());
        System.out.println("response = " + response);
        return null;
    }

    @Override
    public CreateResponse create(ContainerConfig config) throws Exception {
        Map<String, String> map = map("Image", config.image);
        Response response = http.handle(post("containers/create").
                contentType(APPLICATION_JSON).
                entity(Json.json(map)).build());
        String id = cast(Json.map(response.entity().toString()).get("Id"));
        return new CreateResponse(Lazy.lazy(() -> response.status().code()), id);
    }

    @Override
    public CommandResponse remove(String id) throws Exception {
        Response response = http.handle(delete("containers/" + id).build());
        return () -> response.status().code();
    }
}
