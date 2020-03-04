package org.pgramatictesting;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;


@WebMvcTest
public class GreetingControllerTest {

    private GreetingController greetingController;

    @Before
    public void setUp() {
        greetingController = new GreetingController();
    }

    @Test
    public void greet() {
        //given
        String pikachu = "Pikachu";

        //when
        GreetingController.GreetResult response = greetingController.greet(pikachu);

        //expect
        assertThat("Should greet valid pokemon", response.getSay(), equalTo("Hello " + pikachu));
    }

}