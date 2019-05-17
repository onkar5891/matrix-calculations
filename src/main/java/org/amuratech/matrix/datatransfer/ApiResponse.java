package org.amuratech.matrix.datatransfer;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ApiResponse {
    private String message;
    private List<String> details;
}
