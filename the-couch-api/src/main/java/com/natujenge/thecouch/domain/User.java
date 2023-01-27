package com.natujenge.thecouch.domain;

import com.natujenge.thecouch.domain.enums.ContentStatus;
import com.natujenge.thecouch.domain.enums.UserRole;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "tbl_users")
public class User implements UserDetails {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String fullName;
    private String firstName;
    private String lastName;
    private String msisdn;
    private String email;

    // username and password are same
    private String username;
    private String password;

    // management fields
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime lastUpdatedAt;
    @Enumerated(EnumType.STRING)
    private ContentStatus contentStatus;


    // Role of creator, current default assignment ADMIN
    private UserRole createdBy;
    private String lastUpdatedBy;


    @Enumerated(EnumType.STRING) //
    private UserRole userRole;

    // Object Relationships
    @ManyToOne
    @JoinColumn(name="coach_id")
    Coach coach;

    // Access fields
    private Boolean locked = false;
    private Boolean enabled = false;

    // User Registration Constructor
    public User(String firstName,String lastName, String email, String msisdn,String password, UserRole userRole,
                Coach coach) {
        this.fullName = firstName + ' '+lastName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = email;
        this.msisdn = msisdn;
        this.password = password;
        this.userRole = userRole;
        this.coach = coach;
    }
    public User(String firstName, String lastName, String email, String msisdn, String password, UserRole userRole){
        this.fullName = firstName + ' '+lastName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = email;
        this.msisdn = msisdn;
        this.password = password;
        this.userRole = userRole;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(userRole.name());

        return Collections.singletonList(grantedAuthority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    @Override
    public boolean isAccountNonExpired() {
        // allow you to manage and track account expiry
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }


}
