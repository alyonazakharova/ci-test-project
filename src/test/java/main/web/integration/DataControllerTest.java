package main.web.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.entity.Instrument;
import main.entity.Room;
import main.entity.RoomInstrument;
import main.model.InstrumentModel;
import main.model.RoomInstrumentModel;
import main.model.RoomModel;
import main.repository.InstrumentRepository;
import main.repository.RoomInstrumentRepository;
import main.repository.RoomRepository;
import main.service.InstrumentService;
import main.service.RoomInstrumentService;
import main.service.RoomService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private InstrumentService instrumentService;

    @MockBean
    private InstrumentRepository instrumentRepository;

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

    public static RequestPostProcessor admin() {
        return user("admin").roles("ADMIN");
    }

    public static RequestPostProcessor regularUser() {
        return user("user").roles("USER");
    }

    // 3
    @Test
    public void addRoomAndReturnOkIfAdmin() throws Exception {
        RoomModel room = new RoomModel();
        room.setName("Test room");
        room.setDescription("Test description");
        room.setPrice(100L);
        String jsonRequest = mapper.writeValueAsString(room);
        mockMvc.perform(post("/data/room").content(jsonRequest).contentType(MediaType.APPLICATION_JSON)
                        .with(admin()))
                .andExpect(status().isOk());
        verify(roomRepository, times(1)).save(any(Room.class));
    }

    // 4
    @Test
    public void addRoomIsForbiddenIfRegularUser() throws Exception {
        RoomModel room = new RoomModel();
        room.setName("Test room");
        room.setDescription("Test description");
        room.setPrice(100L);
        String jsonRequest = mapper.writeValueAsString(room);
        mockMvc.perform(post("/data/room").content(jsonRequest).contentType(MediaType.APPLICATION_JSON)
                        .with(regularUser()))
                .andExpect(status().isForbidden());
        verify(roomRepository, times(0)).save(any(Room.class));
    }

    // 5
    @Test
    public void addInstrumentAndReturnOkIfAdmin() throws Exception {
        InstrumentModel instrument = new InstrumentModel();
        instrument.setName("Test instrument");
        instrument.setDescription("Test description");
        String jsonRequest = mapper.writeValueAsString(instrument);
        mockMvc.perform(post("/data/instrument").content(jsonRequest).contentType(MediaType.APPLICATION_JSON)
                        .with(admin()))
                .andExpect(status().isOk());
        verify(instrumentRepository, times(1)).save(any(Instrument.class));
    }

    // 6
    @Test
    public void addInstrumentIsForbiddenIfRegularUser() throws Exception {
        InstrumentModel instrument = new InstrumentModel();
        instrument.setName("Test instrument");
        instrument.setDescription("Test description");
        String jsonRequest = mapper.writeValueAsString(instrument);
        mockMvc.perform(post("/data/instrument").content(jsonRequest).contentType(MediaType.APPLICATION_JSON)
                        .with(regularUser()))
                .andExpect(status().isForbidden());
        verify(instrumentRepository, times(0)).save(any(Instrument.class));
    }

    // 6
    @Test
    public void addInstrumentIsForbiddenIfUnauthorized() throws Exception {
        InstrumentModel instrument = new InstrumentModel();
        instrument.setName("Test instrument");
        instrument.setDescription("Test description");
        String jsonRequest = mapper.writeValueAsString(instrument);
        mockMvc.perform(post("/data/room").content(jsonRequest).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    // 7
    @Test
    public void shouldDeleteRoomIfAdmin() throws Exception {
        Room roomToDelete = new Room();
        when(roomRepository.findById(anyLong())).thenReturn(java.util.Optional.of(roomToDelete));
        mockMvc.perform(delete("/data/room/1").with(admin()))
                .andExpect(status().isOk());
        verify(roomRepository, times(1)).delete(roomToDelete);
    }

    // 8
    @Test
    public void shouldDeleteInstrumentIfAdmin() throws Exception {
        Instrument instrumentToDelete = new Instrument();
        when(instrumentRepository.findById(anyLong())).thenReturn(java.util.Optional.of(instrumentToDelete));
        mockMvc.perform(delete("/data/instrument/1").with(admin()))
                .andExpect(status().isOk());
        verify(instrumentRepository, times(1)).delete(instrumentToDelete);
    }

    // 9
    @Test
    public void shouldDeleteRoomInstrumentIfAdmin() throws Exception {
        RoomInstrument roomInstrumentToDelete = new RoomInstrument();
        when(roomInstrumentRepository.findById(1L)).thenReturn(java.util.Optional.of(roomInstrumentToDelete));
        mockMvc.perform(delete("/data/room_instrument/1").with(admin()))
                .andExpect(status().isOk());
        verify(roomInstrumentRepository, times(1)).delete(roomInstrumentToDelete);
    }

    // 20
    @Test
    public void shouldAddRoomInstrumentIfAdmin() throws Exception {
        RoomInstrumentModel model = new RoomInstrumentModel();
        long roomId = 1L;
        long instrumentId = 1L;
        model.setRoomId(roomId);
        model.setInstrumentId(instrumentId);

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(new Room()));
        when(instrumentRepository.findById(instrumentId)).thenReturn(Optional.of(new Instrument()));

        String jsonRequest = mapper.writeValueAsString(model);

        mockMvc.perform(post("/data/room_instrument").content(jsonRequest).contentType(MediaType.APPLICATION_JSON)
                        .with(admin()))
                .andExpect(status().isOk());
        verify(roomInstrumentRepository, times(1)).save(any(RoomInstrument.class));
    }
}
