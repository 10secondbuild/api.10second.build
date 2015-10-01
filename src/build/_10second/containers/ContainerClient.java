package build._10second.containers;

public interface ContainerClient {
    ContainerResponse pull(String name) throws Exception;
    CreateResponse create(ContainerConfig config) throws Exception;
    CommandResponse remove(String id) throws Exception;
}
