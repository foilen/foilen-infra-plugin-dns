/*
    Foilen Infra Resource DNS
    https://github.com/foilen/foilen-infra-resource-dns
    Copyright (c) 2018 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.infra.resource.dns;

import java.util.List;

import com.foilen.infra.plugin.v1.core.context.ChangesContext;
import com.foilen.infra.plugin.v1.core.context.CommonServicesContext;
import com.foilen.infra.plugin.v1.core.eventhandler.AbstractUpdateEventHandler;
import com.foilen.infra.plugin.v1.core.exception.IllegalUpdateException;
import com.foilen.infra.plugin.v1.model.resource.IPResource;
import com.foilen.infra.plugin.v1.model.resource.LinkTypeConstants;
import com.foilen.infra.resource.domain.Domain;
import com.foilen.smalltools.tuple.Tuple3;

public class DomainUpdateHandler extends AbstractUpdateEventHandler<Domain> {

    @Override
    public void addHandler(CommonServicesContext services, ChangesContext changes, Domain domain) {
    }

    @Override
    public void checkAndFix(CommonServicesContext services, ChangesContext changes, Domain resource) {
    }

    @Override
    public void deleteHandler(CommonServicesContext services, ChangesContext changes, Domain resource, List<Tuple3<IPResource, String, IPResource>> previousLinks) {
        if (previousLinks.stream().filter(link -> //
        link.getA() instanceof DnsEntry && //
                LinkTypeConstants.MANAGES.equals(link.getB()) && //
                link.getC().equals(resource) && //
                ((DnsEntry) link.getA()).getName().equals(resource.getName())) //
                .findAny().isPresent()) {
            throw new IllegalUpdateException("You cannot delete a domain that is used by a DnsEntry");
        }
        detachManagedResources(services, changes, resource, previousLinks);
    }

    @Override
    public Class<Domain> supportedClass() {
        return Domain.class;
    }

    @Override
    public void updateHandler(CommonServicesContext services, ChangesContext changes, Domain previousResource, Domain newResource) {
    }

}
