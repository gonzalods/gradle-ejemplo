package org.gms.microservices.consul;

import com.google.common.net.HostAndPort;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.orbitz.consul.Consul;
import com.typesafe.config.Config;
import org.gms.microservices.discovery.ConsulDicovery;
import org.gms.microservices.discovery.ServiceDiscovery;

/**
 * Created by gonzalo on 05/06/2016.
 */
public class ConsulModule extends AbstractModule{
    @Override
    protected void configure() {
        bind(ConsulRegistration.class).in(Scopes.SINGLETON);
        bind(ConsulLifeCicleListener.class).in(Scopes.SINGLETON);
        bind(ServiceDiscovery.class).to(ConsulDicovery.class).in(Scopes.SINGLETON);
    }

    @Provides
    @Singleton
    public Consul consul(Config config){
        String host = config.getString("discovery.consul.host");
        int port = config.getInt("discovery.consul.port");
        HostAndPort hostAndPort = HostAndPort.fromHost(host)
                .withDefaultPort(port);
        return Consul.builder().withHostAndPort(hostAndPort).build();
    }
}
