package ro.adela.bank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

//@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TotalInterestByDayDto {
//    @Column(name = "starDate")
    @XmlElement(name = "starDate")
    @JsonProperty("starDate")
    private LocalDate starDate;

//    @Column(name = "endDate")
    @XmlElement(name = "endDate")
    @JsonProperty("endDate")
    private LocalDate endDate;

//    @Column(name = "totalInterest")
    @XmlElement(name = "totalInterest")
    @JsonProperty("totalInterest")
    private double totalInterest;

//    @Id
    @XmlElement(name = "accountNumber")
    @JsonProperty("accountNumber")
    private Integer accountNumber;
}
