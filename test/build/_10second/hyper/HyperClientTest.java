package build._10second.hyper;

import build._10second.ContainerClient;
import build._10second.ContainerClientContract;

import static org.junit.Assert.*;

public class HyperClientTest extends ContainerClientContract {
    @Override
    protected ContainerClient client() {
        return new HyperClient();
    }
}