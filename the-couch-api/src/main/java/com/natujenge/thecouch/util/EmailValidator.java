package com.natujenge.thecouch.util;

import org.springframework.stereotype.Service;
import java.util.function.Predicate;

@Service
public class EmailValidator implements Predicate<String> {

    // Email Validation Logic Here
    @Override
    public boolean test(String s) {
        return true;
    }
}
