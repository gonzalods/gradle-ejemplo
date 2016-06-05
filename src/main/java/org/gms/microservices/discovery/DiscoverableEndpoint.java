package org.gms.microservices.discovery;

import com.google.common.net.HostAndPort;
import okhttp3.HttpUrl;

/**
 * Created by gonzalo on 05/06/2016.
 */
public class DiscoverableEndpoint {

    private String serviceName;
    private ServiceDiscovery serviceDiscovery;

    public DiscoverableEndpoint(String serviceName, ServiceDiscovery serviceDiscovery) {
        this.serviceName = serviceName;
        this.serviceDiscovery = serviceDiscovery;
    }

    public HttpUrl getHttpUrl(){
        HostAndPort hostAndPort = serviceDiscovery.discover(serviceName);

        return new HttpUrl.Builder()
                .scheme("http")
                .host(hostAndPort.getHostText())
                .port(hostAndPort.getPort())
                .build();

    }
}
