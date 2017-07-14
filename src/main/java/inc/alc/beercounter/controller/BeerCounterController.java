package inc.alc.beercounter.controller;

import inc.alc.beercounter.dao.CountRepository;
import inc.alc.beercounter.domain.Count;
import inc.alc.beercounter.dtos.CountDto;
import inc.alc.beercounter.dtos.Ranking;
import inc.alc.beercounter.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/{event}")
public class BeerCounterController {
    @Autowired
    private CountRepository countRepository;

    @Autowired
    private HttpServletRequest httpServletRequest;

    private String getEvent() {
        Map<String, String> pathVariables = (Map<String, String>) httpServletRequest
                .getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        return pathVariables.get("event");
    }

    @RequestMapping(method= RequestMethod.GET)
    public List<Ranking> getRankings() {
        List<Count> entries = countRepository.findByEvent(getEvent());
        Map<String, Double> data = new HashMap<String, Double>();

        for (Count p : entries) {
            Double d = data.get(p.getName());
            if (d == null) {
                d = 0.d;
            }

            d += p.getAmount();
            data.put(p.getName(), d);
        }

        List<Ranking> result = new ArrayList<Ranking>();
        for (String d : data.keySet()) {
            Ranking r = new Ranking();
            r.setName(d);
            r.setAmount(data.get(d));
            result.add(r);
        }

        Collections.sort(result, (o1, o2) -> o2.getAmount().compareTo(o1.getAmount()));

        return result;
    }

    @RequestMapping(method= RequestMethod.GET, value="/all")
    public List<Count> getAll() {
        List<Count> result = countRepository.findByEvent(getEvent());
        Collections.sort(result, (o1, o2) -> o2.getTimestamp().compareTo(o1.getTimestamp()));
        return result;
    }

    @RequestMapping(method= RequestMethod.POST, value="/")
    public Count addCount(@RequestBody CountDto dto) throws ValidationException {
        if (dto == null || dto.getName() == null || dto.getName().length() == 0 || dto.getMenge() == null) {
            throw new ValidationException("invalid");
        }

        Count p = new Count();
        p.setName(dto.getName());
        p.setAmount(dto.getMenge());
        p.setTimestamp(LocalDateTime.now());
        p.setEvent(getEvent());

        countRepository.save(p);

        return p;
    }
}
