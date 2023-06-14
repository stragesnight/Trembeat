package com.trembeat.services;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;
import java.util.Properties;

public class ResourceMessageSource extends ReloadableResourceBundleMessageSource {
    public Properties getMessages(Locale locale) {
        return getMergedProperties(locale).getProperties();
    }
}