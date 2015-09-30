package build._10second;

import com.googlecode.totallylazy.Streams;
import com.googlecode.totallylazy.Strings;
import org.junit.Test;

import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.predicates.Predicates.*;

public abstract class ContainerClientContract {
    protected abstract ContainerClient client();

    @Test
    public void supportsPulling() throws Exception {
        ContainerResponse result = client().pull("ubuntu");
        Streams.copy(result.log(), System.out);
        assertThat(result.status(), is(0));
    }

    @Test
    public void pullingWithInvalidImageShouldError() throws Exception {
        ContainerResponse result = client().pull("unknownimage");
        Streams.copy(result.log(), System.out);
        assertThat(result.status(), not(0));
    }

    @Test
    public void supportsCreatingAndRemoving() throws Exception {
        CreateResponse result = client().create(new ContainerConfig() {{ image = "ubuntu"; }});
        assertThat(result.status(), is(0));
        assertThat(result.id(), is(instanceOf(String.class)));
        assertThat(result.id(), is(not(Strings.contains("\n"))));
        CommandResponse response = client().remove(result.id());
        assertThat(response.status(), is(0));
    }
}
