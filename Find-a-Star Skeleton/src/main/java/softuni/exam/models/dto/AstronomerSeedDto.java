package softuni.exam.models.dto;

import softuni.exam.util.LocalDateAdaptor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AstronomerSeedDto {
    @XmlElement(name = "average_observation_hours")
    @DecimalMin(value = "500")
    private double averageObservationHours;
    @XmlElement
    @XmlJavaTypeAdapter(LocalDateAdaptor.class)
    private LocalDate birthday;
    @XmlElement(name = "first_name")
    @Size(min = 2, max = 30)
    private String firstName;
    @XmlElement(name = "last_name")
    @Size(min = 2, max = 30)
    private String lastName;
    @XmlElement
    @DecimalMin(value = "15000")
    private double salary;
    @XmlElement(name = "observing_star_id")
    private long observingStarId;

    public double getAverageObservationHours() {
        return averageObservationHours;
    }

    public void setAverageObservationHours(double averageObservationHours) {
        this.averageObservationHours = averageObservationHours;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public long getObservingStarId() {
        return observingStarId;
    }

    public void setObservingStarId(long observingStarId) {
        this.observingStarId = observingStarId;
    }
}
