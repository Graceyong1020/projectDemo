package com.projectdemo1.auth;

import com.projectdemo1.domain.User;
import com.projectdemo1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
@RequiredArgsConstructor
public class PrincipalDetailService  implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername");
        User user=userRepository.findByUsername(username);
        log.info("user" + user);
        if(user==null) return null;
        PrincipalDetails puser=new PrincipalDetails(user);
        log.info(puser);
        return puser;
    }
}