import org.apache.commons.text.WordUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImage;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.util.Calendar;

public class PdfBoxExample {

    public static void writeLineOfText(PDPageContentStream content,
                                       int tx, int ty,
                                       PDFont font,
                                       String text) {
        if (font == null) font = PDType1Font.HELVETICA;

        try {
            content.beginText();
            content.setFont(font, 10);
            content.newLineAtOffset(tx, ty); // 595 x 842
            content.showText(text);
            content.endText();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void writeTextColumn(PDPageContentStream content,
                                       int tx, int ty,
                                       PDFont font,
                                       String text,
                                       int wrapLength) {
        String lines[] = WordUtils.wrap(text, wrapLength)
                .split("\\r?\\n");

                int posY = 0;
                for(int i = 0; i < lines.length; i++) {
                    String line = lines[i];
                    posY = ty - i * 15;

                    writeLineOfText(content, tx, posY, font, line);
                }
    }

    public static void drawImage(PDDocument doc, PDPageContentStream content,
                                 int tx, int ty,
                                 float scaleX, float scaleY,
                                 String fileName) {
        try {
            PDImageXObject image = PDImageXObject.createFromFile(fileName, doc);
            int iw = (int) (image.getWidth() * scaleX);
            int ih = (int) (image.getHeight() * scaleY);
            content.drawImage(image, tx, ty, iw, ih);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        Calendar c = Calendar.getInstance();
        String fileName = "files/file_" + c.get(Calendar.HOUR_OF_DAY)
                + "_" + c.get(Calendar.MINUTE) + ".pdf";

        PDDocument document = new PDDocument();
        PDPage page1 = new PDPage();
        document.addPage(page1);

        PDPageContentStream content = new PDPageContentStream(
                document, page1);

        writeLineOfText(content, 40, 100,
                null, "Hello World!");

        writeTextColumn(content, 40, 700,
                null, "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."
                , 50);

        writeTextColumn(content, 280, 700,
                null, "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."
                , 50);

        drawImage(document, content, 40, 250,
                0.3f, 0.3f,
                "files/img1.jpg");

        content.close();

        // nowa strona
        PDPage page2 = new PDPage();
        PDPageContentStream content2 = new PDPageContentStream(
                document, page2);
        document.addPage(page2);
        writeLineOfText(content2, 40, 700,
                PDType1Font.COURIER_BOLD, "Hello World!");

        content2.close();

        document.save(fileName);
        document.close();
    }
}
