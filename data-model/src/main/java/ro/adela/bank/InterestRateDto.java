package ro.adela.bank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.*;
import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "interest_rate")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@XmlRootElement(name="interest")
@XmlAccessorType(XmlAccessType.FIELD)
public class InterestRateDto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    @JsonIgnore
    @XmlTransient
    private Integer id;

    // Attribute for interest rate
    @Column(name = "interestrate")
    @XmlElement(name = "interestRate")
    @JsonProperty("interestRate")
    private double interestRate;

    @Column(name = "activationdate")
    @XmlElement(name = "activationDate")
    @JsonProperty("activationDate")
    private LocalDate activationDate;

    public InterestRateDto(double interestRate, LocalDate activationDate) {
        this.interestRate = interestRate;
        this.activationDate = activationDate;
    }
}
