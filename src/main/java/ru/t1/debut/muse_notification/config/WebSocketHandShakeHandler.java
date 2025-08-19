package ru.t1.debut.muse_notification.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.net.URI;
import java.security.Principal;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class WebSocketHandShakeHandler extends DefaultHandshakeHandler {

    private final JwtDecoder jwtDecoder;

    @Autowired
    public WebSocketHandShakeHandler(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    protected Principal determineUser(
            final ServerHttpRequest request,
            final WebSocketHandler wsHandler,
            final Map<String, Object> attributes
    ) {

        final var token = getQueryParam(request, "access_token");
        if (token.isPresent()) {
            Jwt jwt = jwtDecoder.decode(token.get());
            Authentication auth = new JwtAuthenticationToken(jwt, AuthorityUtils.createAuthorityList("ROLE_USER"));
            attributes.put("simpUser", auth);
            return auth;
        }
        return null;
    }

    public static Optional<String> getQueryParam(final ServerHttpRequest request, final String paramName) {
        URI uri = request.getURI();
        String query = uri.getQuery();

        if (query != null) {
            String[] params = query.split("&");
            Map<String, String> queryParams = Arrays.stream(params)
                    .map(param -> param.split("=", 2))
                    .collect(Collectors.toMap(p -> p[0], p -> p.length > 1 ? p[1] : ""));

            return Optional.ofNullable(queryParams.get(paramName));
        }

        return Optional.empty();
    }
}
