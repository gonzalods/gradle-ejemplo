package org.gms.microservices;

import com.google.common.collect.ImmutableMap;
import com.google.inject.*;
import com.typesafe.config.Config;
import org.gms.microservices.discovery.ClientFactory;
import org.jboss.resteasy.plugins.guice.ext.RequestScopeModule;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.IOException;
import java.util.Map;

/**
 * Created by gonzalo on 03/06/2016.
 */
public class MyMicroservice extends Microservice{

    public static void main(String[] args) {
        new MyMicroservice().run();
    }

    @Override
    public Module[] getModules() {
        return new Module[]{
            new RequestScopeModule(){
                @Override
                protected void configure() {
                    bind(DataResource.class).in(Scopes.SINGLETON);
                }

                @Provides
                @Singleton
                public OtherMicroserviceClient otherMicroserviceClient(ClientFactory factory){
                    return factory.buildClient("other-microservice", OtherMicroserviceClient.class);
                }
            }
        };
    }

    @Path("/")
    public static class DataResource{

        @Inject
        OtherMicroserviceClient otherMicroserviceClient;

        @GET
        @Produces("application/json")
        public Map<String,Map<String,String>> getOtherData() throws IOException{
            return ImmutableMap.of("retrivedFromOtherMicroservice",
                    otherMicroserviceClient.getData().execute().body());
        }
    }
}
