package org.amuratech.matrix.datatransfer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LongestSubMatrixResponse {
    private int row;
    private int column;
    private int width;
    private int height;
    private int area;
}
