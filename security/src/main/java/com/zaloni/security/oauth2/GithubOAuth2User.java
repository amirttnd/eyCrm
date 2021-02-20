package com.zaloni.security.oauth2;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class GithubOAuth2User implements OAuth2User {
    private OAuth2User user;
    private List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_USER");
    private Map<String, Object> attributes;

    public GithubOAuth2User(OAuth2User oAuth2User) {
        this.user = oAuth2User;
        System.out.println(oAuth2User.getAttributes());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.user.getAuthorities();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.user.getAttributes();
    }

    public String getId() {
        return user.getAttribute("id");
    }

    @Override
    public String getName() {
        return user.getAttribute("name");
    }

    public String getLogin() {
        return user.getAttribute("login");
    }

    public String getEmail() {
        return user.getAttribute("email");
    }
}
