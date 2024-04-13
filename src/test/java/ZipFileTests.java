import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.*;

public class ZipFileTests {

    private final ClassLoader cl = ZipFileTests.class.getClassLoader();

    @Test
    void testPdfFilesInZip() throws Exception {
        try (ZipInputStream zis = new ZipInputStream(
                requireNonNull(cl.getResourceAsStream("files.zip"))
        )) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().toLowerCase().endsWith(".pdf")) {
                    processPdfContent(zis);
                }
            }
        }
    }

    @Test
    void testCsvFilesInZip() throws Exception {
        try (ZipInputStream zis = new ZipInputStream(
                requireNonNull(cl.getResourceAsStream("files.zip"))
        )) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().toLowerCase().endsWith(".csv")) {
                    processCsvContent(zis);
                }
            }
        }
    }

    @Test
    void testXlsxFilesInZip() throws Exception {
        try (ZipInputStream zis = new ZipInputStream(
                requireNonNull(cl.getResourceAsStream("files.zip"))
        )) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().toLowerCase().endsWith(".xlsx")) {
                    processXlsxContent(zis);
                }
            }
        }
    }

    private void processPdfContent(ZipInputStream zis) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = zis.read(buffer)) > 0) {
            bos.write(buffer, 0, len);
        }
        String text = new PDF(new ByteArrayInputStream(bos.toByteArray())).text;
        assertTrue(text.contains("Lorem ipsum dolor sit amet"));
        bos.close();
        zis.closeEntry();
    }

    private void processCsvContent(ZipInputStream zis) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = zis.read(buffer)) > 0) {
            bos.write(buffer, 0, len);
        }
        try (InputStream is = new ByteArrayInputStream(bos.toByteArray());
             CSVReader reader = new CSVReader(new InputStreamReader(is))) {
            List<String[]> data = reader.readAll();
            assertEquals(3, data.size());
            assertArrayEquals(new String[]{"en", "International business, born in Siberia"}, data.get(1));
        }
        bos.close();
        zis.closeEntry();
    }

    private void processXlsxContent(ZipInputStream zis) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = zis.read(buffer)) > 0) {
            bos.write(buffer, 0, len);
        }
        XLS xls = new XLS(new ByteArrayInputStream(bos.toByteArray()));
        Sheet sheet = xls.excel.getSheetAt(0);
        assertEquals("January", sheet.getRow(1).getCell(1).getStringCellValue());
        bos.close();
        zis.closeEntry();
    }
}
