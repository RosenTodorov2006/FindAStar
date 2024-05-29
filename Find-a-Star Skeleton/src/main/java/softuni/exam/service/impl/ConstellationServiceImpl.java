package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ConstellationSeedDto;
import softuni.exam.models.entity.Constellation;
import softuni.exam.repository.ConstellationRepository;
import softuni.exam.service.ConstellationService;
import softuni.exam.util.ValidatorInterface;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class ConstellationServiceImpl implements ConstellationService {
    private final static String FILE_PATH = "src/main/resources/files/json/constellations.json";
    private final ConstellationRepository constellationRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidatorInterface validator;

    public ConstellationServiceImpl(ConstellationRepository constellationRepository, ModelMapper modelMapper, Gson gson, ValidatorInterface validator) {
        this.constellationRepository = constellationRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validator = validator;
    }

    @Override
    public boolean areImported() {
        return this.constellationRepository.count() > 0;
    }

    @Override
    public String readConstellationsFromFile() throws IOException {
        return new String(Files.readAllBytes(Path.of(FILE_PATH)));
    }

    @Override
    public String importConstellations() throws IOException {
        StringBuilder sb = new StringBuilder();
        ConstellationSeedDto[] constellationSeedDtos = this.gson.fromJson(readConstellationsFromFile(), ConstellationSeedDto[].class);
        for (ConstellationSeedDto constellation : constellationSeedDtos) {
            Optional<Constellation> optional = this.constellationRepository.findByName(constellation.getName());
            if(!validator.isValid(constellation) || optional.isPresent()){
                sb.append("Invalid constellation\n");
                continue;
            }
            Constellation map = this.modelMapper.map(constellation, Constellation.class);
            this.constellationRepository.saveAndFlush(map);
            sb.append(String.format("Successfully imported constellation %s - %s\n", map.getName(), map.getDescription()));
        }
        return sb.toString();
    }
}
