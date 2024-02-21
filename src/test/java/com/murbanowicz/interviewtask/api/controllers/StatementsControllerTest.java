package com.murbanowicz.interviewtask.api.controllers;

import com.murbanowicz.interviewtask.api.dto.response.ParentStatementResponse;
import com.murbanowicz.interviewtask.api.dto.response.SchoolStatementResponse;
import com.murbanowicz.interviewtask.exception.ResourceNotFoundException;
import com.murbanowicz.interviewtask.service.StatementsService;
import io.restassured.http.ContentType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.Map;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StatementsControllerTest {

    @MockBean
    private StatementsService statementsService;
    @LocalServerPort
    private Integer port;
    private final String HOST = "http://localhost:";

    @ParameterizedTest
    @MethodSource("getSchoolMonthlyStatement_contractTestArguments")
    void getSchoolMonthlyStatement_contractTest(int expectedStatusCode, long schoolId, Map<String, String> paramMap) {
        when(statementsService.getStatementForSchool(any(), anyInt(), anyInt()))
                .thenReturn(SchoolStatementResponse.builder().build());

        given()
                .contentType(ContentType.JSON)
                .when()
                .pathParam("schoolId", schoolId)
                .queryParams(paramMap)
                .get(buildUrl(ApiEndpoint.GET_STATEMENT_SCHOOL))
                .then()
                .statusCode(expectedStatusCode);
    }

    private static Stream<Arguments> getSchoolMonthlyStatement_contractTestArguments() {
        return Stream.of(
                Arguments.of(200, 1, Map.of("year", "2024", "month", "2")),
                Arguments.of(400, 1, Map.of("year", "2024", "month", "0")),
                Arguments.of(400, 1, Map.of("year", "2024", "month", "13")),
                Arguments.of(400, 1, Map.of("year", "2024", "month", "abc")),
                Arguments.of(400, 1, Map.of("year", "2024")),
                Arguments.of(400, 1, Map.of("month", "2")),
                Arguments.of(400, 1, Map.of())
        );
    }

    @Test
    void getSchoolMonthlyStatement_exceptionHandlerTest() {
        when(statementsService.getStatementForSchool(any(), anyInt(), anyInt()))
                .thenThrow(new ResourceNotFoundException(""));

        given()
                .contentType(ContentType.JSON)
                .when()
                .pathParam("schoolId", 1)
                .queryParams(Map.of("year", "2024", "month", "2"))
                .get(buildUrl(ApiEndpoint.GET_STATEMENT_SCHOOL))
                .then()
                .statusCode(404);
    }

    @ParameterizedTest
    @MethodSource("getParentMonthlyStatement_contractTestArguments")
    void getParentMonthlyStatement_contractTest(int expectedStatusCode, long schoolId, long parentId, Map<String, String> paramMap) {
        when(statementsService.getStatementForParent(any(), any(), anyInt(), anyInt()))
                .thenReturn(ParentStatementResponse.builder().build());

        given()
                .contentType(ContentType.JSON)
                .when()
                .pathParam("schoolId", schoolId)
                .pathParam("parentId", parentId)
                .queryParams(paramMap)
                .get(buildUrl(ApiEndpoint.GET_STATEMENT_PARENT))
                .then()
                .statusCode(expectedStatusCode);
    }

    private static Stream<Arguments> getParentMonthlyStatement_contractTestArguments() {
        return Stream.of(
                Arguments.of(200, 1, 1, Map.of("year", "2024", "month", "2")),
                Arguments.of(400, 1, 1, Map.of("year", "2024", "month", "0")),
                Arguments.of(400, 1, 1, Map.of("year", "2024", "month", "13")),
                Arguments.of(400, 1, 1, Map.of("year", "2024", "month", "abc")),
                Arguments.of(400, 1, 1, Map.of("year", "2024")),
                Arguments.of(400, 1, 1, Map.of("month", "2")),
                Arguments.of(400, 1, 1, Map.of())
        );
    }

    @Test
    void getParentMonthlyStatement_exceptionHandlerTest() {
        when(statementsService.getStatementForParent(any(), any(), anyInt(), anyInt()))
                .thenThrow(new ResourceNotFoundException(""));

        given()
                .contentType(ContentType.JSON)
                .when()
                .pathParam("schoolId", 1)
                .pathParam("parentId", 1)
                .queryParams(Map.of("year", "2024", "month", "2"))
                .get(buildUrl(ApiEndpoint.GET_STATEMENT_PARENT))
                .then()
                .statusCode(404);
    }

    @RequiredArgsConstructor
    @Getter
    private enum ApiEndpoint {
        GET_STATEMENT_SCHOOL("/api/statements/school/{schoolId}"),
        GET_STATEMENT_PARENT("/api/statements/school/{schoolId}/parent/{parentId}");

        private final String path;
    }

    private String buildUrl(ApiEndpoint apiEndpoint) {
        return HOST + port + apiEndpoint.getPath();
    }
}