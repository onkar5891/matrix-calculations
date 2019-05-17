package org.amuratech.matrix.service;

import lombok.ToString;
import org.amuratech.matrix.datatransfer.LongestSubMatrixResponse;
import org.amuratech.matrix.enums.MatrixEvaluationMode;
import org.amuratech.matrix.exception.InvalidInputException;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

@Service
public class MatrixCalculationService {
    public LongestSubMatrixResponse findLongestSubMatrix(int[][] matrix, MatrixEvaluationMode evaluationMode) {
        doValidate(matrix);

        // if needed to evaluate sub-matrix for 0s, make 1s as 0s and vice versa
        invertIfRequired(matrix, evaluationMode);

        MaxAreaOfMatrix areaOfMatrix = findMaxAreaCoveredByMatrix(matrix);
        int height = areaOfMatrix.area / areaOfMatrix.width;
        int column = areaOfMatrix.columnEnd - areaOfMatrix.width + 1;
        int row = areaOfMatrix.row - height + 1;

        // invert the matrix again to get back the original one
        invertIfRequired(matrix, evaluationMode);

        return
                LongestSubMatrixResponse
                        .builder()
                        .row(row)
                        .column(column)
                        .width(areaOfMatrix.width)
                        .height(height)
                        .area(areaOfMatrix.area)
                        .build();
    }

    private void invertIfRequired(int[][] matrix, MatrixEvaluationMode evaluationMode) {
        if (evaluationMode == MatrixEvaluationMode.ZEROES) {
            invert(matrix);
        }
    }

    private void invert(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = Math.abs(matrix[i][j] - 1);
            }
        }
    }

    private MaxAreaOfMatrix findMaxAreaCoveredByMatrix(int[][] matrix) {
        for (int i = 1; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 1) {
                    matrix[i][j] += matrix[i - 1][j];
                }
            }
        }

        MaxAreaOfMatrix maxAreaOfMatrix = new MaxAreaOfMatrix();
        for (int i = 0; i < matrix.length; i++) {
            MaxAreaOfMatrixRow maxAreaOfMatrixRow = findMaxAreaCoveredByMatrixRow(matrix[i]);
            if (maxAreaOfMatrix.area < maxAreaOfMatrixRow.area) {
                maxAreaOfMatrix.area = maxAreaOfMatrixRow.area;
                maxAreaOfMatrix.row = i;
                maxAreaOfMatrix.columnEnd = maxAreaOfMatrixRow.columnEnd;
                maxAreaOfMatrix.width = maxAreaOfMatrixRow.width;
            }
        }

        for (int i = matrix.length - 1; i > 0; i--) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] != 0) {
                    matrix[i][j] -= matrix[i - 1][j];
                }
            }
        }

        return maxAreaOfMatrix;
    }

    private MaxAreaOfMatrixRow findMaxAreaCoveredByMatrixRow(int[] histogram) {
        Deque<Integer> stack = new ArrayDeque<>(histogram.length);
        MaxAreaOfMatrixRow maxAreaOfMatrixRow = new MaxAreaOfMatrixRow();
        int i = 0;

        while (i < histogram.length) {
            if (stack.isEmpty() || histogram[i] >= histogram[stack.peek()]) {
                stack.push(i++);
            } else {
                while (!stack.isEmpty() && histogram[i] < histogram[stack.peek()]) {
                    updateArea(maxAreaOfMatrixRow, histogram, stack, i);
                }
            }
        }

        while (!stack.isEmpty()) {
            updateArea(maxAreaOfMatrixRow, histogram, stack, i);
        }

        return maxAreaOfMatrixRow;
    }

    private void updateArea(MaxAreaOfMatrixRow maxAreaOfMatrixRow, int[] histogram, Deque<Integer> stack, int i) {
        int topIndex = stack.pop();
        int curArea = calculateAreaCoveredByStackTop(histogram, stack, i, topIndex);

        if (maxAreaOfMatrixRow.area < curArea) {
            maxAreaOfMatrixRow.area = curArea;
            maxAreaOfMatrixRow.columnEnd = i - 1;

            if (stack.isEmpty()) {
                // as stack is empty, all the values are more than that of histogram[topIndex], so the width should be i
                maxAreaOfMatrixRow.width = i;
            } else {
                // difference between i and stack top (not including i) indicates width of the rectangle
                maxAreaOfMatrixRow.width = i - stack.peek() - 1;
            }
        }
    }

    private int calculateAreaCoveredByStackTop(int[] histogram, Deque<Integer> stack, int i, int topIndex) {
        if (stack.isEmpty()) {
            return histogram[topIndex] * i;
        }
        return histogram[topIndex] * (i - stack.peek() - 1);
    }

    private void doValidate(int[][] matrix) {
        boolean columnLengthValid = Arrays.stream(matrix).map(sub -> sub.length).distinct().count() == 1L;
        if (!columnLengthValid) {
            throw new InvalidInputException("All 'matrix' columns to have same length");
        }

        boolean contentsValid = Arrays.stream(matrix).flatMap(sub -> Arrays.stream(sub).boxed()).allMatch(i -> i == 0 || i == 1);
        if (!contentsValid) {
            throw new InvalidInputException("Permitted values for 'matrix': (0 | 1)");
        }
    }

    @ToString
    private class MaxAreaOfMatrixRow {
        private int area = -1;
        private int columnEnd = -1;
        private int width = -1;
    }

    private class MaxAreaOfMatrix {
        private int area = -1;
        private int row = -1;
        private int columnEnd = -1;
        private int width = -1;
    }
}
