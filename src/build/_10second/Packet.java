package build._10second;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Unchecked;
import com.googlecode.totallylazy.io.Uri;
import com.googlecode.totallylazy.json.Json;
import com.googlecode.utterlyidle.HttpHeaders;
import com.googlecode.utterlyidle.Request;
import com.googlecode.utterlyidle.RequestBuilder;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.annotations.HttpMethod;
import com.googlecode.utterlyidle.handlers.ClientHttpHandler;
import com.googlecode.utterlyidle.handlers.HttpClient;

import java.util.List;
import java.util.Map;

import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.io.Uri.uri;
import static com.googlecode.totallylazy.predicates.Predicates.is;
import static com.googlecode.utterlyidle.MediaType.APPLICATION_JSON;
import static com.googlecode.utterlyidle.Status.OK;

public class Packet{
    private final String apiKey;
    private final HttpClient http;
    private final Uri baseUrl = uri("https://api.packet.net/");

    public Packet(String apiKey, HttpClient http) {
        this.apiKey = apiKey;
        this.http = http;
    }

    public Packet() {
        this(System.getenv("PACKET_API_KEY"), new ClientHttpHandler());
    }

    public Map<String, Object> getJson(String path) throws Exception {
        Request request = builder(path).build();
        Response response = http.handle(request);
        return Json.map(response.entity().toString());
    }

    public Map<String, Object> postJson(String path, Map<String, Object> json) throws Exception {
        Request request = builder(path).
                method(HttpMethod.POST).
                entity(Json.json(json)).
                header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON).
                build();
        Response response = http.handle(request);
        return Json.map(response.entity().toString());
    }

    private RequestBuilder builder(String path) {
        return RequestBuilder.get(baseUrl.mergePath(path)).
                header("X-Auth-Token", apiKey).
                accepting(APPLICATION_JSON);
    }

    public Sequence<Map<String, Object>> projects() throws Exception {
        return list("projects");
    }

    public Sequence<Map<String, Object>> facilities() throws Exception {
        return list("facilities");
    }

    public Sequence<Map<String, Object>> list(String name) throws Exception {
        return sequence(Unchecked.<List<Map<String, Object>>>cast(getJson(name).get(name)));
    }
}
