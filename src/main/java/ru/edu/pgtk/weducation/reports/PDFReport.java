package ru.edu.pgtk.weducation.reports;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.inject.Inject;
import ru.edu.pgtk.weducation.entity.School;
import static ru.edu.pgtk.weducation.reports.PDFUtils.getPt;
import ru.edu.pgtk.weducation.reports.Report.Orientation;

/**
 * Абстрактный класс, реализующий документ
 *
 * @author leonid
 */
public final class PDFReport implements Report {

  @Inject
  private School school; // Образовательное учреждение
  protected BaseFont baseFont;
  protected Font bigFont;
  protected Font regularFont;
  protected Font smallFont;
  protected String schoolName;
  private final ByteArrayOutputStream stream = new ByteArrayOutputStream();
  private final Document report;

  private PDFReport(Orientation orientation) throws DocumentException {
    Rectangle size = PageSize.A4;
    if (orientation == Orientation.LANDSCAPE) {
      size = size.rotate();
    }
    report = new Document(size, getPt(5), getPt(5), getPt(10), getPt(10));
    try {
      baseFont = BaseFont.createFont("fonts/times.ttf", BaseFont.IDENTITY_H,
        BaseFont.EMBEDDED);
      smallFont = new Font(baseFont, 7);
      regularFont = new Font(baseFont, 10);
      bigFont = new Font(baseFont, 16);
      PdfWriter writer = PdfWriter.getInstance(report, stream);
      report.open();
    } catch (IOException e) {
      throw new DocumentException("IO Exception with message " + e.getMessage());
    }
  }

  public static Report create(Orientation orientation) throws DocumentException {
    return new PDFReport(orientation);
  }

  public String getSchoolName() throws DocumentException {
    if ((null == school) || (null == school.getFullName())) {
      throw new DocumentException("School name or school is null!");
    }
    return school.getFullName().replace("Прокопьевский", "\nПрокопьевский");
  }

  @Override
  public void add(Element element) throws DocumentException {
    report.add(element);
  }

  @Override
  public byte[] getData() {
    report.close();
    return stream.toByteArray();
  }
}
