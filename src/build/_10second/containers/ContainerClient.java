package build._10second.containers;


import java.io.InputStream;

public interface ContainerClient {
    Result<String> pull(ContainerConfig name) throws Exception;

    Result<String> create(ContainerConfig config) throws Exception;

    Result<?> start(String id) throws Exception;

    Result<?> stop(String id) throws Exception;

    Result<?> remove(String id) throws Exception;

    Result<InputStream> attach(String id) throws Exception;
}
