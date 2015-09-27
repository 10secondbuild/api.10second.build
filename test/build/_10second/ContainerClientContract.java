package build._10second;

import org.junit.Test;

import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.predicates.Predicates.instanceOf;
import static com.googlecode.totallylazy.predicates.Predicates.is;

public abstract class ContainerClientContract {
    protected abstract ContainerClient client();

    @Test
    public void supportsPull() throws Exception {
        ContainerResponse result = client().pull("ubuntu");
        assertThat(result.status, is(0));
        assertThat(result.log, instanceOf(String.class));
    }
}
