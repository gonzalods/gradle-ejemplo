package org.gms.microservices.consul;

import com.google.inject.Inject;
import org.eclipse.jetty.util.component.AbstractLifeCycle;
import org.eclipse.jetty.util.component.LifeCycle;

/**
 * Created by gonzalo on 05/06/2016.
 */
public class ConsulLifeCicleListener extends AbstractLifeCycle.AbstractLifeCycleListener{

     private ConsulRegistration consulRegistration;

    @Inject
    public ConsulLifeCicleListener(ConsulRegistration consulRegistration) {
        this.consulRegistration = consulRegistration;
    }

    @Override
    public void lifeCycleStarted(LifeCycle event) {
       consulRegistration.register();
    }

    @Override
    public void lifeCycleStopping(LifeCycle event) {
        System.out.println("Stopping");
        consulRegistration.deregister();
    }
}
