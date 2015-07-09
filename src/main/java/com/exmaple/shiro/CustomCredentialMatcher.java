package com.exmaple.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

public class CustomCredentialMatcher extends SimpleCredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        if (token instanceof BearerAuthenticationToken) {
            Object tokenCredentials = getCredentials(token);
            Object accountCredentials = ((CustomSimpleAuthenticationInfo) info).getPubToken();
            return equals(tokenCredentials, accountCredentials);
        } else {
            Object tokenCredentials = getCredentials(token);
            Object accountCredentials = getCredentials(info);
            return equals(tokenCredentials, accountCredentials);
        }
    }

    @Override
    protected Object getCredentials(AuthenticationToken token) {
        if (token instanceof BearerAuthenticationToken) {
            return ((BearerAuthenticationToken) token).getPubToken();
        } else {
            return token.getCredentials();
        }
    }
}
