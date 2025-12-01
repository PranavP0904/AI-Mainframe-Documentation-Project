package com.ai_doc_generator;


import java.util.*;

public class Main {

	public static void main(String[] args) {
		try {
			// Input / Output setup
			String inputFile = "FTpt-small.txt";                 // IDEAL source file
			String outputFile = "FTpt-Small-Documentation.docx"; // Output Word file

			System.out.println("Reading file: " + inputFile);

			// Extract procedures from the input text
			Map<String, String> procedures = ProcedureExtractor.extractProcedures(inputFile);
			if (procedures.isEmpty()) {
				System.out.println("No procedures found in " + inputFile);
				return;
			}
			System.out.println("Found " + procedures.size() + " procedure(s).");

			// Initialize AI client
			AIClient ai = new AIClient();

			// Generate documentation
			Map<String, String> generatedDocs = new LinkedHashMap<>();
			int total = procedures.size();
			int count = 1;

			for (Map.Entry<String, String> entry : procedures.entrySet()) {
				String procName = entry.getKey();
				String procBody = entry.getValue();

				System.out.println("Generating documentation for [" + count + "/" + total + "]: " + procName);

				try {
					String doc = ai.generateDocumentation(procName, procBody);
					generatedDocs.put(procName, doc);
				} catch (Exception e) {
					System.err.println("Error generating doc for " + procName + ": " + e.getMessage());
					e.printStackTrace();
				}

				count++;
				Thread.sleep(1000); // small delay between API calls
			}

			// Write everything to a Word document
			System.out.println("Writing documentation to " + outputFile + " ...");
			DocWriter.writeToDocx(generatedDocs, outputFile);

			System.out.println("\nDocumentation successfully generated!");
			System.out.println("Output file: " + outputFile);

		} catch (Exception e) {
			System.err.println("Fatal error: " + e.getMessage());
			e.printStackTrace();
		}
	}
}

