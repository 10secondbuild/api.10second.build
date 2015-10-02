package build._10second;

import com.googlecode.totallylazy.Option;

import static java.lang.String.format;

public interface Environment {
    static Option<String> get(String name){
        return Option.option(System.getenv(name));
    }

    static String getMandatory(String name){
        return get(name).getOrThrow(new IllegalStateException(format("Environment variable %s not set", name)));
    }

    static String getOrDefault(String name, String defaultValue){
        return get(name).getOrElse(defaultValue);
    }
}

