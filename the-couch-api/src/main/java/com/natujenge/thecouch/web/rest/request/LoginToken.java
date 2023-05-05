package com.natujenge.thecouch.web.rest.request;

import com.natujenge.thecouch.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginToken {

    // return user DTO rather than entire user
    private User user;
    private String token;
}

