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
public class CreateAccountReadObject {
    // Attribute for account number
    private Integer accountNumber;
    // Attribute for account holder's name
    private String accountHolderName;
    // Attribute for account balance
    private double balance;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birtDate;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate startDate;
}
