package com.luana.centros_comunitarios.service;

import com.luana.centros_comunitarios.model.*;
import com.luana.centros_comunitarios.repository.ExchangeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExchangeService {

    private final ExchangeRepository exchangeRepository;
    private final CommunityCenterService communityCenterService;

    private int calculatePoints(List<Resource> resources) {
        return resources.stream()
                .mapToInt(r -> r.getType().getPontos() * r.getQuantity())
                .sum();
    }

    public Exchange exchangeResources(String centerFromId, String centerToId,
                                      List<Resource> resourcesFrom, List<Resource> resourcesTo) {

        CommunityCenter from = communityCenterService.findById(centerFromId);
        CommunityCenter to = communityCenterService.findById(centerToId);

        int pointsFrom = calculatePoints(resourcesFrom);
        int pointsTo = calculatePoints(resourcesTo);

        boolean fromHighOccupancy = from.getCurrentOccupancy() >= 0.9 * from.getMaxCapacity();
        boolean toHighOccupancy = to.getCurrentOccupancy() >= 0.9 * to.getMaxCapacity();

        if (!fromHighOccupancy && !toHighOccupancy && pointsFrom != pointsTo) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Pontos dos recursos oferecidos devem ser iguais, exceto se centro com ocupação > 90%");
        }

        // Verifica se os centros possuem os recursos que querem trocar
        validateResourcesAvailability(from, resourcesFrom);
        validateResourcesAvailability(to, resourcesTo);

        // Atualiza recursos após a troca
        updateCenterResourcesAfterExchange(from, resourcesFrom, resourcesTo);
        updateCenterResourcesAfterExchange(to, resourcesTo, resourcesFrom);

        // Salva centros atualizados
        communityCenterService.save(from);
        communityCenterService.save(to);

        // Cria e salva histórico da troca
        Exchange exchange = Exchange.builder()
                .centerFromId(centerFromId)
                .centerToId(centerToId)
                .resourcesFrom(resourcesFrom)
                .resourcesTo(resourcesTo)
                .timestamp(LocalDateTime.now())
                .build();

        return exchangeRepository.save(exchange);
    }

    // Valida se centro possui recursos suficientes para trocar
    private void validateResourcesAvailability(CommunityCenter center, List<Resource> offeredResources) {
        for (Resource offered : offeredResources) {
            Resource current = center.getResources().stream()
                    .filter(r -> r.getType() == offered.getType())
                    .findFirst()
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Centro " + center.getId() + " não possui recurso " + offered.getType()));
            if (current.getQuantity() < offered.getQuantity()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Centro " + center.getId() + " não tem quantidade suficiente do recurso " + offered.getType());
            }
        }
    }

    private void updateCenterResourcesAfterExchange(CommunityCenter center,
                                                    List<Resource> offered,
                                                    List<Resource> received) {
        // Remove recursos oferecidos
        for (Resource off : offered) {
            center.getResources().stream()
                    .filter(r -> r.getType() == off.getType())
                    .findFirst()
                    .ifPresent(r -> r.setQuantity(r.getQuantity() - off.getQuantity()));
        }

        // Adiciona recursos recebidos
        for (Resource rec : received) {
            center.getResources().stream()
                    .filter(r -> r.getType() == rec.getType())
                    .findFirst()
                    .ifPresentOrElse(
                            r -> r.setQuantity(r.getQuantity() + rec.getQuantity()),
                            () -> center.getResources().add(rec)
                    );
        }

        // Remove recursos com quantidade zero ou menor para manter lista limpa
        center.getResources().removeIf(r -> r.getQuantity() <= 0);
    }

    public List<Exchange> getHistory(String centerId, LocalDateTime from) {
        return exchangeRepository.findByCenterFromIdOrCenterToIdAndTimestampAfter(centerId, centerId, from);
    }
}
