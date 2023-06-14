package com.trembeat.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.*;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MessageResolverService implements MessageSourceAware {
    @Autowired
    private MessageSource _messageSource;


    @Override
    public void setMessageSource(MessageSource messageSource) {
        _messageSource = messageSource;
    }

    public String getMessage(String code) {
        Locale locale = LocaleContextHolder.getLocale();
        return _messageSource.getMessage(code, new Object[0], locale);
    }

    public Map<String, String> resolveMessages(Map<String, String> codes) {
        Map<String, String> result = new HashMap<>();

        for (String key : codes.keySet())
            result.put(key, getMessage(codes.get(key)));

        return result;
    }
}