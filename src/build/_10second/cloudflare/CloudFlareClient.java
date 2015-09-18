package build._10second.cloudflare;

import build._10second.JsonRecord;
import build._10second.ModifyRequest;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.io.Uri;
import com.googlecode.totallylazy.json.Json;
import com.googlecode.utterlyidle.Request;
import com.googlecode.utterlyidle.handlers.AuditHandler;
import com.googlecode.utterlyidle.handlers.ClientHttpHandler;
import com.googlecode.utterlyidle.handlers.HttpClient;
import com.googlecode.utterlyidle.handlers.PrintAuditor;

import java.util.List;
import java.util.Map;

import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.blank;
import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.io.Uri.uri;
import static com.googlecode.totallylazy.predicates.Predicates.not;
import static com.googlecode.utterlyidle.HttpHeaders.CONTENT_TYPE;
import static com.googlecode.utterlyidle.MediaType.APPLICATION_JSON;
import static com.googlecode.utterlyidle.PathParameters.pathParameters;
import static com.googlecode.utterlyidle.RequestBuilder.*;
import static com.googlecode.utterlyidle.UriTemplate.uriTemplate;

public class CloudFlareClient {
    private final HttpClient http;
    private static final Uri baseUrl = uri("https://api.cloudflare.com/client/v4/");

    public CloudFlareClient(String apiKey, String email, HttpClient http) {
        this.http = new ModifyRequest(new AuditHandler(http, new PrintAuditor(System.out)), request ->
                modify(request).
                uri(baseUrl.mergePath(request.uri().path())).
                header("X-Auth-Key", apiKey).
                header("X-Auth-Email", email).
                accepting(APPLICATION_JSON).
                build());
    }

    public CloudFlareClient() {
        this(apiKey(), email(), new ClientHttpHandler());
    }

    private static String apiKey() {
        return hasValue(System.getenv("CLOUDFLARE_API_KEY"));
    }

    public static String email() {
        return hasValue(System.getenv("CLOUDFLARE_EMAIL"));
    }

    private static String hasValue(String value) {
        assertThat(value, not(blank));
        return value;
    }

    public <T> T getJson(String path) throws Exception {
        return json(get(path).build());
    }

    public <T> T postJson(String path, Map<String, Object> json) throws Exception {
        return json(post(path).
                entity(Json.json(json)).
                header(CONTENT_TYPE, APPLICATION_JSON).
                build());
    }

    private <T> T json(Request request) throws Exception {
        return cast(Json.map(http.handle(request).entity().toString()).get("result"));
    }

    public User user() throws Exception {
        return JsonRecord.create(User.class, getJson("user"));
    }

    public Sequence<Zone> zones() throws Exception {
        return sequence(this.<List<Map<String, Object>>>getJson("zones")).
                map(data -> JsonRecord.create(Zone.class, data));
    }

    public Zone zone(String name) throws Exception {
        List<Map<String, Object>> zones = json(get("zones").query("name", name).build());
        return JsonRecord.create(Zone.class, zones.get(0));
    }

    public Sequence<DnsRecord> dnsRecords(Zone zone) throws Exception {
        List<Map<String, Object>> zones = json(get(dnsRecordsPath(zone)).build());
        return sequence(zones).map(data -> JsonRecord.create(DnsRecord.class, data));
    }

    private String dnsRecordsPath(Zone zone) {
        return uriTemplate("zones/{id}/dns_records").generate(pathParameters().add("id", zone.id));
    }

}
