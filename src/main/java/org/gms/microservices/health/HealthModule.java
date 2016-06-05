package org.gms.microservices.health;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

/**
 * Created by gonzalo on 05/06/2016.
 */
public class HealthModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(HealthResource.class).in(Scopes.SINGLETON);
    }
}
