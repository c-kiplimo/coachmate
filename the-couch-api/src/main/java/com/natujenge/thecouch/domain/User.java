package com.natujenge.thecouch.domain;

import com.natujenge.thecouch.domain.enums.*;
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

@Data
@AllArgsConstructor
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
    private String businessName;

    @ManyToMany
    @JoinColumn(name="addedBy")
    User addedBy; //user - coach id
    @Column(unique = true)
    private String msisdn;
    @Column(unique = true)
    private String email;

    // LOGIN DETAILS
    private String username; //email
    private String password;

    @Enumerated(EnumType.STRING) //COACH, CLIENT, ORGANIZATION, ADMIN
    private UserRole userRole;


    //CLIENT DETAILS
    @Enumerated(EnumType.STRING)
    private ClientType clientType;

    @Enumerated(EnumType.STRING)
    private ClientStatus clientStatus;

    @Enumerated(EnumType.STRING)
    private PaymentModeSubscription paymentMode;
    private  String profession;
    private  String physicalAddress;
    private String clientNumber;


    //COACH DETAILS
    @Enumerated(EnumType.STRING)
    private CoachStatus coachStatus;
    private boolean onboarded; //COACH ADDED BY ORGANIZATION

    //FOR ONBOARDED COACH AND ALL CLIENTS
    private String reason;


    private String createdBy; //full name
    private String lastUpdatedBy; //full name


    // management fields
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime lastUpdatedAt;
    @Enumerated(EnumType.STRING)
    private ContentStatus contentStatus;

    // Object Relationships
    @ManyToOne
    @JoinColumn(name="org_id")
    Organization organization;

    @ManyToOne
    @JoinColumn(name="notification_settings_id")
    NotificationSettings notificationSettings;

    @ManyToOne
    @JoinColumn(name="contract_template_id")
    ContractTemplate contractTemplate;

    // Access fields
    private Boolean locked = false;
    private Boolean enabled = false;

    // User Registration Constructor
    public User(String firstName,String lastName, String email, String msisdn,String password, UserRole userRole,
                ) {
        this.fullName = firstName + ' '+lastName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = email;
        this.msisdn = msisdn;
        this.password = password;
        this.userRole = userRole;

    }
    public User(String firstName, String lastName, String email, String msisdn, String password, UserRole userRole,
                Organization organization) {
        this.fullName = firstName + ' '+lastName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = email;
        this.msisdn = msisdn;
        this.password = password;
        this.userRole = userRole;
        this.organization = organization;
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

// org coach user
    public User(String firstName, String lastName, String email, String msisdn, UserRole userRole, Organization organization ){
        this.fullName = firstName + ' '+lastName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = email;
        this.msisdn = msisdn;
        this.userRole = userRole;
        this.organization = organization;
    }
    // org client user
    public User(String firstName, String lastName, String email, String msisdn, UserRole userRole, Organization organization ){
        this.fullName = firstName + ' '+lastName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = email;
        this.msisdn = msisdn;
        this.userRole = userRole;
        this.organization = organization;
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
