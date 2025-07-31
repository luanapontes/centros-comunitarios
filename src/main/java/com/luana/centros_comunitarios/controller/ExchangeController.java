package com.luana.centros_comunitarios.controller;

import com.luana.centros_comunitarios.dto.ExchangeRequestDTO;
import com.luana.centros_comunitarios.model.Exchange;
import com.luana.centros_comunitarios.service.ExchangeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Trocas", description = "Troca de recursos entre centros")
@RestController
@RequestMapping("/exchanges")
@RequiredArgsConstructor
public class ExchangeController {

    private final ExchangeService exchangeService;

    @Operation(summary = "Realizar troca de recursos entre centros")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Exchange makeExchange(@RequestBody ExchangeRequestDTO request) {
        return exchangeService.exchangeResources(
                request.getFromCenterId(),
                request.getToCenterId(),
                request.getResourcesGiven(),
                request.getResourcesReceived()
        );
    }

    @Operation(summary = "Histórico de trocas por centro")
    @GetMapping("/history")
    public List<Exchange> getHistory(@RequestParam String centerId,
                                     @RequestParam(required = false)
                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from) {
        if (from == null) {
            from = LocalDateTime.now().minusYears(100);
        }
        return exchangeService.getHistory(centerId, from);
    }
}
