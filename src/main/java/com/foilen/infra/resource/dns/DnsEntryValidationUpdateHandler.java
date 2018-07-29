/*
    Foilen Infra Resource DNS
    https://github.com/foilen/foilen-infra-resource-dns
    Copyright (c) 2018 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.infra.resource.dns;

import java.util.List;
import java.util.regex.Pattern;

import com.foilen.infra.plugin.v1.core.context.ChangesContext;
import com.foilen.infra.plugin.v1.core.context.CommonServicesContext;
import com.foilen.infra.plugin.v1.core.eventhandler.AbstractUpdateEventHandler;
import com.foilen.infra.plugin.v1.core.exception.IllegalUpdateException;
import com.foilen.infra.plugin.v1.model.resource.IPResource;
import com.foilen.smalltools.tuple.Tuple3;
import com.google.common.base.Strings;

public class DnsEntryValidationUpdateHandler extends AbstractUpdateEventHandler<DnsEntry> {

    private static Pattern startWithLetterValidationRegex = Pattern.compile("[a-zA-Z].*");

    static protected void validate(CommonServicesContext services, DnsEntry resource) {
        if (!Strings.isNullOrEmpty(resource.getName())) {
            if (!startWithLetterValidationRegex.matcher(resource.getName()).matches()) {
                throw new IllegalUpdateException(services.getTranslationService().translate("error.notStartingWithLetter"));
            }
        }
    }

    @Override
    public void addHandler(CommonServicesContext services, ChangesContext changes, DnsEntry resource) {
        validate(services, resource);
    }

    @Override
    public void checkAndFix(CommonServicesContext services, ChangesContext changes, DnsEntry resource) {

    }

    @Override
    public void deleteHandler(CommonServicesContext services, ChangesContext changes, DnsEntry resource, List<Tuple3<IPResource, String, IPResource>> previousLinks) {
    }

    @Override
    public Class<DnsEntry> supportedClass() {
        return DnsEntry.class;
    }

    @Override
    public void updateHandler(CommonServicesContext services, ChangesContext changes, DnsEntry previousResource, DnsEntry newResource) {
        validate(services, newResource);
    }

}
