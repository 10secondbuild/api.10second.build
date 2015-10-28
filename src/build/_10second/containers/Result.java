package build._10second.containers;

import com.googlecode.totallylazy.Callers;
import com.googlecode.totallylazy.Functor;
import com.googlecode.totallylazy.Value;
import com.googlecode.totallylazy.functions.Function1;
import com.googlecode.totallylazy.functions.Lazy;

import java.util.concurrent.Callable;

public interface Result<T> extends Value<T>, Functor<T> {
    boolean success();

    @Override
    default <S> Result<S> map(Function1<? super T, ? extends S> callable){
        return result(this::success, Callers.call(callable, value()));
    }

    static <T> Result<T> result(Callable<Boolean> success, T instance){
        Lazy<Boolean> result = Lazy.lazy(success);
        return new Result<T>() {
            @Override
            public boolean success() {
                return result.value();
            }

            @Override
            public T value() {
                return instance;
            }
        };
    }
}
