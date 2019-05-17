package org.amuratech.matrix.service;

import org.amuratech.matrix.datatransfer.LongestSubMatrixResponse;
import org.amuratech.matrix.enums.MatrixEvaluationMode;
import org.amuratech.matrix.exception.InvalidInputException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MatrixCalculationServiceTest {
    private MatrixCalculationService matrixCalculationService;

    private static final int[][] MATRIX_1x2 = {
        {1, 0}
    };

    private static final int[][] MATRIX1_2x2 = {
        {1, 0},
        {0, 1}
    };

    private static final int[][] MATRIX2_2x2 = {
        {1, 0},
        {1, 1}
    };

    private static final int[][] MATRIX_3x3 = {
        { 1, 1, 0 },
        { 1, 1, 1 },
        { 0, 1, 0 }
    };

    private static final int[][] MATRIX_5x4 = {
        { 1, 0, 0, 1 },
        { 1, 1, 1, 0 },
        { 1, 1, 1, 1 },
        { 1, 1, 1, 0 },
        { 1, 1, 1, 1 }
    };

    private static final int[][] MATRIX1_4x6 = {
        { 1, 0, 0, 1, 1, 1 },
        { 0, 0, 0, 1, 1, 0 },
        { 0, 0, 0, 1, 1, 0 },
        { 0, 0, 0, 1, 1, 0 }
    };

    private static final int[][] MATRIX2_4x6 = {
        { 1, 0, 0, 0, 0, 1 },
        { 0, 1, 1, 1, 0, 0 },
        { 0, 1, 1, 1, 0, 0 },
        { 0, 0, 0, 1, 0, 0 }
    };

    private static final int[][] MATRIX3_4x6 = {
        { 1, 0, 0, 0, 0, 1 },
        { 0, 1, 1, 1, 0, 0 },
        { 0, 1, 1, 1, 0, 0 },
        { 0, 0, 0, 1, 0, 0 }
    };

    private static final int[][] MATRIX4_4x6 = {
        { 0, 1, 1, 1, 1, 0 },
        { 1, 0, 0, 0, 1, 1 },
        { 1, 0, 0, 0, 1, 1 },
        { 1, 1, 1, 0, 1, 1 }
    };

    private static final Object[][] MATRICES_WITH_EXPECTED_SUB_MATRIX_DIMENSIONS = {
        { "MATRIX",    "EVALUATION_MODE",           "ROW", "COLUMN", "WIDTH", "HEIGHT" },
        { MATRIX_1x2,  MatrixEvaluationMode.ONES,   0,     0,        1,       1        },
        { MATRIX1_2x2, MatrixEvaluationMode.ONES,   0,     0,        1,       1        },
        { MATRIX2_2x2, MatrixEvaluationMode.ONES,   0,     0,        1,       2        },
        { MATRIX2_2x2, MatrixEvaluationMode.ZEROES, 0,     1,        1,       1        },
        { MATRIX_3x3,  MatrixEvaluationMode.ONES,   0,     0,        2,       2        },
        { MATRIX_5x4,  MatrixEvaluationMode.ONES,   1,     0,        3,       4        },
        { MATRIX1_4x6, MatrixEvaluationMode.ONES,   0,     3,        2,       4        },
        { MATRIX2_4x6, MatrixEvaluationMode.ONES,   1,     1,        3,       2        },
        { MATRIX3_4x6, MatrixEvaluationMode.ZEROES, 1,     4,        2,       3        },
        { MATRIX4_4x6, MatrixEvaluationMode.ONES,   1,     4,        2,       3        }
    };

    @Before
    public void setUp() {
        matrixCalculationService = new MatrixCalculationService();
    }

    @Test(expected = InvalidInputException.class)
    public void shouldNotCalculateLongestSubMatrixWhenContentsAreInvalid() {
        int[][] matrix = {
            { 1, 0, 5 },
            { 0, 1, 1 }
        };

        matrixCalculationService.findLongestSubMatrix(matrix, MatrixEvaluationMode.ONES);
    }

    @Test(expected = InvalidInputException.class)
    public void shouldNotCalculateLongestSubMatrixWhenColumnLengthIsUneven() {
        int[][] matrix = {
            { 1, 0, 1 },
            { 0, 1, 1, 1 }
        };

        matrixCalculationService.findLongestSubMatrix(matrix, MatrixEvaluationMode.ONES);
    }

    @Test
    public void shouldFindLongestSubMatrixForVariousScenarios() {
        final Object[][] matrices = MATRICES_WITH_EXPECTED_SUB_MATRIX_DIMENSIONS;
        for (int i = 1; i < matrices.length; i++) {
            int[][] matrix = (int[][]) matrices[i][0];
            MatrixEvaluationMode evaluation = (MatrixEvaluationMode) matrices[i][1];
            int row = (int) matrices[i][2];
            int column = (int) matrices[i][3];
            int width = (int) matrices[i][4];
            int height = (int) matrices[i][5];

            LongestSubMatrixResponse subMatrix = matrixCalculationService.findLongestSubMatrix(matrix, evaluation);
            assertSubMatrixDimensions("SCENARIO " + i, subMatrix, row, column, width, height);
        }
    }

    private void assertSubMatrixDimensions(String scenario, LongestSubMatrixResponse subMatrix, int row, int column, int width, int height) {
        assertEquals(scenario + " FAILED in matching row", row, subMatrix.getRow());
        assertEquals(scenario + " FAILED in matching column", column, subMatrix.getColumn());
        assertEquals(scenario + " FAILED in matching width", width, subMatrix.getWidth());
        assertEquals(scenario + " FAILED in matching height", height, subMatrix.getHeight());
    }
}
