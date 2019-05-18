package org.amuratech.matrix.controller;

import org.amuratech.matrix.datatransfer.ApiResponse;
import org.amuratech.matrix.datatransfer.LongestSubMatrixRequest;
import org.amuratech.matrix.datatransfer.LongestSubMatrixResponse;
import org.amuratech.matrix.enums.MatrixEvaluationMode;
import org.amuratech.matrix.exception.InvalidInputException;
import org.amuratech.matrix.service.MatrixCalculationService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/matrix-calculations")
public class MatrixCalculationController {
    private final MatrixCalculationService matrixCalculationService;

    public MatrixCalculationController(MatrixCalculationService matrixCalculationService) {
        this.matrixCalculationService = matrixCalculationService;
    }

    @PutMapping(value = "/longest-sub-matrix", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity findLongestSubMatrix(@Valid @RequestBody LongestSubMatrixRequest subMatrixRequest) {
        MatrixEvaluationMode evaluation = findEvaluationMode(subMatrixRequest.getEvaluationMode());
        Optional<LongestSubMatrixResponse> longestSubMatrix = matrixCalculationService.findLongestSubMatrix(subMatrixRequest.getMatrix(), evaluation);

        if (longestSubMatrix.isPresent()) {
            return ResponseEntity.ok(longestSubMatrix);
        }

        return ResponseEntity.ok(new ApiResponse("No longest sub-matrix exists", Collections.emptyList()));
    }

    private MatrixEvaluationMode findEvaluationMode(String evaluationMode) {
        try {
            return MatrixEvaluationMode.valueOf(evaluationMode);
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException("Permitted values for 'evaluationMode': " + Arrays.toString(MatrixEvaluationMode.values()));
        }
    }
}
