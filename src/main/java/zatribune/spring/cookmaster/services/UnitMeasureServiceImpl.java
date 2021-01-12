package zatribune.spring.cookmaster.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zatribune.spring.cookmaster.data.entities.UnitMeasure;
import zatribune.spring.cookmaster.data.repositories.UnitMeasureRepository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UnitMeasureServiceImpl implements UnitMeasureService {

    private final UnitMeasureRepository repository;

    @Autowired
    public UnitMeasureServiceImpl(UnitMeasureRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<UnitMeasure> getUnitMeasureById(Long id) {
        return repository.findById(id);
    }

    @Override
    public UnitMeasure saveOrUpdate(UnitMeasure unitMeasure) {
        return repository.save(unitMeasure);
    }

    @Override
    public void delete(UnitMeasure unitMeasure) {
        repository.delete(unitMeasure);
    }

    @Override
    public void deleteById(Long id) {
       repository.deleteById(id);
    }

    @Override
    public Set<UnitMeasure> getAllUnitMeasures() {
        return StreamSupport.stream(repository.findAll().spliterator(),false)
                .collect(Collectors.toSet());
    }
}
