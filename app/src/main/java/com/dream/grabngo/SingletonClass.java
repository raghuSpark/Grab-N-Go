package com.dream.grabngo;

import com.ibm.cloud.appid.android.api.tokens.IdentityToken;

public class SingletonClass {
    private static final SingletonClass ourInstance = new SingletonClass();
    IdentityToken identityToken;

    private SingletonClass() {
    }

    public static SingletonClass getInstance() {
        return ourInstance;
    }

    public IdentityToken getIdentityToken() {
        return identityToken;
    }

    public void setIdentityToken(IdentityToken identityToken) {
        this.identityToken = identityToken;
    }
}
