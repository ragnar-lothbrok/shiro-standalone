package com.exmaple.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

public class BearerAuthenticationToken extends UsernamePasswordToken {

    private static final long serialVersionUID = 1L;
    private String pubToken;

    public String getPubToken() {
        return pubToken;
    }

    public void setPubToken(String pubToken) {
        this.pubToken = pubToken;
    }

    public BearerAuthenticationToken(final String username, final char[] password, String pubToken) {
        super(username, password, false, null);
        this.pubToken = pubToken;
    }

}
