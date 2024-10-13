package com.nhnacademy.miniDooray.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProjectPageResponse {
    private List<ProjectDto> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}