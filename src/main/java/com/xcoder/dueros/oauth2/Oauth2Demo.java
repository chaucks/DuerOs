package com.xcoder.dueros.oauth2;

import io.vertx.core.Vertx;
import io.vertx.ext.auth.oauth2.OAuth2Auth;
import io.vertx.ext.auth.oauth2.providers.GithubAuth;

public class Oauth2Demo {

    public void auth() {

        OAuth2Auth auth = GithubAuth.create(Vertx.vertx(), "", "");


    }

}
