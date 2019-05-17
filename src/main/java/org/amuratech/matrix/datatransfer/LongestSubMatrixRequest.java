package org.amuratech.matrix.datatransfer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LongestSubMatrixRequest {
    @NotNull
    private int[][] matrix;
    @NotNull
    private String evaluationMode;
}
