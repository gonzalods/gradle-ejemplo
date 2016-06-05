package org.gms.microservices.discovery;

import com.google.common.net.HostAndPort;

/**
 * Created by gonzalo on 05/06/2016.
 */
public interface ServiceDiscovery {

    HostAndPort discover(String service);
}
