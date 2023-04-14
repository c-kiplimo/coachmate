package ke.natujenge.baked.security;

import ke.natujenge.baked.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SecurityUtils {

    public static String getCurrentUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username;
        if (authentication != null){
            username = authentication.getName();
        } else {
            username ="System";
        }
        return username;
    }

    public static String getCurrentUserEmail() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractEmailFromPrincipal(securityContext.getAuthentication())).orElse("unknown");
    }
    private static String extractEmailFromPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            return user.getEmail();
        }

        return null;
    }

    public static Long getCurrentBakerId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof User) {
            return ((User) authentication.getPrincipal()).getBaker().getId();
        }

        return null;
    }

    public static User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }

        return null;
    }
}
