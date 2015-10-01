package build._10second.containers.docker;

import build._10second.containers.ContainerClient;
import build._10second.containers.ContainerClientContract;

public class DockerClientTest extends ContainerClientContract {
    @Override
    protected ContainerClient client() {
        return new DockerClient();
    }

}