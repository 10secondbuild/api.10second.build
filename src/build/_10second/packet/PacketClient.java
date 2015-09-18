package build._10second.packet;

import build._10second.AuditFailures;
import build._10second.JsonRecord;
import build._10second.ModifyRequest;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Unchecked;
import com.googlecode.totallylazy.io.Uri;
import com.googlecode.totallylazy.json.Json;
import com.googlecode.totallylazy.predicates.Predicate;
import com.googlecode.utterlyidle.Request;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.Status;
import com.googlecode.utterlyidle.handlers.AuditHandler;
import com.googlecode.utterlyidle.handlers.ClientHttpHandler;
import com.googlecode.utterlyidle.handlers.HttpClient;
import com.googlecode.utterlyidle.handlers.PrintAuditor;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.Assert.assertTrue;
import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.blank;
import static com.googlecode.totallylazy.io.Uri.uri;
import static com.googlecode.totallylazy.predicates.Predicates.is;
import static com.googlecode.totallylazy.predicates.Predicates.not;
import static com.googlecode.utterlyidle.HttpHeaders.CONTENT_TYPE;
import static com.googlecode.utterlyidle.MediaType.APPLICATION_JSON;
import static com.googlecode.utterlyidle.RequestBuilder.*;
import static java.lang.Thread.sleep;

public class PacketClient {
    private final HttpClient http;
    private static final Uri baseUrl = uri("https://api.packet.net/");

    public PacketClient(String apiKey, HttpClient http) {
        this.http = new ModifyRequest(new AuditFailures(http), request ->
                modify(request).
                uri(baseUrl.mergePath(request.uri().path())).
                header("X-Auth-Token", apiKey).
                accepting(APPLICATION_JSON).
                build());
    }

    public PacketClient() {
        this(hasValue(System.getenv("PACKET_API_KEY")), new ClientHttpHandler());
    }

    public Sequence<Project> projects() throws Exception {
        return list("projects").map(data -> JsonRecord.create(Project.class, data));
    }

    public Project project(String name) throws Exception {
        return projects().find(p -> p.name.equals(name)).get();
    }

    public Sequence<Facility> facilities() throws Exception {
        return list("facilities").map(data -> JsonRecord.create(Facility.class, data));
    }

    public Sequence<Device> devices(Project project) throws Exception {
        return list("devices", project.devicesPath()).map(data -> JsonRecord.create(Device.class, data));
    }

    public Device provisionDevice(Project project, Map<String, Object> device) throws Exception {
        Uri path = project.devicesPath();
        return JsonRecord.create(Device.class, postJson(path, device));
    }

    public Device pollUntil(Device device, Predicate<Device> predicate) throws Exception {
        return repeat(() -> {
            sleep(Duration.ofSeconds(10).toMillis());
            return reload(device);
        }).find(predicate).get();
    }

    public Device reload(Device device) throws Exception {
        return JsonRecord.create(Device.class, getJson(device.href));
    }

    private static String hasValue(String value) {
        assertThat(value, not(blank));
        return value;
    }

    private Map<String, Object> getJson(String path) throws Exception {
        return getJson(uri(path));
    }

    private Map<String, Object> getJson(Uri path) throws Exception {
        return json(get(path).build());
    }

    private Map<String, Object> postJson(String path, Map<String, Object> json) throws Exception {
        return postJson(uri(path), json);
    }

    private Map<String, Object> postJson(Uri path, Map<String, Object> json) throws Exception {
        return json(post(path).
                entity(Json.json(json)).
                header(CONTENT_TYPE, APPLICATION_JSON).
                build());
    }

    private Map<String, Object> json(Request request) throws Exception {
        return Json.map(http.handle(request).entity().toString());
    }

    private Sequence<Map<String, Object>> list(String name) throws Exception {
        return list(name, uri(name));
    }

    private Sequence<Map<String, Object>> list(String name, Uri path) throws Exception {
        return sequence(Unchecked.<List<Map<String, Object>>>cast(getJson(path).get(name)));
    }

    public void deprovisionDevice(Project project, Device device) throws Exception {
        Response response = http.handle(delete(project.devicesPath() + "/" + device.id).build());
        assertTrue(response.status().isSuccessful());
    }
}
