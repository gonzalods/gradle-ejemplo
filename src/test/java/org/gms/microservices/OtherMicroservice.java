package org.gms.microservices;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Module;
import com.google.inject.Scopes;
import com.orbitz.apache.commons.lang3.RandomStringUtils;
import org.jboss.resteasy.plugins.guice.ext.RequestScopeModule;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

/**
 * Created by gonzalo on 05/06/2016.
 */
public class OtherMicroservice extends Microservice {

    public static void main(String[] args) {
        new OtherMicroservice().run();
    }
    @Override
    public Module[] getModules() {
        return new Module[]{
                new RequestScopeModule(){
                    @Override
                    protected void configure() {
                        bind(DataResource.class).in(Scopes.SINGLETON);
                    }
                }
        };
    }

    @Path("/data")
    public static class DataResource{

        @GET
        @Produces("application/json")
        public Map<String,String> getData(){
            return ImmutableMap.of("data", RandomStringUtils.random(20));
        }
    }
}
