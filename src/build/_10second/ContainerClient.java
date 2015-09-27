package build._10second;

public interface ContainerClient {
    ContainerResponse pull(String name) throws Exception;
}
