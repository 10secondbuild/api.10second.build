package build._10second;

import com.googlecode.totallylazy.functions.Unary;
import com.googlecode.utterlyidle.HttpHandler;
import com.googlecode.utterlyidle.Request;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.handlers.HttpClient;

public class ModifyRequest implements HttpClient {
    private final Unary<Request> modifier;
    private final HttpHandler handler;

    public ModifyRequest(HttpHandler handler, Unary<Request> modifier) {
        this.modifier = modifier;
        this.handler = handler;
    }

    @Override
    public Response handle(Request request) throws Exception {
        return handler.handle(modifier.call(request));
    }
}
