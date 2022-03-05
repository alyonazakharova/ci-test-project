package main.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.model.InstrumentModel;
import main.model.RoomModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @WithMockUser("/user")
    @Test
    public void shouldAddRoom() throws Exception {
        RoomModel room = new RoomModel();
        room.setName("Test room");
        room.setDescription("Test description");
        room.setPrice(100L);
        String jsonRequest = mapper.writeValueAsString(room);
        mockMvc.perform(post("/data/room").content(jsonRequest).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser("/user")
    @Test
    public void shouldReturnRooms() throws Exception {
        mockMvc.perform(get("/rooms/all"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldNotReturnRoomsForUnauthorized() throws Exception {
        mockMvc.perform(get("/rooms/all"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void addRoomIsForbiddenIfUnauthorized() throws Exception {
        RoomModel room = new RoomModel();
        room.setName("Test room");
        room.setDescription("Test description");
        room.setPrice(100L);
        String jsonRequest = mapper.writeValueAsString(room);
        mockMvc.perform(post("/data/room").content(jsonRequest).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @WithMockUser("/user")
    @Test
    public void shouldAddInstrument() throws Exception {
        // FIXME
//        InstrumentModel instrument = new InstrumentModel();
//        instrument.setName("Test instrument");
//        instrument.setDescription("Test description");
//        String jsonRequest = mapper.writeValueAsString(instrument);
//        mockMvc.perform(post("/data/instrument").content(jsonRequest).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
    }


}
