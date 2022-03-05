package main.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.model.AuthModel;
import main.model.RegisterModel;
import main.model.RoomModel;
import main.repository.UserRepository;
import main.security.jwt.JwtTokenProvider;
import main.service.CustomerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper mapper;

//    @Autowired
//    AuthenticationManager authenticationManager;
//
//    @Autowired
//    JwtTokenProvider jwtTokenProvider;

    @MockBean
    UserRepository userRepository;

//    @Autowired
//    PasswordEncoder pwdEncoder;

    @MockBean
    CustomerService customerService;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @Test
    public void registrationIsAvailableForUnauthenticated() throws Exception {
        RegisterModel userData = new RegisterModel("username", "password", "Alyona", "89215824646");
        String jsonRequest = mapper.writeValueAsString(userData);
        mockMvc.perform(post("/auth/register").content(jsonRequest).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }


}