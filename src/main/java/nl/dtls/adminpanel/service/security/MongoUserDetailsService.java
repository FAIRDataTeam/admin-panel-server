package nl.dtls.adminpanel.service.security;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import nl.dtls.adminpanel.database.repository.UserRepository;
import nl.dtls.adminpanel.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MongoUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> oUser = repository.findByEmail(email);
        if (oUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        User user = oUser.get();
        List<SimpleGrantedAuthority> authorities = Collections
            .singletonList(new SimpleGrantedAuthority("user"));
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
            user.getPasswordHash(), authorities);
    }
}