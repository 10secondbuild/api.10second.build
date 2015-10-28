package build._10second.containers;

import com.googlecode.totallylazy.Strings;
import org.junit.Test;

import java.io.InputStream;

import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.Lists.list;
import static com.googlecode.totallylazy.predicates.Predicates.*;

public abstract class ContainerClientContract {
    protected abstract ContainerClient client();

    @Test
    public void supportsPulling() throws Exception {
        Result<String> result = client().pull(new ContainerConfig() {{
            image = "ubuntu";
        }});
        assertThat(result.success(), is(true));
    }

    @Test
    public void pullingWithInvalidImageShouldError() throws Exception {
        Result<String> result = client().pull(new ContainerConfig() {{
            image = "unknownimage";
        }});
        assertThat(result.success(), is(false));
    }

    @Test
    public void supportsLifecycle() throws Exception {
        ContainerConfig config = new ContainerConfig() {{
            image = "danielbodart/crazyfast.build";
        }};

        System.out.println("Pull");
        Result<String> pull = client().pull(config);
        assertThat(pull.success(), is(true));

        System.out.println("Create");
        Result<String> created = client().create(config);
        assertThat(created.value(), created.success(), is(true));
        String id = created.value();
        assertThat(id, is(instanceOf(String.class)));
        assertThat(id, is(not(Strings.contains("\n"))));

        try {
            System.out.println("Start");
            Result<?> started = client().start(id);
            assertThat(started.success(), is(true));

            System.out.println("Attach");
            Result<InputStream> attached = client().attach(id);
            assertThat(attached.success(), is(true));
            System.out.println(Strings.string(attached.value()));

            System.out.println("Stop");
            Result<?> stopped = client().stop(id);
            assertThat(stopped.success(), is(true));
        } finally {
            System.out.println("Remove");
            Result<?> removed = client().remove(id);
            assertThat(removed.success(), is(true));
        }
    }
}
