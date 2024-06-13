package com.sogeti.java.anonymous_artist.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class JacksonConfigTest {
    @Test
    void givenJacksonConfig_when_CreatingACustomModule_thenANewJacksonModuleIsRegistered() {
        // Given
        JacksonConfig jacksonConfig = new JacksonConfig();

        // When
        ObjectMapper objectMapper = jacksonConfig.objectMapper();

        // Then
        assertNotNull(objectMapper);
        assertTrue(objectMapper.findModules().stream().anyMatch(module -> module instanceof JavaTimeModule));
    }

}
