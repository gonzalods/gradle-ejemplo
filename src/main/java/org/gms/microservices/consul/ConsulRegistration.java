package org.gms.microservices.consul;

import com.google.inject.Inject;
import com.orbitz.consul.AgentClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.model.agent.ImmutableRegCheck;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import com.orbitz.consul.model.agent.Registration;
import com.typesafe.config.Config;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * Created by gonzalo on 05/06/2016.
 */
public class ConsulRegistration {

    private AgentClient agentClient;

   //private Config config;

    private String host, name, id;
    private int port;

    @Inject
    public ConsulRegistration(Config config, Consul consul) {
        agentClient = consul.agentClient();
        host = config.getString("discovery.advertised.host");
        port = config.getInt("discovery.advertised.port");
        name = config.getString("microservice.name");
        id = String.format("%s-%s", name, UUID.randomUUID().toString());
    }

    public void register(){
        try{
            URL healthUrl = new URL("http", host, port,"/health");

            Registration.RegCheck check = ImmutableRegCheck.builder()
                    .http(healthUrl.toExternalForm())
                    .interval("5s")
                    .build();

            Registration registration = ImmutableRegistration.builder()
                    .address(host)
                    .port(port)
                    .id(id)
                    .name(name)
                    .check(check)
                    .build();

            agentClient.register(registration);
        }catch (MalformedURLException e){
            throw new RuntimeException(e);
        }
    }

    public void deregister(){
        agentClient.deregister(id);
    }
}
