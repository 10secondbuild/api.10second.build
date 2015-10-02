package build._10second.containers;

import com.googlecode.totallylazy.Strings;
import org.junit.Test;

import java.io.InputStream;

import static com.googlecode.totallylazy.Assert.assertThat;
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
        // Create
        Result<String> created = client().create(new ContainerConfig() {{ image = "ubuntu"; }});
        assertThat(created.value(), created.success(), is(true));
        String id = created.value();
        assertThat(id, is(instanceOf(String.class)));
        assertThat(id, is(not(Strings.contains("\n"))));

        try {
            // Start
            Result<?> started = client().start(id);
            assertThat(started.success(), is(true));

            // Attach
            Result<InputStream> attached = client().attach(id);
            assertThat(attached.success(), is(true));

            // Stop
            Result<?> stopped = client().stop(id);
            assertThat(stopped.success(), is(true));

        } finally {

            // Remove
            Result<?> removed = client().remove(id);
            assertThat(removed.success(), is(true));
        }
    }
}
