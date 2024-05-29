package softuni.exam.models.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "stars")
public class Star extends BaseEntity{
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
    @Column(name = "light_years", nullable = false)
    private double lightYears;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(name = "star_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private StarTypes starType;
    @ManyToOne
    @JoinColumn(name = "constellation_id", referencedColumnName = "id")
    private Constellation constellation;
    @OneToMany(mappedBy = "star")
    private List<Astronomer> astronomers;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLightYears() {
        return lightYears;
    }

    public void setLightYears(double lightYears) {
        this.lightYears = lightYears;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StarTypes getStarType() {
        return starType;
    }

    public void setStarType(StarTypes starType) {
        this.starType = starType;
    }

    public Constellation getConstellation() {
        return constellation;
    }

    public void setConstellation(Constellation constellation) {
        this.constellation = constellation;
    }

    public List<Astronomer> getAstronomers() {
        return astronomers;
    }

    public void setAstronomers(List<Astronomer> astronomers) {
        this.astronomers = astronomers;
    }
}
