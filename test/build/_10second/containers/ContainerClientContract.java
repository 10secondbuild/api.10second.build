package build._10second.containers;

import com.googlecode.totallylazy.Strings;
import org.junit.Test;

import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.predicates.Predicates.*;

public abstract class ContainerClientContract {
    protected abstract ContainerClient client();

    @Test
    public void supportsPulling() throws Exception {
        Result<String> result = client().pull("ubuntu");
        assertThat(result.success(), is(true));
    }

    @Test
    public void pullingWithInvalidImageShouldError() throws Exception {
        Result<String> result = client().pull("unknownimage");
        assertThat(result.success(), is(false));
    }

    @Test
    public void supportsCreatingAndRemoving() throws Exception {
        Result<String> result = client().create(new ContainerConfig() {{ image = "ubuntu"; }});
        assertThat(result.success(), is(true));
        assertThat(result.value(), is(instanceOf(String.class)));
        assertThat(result.value(), is(not(Strings.contains("\n"))));
        Result<?> response = client().remove(result.value());
        assertThat(response.success(), is(true));
    }
}
