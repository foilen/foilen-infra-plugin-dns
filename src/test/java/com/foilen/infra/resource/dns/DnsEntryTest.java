/*
    Foilen Infra Resource DNS
    https://github.com/foilen/foilen-infra-resource-dns
    Copyright (c) 2018 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.infra.resource.dns;

import org.junit.Assert;
import org.junit.Test;

import com.foilen.infra.plugin.core.system.fake.junits.AbstractIPPluginTest;
import com.foilen.infra.plugin.core.system.junits.JunitsHelper;
import com.foilen.infra.plugin.v1.core.common.DomainHelper;
import com.foilen.infra.plugin.v1.core.context.ChangesContext;
import com.foilen.infra.plugin.v1.core.exception.IllegalUpdateException;
import com.foilen.infra.plugin.v1.core.service.IPResourceService;
import com.foilen.infra.plugin.v1.core.service.internal.InternalChangeService;
import com.foilen.infra.resource.dns.model.DnsEntryType;
import com.foilen.infra.resource.domain.Domain;

public class DnsEntryTest extends AbstractIPPluginTest {

    private Domain createDomain(String name) {
        return new Domain(name, DomainHelper.reverseDomainName(name));
    }

    @Test
    public void test_basic() {

        IPResourceService resourceService = getCommonServicesContext().getResourceService();
        InternalChangeService internalChangeService = getInternalServicesContext().getInternalChangeService();

        // Create some
        ChangesContext changes = new ChangesContext(resourceService);
        for (int i = 0; i < 5; ++i) {
            changes.resourceAdd(new DnsEntry("d" + i + ".example.com", DnsEntryType.TXT, "hello"));
        }
        internalChangeService.changesExecute(changes);
        JunitsHelper.assertState(getCommonServicesContext(), getInternalServicesContext(), "DnsEntryTest_test_basic-state-1.json", getClass(), true);

        // Update some
        DnsEntry dnsEntry = resourceService.resourceFindByPk(new DnsEntry("d1.example.com", DnsEntryType.TXT, "hello")).get();
        changes.resourceUpdate(dnsEntry, new DnsEntry("d1.example.com", DnsEntryType.TXT, "hello 2"));
        dnsEntry = resourceService.resourceFindByPk(new DnsEntry("d2.example.com", DnsEntryType.TXT, "hello")).get();
        changes.resourceUpdate(dnsEntry, new DnsEntry("u2.example.com", DnsEntryType.TXT, "hello"));
        internalChangeService.changesExecute(changes);
        JunitsHelper.assertState(getCommonServicesContext(), getInternalServicesContext(), "DnsEntryTest_test_basic-state-2.json", getClass(), true);

        // Delete domain (fail)
        changes.resourceDelete(createDomain("d1.example.com"));
        try {
            internalChangeService.changesExecute(changes);
            Assert.fail("Must fail");
        } catch (IllegalUpdateException e) {
            Assert.assertEquals("You cannot delete a domain that is used by a DnsEntry", e.getMessage());
        }

        // Remove some
        changes.resourceDelete(resourceService.resourceFindByPk(new DnsEntry("d4.example.com", DnsEntryType.TXT, "hello")).get());
        internalChangeService.changesExecute(changes);
        JunitsHelper.assertState(getCommonServicesContext(), getInternalServicesContext(), "DnsEntryTest_test_basic-state-3.json", getClass(), true);

    }

}
