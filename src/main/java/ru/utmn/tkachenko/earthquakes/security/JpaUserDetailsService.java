package ru.utmn.tkachenko.earthquakes.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class JpaUserDetailsService implements UserDetailsService {

    PersonRepository personRepository;

    PasswordEncoder encoder;

    public JpaUserDetailsService(PersonRepository personRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.personRepository = personRepository;
        this.encoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Person person = personRepository.findByEmailIgnoreCase(username);
        if (person != null) {
            String pass = encoder.encode(person.getPassword());
            return User
                    .withUsername(person.getEmail())
                    .accountLocked(!person.isEnabled())
                    .password(pass)
                    .roles(person.getRole())
                    .build();
        }
        throw new UsernameNotFoundException(username);
    }
}
