package com.workable.matchmakers.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class Adapter {

    @Autowired
    protected RestTemplate rest;

    @Value("${application.url.ats}")
    protected String atsUrl;

}
