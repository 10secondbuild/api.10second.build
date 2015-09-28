package build._10second;

import com.googlecode.totallylazy.functions.Lazy;

public class CreateResponse implements CommandResponse{
    private final String id;
    private final Lazy<Integer> status;

    public CreateResponse(Lazy<Integer> status, String id) {
        this.status = status;
        this.id = id;
    }

    public int status() {
        return status.value();
    }

    public String id() {
        return id;
    }
}
