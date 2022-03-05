package main.service;

import main.entity.Customer;
import main.exception.EntityNotFoundException;
import main.model.CustomerModel;
import org.springframework.beans.factory.annotation.Autowired;
import main.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CustomerServiceImpl implements CustomerService{
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public boolean add(Customer customer) {
        Optional<Customer> customerFromDb = customerRepository.findById(customer.getId());
        if (customerFromDb.isPresent()) {
            return false;
        }

        if (!isValidNumber(customer.getPhone())) {
            throw new InvalidParameterException();
        }

        customerRepository.save(customer);

        return true;
    }

    @Override
    public void delete(long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (!customer.isPresent()) {
            throw new EntityNotFoundException("Customer not found");
        }
        customerRepository.delete(customer.get());
    }

    @Override
    public void update(long id, CustomerModel customer) {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        if (!customerOptional.isPresent()) {
            throw new EntityNotFoundException("Customer not found");
        }
        Customer customerOld = customerOptional.get();
        customerOld.setName(customer.getName());
        customerOld.setPhone(customer.getPhone());
        customerRepository.save(customerOld);
    }

    @Override
    public Customer getById(long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (!customer.isPresent()) {
            throw new EntityNotFoundException("Customer not found");
        }
        return customer.get();
    }

    @Override
    public Customer getByUserId(long id) {
        Optional<Customer> customer = ((List<Customer>) customerRepository.findAll())
            .stream()
            .filter(c -> c.getUser().getId().compareTo(id) == 0)
            .findAny();
        if (!customer.isPresent()) {
            throw new EntityNotFoundException("Customer not found");
        }
        return customer.get();
    }

    @Override
    public List<Customer> getAll() {
        return (List<Customer>) customerRepository.findAll();
    }

    private boolean isValidNumber(String number) {
        Pattern p = Pattern.compile("89\\d{9}");
        Matcher m = p.matcher(number);
        return (m.matches());
    }
}
