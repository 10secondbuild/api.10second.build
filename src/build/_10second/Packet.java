package build._10second;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Unchecked;
import com.googlecode.totallylazy.io.Uri;
import com.googlecode.totallylazy.json.Json;
import com.googlecode.utterlyidle.Request;
import com.googlecode.utterlyidle.RequestBuilder;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.annotations.HttpMethod;
import com.googlecode.utterlyidle.handlers.AuditHandler;
import com.googlecode.utterlyidle.handlers.ClientHttpHandler;
import com.googlecode.utterlyidle.handlers.HttpClient;
import com.googlecode.utterlyidle.handlers.PrintAuditor;

import java.util.List;
import java.util.Map;

import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.blank;
import static com.googlecode.totallylazy.io.Uri.uri;
import static com.googlecode.totallylazy.predicates.Predicates.is;
import static com.googlecode.totallylazy.predicates.Predicates.not;
import static com.googlecode.utterlyidle.HttpHeaders.CONTENT_TYPE;
import static com.googlecode.utterlyidle.MediaType.APPLICATION_JSON;

public class Packet {
    private final String apiKey;
    private final HttpClient http;
    private final Uri baseUrl = uri("https://api.packet.net/");

    public Packet(String apiKey, HttpClient http) {
        this.apiKey = apiKey;
        this.http = http;
    }

    public Packet() {
        this(hasValue(System.getenv("PACKET_API_KEY")),
                new AuditHandler(new ClientHttpHandler(), new PrintAuditor(System.out)));
    }

    private static String hasValue(String value) {
        assertThat(value, not(blank));
        return value;
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
                header(CONTENT_TYPE, APPLICATION_JSON).
                build();
        Response response = http.handle(request);
        return Json.map(response.entity().toString());
    }

    private RequestBuilder builder(String path) {
        return RequestBuilder.get(baseUrl.mergePath(path)).
                header("X-Auth-Token", apiKey).
                accepting(APPLICATION_JSON);
    }

    public Sequence<Project> projects() throws Exception {
        return list("projects").map(data -> JsonRecord.create(Project.class, data));
    }

    public Project project(String name) throws Exception {
        return projects().find(p -> p.name.equals(name)).get();
    }

    public Sequence<Map<String, Object>> facilities() throws Exception {
        return list("facilities");
    }

    public Sequence<Map<String, Object>> list(String name) throws Exception {
        return list(name, name);
    }

    public Sequence<Map<String, Object>> list(String name, String path) throws Exception {
        return sequence(Unchecked.<List<Map<String, Object>>>cast(getJson(path).get(name)));
    }
}
