package build._10second;

import com.googlecode.utterlyidle.Request;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.handlers.HttpClient;

import java.util.Date;

import static java.lang.String.format;

public class AuditFailures implements HttpClient {
    private final HttpClient httpClient;

    public AuditFailures(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public Response handle(Request request) throws Exception {
        Date start = new Date();
        final Response response = httpClient.handle(request);
        Date end = new Date();
        if(response.status().code() >= 400){
            System.out.println(request);
            System.out.println(response);
        } else {
            System.out.println(format("%s %s -> %s in %s msecs", request.method(), request.uri(), response.status(), end.getTime() - start.getTime()));
        }
        return response;
    }
}