package com.sogeti.java.anonymous_artist.handler;

import com.sogeti.java.anonymous_artist.exception.handler.CustomAccessDeniedHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomAccessDeniedHandlerTest {

    @InjectMocks
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Mock
    private HttpServletResponse response;

    private StringWriter responseOutput;
    private PrintWriter writer;

    @BeforeEach
    void setUp() throws IOException {
        responseOutput = new StringWriter();
        writer = new PrintWriter(responseOutput);
        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    void givenRequestResponseAndException_whenHandled_thenReturn403AndCorrectResponseMessage() throws IOException {
        // Given
        HttpServletRequest request = mock(HttpServletRequest.class);
        AccessDeniedException accessDeniedException = new AccessDeniedException("");
        String expectedResponseMessage = "Oh oh! It seems that you don't have the rights to perform this action. Please contact an administrator." + "\ntimestamp: " + LocalDateTime.now().truncatedTo(java.time.temporal.ChronoUnit.SECONDS);

        // When
        customAccessDeniedHandler.handle(request, response, accessDeniedException);
        writer.flush();

        // Then
        verify(response).setStatus(403);
        assertEquals(expectedResponseMessage, responseOutput.toString());
    }
}
