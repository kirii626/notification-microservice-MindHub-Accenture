package com.midhub.notification_microservice.services.implementations;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.midhub.notification_microservice.events.OrderItemDtoOutput;
import com.midhub.notification_microservice.exceptions.PdfGenerationException;
import com.midhub.notification_microservice.services.PdfGeneratorService;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


@Service
public class PdfGeneratorServiceImpl implements PdfGeneratorService {

    private static final Logger logger = Logger.getLogger(PdfGeneratorServiceImpl.class.getName());

    @Override
    public byte[] generateOrderPdf(Long orderId, List<OrderItemDtoOutput> orderItems) throws java.io.IOException {
        try {
            logger.info("Starting PDF generation for Order ID: " + orderId);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            PdfFont boldFont = PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA_BOLD);
            PdfFont normalFont = PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA);

            DecimalFormat currencyFormat = new DecimalFormat("$#,##0.00");

            Paragraph title = new Paragraph("Your Order Details #" + orderId)
                    .setFont(boldFont)
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(title);

            Table table = new Table(UnitValue.createPercentArray(new float[]{2, 1, 1, 1}))
                    .useAllAvailableWidth();

            table.addHeaderCell(new Cell().add(new Paragraph("Product Name").setFont(boldFont)));
            table.addHeaderCell(new Cell().add(new Paragraph("Quantity").setFont(boldFont)));
            table.addHeaderCell(new Cell().add(new Paragraph("Unit Price").setFont(boldFont)));
            table.addHeaderCell(new Cell().add(new Paragraph("Subtotal").setFont(boldFont)));

            double total = 0.0;
            for (OrderItemDtoOutput item : orderItems) {
                double subtotal = item.getQuantity() * item.getPrice();
                total += subtotal;

                table.addCell(new Cell().add(new Paragraph(item.getName()).setFont(normalFont)));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(item.getQuantity())).setFont(normalFont)));
                table.addCell(new Cell().add(new Paragraph(currencyFormat.format(item.getPrice())).setFont(normalFont)));
                table.addCell(new Cell().add(new Paragraph(currencyFormat.format(subtotal)).setFont(normalFont)));
            }

            document.add(table);

            Paragraph totalText = new Paragraph("Total Amount: " + currencyFormat.format(total))
                    .setFont(boldFont)
                    .setFontSize(14)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setMarginTop(15);
            document.add(totalText);

            document.close();
            logger.info("PDF successfully generated for Order ID: " + orderId);
            return outputStream.toByteArray();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "ERROR generating PDF for Order ID: " + orderId, e);
            throw new PdfGenerationException("Failed to generate PDF for Order ID: " + orderId, e);
        }
    }
}
