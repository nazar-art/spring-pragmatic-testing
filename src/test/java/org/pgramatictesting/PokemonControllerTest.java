package org.pgramatictesting;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringRunner;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(
        webEnvironment = RANDOM_PORT,
        properties = {"pokemon.home=http://localhost:12000/pokemonRepository/Bulbasaur"}
)
@RunWith(SpringRunner.class)
@AutoConfigureRestDocs("build/generated-snippets")
public class PokemonControllerTest extends MockMvcBaseTest {
// OK but not actual for this test @MockBean
// OK but not actual for this test PokemonService pokemonService;

    @Test
    public void should_return_power() throws Exception {
        //given
        String bulbasaur = "Bulbasaur";
// OK but not actual for this test   given(pokemonService.getPokemonPower(any()))
// OK but not actual for this test       .willReturn(15.0d);
        stubFor(WireMock.get(
                urlEqualTo("/pokemonRepository/Bulbasaur"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("14.5")
                )
        );

        //expect
        mockMvc.perform(
                get("/pokemon/" + bulbasaur)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.name", equalTo(bulbasaur))
                )
                .andExpect(
                        jsonPath("$.power", Matchers.closeTo(15.0d, 1.0d))
                )
                .andDo(document("pokemon power",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestHeaders(
                            headerWithName("Accept").description("Content-Type")
                    ),
                    responseFields(
                            fieldWithPath(".name").description("pokemon name").type(JsonFieldType.STRING),
                            fieldWithPath(".power").description("pokemon power").type(JsonFieldType.NUMBER)
                    )
                ));


        wireMockRule.verify(
                getRequestedFor(
                        urlEqualTo("/pokemonRepository/Bulbasaur")
                ).withoutHeader("Content-Type"));
    }

}