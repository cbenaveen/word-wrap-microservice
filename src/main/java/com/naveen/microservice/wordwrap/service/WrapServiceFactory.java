package com.naveen.microservice.wordwrap.service;

import com.naveen.microservice.wordwrap.wrap.WrapTypes;

public interface WrapServiceFactory {
    WordWrapService get(final WrapTypes wrapTypes);
}
