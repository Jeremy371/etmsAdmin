package com.humane.util.jasperreports;

import com.humane.util.file.FileNameEncoder;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class JasperReportsExportHelper {
    private static final String XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private static final String XLS = "application/vnd.ms-excel";
    private static final String PDF = "application/pdf";
    public static final String EXT_PDF = "pdf";
    public static final String EXT_XLS = "xls";
    public static final String EXT_XLSX = "xlsx";

    private static JasperReportsExportHelper instance;

    static {
        instance = new JasperReportsExportHelper();
    }

    public static ResponseEntity toResponseEntity(String fileName, String format, List<?> content) {
        try {
            switch (format) {
                case EXT_PDF:
                    return instance.toPdf(fileName, content);
                case EXT_XLS:
                    return instance.toXls(fileName, content);
                case EXT_XLSX:
                    return instance.toXlsx(fileName, content);
                default:
            }
        } catch (JRException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return null;
    }

    private ResponseEntity<byte[]> toXlsx(String viewName, Collection<?> collection) throws JRException {
        return toXlsx(viewName, new LinkedHashMap<>(), collection);
    }

    private ResponseEntity<byte[]> toXlsx(String viewName, Map<String, Object> params, Collection<?> collection) throws JRException {
        JasperReport jasperReport = loadReport(viewName);
        if (jasperReport == null) return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(collection));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JRXlsxExporter exporter = new JRXlsxExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));

        SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
        exporter.setConfiguration(configuration);

        exporter.exportReport();

        byte[] ba = baos.toByteArray();

        String fileName = FileNameEncoder.encode(jasperPrint.getName()) + "." + EXT_XLSX;

        HttpHeaders headers = getHeaders(XLSX, fileName);
        return new ResponseEntity<>(ba, headers, HttpStatus.OK);
    }

    private ResponseEntity<byte[]> toXls(String viewName, Collection<?> collection) throws JRException {
        return toXls(viewName, new LinkedHashMap<>(), collection);
    }

    private ResponseEntity<byte[]> toXls(String viewName, Map<String, Object> params, Collection<?> collection) throws JRException {
        JasperReport jasperReport = loadReport(viewName);
        if (jasperReport == null) return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);

        if (params == null) params = new LinkedHashMap<>();

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(collection));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JRXlsExporter exporter = new JRXlsExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));

        SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
        exporter.setConfiguration(configuration);

        exporter.exportReport();

        byte[] ba = baos.toByteArray();

        String fileName = FileNameEncoder.encode(jasperPrint.getName()) + "." + EXT_XLS;

        HttpHeaders headers = getHeaders(XLS, fileName);
        return new ResponseEntity<>(ba, headers, HttpStatus.OK);
    }

    private ResponseEntity<byte[]> toPdf(String viewName, Collection<?> collection) throws JRException {
        return toPdf(viewName, new LinkedHashMap<>(), collection);
    }

    private ResponseEntity<byte[]> toPdf(String viewName, Map<String, Object> params, Collection<?> collection) throws JRException {
        JasperReport jasperReport = loadReport(viewName);
        if (jasperReport == null) return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(collection));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, baos);
        byte[] ba = baos.toByteArray();

        String fileName = FileNameEncoder.encode(jasperPrint.getName()) + "." + EXT_PDF;

        HttpHeaders headers = getHeaders(PDF, fileName);
        return new ResponseEntity<>(ba, headers, HttpStatus.OK);
    }

    private JasperReport loadReport(String viewName) {
        try (InputStream inputStream = JasperReportsExportHelper.class.getClassLoader().getResourceAsStream(viewName)) {
            JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
            return JasperCompileManager.compileReport(jasperDesign);
        } catch (IOException | JRException e) {
            e.printStackTrace();
        }
        return null;
    }

    private HttpHeaders getHeaders(String contentType, String fileName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.set("Content-Transfer-Encoding", "binary");
        headers.set("Set-Cookie", "fileDownload=true; path=/");
        headers.set("X-Frame-Options", " SAMEORIGIN");
        headers.set("Content-Disposition", fileName);
        return headers;
    }
}
