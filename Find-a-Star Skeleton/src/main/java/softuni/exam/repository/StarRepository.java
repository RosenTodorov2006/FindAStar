package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Star;
import softuni.exam.models.entity.StarTypes;

import java.util.List;
import java.util.Optional;

@Repository
public interface StarRepository extends JpaRepository<Star, Long> {

    Optional<Star> findByName(String name);
    @Query("FROM Star s WHERE SIZE(s.astronomers) = 0 AND s.starType = :starType ORDER BY s.lightYears")
    List<Star> findAllByStarTypeAndAstronomers(StarTypes starType);
}
