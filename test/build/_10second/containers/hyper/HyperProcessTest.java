package build._10second.containers.hyper;

import build._10second.containers.ContainerClient;
import build._10second.containers.ContainerClientContract;

public class HyperProcessTest extends ContainerClientContract {
    @Override
    protected ContainerClient client() {
        return new HyperProcess();
    }
}