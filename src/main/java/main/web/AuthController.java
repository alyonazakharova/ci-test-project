package main.web;

import main.entity.Customer;
import main.entity.User;
import main.exception.InvalidLoginDataException;
import main.model.AuthModel;
import main.model.RegisterModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import main.repository.UserRepository;
import main.security.jwt.JwtTokenProvider;
import main.service.CustomerService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder pwdEncoder;

    CustomerService customerService;

    @PostMapping("/signin")
    public ResponseEntity signIn(@RequestBody AuthModel request) {
        try {
            String name = request.getUserName();
            Optional<User> user = userRepository.findUserByUserName(name);

            if (!user.isPresent() || !pwdEncoder.matches(request.getPassword(), user.get().getPassword())) {
                throw new InvalidLoginDataException();
            }

            Customer customer = customerService.getByUserId(user.get().getId());

            String token = jwtTokenProvider.createToken(
                    name,
                    user.get().getRole()
            );

            Map<Object, Object> model = new HashMap<>();
            model.put("userId", user.get().getId());
            model.put("customerId", customer.getId());
            model.put("userName", name);
            model.put("token", token);

            return ResponseEntity.ok(model);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterModel registerModel) {
        try {
            User user = new User(
                    registerModel.getUserName(),
                    pwdEncoder.encode(registerModel.getPassword()) ,
                    "ROLE_USER");
            userRepository.save(user);
            customerService.add(new Customer(registerModel.getName(), registerModel.getPhone(), user));
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exist");
        }
        return ResponseEntity.ok(null);
    }

    @Autowired
    void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }
}
