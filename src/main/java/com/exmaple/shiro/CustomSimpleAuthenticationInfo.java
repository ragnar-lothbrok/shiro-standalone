package com.exmaple.shiro;

import org.apache.shiro.authc.SimpleAuthenticationInfo;

public class CustomSimpleAuthenticationInfo extends SimpleAuthenticationInfo {

    private static final long serialVersionUID = 1L;
    private Object pubToken;

    public Object getPubToken() {
        return pubToken;
    }

    public void setPubToken(Object pubToken) {
        this.pubToken = pubToken;
    }

    public CustomSimpleAuthenticationInfo(Object principal, Object credentials, String realmName, String pubToken) {
        super(principal, credentials, realmName);
        this.pubToken = pubToken;
    }

}
