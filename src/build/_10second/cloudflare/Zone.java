package build._10second.cloudflare;

import build._10second.JsonRecord;
import com.googlecode.totallylazy.io.Uri;

import static com.googlecode.totallylazy.io.Uri.uri;
import static com.googlecode.utterlyidle.PathParameters.pathParameters;
import static com.googlecode.utterlyidle.UriTemplate.uriTemplate;

public class Zone extends JsonRecord {
    public String id;
    public String name;
    public String status;

    public Uri dnsRecordsPath() {
        return uri(uriTemplate("zones/{id}/dns_records").generate(pathParameters().add("id", id)));
    }
}
