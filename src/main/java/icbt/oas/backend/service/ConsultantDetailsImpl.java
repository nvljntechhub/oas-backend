package icbt.oas.backend.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import icbt.oas.backend.model.Consultant;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class ConsultantDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    @JsonIgnore
    private String password;
    private GrantedAuthority authority;

    public ConsultantDetailsImpl(Long userId, String firstName, String lastName, String email, String password,
                                 GrantedAuthority authority) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.authority = authority;
    }

    public static ConsultantDetailsImpl build(Consultant consultant) {
        return new ConsultantDetailsImpl(
                consultant.getId(),
                consultant.getFirstName(),
                consultant.getLastName(),
                consultant.getEmail(),
                consultant.getPassword(),
                mapRolesToAuthority(consultant.getRole())
        );
    }

    private static GrantedAuthority mapRolesToAuthority(String role) {
        return new SimpleGrantedAuthority(role);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(authority);
        return authorities;
    }

    public String getAuthority() {
        return authority.getAuthority();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAuthority(GrantedAuthority authority) {
        this.authority = authority;
    }


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ConsultantDetailsImpl consultant = (ConsultantDetailsImpl) o;
        return Objects.equals(userId, consultant.userId);
    }
}
