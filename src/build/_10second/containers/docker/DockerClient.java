package build._10second.containers.docker;

import build._10second.AuditFailures;
import build._10second.Environment;
import build._10second.ModifyRequest;
import build._10second.containers.ContainerClient;
import build._10second.containers.ContainerConfig;
import build._10second.containers.Result;
import com.googlecode.totallylazy.Strings;
import com.googlecode.totallylazy.functions.Lazy;
import com.googlecode.totallylazy.io.Uri;
import com.googlecode.totallylazy.json.Json;
import com.googlecode.utterlyidle.Request;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.handlers.ClientHttpHandler;
import com.googlecode.utterlyidle.handlers.HttpClient;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.io.InputStream;
import java.net.URI;
import java.util.Map;

import static com.googlecode.totallylazy.Maps.map;
import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.io.Uri.uri;
import static com.googlecode.utterlyidle.MediaType.APPLICATION_JSON;
import static com.googlecode.utterlyidle.RequestBuilder.*;

public class DockerClient implements ContainerClient {
    private final Uri baseUrl;
    private final HttpClient http;

    public DockerClient(Uri baseUrl, HttpClient http) {
        this.baseUrl = baseUrl;
        this.http = new ModifyRequest(new AuditFailures(http), request ->
                modify(request).
                        uri(merge(request.uri())).
                        build());
    }

    private Uri merge(Uri original) {
        return baseUrl.mergePath(original.path()).query(original.query());
    }

    public DockerClient() {
        this(host(), new ClientHttpHandler());
    }

    public static Uri host() {
        String docker_host = Environment.getMandatory("DOCKER_HOST");
        if (Environment.get("DOCKER_TLS_VERIFY").contains("1"))
            throw new UnsupportedOperationException("Not yet implemented TLS handling");
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
    public Result<?> start(String id) throws Exception {
        Response response = http.handle(post("containers/" + id + "/start").build());
        return Result.result(() -> response.status().isSuccessful(), null);
    }

    @Override
    public Result<?> stop(String id) throws Exception {
        Response response = http.handle(post("containers/" + id + "/stop").build());
        return Result.result(() -> response.status().isSuccessful(), null);
    }

    @Override
    public Result<?> remove(String id) throws Exception {
        Response response = http.handle(delete("containers/" + id).build());
        return Result.result(() -> response.status().isSuccessful(), null);
    }

    @Override
    public Result<InputStream> attach(String id) throws Exception {

        Request req = get("/containers/" + id + "/attach/ws").
                query("logs", "1").
                query("stream", "1").
                query("stdin", "1").
                query("stdout", "1").
                query("stderr", "1").
                build();

        WebSocketClient client = new WebSocketClient();
        WebSocketAdapter socket = new WebSocketAdapter() {
            @Override
            public void onWebSocketText(String message) {
                System.out.println("text = " + message);
            }

            @Override
            public void onWebSocketBinary(byte[] payload, int offset, int len) {
                System.out.println("binary");
            }

        };
        client.start();
        URI uri = merge(req.uri()).scheme("ws").toURI();
        System.out.printf("Connecting to : %s%n", uri);
        Session session = client.connect(socket, uri).get();

        client.stop();
        socket.getSession().close();


        return null;
    }
}


