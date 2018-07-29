/*
    Foilen Infra Resource DNS
    https://github.com/foilen/foilen-infra-resource-dns
    Copyright (c) 2018 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.infra.resource.dns;

import org.junit.Test;

import com.foilen.infra.plugin.core.system.fake.junits.AbstractIPPluginTest;
import com.foilen.infra.plugin.v1.core.exception.IllegalUpdateException;
import com.foilen.infra.resource.dns.model.DnsEntryType;

public class DnsEntryValidationUpdateHandlerTest extends AbstractIPPluginTest {

    private DnsEntry create(String domainName) {
        return new DnsEntry(domainName, DnsEntryType.A, "127.0.0.1");
    }

    @Test(expected = IllegalUpdateException.class)
    public void testValidate_FAIL() {
        DnsEntryValidationUpdateHandler.validate(getCommonServicesContext(), create("10www.example.com"));
    }

    @Test
    public void testValidate_OK() {
        DnsEntryValidationUpdateHandler.validate(getCommonServicesContext(), create("www.example.com"));
    }

}
