package main.web.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.entity.Instrument;
import main.entity.Room;
import main.entity.RoomInstrument;
import main.repository.RoomInstrumentRepository;
import main.repository.RoomRepository;
import main.service.RoomInstrumentService;
import main.service.RoomService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoomControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private RoomService roomService;

    @MockBean
    private RoomRepository roomRepository;

    @Autowired
    private RoomInstrumentService roomInstrumentService;

    @MockBean
    private RoomInstrumentRepository roomInstrumentRepository;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    // 10
    @WithMockUser("/user")
    @Test
    public void shouldReturnRoomsAndOkIfAuthorized() throws Exception {
        List<Room> expectedRooms = Arrays.asList(new Room(), new Room());
        when(roomRepository.findAll()).thenReturn(expectedRooms);
        String expectedJsonContent = mapper.writeValueAsString(expectedRooms);

        mockMvc.perform(get("/rooms/all"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(2)))
                .andExpect(content().json(expectedJsonContent));
    }

    // 11
    @Test
    public void shouldNotReturnRoomsIfUnauthorized() throws Exception {
        mockMvc.perform(get("/rooms/all"))
                .andExpect(status().isForbidden());
    }

    // 18
    @WithMockUser("/user")
    @Test
    public void shouldReturnAllRoomInstruments() throws Exception {
        RoomInstrument roomInstrument1 = new RoomInstrument();
        RoomInstrument roomInstrument2 = new RoomInstrument();

        List<RoomInstrument> expectedRoomInstruments = Arrays.asList(roomInstrument1, roomInstrument2);

        when(roomInstrumentRepository.findAll()).thenReturn(expectedRoomInstruments);

        String expectedJsonContent = mapper.writeValueAsString(expectedRoomInstruments);

        mockMvc.perform(get("/rooms/instruments"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(2)))
                .andExpect(content().json(expectedJsonContent));
    }

    // 19
    @WithMockUser("/user")
    @Test
    public void shouldReturnInstrumentsFromRoom() throws Exception {
        Room room = new Room();
        room.setId(1L);
        Instrument instrument1 = new Instrument();
        RoomInstrument roomInstrument1 = new RoomInstrument();
        roomInstrument1.setRoom(room);
        roomInstrument1.setInstrument(instrument1);
        Instrument instrument2 = new Instrument();
        RoomInstrument roomInstrument2 = new RoomInstrument();
        roomInstrument2.setRoom(room);
        roomInstrument2.setInstrument(instrument2);

        List<RoomInstrument> roomInstrumentList = Arrays.asList(roomInstrument1, roomInstrument2);

        when(roomInstrumentRepository.findAll()).thenReturn(roomInstrumentList);

        List<Instrument> expectedInstrument = Arrays.asList(instrument1, instrument2);

        String expectedJsonContent = mapper.writeValueAsString(expectedInstrument);

        mockMvc.perform(get("/rooms/1/instruments"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(2)))
                .andExpect(content().json(expectedJsonContent));
    }
}