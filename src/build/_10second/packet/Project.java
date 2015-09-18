package build._10second.packet;

import build._10second.JsonRecord;
import com.googlecode.totallylazy.io.Uri;

import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.io.Uri.uri;
import static com.googlecode.utterlyidle.PathParameters.pathParameters;
import static com.googlecode.utterlyidle.UriTemplate.uriTemplate;
import static java.lang.Thread.sleep;

public class Project extends JsonRecord {
    public String id;
    public String name;

    public Uri devicesPath() {
        return uri(uriTemplate("/projects/{id}/devices").generate(pathParameters().add("id", id)));
    }
}

