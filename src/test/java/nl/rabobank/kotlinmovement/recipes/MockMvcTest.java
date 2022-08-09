package nl.rabobank.kotlinmovement.recipes;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.rabobank.kotlinmovement.recipes.model.RecipeResponseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MockMvcTest {
    @Autowired
    MockMvc mockMvc;
    static ObjectMapper objectMapper = new ObjectMapper();

     <T> T mockMvcPerformRequest(MockHttpServletRequestBuilder requestBuilder, Class<T> responseType, ResultMatcher status) throws Exception {
        var responseArrayString = mockMvc
                .perform(requestBuilder)
                .andDo(print())
                .andExpect(status)
                .andReturn()
                .getResponse()
                .getContentAsString();
        return objectMapper.readValue(responseArrayString, responseType);
    }

   void mockMvcPerformRequest(MockHttpServletRequestBuilder requestBuilder, ResultMatcher status) throws Exception {
         mockMvc
                .perform(requestBuilder)
                .andDo(print())
                .andExpect(status)
                .andReturn()
                .getResponse()
                .getContentAsString();
    }


    RecipeResponseTest[] getAllRecipes() throws Exception {
        return mockMvcPerformRequest(get("/recipes"), RecipeResponseTest[].class, status().isOk());
    }

}
