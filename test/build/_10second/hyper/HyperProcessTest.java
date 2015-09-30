package build._10second.hyper;

import build._10second.ContainerClient;
import build._10second.ContainerClientContract;

public class HyperProcessTest extends ContainerClientContract {
    @Override
    protected ContainerClient client() {
        return new HyperProcess();
    }
}