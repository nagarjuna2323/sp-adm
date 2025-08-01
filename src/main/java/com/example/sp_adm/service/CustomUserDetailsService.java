package com.example.sp_adm.service;

import com.example.sp_adm.model.User;
import com.example.sp_adm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase()))
        );
    }
}


// package com.example.sp_adm.service;

// import com.example.sp_adm.model.User;
// import com.example.sp_adm.repository.UserRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.core.userdetails.*;
// import org.springframework.stereotype.Service;
// import java.util.Collections;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.security.core.userdetails.User;


// @Service
// public class CustomUserDetailsService implements UserDetailsService {

//     @Autowired
//     private UserRepository userRepository;

//     @Override
//     public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//         User user = userRepository.findByUsername(username)
//                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

//         return new org.springframework.security.core.userdetails.User(
//             user.getUsername(),
//             user.getPassword(),
//             List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase()))
//         );
//     }
// }
