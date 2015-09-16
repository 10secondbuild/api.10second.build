package build._10second;

import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.io.Uri;
import com.googlecode.totallylazy.json.Json;
import com.googlecode.utterlyidle.*;
import com.googlecode.utterlyidle.handlers.ClientHttpHandler;
import com.googlecode.utterlyidle.handlers.HttpClient;

import java.util.List;
import java.util.Map;

import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.io.Uri.uri;
import static com.googlecode.totallylazy.predicates.Predicates.is;
import static com.googlecode.utterlyidle.MediaType.APPLICATION_JSON;
import static com.googlecode.utterlyidle.RequestBuilder.get;
import static com.googlecode.utterlyidle.Status.OK;

public class Main {
    public static void main(String[] args) throws Exception {
        Packet packet = new Packet();
        Map<String, Object> project = packet.projects().find(m -> m.get("name").equals("10second.build")).get();
        System.out.println(project.get("id"));
    }
}

