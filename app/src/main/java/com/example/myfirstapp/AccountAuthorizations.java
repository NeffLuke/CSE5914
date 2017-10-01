package com.example.myfirstapp;

/**
 * Created by simonrouse9461 on 10/1/17.
 */

public class AccountAuthorizations {
    private static final AccountAuthorizations instance = new AccountAuthorizations();

    public static AccountAuthorizations getInstance() {
        return instance;
    }

    private String iftttKey = null;

    private String nestAuthCode = null;

    private AccountAuthorizations() { }

    public void setIftttKey(String key) {
        this.iftttKey = key;
    }

    public String getIftttKey() {
        return iftttKey;
    }

    public void setNestAuthCode(String code) {
        this.nestAuthCode = code;
    }

    public String getNestAuthCode() {
        return nestAuthCode;
    }
}
