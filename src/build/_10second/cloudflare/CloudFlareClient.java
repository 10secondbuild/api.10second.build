package build._10second.cloudflare;

import build._10second.AuditFailures;
import build._10second.Environment;
import build._10second.JsonRecord;
import build._10second.ModifyRequest;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.io.Uri;
import com.googlecode.totallylazy.json.Json;
import com.googlecode.utterlyidle.HttpHeaders;
import com.googlecode.utterlyidle.Request;
import com.googlecode.utterlyidle.handlers.ClientHttpHandler;
import com.googlecode.utterlyidle.handlers.HttpClient;

import java.util.List;
import java.util.Map;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.io.Uri.uri;
import static com.googlecode.utterlyidle.HttpHeaders.ACCEPT;
import static com.googlecode.utterlyidle.HttpHeaders.CONTENT_TYPE;
import static com.googlecode.utterlyidle.MediaType.APPLICATION_JSON;
import static com.googlecode.utterlyidle.Request.*;

public class CloudFlareClient {
    private final HttpClient http;
    private static final Uri baseUrl = uri("https://api.cloudflare.com/client/v4/");

    public CloudFlareClient(String apiKey, String email, HttpClient http) {
        this.http = new ModifyRequest(new AuditFailures(http), request -> {
            Uri original = request.uri();
            return request.
                    uri(baseUrl.mergePath(original.path()).query(original.query())).
                    header("X-Auth-Key", apiKey).
                    header("X-Auth-Email", email).
                    header(ACCEPT, APPLICATION_JSON);
        });
    }

    public CloudFlareClient() {
        this(apiKey(), email(), new ClientHttpHandler());
    }

    private static String apiKey() {
        return Environment.getMandatory("CLOUDFLARE_API_KEY");
    }

    public static String email() {
        return Environment.getMandatory("CLOUDFLARE_EMAIL");
    }

    public <T> T getJson(String path) throws Exception {
        return json(get(path));
    }

    public <T> T postJson(String path, Map<String, Object> json) throws Exception {
        return json(post(path).
                entity(Json.json(json)).
                header(CONTENT_TYPE, APPLICATION_JSON));
    }

    private <T> T json(Request request) throws Exception {
        Map<String, Object> json = Json.map(http.handle(request).entity().toString());
        return cast(json.get("result"));
    }

    public User user() throws Exception {
        return JsonRecord.create(User.class, getJson("user"));
    }

    public Sequence<Zone> zones() throws Exception {
        return sequence(this.<List<Map<String, Object>>>getJson("zones")).
                map(data -> JsonRecord.create(Zone.class, data));
    }

    public Zone zone(String name) throws Exception {
        List<Map<String, Object>> zones = json(get("zones").query("name", name));
        return JsonRecord.create(Zone.class, zones.get(0));
    }

    public Sequence<DnsRecord> dnsRecords(Zone zone) throws Exception {
        List<Map<String, Object>> zones = json(get(zone.dnsRecordsPath()));
        return sequence(zones).map(data -> JsonRecord.create(DnsRecord.class, data));
    }

    public DnsRecord createDnsRecord(Zone zone, Map<String, Object> map) throws Exception {
        Map<String, Object> data = json(post(zone.dnsRecordsPath()).
                entity(Json.json(map)).
                contentType(APPLICATION_JSON));
        return JsonRecord.create(DnsRecord.class, data);
    }

    public String delete(Zone zone, DnsRecord dnsRecord) throws Exception {
        Map<String, Object> json = json(Request.delete(zone.dnsRecordsPath().toString() + "/" + dnsRecord.id));
        return cast(json.get("id"));
    }
}
