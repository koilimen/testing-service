package ru.testservice;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.*;
import org.junit.Test;

import java.io.FileInputStream;
import java.util.Iterator;
import java.util.List;

public class WordParserTest {
    private final static String PATH_TO_DOC = "/home/igor/Документы/фриланс/prmbez24/G.2.1.docx";
    private final static String PATH_TO_TABLED = "/home/igor/Документы/фриланс/prmbez24/E.2.1.docx";

    @Test
    public void parse() {
        try {
            FileInputStream fis = new FileInputStream(PATH_TO_DOC);
            XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(fis));
            List<XWPFParagraph> paragraphList = xdoc.getParagraphs();
            for (XWPFParagraph paragraph : paragraphList) {
                if (paragraph.getText().isEmpty()) continue;
                extract(paragraph);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void extract(XWPFParagraph paragraph) {
        char startChar = paragraph.getText().charAt(0);
        XWPFRun isColored = paragraph.getRuns().stream().filter(r -> r.getColor() != null).findFirst().orElse(null);

        if (Character.isDigit(startChar) || (isColored != null && isColored.getColor().equals("00B050"))) {
            System.out.println(paragraph.getText().substring(0, 25) + "... is question text");
        } else {
            XWPFRun isItalic = paragraph.getRuns().stream().filter(XWPFRun::isItalic).findFirst().orElse(null);
            String sout = paragraph.getText().substring(0, 25) + "... is answer text. ";
            if (null != isItalic)
                sout += "And it's correct!";
            System.out.println(sout);

        }
    }


    @Test
    public void parseTables() {
        try {
            FileInputStream fis = new FileInputStream(PATH_TO_TABLED);
            XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(fis));
            Iterator bodyElementIterator = xdoc.getBodyElementsIterator();
            while (bodyElementIterator.hasNext()) {
                IBodyElement element = (IBodyElement) bodyElementIterator.next();

                if ("TABLE".equalsIgnoreCase(element.getElementType().name())) {
                    List<XWPFTable> tableList = element.getBody().getTables();
                    int c = 0;
                    for (XWPFTable table : tableList) {
                        for (int i = 0; i < table.getRows().size(); i++) {
                            c++;
                            if (c > 125) break;
                            XWPFTableRow row = table.getRow(i);
                            XWPFTableCell cell = row.getCell(0);
                            XWPFParagraph paragraph = cell.getParagraphArray(0);
                            if (paragraph.getText().isEmpty()) continue;
                            extract(paragraph);
                        }
                        if (c > 125) break;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
