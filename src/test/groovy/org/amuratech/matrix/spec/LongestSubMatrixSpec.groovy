package org.amuratech.matrix.spec

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.hamcrest.Matchers.is
import static org.junit.Assert.assertNotNull
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class LongestSubMatrixSpec extends Specification {
    private static final String REQUEST_PATH_LONGEST_SUB_MATRIX = "/matrix-calculations/longest-sub-matrix"
    private static final String HEADER_AUTHORIZATION_KEY = "Authorization"

    @Value("\${app.secret-token}")
    private String secretToken

    @Autowired
    MockMvc mockMvc

    def "SCENARIO 1: Input provided for longest sub-matrix calculation should be valid"() {
        def authorization = "Basic ${secretToken}"

        when: "I provide invalid matrices"
        def invalidContentsOperation = mockMvc.perform(
                put(REQUEST_PATH_LONGEST_SUB_MATRIX)
                        .header(HEADER_AUTHORIZATION_KEY, authorization)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(readFileData("request-data/invalid-request-matrix-contents.json"))
        )
        def invalidColumnsOperation = mockMvc.perform(
                put(REQUEST_PATH_LONGEST_SUB_MATRIX)
                        .header(HEADER_AUTHORIZATION_KEY, authorization)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(readFileData("request-data/invalid-request-matrix-columns.json"))
        )
        def invalidEvaluation = mockMvc.perform(
                put(REQUEST_PATH_LONGEST_SUB_MATRIX)
                        .header(HEADER_AUTHORIZATION_KEY, authorization)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(readFileData("request-data/invalid-request-matrix-evaluation.json"))
        )

        then: "Longest sub-matrix should not be calculated"
        invalidContentsOperation
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("details[0]", is("Permitted values for 'matrix': (0 | 1)")))

        invalidColumnsOperation
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("details[0]", is("All 'matrix' columns to have same length")))

        invalidEvaluation
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("details[0]", is("Permitted values for 'evaluationMode': [ZEROES, ONES]")))
    }

    def "SCENARIO 2: Longest sub-matrix calculation should be correctly calculated with all 1s"() {
        def authorization = "Basic ${secretToken}"

        when: "I request to calculate longest sub-matrix with all 1s"
        def matrixOperation = mockMvc.perform(
                put(REQUEST_PATH_LONGEST_SUB_MATRIX)
                        .header(HEADER_AUTHORIZATION_KEY, authorization)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(readFileData("request-data/matrix-evaluation-for-1s.json"))
        )

        then: "Correct sub-matrix dimensions should be returned"
        matrixOperation
                .andExpect(status().isOk())
                .andExpect(jsonPath("row", is(1)))
                .andExpect(jsonPath("column", is(4)))
                .andExpect(jsonPath("width", is(3)))
                .andExpect(jsonPath("height", is(4)))
                .andExpect(jsonPath("area", is(12)))
    }

    def "SCENARIO 3: Longest sub-matrix calculation should be correctly calculated with all 0s"() {
        def authorization = "Basic ${secretToken}"

        when: "I request to calculate longest sub-matrix with all 0s"
        def matrixOperation = mockMvc.perform(
                put(REQUEST_PATH_LONGEST_SUB_MATRIX)
                        .header(HEADER_AUTHORIZATION_KEY, authorization)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(readFileData("request-data/matrix-evaluation-for-0s.json"))
        )

        then: "Correct sub-matrix dimensions should be returned"
        matrixOperation
                .andExpect(status().isOk())
                .andExpect(jsonPath("row", is(3)))
                .andExpect(jsonPath("column", is(0)))
                .andExpect(jsonPath("width", is(7)))
                .andExpect(jsonPath("height", is(2)))
                .andExpect(jsonPath("area", is(14)))
    }

    byte[] readFileData(String jsonFileName) {
        def jsonInput = this.getClass().getClassLoader().getResourceAsStream(jsonFileName)
        assertNotNull(jsonInput)

        byte[] data = new byte[jsonInput.available()]
        jsonInput.read(data)
        jsonInput.close()

        data
    }
}
