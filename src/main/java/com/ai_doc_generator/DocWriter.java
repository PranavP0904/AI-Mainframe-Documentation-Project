package com.ai_doc_generator;

import org.apache.poi.xwpf.usermodel.*;
import java.io.*;
import java.util.Map;

public class DocWriter {

    public static void writeToDocx(Map<String, String> docs, String outputPath) throws IOException {
        XWPFDocument document = new XWPFDocument();

        for (Map.Entry<String, String> entry : docs.entrySet()) {
            // Add procedure name as a bold heading
            XWPFParagraph title = document.createParagraph();
            title.setStyle("Heading1");
            XWPFRun runTitle = title.createRun();
            runTitle.setBold(true);
            runTitle.setFontSize(14);
            runTitle.setText("Procedure: <<" + entry.getKey() + ">>");

            // Add AI-generated description & business rules
            XWPFParagraph body = document.createParagraph();
            XWPFRun runBody = body.createRun();
            runBody.setFontSize(12);
            runBody.setText(entry.getValue());
            runBody.addCarriageReturn();

            // Add a blank line
            document.createParagraph();
        }

        try (FileOutputStream out = new FileOutputStream(outputPath)) {
            document.write(out);
        }
        document.close();
    }
}

