package com.ashokit.service;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.ashokit.entity.FormData;
import com.ashokit.repository.FormDataRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;

import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class FormDataService {

    @Autowired
    private FormDataRepository formDataRepository;

    @Autowired
    private JavaMailSender mailSender;

    public FormData saveFormData(FormData formData) {
        return formDataRepository.save(formData);
    }

    public void sendExcelToEmail(String toEmail) throws IOException, MessagingException {
        FormData lastRecord = formDataRepository.findTopByOrderByIdDesc();

        if (lastRecord != null) {
            // Create Excel file
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("FormData");
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("Name");
            row.createCell(1).setCellValue("Email");
            row.createCell(2).setCellValue("Subject");
            row.createCell(3).setCellValue("Message");

            Row dataRow = sheet.createRow(1);
            dataRow.createCell(0).setCellValue(lastRecord.getName());
            dataRow.createCell(1).setCellValue(lastRecord.getEmail());
            dataRow.createCell(2).setCellValue(lastRecord.getSubject());
            dataRow.createCell(3).setCellValue(lastRecord.getMessage());

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            workbook.close();

            // Send Email with Excel attachment
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(toEmail);
            helper.setSubject("Form Data Excel");
            helper.setText("Please find attached the latest form data in Excel format.");

            ByteArrayDataSource dataSource = new ByteArrayDataSource(bos.toByteArray(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            helper.addAttachment("FormData.xlsx", dataSource);

            mailSender.send(message);
        }
    }
    
    public List<FormData> getAllMessages() {
        return formDataRepository.findAll();
    }
}

