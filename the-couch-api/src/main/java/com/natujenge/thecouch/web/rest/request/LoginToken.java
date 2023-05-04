package com.natujenge.thecouch.web.rest.request;

import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.service.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginToken {

    // return user DTO rather than entire user
    private UserDTO user;
    private String token;
}

