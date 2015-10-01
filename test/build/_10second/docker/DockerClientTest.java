package build._10second.docker;

import build._10second.ContainerClient;
import build._10second.ContainerClientContract;

public class DockerClientTest extends ContainerClientContract {
    @Override
    protected ContainerClient client() {
        return new DockerClient();
    }

}