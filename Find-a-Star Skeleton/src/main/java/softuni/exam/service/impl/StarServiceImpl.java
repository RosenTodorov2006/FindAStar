package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.apache.logging.log4j.util.StringBuilders;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.StarSeedDto;
import softuni.exam.models.entity.Star;
import softuni.exam.models.entity.StarTypes;
import softuni.exam.repository.ConstellationRepository;
import softuni.exam.repository.StarRepository;
import softuni.exam.service.StarService;
import softuni.exam.util.ValidatorInterface;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StarServiceImpl implements StarService {
    private final static String FILE_PATH = "src/main/resources/files/json/stars.json";
    private final StarRepository starRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidatorInterface validator;
    private final ConstellationRepository constellationRepository;

    public StarServiceImpl(StarRepository starRepository, ModelMapper modelMapper, Gson gson, ValidatorInterface validator, ConstellationRepository constellationRepository) {
        this.starRepository = starRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validator = validator;
        this.constellationRepository = constellationRepository;
    }

    @Override
    public boolean areImported() {
        return this.starRepository.count() > 0;
    }

    @Override
    public String readStarsFileContent() throws IOException {
        return new String(Files.readAllBytes(Path.of(FILE_PATH)));
    }

    @Override
    public String importStars() throws IOException {
        StringBuilder sb = new StringBuilder();
        StarSeedDto[] starSeedDtos = this.gson.fromJson(readStarsFileContent(), StarSeedDto[].class);
        for (StarSeedDto starSeedDto : starSeedDtos) {
            Optional<Star> optional = this.starRepository.findByName(starSeedDto.getName());
            if(!validator.isValid(starSeedDto) || optional.isPresent()){
                sb.append("Invalid star\n");
                continue;
            }
            Star map = this.modelMapper.map(starSeedDto, Star.class);
            map.setStarType(StarTypes.valueOf(starSeedDto.getStarType()));
            map.setConstellation(constellationRepository.findById(starSeedDto.getConstellation()).get());
            this.starRepository.saveAndFlush(map);
            sb.append(String.format("Successfully imported star %s - %.2f light years\n", map.getName(), map.getLightYears()));
        }
        return sb.toString();
    }

    @Override
    public String exportStars() {
        return this.starRepository.findAllByStarTypeAndAstronomers(StarTypes.RED_GIANT)
                .stream()
                .map(s->{
                    return String.format("Star: %s\n" +
                            "   *Distance: %.2f light years\n" +
                            "   **Description: %s\n" +
                            "   ***Constellation: %s\n", s.getName(), s.getLightYears(), s.getDescription(), s.getConstellation().getName());
                }).collect(Collectors.joining());
    }
}
