package main.web;

import main.exception.EntityNotFoundException;
import main.model.CustomerModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import main.service.CustomerService;
import main.service.RoomInstrumentService;

@RestController
@RequestMapping("/customers")
public class CustomersController {
    private CustomerService customerService;

    private RoomInstrumentService roomInstrumentService;

    @PostMapping(value = "/{id}", consumes = "application/json")
    public void updateCustomer(@PathVariable("id") long id, @RequestBody CustomerModel customer) {
        try {
            customerService.update(id, customer);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{id}/user")
    public ResponseEntity<Long> gerUserId(@PathVariable("id") Long id) {
        try {
            Long userId = customerService.getById(id).getUser().getId();
            return new ResponseEntity<>(userId, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
