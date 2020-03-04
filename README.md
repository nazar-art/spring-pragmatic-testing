# Spring pragmatic testing

## Step 1

Run application by `./gradlew bootRun` command

    $ http :8080/greet/kirill
    
    HTTP/1.1 200
    Content-Type: application/json;charset=UTF-8
    Date: Sun, 11 Sep 2016 11:04:53 GMT
    Transfer-Encoding: chunked
    X-Application-Context: application
    
    {
        "name": "kirill",
        "say": "Hello kirill"
    }

See the GreetingControllerTest.java. Say some words about Unit test problems and go to write integration tests.

### Results

* `./gradlew build`
* `open build/reports/tests/index.html`

## Step 2

* Add `@MockMvcTest`. Avoid boilerplate code. 
* Add test with full context load `ApplicatoinTests.java`
* Some words about MockMvc integrations with SpringSecurity

## Step 3

    http :8080/pokemon/bulbosaur
    
    HTTP/1.1 200
    Content-Type: application/json;charset=UTF-8
    Date: Sun, 11 Sep 2016 14:21:05 GMT
    Transfer-Encoding: chunked
    X-Application-Context: pragmatic-testing
    
    {
        "name": "bulbosaur",
        "power": 0.4175532166907524
    }
    
* Add `PokemonControllerTest.java`. 
* If you try run this test without @MockBean, you have a error:

```
ResourceAccessException: I/O error on GET request for 
"http://localhost:10000": Connection refused; nested exception is java.net.ConnectException: Connection refused
```

Fix test with `@MockBean` or `@SpyBean` 

## Step 4

Do you remember about external call? No? But we have a call to pokemon rest repository!

    ResponseEntity<Double> forEntity = restTemplate
        .getForEntity(
            pokemonHome.getHome(), 
            Double.class
        );

Do you want test it? Wiremock help you! But you have to say some words about `@RestClientTest` and `MockRestServiceServer`
Let`s go:

* Add WireMock dependency `compile 'com.github.tomakehurst:wiremock-standalone:2.1.9'` to build.gradle
* Use WireMock JUnit rule in our tests. See `MockMvcBaseTest.java`


    @Rule
    public WireMockRule wireMockRule = new WireMockRule(
        WireMockConfiguration.wireMockConfig()
            .port(12000)
            .notifier(new ConsoleNotifier(true))
    );
    
* Add stub or verify logic in some tests:
 
Stub:

    stubFor(WireMock.get(
        urlEqualTo("/pokemone/Bulbasaur"))
        .willReturn(aResponse()
            .withStatus(200)
            .withBody("14.5")
        )
    );
    
And verify:
    
    wireMockRule.verify(
        getRequestedFor(
            urlEqualTo("/pokemonRepository/Bulbasaur")
        ).withoutHeader("Content-Type"));
        
## Step 5
                
We have a lot of Tests. Try to get the advanced benefits from our tests!

Each integration test handle request/response to our app. Use this date for generate documentation!

Add next annotation to some test: `@AutoConfigureRestDocs("build/generated-snippets")`

and: (add `.andDo` block)

    .andExpect(content().string(containsString(pikachu)))
    .andDo(document("greet pokemon"));
    
Run build `./gradlew build` and look at build/generated-snippets directory. It is awesome! It is our request and response. It ready for including to asciidoc source!
    
Next, improve our docs! Add descriptions for documenting and checking
    
    .andDo(document("greet pokemon",
        preprocessRequest(prettyPrint()),
        requestHeaders(
            headerWithName("Accept")
                .description("Content type :)")
        ),
        responseFields(
            fieldWithPath(".name")
                .description("Pokemon name")
                .type(JsonFieldType.STRING),
            fieldWithPath(".say")
                .description("Pokemon greetings")
                .type(JsonFieldType.STRING)
        )
        ...

Run the test again and look `generated-snippets` dir. We have a new files! Files with description
        
It is not just a description, because if something actually happened in test execution and this not related to you description - test fail with next description:
        
`Headers with the following names were not found in the request: [Accept]`

And similar errors for request/response fields

## Step 6

Make single document for all project documentation. Use asciidoctor for solve it.

* Add dependencies to build.gradle

```groovy
plugins {
  id "org.asciidoctor.convert" version "1.5.2"
}

apply plugin: 'java'
apply plugin: 'spring-boot'

ext {
  snippetsDir = file('build/generated-snippets')
}

test {
  outputs.dir snippetsDir
}

asciidoctor {
  attributes 'snippets': snippetsDir
  inputs.dir snippetsDir
  dependsOn test
}
```

* create `src/docs/asciidoc/index.adoc` file, which represent our single page doc.
* include all needed files from autogeneration directory (use `snippets` var in asciidoc file)
* run command `./gradlew clean asciidoctor`
* See your docs `open build/asciidoc/html5/index.html`

## Step 7

Publish docs

Add to build.gradle:

    jar {
      dependsOn asciidoctor
      from("${asciidoctor.outputDir}/html5") {
        into 'static/api-docs'
      }
    }

Run `./gradlew build` and go to [localhost:8080/api-docs](http://localhost:8080/api-docs/index.html)    