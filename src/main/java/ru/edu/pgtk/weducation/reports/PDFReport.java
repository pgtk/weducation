package ru.edu.pgtk.weducation.reports;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import ru.edu.pgtk.weducation.entity.School;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;

import static ru.edu.pgtk.weducation.reports.PDFUtils.getPt;

/**
 * Класс, реализующий базовый функционал PDF отчета.
 * В данном классе реализован минимальный набор шрифтов, стандартные отступы и
 * поддержка альбомной и портретной ориентации. Также, данный класс имеет
 * проброс метода {@code add(Element element)} для добавления различных
 * элементов в отчет.
 * @author Воронин Леонид
 */
final class PDFReport implements Report {

	@Inject
	private School school; // Образовательное учреждение
	private final ByteArrayOutputStream stream = new ByteArrayOutputStream();
	private final Document report;

	private PDFReport(Orientation orientation, String title) throws DocumentException {
		Rectangle size = PageSize.A4;
		if (orientation == Orientation.LANDSCAPE) {
			size = size.rotate();
		}
		report = new Document(size, getPt(5), getPt(5), getPt(10), getPt(10));
		report.addAuthor("weducation project");
		report.addTitle(title);
		PdfWriter writer = PdfWriter.getInstance(report, stream);
		report.open();
	}

	public static Report create(Orientation orientation, String title) throws DocumentException {
		return new PDFReport(orientation, title);
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
