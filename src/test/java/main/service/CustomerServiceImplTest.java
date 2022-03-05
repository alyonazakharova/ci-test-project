package main.service;

import main.entity.Customer;
import main.exception.EntityNotFoundException;
import main.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * TOTAL: 6
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CustomerServiceImpl.class})
class CustomerServiceImplTest {
    @MockBean
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerService customerService;

    @Test
    void addCustomerTest() {
        Customer customer = new Customer();
        customer.setPhone("89215824646");
        boolean isCustomerCreated = customerService.add(customer);
        assertTrue(isCustomerCreated);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void addCustomerNegativeTest() {
        Customer customer = new Customer();
        customer.setId(1L);
        when(customerRepository.findById(customer.getId()))
                .thenReturn(Optional.of(customer));
        boolean isCustomerCreated = customerService.add(customer);
        assertFalse(isCustomerCreated);
    }

    @Test
    void shouldThrowExceptionForInvalidNumber() {
        Customer customer = new Customer();
        customer.setPhone("103");
        assertThrows(InvalidParameterException.class, () -> customerService.add(customer));
    }

    @Test
    void shouldReturnAllCustomers() {
        List<Customer> customers = new ArrayList();
        customers.add(new Customer());
        given(customerRepository.findAll()).willReturn(customers);
        List<Customer> expected = customerService.getAll();
        assertEquals(expected, customers);
        verify(customerRepository).findAll();
    }

    @Test
    void shouldDeleteCustomerIfFound() {
        Customer customer = new Customer();
        customer.setId(1L);
        when(customerRepository.findById(customer.getId()))
                .thenReturn(Optional.of(customer));
        customerService.delete(customer.getId());
        verify(customerRepository, times(1)).delete(customer);
    }

    @Test
    void shouldThrowExceptionIfCustomerNotFound() {
        Customer customer = new Customer();
        customer.setId(666L);
        given(customerRepository.findById(anyLong())).willReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> customerService.delete(customer.getId()));
    }
}
