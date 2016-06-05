package org.gms.microservices.discovery;

import com.google.inject.Inject;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;


/**
 * Created by gonzalo on 05/06/2016.
 */
public class ClientFactory {

    @Inject
    private ServiceDiscovery serviceDiscovery;

    @Inject
    public ClientFactory(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    public <T> T buildClient(String service, Class<T> clazz){
        DiscoverableEndpoint endpoint = new DiscoverableEndpoint(service, serviceDiscovery);
        return new Retrofit.Builder()
                .baseUrl(endpoint.getHttpUrl().toString())
                .addConverterFactory(JacksonConverterFactory.create())
                .build().create(clazz);
    }
}
