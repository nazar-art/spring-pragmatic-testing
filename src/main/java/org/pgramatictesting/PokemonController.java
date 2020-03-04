package org.pgramatictesting;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.pgramatictesting.service.PokemonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class PokemonController {
    private final PokemonService pokemonService;

    @GetMapping("/pokemon/{name}")
    public PokemonResponse pokemon(@PathVariable String name) {
        Double pokemonPower = pokemonService.getPokemonPower(name);
        return PokemonResponse.builder()
                .name(name)
                .power(pokemonPower)
                .build();
    }

    @Data
    @Builder
    static class PokemonResponse {
        String name;
        Double power;
    }
}
