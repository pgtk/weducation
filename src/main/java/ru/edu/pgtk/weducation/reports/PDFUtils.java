package ru.edu.pgtk.weducation.reports;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

/**
 * Класс для реализации различных функций, предназначенных для работы с
 * PDF-документами
 *
 * @author Воронин Леонид
 */
public class PDFUtils {

  /**
   * Преобразует миллиметры в пункты из расчета, что один пункт равен 1/72
   * дюйма.
   *
   * @param milimeters миллиметры
   * @return пункты
   */
  public static float getPt(float milimeters) {
    return milimeters * 72 / 25.4f;
  }

  /**
   * Изготавливает "обертку" для элемента, помещая его в таблицу с одной
   * ячейкой.
   *
   * @param element - элемент для обертывания
   * @param minHeight - минимальная высота таблицы-обертки
   * @return объект типа PdfPTable
   */
  public static PdfPTable wrapElement(final Element element, final float minHeight) {
    PdfPTable wrapperTable = new PdfPTable(1);
    wrapperTable.setWidthPercentage(100.0f);
    PdfPCell wrapperCell = new PdfPCell();
    wrapperCell.setBorder(PdfPCell.NO_BORDER);
    wrapperCell.setMinimumHeight(minHeight);
    wrapperCell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
    wrapperCell.addElement(element);
    wrapperTable.addCell(wrapperCell);
    return wrapperTable;
  }

  /**
   * Изготавливает "обертку" для элемента, помещая его в таблицу с одной
   * ячейкой.
   *
   * @param element - элемент для обертывания
   * @param minHeight - минимальная высота таблицы-обертки
   * @param hAlign - горизонтальное выравнивание
   * @param vAlign - вертикальное выравнивание
   * @return объект типа PdfPTable
   */
  public static PdfPTable wrapElement(final Element element,
          final float minHeight, final int hAlign, final int vAlign) {
    PdfPTable wrapperTable = new PdfPTable(1);
    wrapperTable.setWidthPercentage(100.0f);
    PdfPCell wrapperCell = new PdfPCell();
    wrapperCell.setBorder(PdfPCell.NO_BORDER);
    wrapperCell.setMinimumHeight(minHeight);
    wrapperCell.setHorizontalAlignment(hAlign);
    wrapperCell.setVerticalAlignment(vAlign);
    wrapperCell.addElement(element);
    wrapperTable.addCell(wrapperCell);
    return wrapperTable;
  }

  /**
   * Готовит параграф с требуемым выравниванием, текстом и шрифтом
   *
   * @param text текст
   * @param font шрифт
   * @param alignment выравнивание
   * @return объект типа paragraph
   */
  public static Paragraph getParagraph(String text, Font font, int alignment) {
    Paragraph result = new Paragraph(text, font);
    result.setAlignment(alignment);
    result.setLeading(font.getSize() * 1.1f);
    return result;
  }
  
    /**
   * Выводит текст в заданные координаты относительно левого нижнего угла
   *
   * @param canvas канва документа
   * @param font используемый шрифт
   * @param text текст
   * @param x координата X в миллиметрах
   * @param y координата Y в миллиметрах
   */
  public static void putText(final PdfContentByte canvas, final Font font,
          final String text, final float x, final float y) {
    canvas.saveState();
    canvas.beginText();
    canvas.moveText(getPt(x), getPt(y));
    canvas.setFontAndSize(font.getBaseFont(), font.getSize());
    canvas.showText(text);
    canvas.endText();
    canvas.restoreState();
  }
}
