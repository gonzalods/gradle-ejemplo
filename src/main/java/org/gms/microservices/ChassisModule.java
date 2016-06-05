package org.gms.microservices;

import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import org.gms.microservices.config.ConfigModule;
import org.gms.microservices.consul.ConsulModule;
import org.gms.microservices.health.HealthModule;
import org.jboss.resteasy.plugins.guice.GuiceResteasyBootstrapServletContextListener;
import org.jboss.resteasy.plugins.guice.ext.RequestScopeModule;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJackson2Provider;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;

/**
 * Created by gonzalo on 03/06/2016.
 */
// A module is a collection of bindings specified using fluent, English-like method calls.
// The modules are the building blocks of an injector, which is Guice's object-graph builder.
// Add the RequestScopeModule to your modules to allow objects to be scoped to the HTTP request by
// adding the @RequestScoped annotation to your class. All the objects injectable via the @Context
// annotation are also injectable, except ServletConfig and ServletContext.
public class ChassisModule extends RequestScopeModule {
    @Override
    protected void configure() {
        // This statement does essentially nothing; it "binds the
        // (GuiceResteasyBootstrapServletContextListener}
        // class to itself" and does not change Guice's default behavior.
        // GuiceResteasyBootstrapServletContextListener is a subclass of ResteasyBootstrap,
        // ResteasyBootstrap class is a ServletContextListener that configures an instance
        // of an ResteasyProviderFactory and Registry.
        bind(GuiceResteasyBootstrapServletContextListener.class);

        // Uses the given module to configure more bindings.
        install(new ConfigModule());
        install(new HealthModule());
        install(new ConsulModule());

        install(new ServletModule(){
            //El ServletModule se puede ver como el sustituto programático de web.xml
            //Se configuran aqui los Servlets y Filtros
            @Override
            protected  void configureServlets(){
                // HttpServletDispatcher Resteasy servlet is responsible for initializing
                // some basic components of RESTeasy.
                bind(HttpServletDispatcher.class).in(Scopes.SINGLETON);
                // This registers a servlet HttpServletDispatcher
                // to serve any web requests.
                serve("/*").with(HttpServletDispatcher.class);
            }
        });
    }

    // When you need code to create an object, use an @Provides method. The method must be defined
    // within a module, and it must have an @Provides annotation. The method's return type is the
    // bound type. Whenever the injector needs an instance of that type, it will invoke the method.
    @Provides
    @Singleton
    ResteasyJackson2Provider getJacksonJsonProvider(){
        return new ResteasyJackson2Provider();
    }
}
