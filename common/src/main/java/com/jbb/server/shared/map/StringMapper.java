package com.jbb.server.shared.map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbb.domain.LoanPlatformPolicy;
import com.jbb.server.common.exception.ExecutionException;

/**
 * Object to String and String to object mapping wrapper class This implementation utilises Jackson ObjectMapper.
 */
public class StringMapper {
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static LoanPlatformPolicy readPolicy(String str) throws Exception {
        return mapper.readValue(str, LoanPlatformPolicy.class);
    }

    public static String toString(LoanPlatformPolicy input) {
        try {
            return mapper.writeValueAsString(input);
        } catch (Exception e) {
            throw new ExecutionException(e);
        }
    }
}
