package com.codingjoa.security.service;

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("/WEB-INF/properties/security.properties")
public class JwtProvider {

}
