package readobject;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class AddInterestRateReadObject {

    // Attribute for interest rate
    private double interestRate;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate activationDate;

    //Default constructor
}
