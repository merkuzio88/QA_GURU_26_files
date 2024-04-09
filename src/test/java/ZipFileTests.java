import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ZipFileTests {

    private final ClassLoader cl = ZipFileTests.class.getClassLoader();

    @Test
    void zipFileParsingTest() throws Exception {
        try (ZipInputStream zis = new ZipInputStream(
                requireNonNull(cl.getResourceAsStream("files.zip"))
        )) {
            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {
                processZipEntry(entry, zis);
            }
        }
    }

    private void processZipEntry(ZipEntry entry, ZipInputStream zis) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = zis.read(buffer)) > 0) {
            bos.write(buffer, 0, len);
        }

        if (entry.getName().toLowerCase().endsWith(".pdf")) {
            processPdfContent(bos.toByteArray());
        } else if (entry.getName().toLowerCase().endsWith(".csv")) {
            processCsvContent(bos.toByteArray());
        } else if (entry.getName().toLowerCase().endsWith(".xlsx")) {
            processXlsxContent(bos.toByteArray());
        }

        bos.close();
        zis.closeEntry();
    }

    private void processPdfContent(byte[] pdfBytes) throws Exception {
        String text = new PDF(new ByteArrayInputStream(pdfBytes)).text;
        Assertions.assertTrue(text.contains("Lorem ipsum dolor sit amet"));
    }

    private void processCsvContent(byte[] csvBytes) throws Exception {
        try (InputStream is = new ByteArrayInputStream(csvBytes);
             CSVReader reader = new CSVReader(new InputStreamReader(is))) {
            List<String[]> data = reader.readAll();
            assertEquals(3, data.size());
            assertArrayEquals(new String[]{"en", "International business, born in Siberia"}, data.get(1));
        }
    }

    private void processXlsxContent(byte[] xlsxBytes) throws Exception {
        XLS xls = new XLS(new ByteArrayInputStream(xlsxBytes));
        Sheet sheet = xls.excel.getSheetAt(0);
        Assertions.assertEquals("January", sheet.getRow(1).getCell(1).getStringCellValue());
    }
}
