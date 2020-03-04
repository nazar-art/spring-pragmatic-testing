package org.pgramatictesting;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author tolkv
 * @version 11/09/16
 */
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