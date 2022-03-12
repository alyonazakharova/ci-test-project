package main.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.entity.Customer;
import main.entity.User;
import main.model.AuthModel;
import main.model.RegisterModel;
import main.repository.CustomerRepository;
import main.repository.UserRepository;
import main.service.CustomerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private CustomerService customerService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @Test
    public void signInIsAvailableForUnauthenticatedAndReturnsOkTest() throws Exception {
        AuthModel userData = new AuthModel("username", "password");
        String jsonRequest = mapper.writeValueAsString(userData);

        User user = new User("username", passwordEncoder.encode("password"), "USER");
        when(userRepository.findUserByUserName("username")).thenReturn(java.util.Optional.of(user));

        Customer customer = new Customer();
        customer.setUser(user);
        when(customerRepository.findAll()).thenReturn(Collections.singletonList(customer));

        mockMvc.perform(post("/auth/signin").content(jsonRequest).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void registrationIsAvailableForUnauthenticatedAndReturnsOk() throws Exception {
        RegisterModel userData = new RegisterModel(
                "username",
                "password",
                "89215824646",
                "Alyona"
        );
        String jsonRequest = mapper.writeValueAsString(userData);

        mockMvc.perform(post("/auth/register").content(jsonRequest).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userRepository, times(1)).save(any(User.class));
    }
}