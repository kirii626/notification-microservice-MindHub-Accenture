package com.midhub.notification_microservice.services.implementations;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.UnitValue;
import com.midhub.notification_microservice.events.OrderItemDtoOutput;
import com.midhub.notification_microservice.services.PdfGeneratorService;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;


@Service
public class PdfGeneratorServiceImpl implements PdfGeneratorService {

    @Override
    public byte[] generateOrderPdf(Long orderId, List<OrderItemDtoOutput> orderItems) throws java.io.IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // 🔹 Definir fuente en negrita
        PdfFont boldFont = PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA_BOLD);
        PdfFont normalFont = PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA);

        // 🔹 Agregar título con negrita
        Paragraph title = new Paragraph("📦 Order details #" + orderId)
                .setFont(boldFont)
                .setFontSize(16)
                .setMarginBottom(10);

        document.add(title);

        // 🔹 Crear tabla con encabezados
        Table table = new Table(UnitValue.createPercentArray(new float[]{1, 2, 2}))
                .useAllAvailableWidth();

        table.addHeaderCell(new Cell().add(new Paragraph("ID Item").setFont(boldFont)));
        table.addHeaderCell(new Cell().add(new Paragraph("ID Producto").setFont(boldFont)));
        table.addHeaderCell(new Cell().add(new Paragraph("Quantity").setFont(boldFont)));

        // 🔹 Agregar filas con datos de la orden
        for (OrderItemDtoOutput item : orderItems) {
            table.addCell(new Cell().add(new Paragraph(String.valueOf(item.getId())).setFont(normalFont)));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(item.getProductId())).setFont(normalFont)));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(item.getQuantity())).setFont(normalFont)));
        }

        document.add(table);
        document.close();
        return outputStream.toByteArray();
    }
}
