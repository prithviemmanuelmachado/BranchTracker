package com.picnote.gateway.gateway_broker.configuration;


import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.MultiValueMap;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

public class WebSocketInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(
        ServerHttpRequest request, 
        ServerHttpResponse response,
        WebSocketHandler wsHandler, 
        Map<String, Object> attributes
    ) {
        //get the userID from query in handshake rquest
        String id = extractKeyFromQueryParams(request, "userID");
        //set the id as username so that it can be used to send userspecific messages
        attributes.put("username", id);
        return true;
    }

    @Override
    public void afterHandshake(
        ServerHttpRequest request, 
        ServerHttpResponse response,
        WebSocketHandler wsHandler, 
        Exception exception
    ) {
    }

    private String extractKeyFromQueryParams(ServerHttpRequest request, String paramName) {
        URI uri = request.getURI();
        MultiValueMap<String, String> queryParams = UriComponentsBuilder.fromUri(uri).build().getQueryParams();
        List<String> values = queryParams.get(paramName);
        if (values != null && !values.isEmpty()) {
            return values.get(0);
        }
        return null;
    }

}