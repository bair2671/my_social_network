package socialnetwork.pdf;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import socialnetwork.domain.Activitate;
import socialnetwork.domain.MessageForTable;
import socialnetwork.domain.Utilizator;

import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GeneratorPDF {
    public static void generarePdfActivitati(Utilizator user, List<Activitate> activitati, LocalDate start, LocalDate end ){
        try {
            String fileName = "pdf_files/activitati_ID"+user.getId().toString()+"_"+user.getFirstName()+"_"+user.getLastName()+
                    "_"+start.toString()+"_"+end.toString()+".pdf";
            PdfWriter writer = new PdfWriter(fileName);

            PdfDocument pdfDoc = new PdfDocument(writer);
            pdfDoc.addNewPage();
            Document document = new Document(pdfDoc);
            document.add(new Paragraph("Activitati Utilizator "+user.getFirstName()+" "+user.getLastName()+" (ID "+user.getId()+") "+
                    "pe perioada "+start.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))+" - "+end.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
            document.add(new Paragraph(""));

            if(activitati.size()==0)
                document.add(new Paragraph("Nu exista activitati in perioada data!"));
            else {
                Table table = new Table(3);
                table.addCell(new Cell().add("TIP"));
                table.addCell(new Cell().add("PARTENER DE ACTIVITATE"));
                table.addCell(new Cell().add("DATA"));

                activitati.forEach(x -> {
                    table.addCell(new Cell().add(x.getTip().toString()));
                    table.addCell(new Cell().add(x.getPartener()));
                    table.addCell(new Cell().add(x.getData()));
                });
                document.add(table);
            }

            document.close();

            Desktop.getDesktop().open(new File(fileName));
        }
        catch(Exception e) {}
    }

    public static void generarePdfMesaje(Utilizator userLogat, Utilizator expeditor ,List<MessageForTable> messages, LocalDate start, LocalDate end ){
        try {
            String fileName = "pdf_files/mesaje_ID"+userLogat.getId().toString()+"_"+userLogat.getFirstName()+"_"+userLogat.getLastName()+
                    "_de_la_ID"+expeditor.getId().toString()+"_"+expeditor.getFirstName()+"_"+expeditor.getLastName()+
                    "_"+start.toString()+"_"+end.toString()+".pdf";
            PdfWriter writer = new PdfWriter(fileName);

            PdfDocument pdfDoc = new PdfDocument(writer);
            pdfDoc.addNewPage();
            Document document = new Document(pdfDoc);
            document.add(new Paragraph("Mesaje primite de Utilizator "+userLogat.getFirstName()+" "+userLogat.getLastName()+" (ID "+userLogat.getId()+") "+
                    "de la Utilizator "+expeditor.getFirstName()+" "+expeditor.getLastName()+" (ID "+expeditor.getId()+") "+
                    "pe perioada "+start.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))+" - "+end.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
            document.add(new Paragraph(""));

            if(messages.size()==0)
                document.add(new Paragraph("Nu exista mesaje primite de la acest utilizator in perioada data!"));
            else {
                Table table = new Table(2);
                table.addCell(new Cell().add("DATA"));
                table.addCell(new Cell().add("TEXT"));

                messages.forEach(x -> {
                    table.addCell(new Cell().add(x.getData()));
                    table.addCell(new Cell().add(x.getText()));
                });
                document.add(table);
            }

            document.close();

            Desktop.getDesktop().open(new File(fileName));
        }
        catch(Exception e) {}
    }
}
