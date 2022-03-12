package main.service;

import main.entity.User;
import main.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CustomUserDetailsService.class})
class CustomUserDetailsServiceTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldLoadUserByUserName() {
        User expectedUser = new User();
        when(userRepository.findUserByUserName("username")).thenReturn(Optional.of(expectedUser));
        UserDetails user = customUserDetailsService.loadUserByUsername("username");
        assertEquals(expectedUser, user);
        verify(userRepository, times(1)).findUserByUserName("username");
    }


}