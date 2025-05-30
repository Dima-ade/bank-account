package ro.adela.bank.readobject;

import java.time.LocalDate;

public class GetInterestRatesByPageReadObject {

    private Integer pageNumber;

    private Integer pageSize;

    //Default constructor
    public GetInterestRatesByPageReadObject() {
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
