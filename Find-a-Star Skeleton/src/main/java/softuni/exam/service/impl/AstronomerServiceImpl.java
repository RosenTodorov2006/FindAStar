package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.AstronomerSeedDto;
import softuni.exam.models.dto.AstronomersRootSeedDto;
import softuni.exam.models.entity.Astronomer;
import softuni.exam.models.entity.Star;
import softuni.exam.repository.AstronomerRepository;
import softuni.exam.repository.StarRepository;
import softuni.exam.service.AstronomerService;
import softuni.exam.util.ValidatorInterface;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
public class AstronomerServiceImpl implements AstronomerService {
    private final static String FILE_PATH = "src/main/resources/files/xml/astronomers.xml";
    private final AstronomerRepository astronomerRepository;
    private final ModelMapper modelMapper;
    private final ValidatorInterface validator;
    private final StarRepository starRepository;

    public AstronomerServiceImpl(AstronomerRepository astronomerRepository, ModelMapper modelMapper, ValidatorInterface validator, StarRepository starRepository) {
        this.astronomerRepository = astronomerRepository;
        this.modelMapper = modelMapper;
        this.validator = validator;
        this.starRepository = starRepository;
    }

    @Override
    public boolean areImported() {
        return this.astronomerRepository.count() > 0;
    }

    @Override
    public String readAstronomersFromFile() throws IOException {
        return new String(Files.readAllBytes(Path.of(FILE_PATH)));
    }

    @Override
    public String importAstronomers() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();
        JAXBContext jaxbContext = JAXBContext.newInstance(AstronomersRootSeedDto.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        AstronomersRootSeedDto unmarshal = (AstronomersRootSeedDto) unmarshaller.unmarshal(new File(FILE_PATH));
        List<AstronomerSeedDto> astronomerSeedDtos = unmarshal.getAstronomerSeedDtos();
        for (AstronomerSeedDto astronomerSeedDto : astronomerSeedDtos) {
            Optional<Astronomer> optional = this.astronomerRepository.findByFirstNameAndLastName(astronomerSeedDto.getFirstName(),astronomerSeedDto.getLastName());
            Optional<Star> optionalStar = this.starRepository.findById(astronomerSeedDto.getObservingStarId());
            if(!validator.isValid(astronomerSeedDto) || optional.isPresent() || optionalStar.isEmpty()){
                sb.append("Invalid astronomer\n");
                continue;
            }
            Astronomer map = this.modelMapper.map(astronomerSeedDto, Astronomer.class);
            map.setStar(optionalStar.get());
            this.astronomerRepository.saveAndFlush(map);
            sb.append(String.format("Successfully imported astronomer %s %s - %.2f\n", map.getFirstName(), map.getLastName(), map.getAverageObservationHours()));
        }
        return sb.toString();
    }
}
