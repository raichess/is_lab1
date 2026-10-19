package com.raichess.controller;

import com.raichess.model.Color;
import com.raichess.model.Person;
import com.raichess.service.SpecialOperationsService;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/special")
public class SpecialController {

    private final SpecialOperationsService service;

    public SpecialController(SpecialOperationsService service) {
        this.service = service;
    }

    @DeleteMapping("/by-height")
    public Map<String, Object> deleteByHeight(@RequestParam("height") @NotNull(message = "Укажите рост") Float height) {
        return Map.of("deletedId", service.deleteOneByHeight(height));
    }

    @GetMapping("/height-sum")
    public Map<String, Object> heightSum() {
        return Map.of("sum", service.sumHeights());
    }

    @GetMapping("/height-less")
    public List<Person> heightLess(@RequestParam("height") @NotNull(message = "Укажите рост") Float height) {
        return service.heightLowerThan(height);
    }

    @GetMapping("/hair-percent")
    public Map<String, Object> hairPercent(@RequestParam("color") @NotNull(message = "Укажите цвет") Color color) {
        return Map.of("percent", service.hairColorPercent(color));
    }

    @GetMapping("/eye-count")
    public Map<String, Object> eyeCount(@RequestParam("color") @NotNull(message = "Укажите цвет") Color color) {
        return Map.of("count", service.eyeColorCount(color));
    }
}
