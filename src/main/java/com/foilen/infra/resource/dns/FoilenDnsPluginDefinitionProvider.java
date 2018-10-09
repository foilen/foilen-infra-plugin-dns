/*
    Foilen Infra Resource DNS
    https://github.com/foilen/foilen-infra-resource-dns
    Copyright (c) 2018 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.infra.resource.dns;

import java.util.Arrays;

import com.foilen.infra.plugin.v1.core.context.CommonServicesContext;
import com.foilen.infra.plugin.v1.core.plugin.IPPluginDefinitionProvider;
import com.foilen.infra.plugin.v1.core.plugin.IPPluginDefinitionV1;

public class FoilenDnsPluginDefinitionProvider implements IPPluginDefinitionProvider {

    @Override
    public IPPluginDefinitionV1 getIPPluginDefinition() {
        IPPluginDefinitionV1 pluginDefinitionV1 = new IPPluginDefinitionV1("Foilen", "DNS", "To manage DNS entries", "1.0.0");

        pluginDefinitionV1.addCustomResource(DnsEntry.class, "Dns Entry", //
                Arrays.asList( //
                        DnsEntry.PROPERTY_NAME, //
                        DnsEntry.PROPERTY_TYPE, //
                        DnsEntry.PROPERTY_DETAILS //
                ), //
                Arrays.asList( //
                        DnsEntry.PROPERTY_NAME, //
                        DnsEntry.PROPERTY_TYPE, //
                        DnsEntry.PROPERTY_DETAILS //
                ));

        pluginDefinitionV1.addCustomResource(DnsPointer.class, "Dns Pointer", //
                Arrays.asList( //
                        DnsPointer.PROPERTY_NAME //
                ), //
                Arrays.asList( //
                        DnsPointer.PROPERTY_NAME //
                ));

        pluginDefinitionV1.addTranslations("/com/foilen/infra/resource/dns/messages");
        pluginDefinitionV1.addResourceEditor(new ManualDnsEntryEditor(), ManualDnsEntryEditor.EDITOR_NAME);

        pluginDefinitionV1.addUpdateHandler(new DnsEntryUpdateHandler());
        pluginDefinitionV1.addUpdateHandler(new DnsPointerUpdateHandler());
        pluginDefinitionV1.addUpdateHandler(new DomainUpdateHandler());
        pluginDefinitionV1.addUpdateHandler(new MachineUpdateHandler());

        return pluginDefinitionV1;
    }

    @Override
    public void initialize(CommonServicesContext commonServicesContext) {
    }

}
