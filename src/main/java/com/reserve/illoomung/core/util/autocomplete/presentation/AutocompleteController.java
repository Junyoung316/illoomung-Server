package com.reserve.illoomung.core.util.autocomplete.presentation;

import com.reserve.illoomung.core.util.autocomplete.application.AutocompleteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/autocomplete")
@RequiredArgsConstructor
@Slf4j
public class AutocompleteController {

    private final AutocompleteService autocompleteService;

    @GetMapping
    public List<String> autocomplete(@RequestParam String search) {
        return autocompleteService.getSuggestions(search);
    }
}
