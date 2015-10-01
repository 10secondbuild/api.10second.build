package build._10second.containers;


public interface ContainerClient {
    Result<String> pull(String name) throws Exception;
    Result<String> create(ContainerConfig config) throws Exception;
    Result<?> remove(String id) throws Exception;
}
