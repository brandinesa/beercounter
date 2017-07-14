package inc.alc.beercounter.dao;

import inc.alc.beercounter.domain.Count;

import java.util.List;

public interface CountRepository {
    List<Count> findAll();
    List<Count> findByEvent(String event);
    Count save(Count count);
}
