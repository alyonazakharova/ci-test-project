package main.web.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.entity.Customer;
import main.entity.Reservation;
import main.entity.Room;
import main.model.ReservationModel;
import main.repository.CustomerRepository;
import main.repository.ReservationRepository;
import main.repository.RoomRepository;
import main.service.CustomerService;
import main.service.ReservationService;
import main.service.RoomService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReservationControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ReservationService reservationService;

    @MockBean
    private ReservationRepository reservationRepository;

    @Autowired
    private CustomerService customerService;

    @MockBean
    private CustomerRepository customerRepository;

    @Autowired
    private RoomService roomService;

    @MockBean
    private RoomRepository roomRepository;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    // 12
    @WithMockUser("/user")
    @Test
    public void shouldAddReservation() throws Exception {
        ReservationModel reservationModel = new ReservationModel();
        long customerId = 1;
        long roomId = 1;
        reservationModel.setCustomerId(customerId);
        reservationModel.setRoomId(roomId);
        reservationModel.setDate(LocalDate.now().toString());
        when(customerRepository.findById(customerId)).thenReturn(java.util.Optional.of(new Customer()));
        when(roomRepository.findById(roomId)).thenReturn(java.util.Optional.of(new Room()));
        String jsonRequest = mapper.writeValueAsString(reservationModel);
        mockMvc.perform(post("/reservations/add").content(jsonRequest).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    // 13
    @WithMockUser("/user")
    @Test
    public void shouldDeleteReservation() throws Exception {
        Reservation reservationToDelete = new Reservation();
        when(reservationRepository.findById(anyLong())).thenReturn(java.util.Optional.of(reservationToDelete));
        mockMvc.perform(delete("/reservations/customer/1"))
                .andExpect(status().isOk());
        verify(reservationRepository, times(1)).delete(reservationToDelete);
    }

    // 14
    @WithMockUser("/user")
    @Test
    public void shouldChangeReservationStatus() throws Exception {
        Reservation reservation = new Reservation();
        boolean initialStatus = false;
        reservation.setId(1L);
        reservation.setDate(Date.valueOf(LocalDate.now()));
        reservation.setConfirmed(initialStatus);

        when(reservationRepository.findById(1L)).thenReturn(java.util.Optional.of(reservation));

        mockMvc.perform(put("/reservations/1"))
                .andExpect(status().isOk());

        Optional<Reservation> updatedReservation = reservationRepository.findById(1L);
        assertEquals(updatedReservation.get().isConfirmed(), !initialStatus);
    }

    // 15

    // 16
    @WithMockUser("/user")
    @Test
    public void shouldReturnCustomersReservations() throws Exception {
        Reservation reservation = new Reservation();
        Customer customer = new Customer();
        customer.setId(1L);
        reservation.setCustomer(customer);
        List<Reservation> expectedReservations = Collections.singletonList(reservation);
        when(reservationRepository.findAll()).thenReturn(expectedReservations);
        String expectedJsonContent = mapper.writeValueAsString(expectedReservations);

        mockMvc.perform(get("/reservations/customer/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJsonContent));
    }

    // 17
    @WithMockUser("/user")
    @Test
    public void shouldReturnAllReservations() throws Exception {
        List<Reservation> expectedReservations = Arrays.asList(new Reservation(), new Reservation());
        when(reservationRepository.findAll()).thenReturn(expectedReservations);
        String expectedJsonContent = mapper.writeValueAsString(expectedReservations);

        mockMvc.perform(get("/reservations/all"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(2)))
                .andExpect(content().json(expectedJsonContent));
    }
}
