package com.security.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Created by 陈圣融 on 2017-08-02.
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "username_unique_authority", columnNames = {"username", "authority"}))
public class OauthAuthorities {

    @Id
//    @GenericGenerator(name = "PKUUID", strategy = "uuid2")
//    @GeneratedValue(generator = "PKUUID")
    private Long id;
    private String username;
    private String authority;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
