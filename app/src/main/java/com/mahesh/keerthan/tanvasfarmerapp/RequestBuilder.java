package com.mahesh.keerthan.tanvasfarmerapp;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.RequestBody;

public class RequestBuilder {
    public static RequestBody LoginBody(String username, String password, String token) {
         return new FormBody.Builder()
                .add("action", "login")
                .add("format", "json")
                .add("username", username)
                .add("password", password)
                .add("logintoken", token)
                .build();
    }

    public static HttpUrl buildURL(String path, String[] names,String[] values) {
        HttpUrl.Builder builder =  new HttpUrl.Builder()
                .scheme("http") //http
                .host("192.168.43.17")
                .addPathSegment("~vandit")
                .addPathSegment(path);

        if (names != null) {
            builder.addQueryParameter(names[0], values[0]); //add query parameters to the URL

            for(int i = 1;i<names.length;i++)
                builder.addEncodedQueryParameter(names[i], values[i]);
            return builder.build();
        }//adds "/pathSegment" at the end of hostname

        return builder.build();
    }
}
