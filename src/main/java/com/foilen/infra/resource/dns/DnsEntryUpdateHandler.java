/*
    Foilen Infra Resource DNS
    https://github.com/foilen/foilen-infra-resource-dns
    Copyright (c) 2018 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.infra.resource.dns;

import java.util.List;

import com.foilen.infra.plugin.v1.core.common.DomainHelper;
import com.foilen.infra.plugin.v1.core.context.ChangesContext;
import com.foilen.infra.plugin.v1.core.context.CommonServicesContext;
import com.foilen.infra.plugin.v1.core.eventhandler.AbstractUpdateEventHandler;
import com.foilen.infra.plugin.v1.model.resource.IPResource;
import com.foilen.infra.plugin.v1.model.resource.LinkTypeConstants;
import com.foilen.infra.resource.domain.Domain;
import com.foilen.smalltools.tools.StringTools;
import com.foilen.smalltools.tuple.Tuple3;

public class DnsEntryUpdateHandler extends AbstractUpdateEventHandler<DnsEntry> {

    @Override
    public void addHandler(CommonServicesContext services, ChangesContext changes, DnsEntry dnsEntry) {
        IPResource domain = retrieveOrCreateResource(services.getResourceService(), changes, new Domain(dnsEntry.getName(), DomainHelper.reverseDomainName(dnsEntry.getName())));
        changes.linkAdd(dnsEntry, LinkTypeConstants.MANAGES, domain);
    }

    @Override
    public void checkAndFix(CommonServicesContext services, ChangesContext changes, DnsEntry resource) {
    }

    @Override
    public void deleteHandler(CommonServicesContext services, ChangesContext changes, DnsEntry resource, List<Tuple3<IPResource, String, IPResource>> previousLinks) {
        detachManagedResources(services, changes, resource, previousLinks);
    }

    @Override
    public Class<DnsEntry> supportedClass() {
        return DnsEntry.class;
    }

    @Override
    public void updateHandler(CommonServicesContext services, ChangesContext changes, DnsEntry previousResource, DnsEntry newResource) {
        // If domain is different, detach and use another
        if (!StringTools.safeEquals(previousResource.getName(), newResource.getName())) {
            // Remove old
            List<Domain> domains = services.getResourceService().linkFindAllByFromResourceAndLinkTypeAndToResourceClass(newResource, LinkTypeConstants.MANAGES, Domain.class);
            removeManagedLinkAndDeleteIfNotManagedByAnyoneElse(services.getResourceService(), changes, domains, newResource);

            // Add new
            IPResource domain = retrieveOrCreateResource(services.getResourceService(), changes, new Domain(newResource.getName(), DomainHelper.reverseDomainName(newResource.getName())));
            changes.linkAdd(newResource, LinkTypeConstants.MANAGES, domain);
        }

    }

}
