package com.natujenge.thecouch.web.rest.dto;

import com.natujenge.thecouch.domain.SessionSchedules;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor

public class ListResponse {
    private List<?> data;
    private int totalPages;
    private int totalItemsInPage;
    private long totalElements;

    public ListResponse(SessionSchedules sessionSchedules) {

    }

    public void setPage(int page) {
    }

    public void setPerPage(int perPage) {
    }
}
