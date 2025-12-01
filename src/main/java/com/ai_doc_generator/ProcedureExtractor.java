package com.ai_doc_generator;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class ProcedureExtractor {

	private static final Pattern PROCEDURE_PATTERN = Pattern.compile(
            "(?s)" +                                    		// Dot matches newlines
            "(?:[:=\\-]{2,}.*?[:=\\-]{2,}\\s*)?" +      		// Optional comment header above procedure
            "<<\\s*(.*?)\\s*>>\\s*PROCEDURE" +          		// Capture procedure name
            "([\\s\\S]*?ENDPROC\\s*(?:EJECT)?\\s*(?=$|<<))"     // Capture everything up to ENDPROC
    );

	public static Map<String, String> extractProcedures(String filePath) throws IOException {
        String text = Files.readString(Path.of(filePath));

        // Clean up numeric line numbers at the end of lines
        text = text.replaceAll("\\s+\\d{7,8}(?=\\r?\\n)", "");

        Matcher matcher = PROCEDURE_PATTERN.matcher(text);
        Map<String, String> procedures = new LinkedHashMap<>();

        while (matcher.find()) {
            String name = matcher.group(1).trim();
            String fullText = matcher.group(0).trim();
            procedures.put(name, fullText);
        }
        return procedures;
    }
}

