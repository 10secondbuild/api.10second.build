package build._10second.containers;

import static com.googlecode.totallylazy.functions.Lazy.lazy;

public interface CommandResponse {
    int status();

    static CommandResponse commandResponse(Process process){
        return lazy(process::waitFor)::value;
    }
}
