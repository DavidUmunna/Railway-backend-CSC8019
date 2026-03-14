// package org.coffeeshop.Users.StaffTests;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import org.coffeeshop.Users.controllers.StaffController;
// import org.coffeeshop.Users.dtos.StaffDto;
// import org.coffeeshop.Users.service.StaffService;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// // import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.BDDMockito.given;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


// @WebMvcTest(StaffController.class)
// class StaffControllerTest {

//     @Autowired
//     private MockMvc mockMvc;

//     @Autowired
//     private ObjectMapper objectMapper;

//     @MockBean
//     private StaffService staffService;

//     @Test
//     void createStaff_returnsCreatedStaff() throws Exception {
//         //request dto
//         StaffDto request = new StaffDto();
//         request.setUsername("barista1@gmail.com");
//         request.setFirstName("John");
//         request.setLastName("Doe");
//         request.setRole("BARISTA1");
//         request.setPassword("Secret123!");

//         // response dto (what service returns)
//         StaffDto response = new StaffDto();
//         response.setId(1L);
//         response.setUsername("barista1@gmail.com");
//         response.setFirstName("John");
//         response.setLastName("Doe");
//         response.setRole("BARISTA1");


//         given(staffService.create(any(StaffDto.class))).willReturn(response);

//         mockMvc.perform(post("/api/staff/create")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(request)))
//                 .andExpect(status().isCreated())
//                 .andExpect(jsonPath("$.id").value(1L))
//                 .andExpect(jsonPath("$.username").value("barista1@gmail.com"))
//                 .andExpect(jsonPath("$.password").doesNotExist());
//     }
// }
