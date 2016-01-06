package build._10second.containers.hyper;

import build._10second.containers.ContainerClient;
import build._10second.containers.ContainerClientContract;
import build._10second.containers.docker.DockerClient;

public class HyperClientTest extends ContainerClientContract {
    @Override
    protected ContainerClient client() {
        return new HyperClient();
    }

}