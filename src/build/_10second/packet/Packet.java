package build._10second.packet;

import build._10second.JsonRecord;
import build._10second.ModifyRequest;
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
import static com.googlecode.utterlyidle.RequestBuilder.get;
import static com.googlecode.utterlyidle.RequestBuilder.modify;
import static com.googlecode.utterlyidle.RequestBuilder.post;

public class Packet {
    private final HttpClient http;
    private static final Uri baseUrl = uri("https://api.packet.net/");

    public Packet(String apiKey, HttpClient http) {
        this.http = new ModifyRequest(new AuditHandler(http, new PrintAuditor(System.out)), request ->
                modify(request).
                uri(baseUrl.mergePath(request.uri().path())).
                header("X-Auth-Token", apiKey).
                accepting(APPLICATION_JSON).
                build());
    }

    public Packet() {
        this(hasValue(System.getenv("PACKET_API_KEY")), new ClientHttpHandler());
    }

    private static String hasValue(String value) {
        assertThat(value, not(blank));
        return value;
    }

    public Map<String, Object> getJson(String path) throws Exception {
        return json(get(path).build());
    }

    public Map<String, Object> postJson(String path, Map<String, Object> json) throws Exception {
        return json(post(path).
                entity(Json.json(json)).
                header(CONTENT_TYPE, APPLICATION_JSON).
                build());
    }

    private Map<String, Object> json(Request request) throws Exception {
        return Json.map(http.handle(request).entity().toString());
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
