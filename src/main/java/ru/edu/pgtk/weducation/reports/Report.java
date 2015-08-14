package ru.edu.pgtk.weducation.reports;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;

/**
 * Интерфейс для работы с различными отчетами.
 *
 * @author Воронин Леонид
 */
public interface Report {

  public enum Orientation {
    PORTRET,
    LANDSCAPE;
  }

  public void add(Element element) throws DocumentException;
  public byte[] getData();

}
