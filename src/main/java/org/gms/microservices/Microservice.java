package org.gms.microservices;

import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.google.inject.servlet.GuiceFilter;
import com.typesafe.config.Config;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.gms.microservices.consul.ConsulLifeCicleListener;
import org.jboss.resteasy.plugins.guice.GuiceResteasyBootstrapServletContextListener;

import javax.servlet.DispatcherType;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by gonzalo on 03/06/2016.
 */
public abstract class Microservice {
    public void run(){
        List<Module> serviceModules = Lists.asList(new ChassisModule(), getModules());
        Injector injector = Guice.createInjector(Stage.PRODUCTION,serviceModules);

        int port = injector.getInstance(Config.class).getInt("microservice.port");
        Server server = new Server(port);
        // A ServletContextHandler is a specialization of ContextHandler with support
        // for standard sessions and Servlets.
        ServletContextHandler context = new ServletContextHandler(server,"/",
                ServletContextHandler.SESSIONS);

        context.addEventListener(injector.getInstance(
                GuiceResteasyBootstrapServletContextListener.class));
        // GuiceFilter supongo que es el filtro que permite configurar
        // Guice en un entorno web.
        context.addFilter(GuiceFilter.class,"/*",
                EnumSet.of(DispatcherType.REQUEST,
                        DispatcherType.ASYNC));
        context.addServlet(DefaultServlet.class,"/*");

        try {
            server.addLifeCycleListener(injector.getInstance(ConsulLifeCicleListener.class));
            server.setStopAtShutdown(true);
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                try {
                    server.stop();
                    System.out.println("Server Stopping");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            server.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public abstract Module[] getModules();
}
