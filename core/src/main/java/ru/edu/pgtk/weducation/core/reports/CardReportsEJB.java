package ru.edu.pgtk.weducation.core.reports;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import ru.edu.pgtk.weducation.core.ejb.CourseWorkMarksDAO;
import ru.edu.pgtk.weducation.core.ejb.FinalMarksEJB;
import ru.edu.pgtk.weducation.core.ejb.FinalPracticMarksEJB;
import ru.edu.pgtk.weducation.core.ejb.GOSMarksEJB;
import ru.edu.pgtk.weducation.core.ejb.RenamingsEJB;
import ru.edu.pgtk.weducation.core.ejb.StudycardsDAO;
import ru.edu.pgtk.weducation.core.entity.AccountRole;
import ru.edu.pgtk.weducation.core.entity.CourseWorkMark;
import ru.edu.pgtk.weducation.core.entity.FinalMark;
import ru.edu.pgtk.weducation.core.entity.FinalPracticMark;
import ru.edu.pgtk.weducation.core.entity.GOSMark;
import ru.edu.pgtk.weducation.core.entity.Person;
import ru.edu.pgtk.weducation.core.entity.Renaming;
import ru.edu.pgtk.weducation.core.entity.School;
import ru.edu.pgtk.weducation.core.entity.StudyCard;
import ru.edu.pgtk.weducation.core.interceptors.Restricted;
import ru.edu.pgtk.weducation.core.interceptors.WithLog;
import ru.edu.pgtk.weducation.core.utils.Utils;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static ru.edu.pgtk.weducation.core.reports.PDFUtils.getParagraph;
import static ru.edu.pgtk.weducation.core.reports.PDFUtils.getPt;
import static ru.edu.pgtk.weducation.core.reports.PDFUtils.putText;
import static ru.edu.pgtk.weducation.core.reports.PDFUtils.wrapElement;
import static ru.edu.pgtk.weducation.core.utils.Utils.getMonthString;
import static ru.edu.pgtk.weducation.core.utils.Utils.getYearString;

/**
 * Класс, генерирующий отчет в виде pdf документа. Поскольку планируется
 * генерить относительно простые отчеты, класс использует поток для записи в
 * память. Думаю, это быстрее файлового ввода-вывода.
 * @author Воронин Леонид
 */
@Path("/card")
@Stateless
public class CardReportsEJB {

	// поток байт в котором будет "собираться" отчет.
	private final ByteArrayOutputStream stream = new ByteArrayOutputStream();
	private Font regularFont;
	private Font smallFont;
	private Font bigFont;
	private Font hugeFont;
	@EJB
	private FinalMarksEJB finalMarks;
	@EJB
	private FinalPracticMarksEJB practicMarks;
	@EJB
	private GOSMarksEJB gosMarks;
	@EJB
	private CourseWorkMarksDAO courseWorks;
	@EJB
	private RenamingsEJB renamings;
	@EJB
	private StudycardsDAO cards;

	@GET
	@Path("{cardId: \\d+}/diplome")
	@Produces("application/pdf")
	@Restricted(allowedRoles = {AccountRole.DEPARTMENT, AccountRole.DEPOT})
	@WithLog
	public Response diplome(@PathParam("cardId") int cardCode) {
		try {
			StudyCard card = cards.get(cardCode);
			Response.ResponseBuilder response = Response.ok(getDiplome(card, false, false));
			return response.build();
		} catch (Exception e) {
			throw new NotFoundException();
		}
	}

	@GET
	@Path("{cardId: \\d+}/diplome/copy")
	@Produces("application/pdf")
	@Restricted(allowedRoles = {AccountRole.DEPARTMENT, AccountRole.DEPOT})
	@WithLog
	public Response diplomeCopy(@PathParam("cardId") int cardCode) {
		try {
			StudyCard card = cards.get(cardCode);
			Response.ResponseBuilder response = Response.ok(getDiplome(card, true, false));
			return response.build();
		} catch (Exception e) {
			throw new NotFoundException();
		}
	}

	@GET
	@Path("{cardId: \\d+}/diplome/duplicate")
	@Produces("application/pdf")
	@Restricted(allowedRoles = {AccountRole.DEPARTMENT, AccountRole.DEPOT})
	@WithLog
	public Response diplomeDuplicate(@PathParam("cardId") int cardCode) {
		try {
			StudyCard card = cards.get(cardCode);
			Response.ResponseBuilder response = Response.ok(getDiplome(card, false, true));
			return response.build();
		} catch (Exception e) {
			throw new NotFoundException();
		}
	}

	@GET
	@Path("{cardId: \\d+}/diplome/duplicatecopy")
	@Produces("application/pdf")
	@Restricted(allowedRoles = {AccountRole.DEPARTMENT, AccountRole.DEPOT})
	@WithLog
	public Response diplomeDuplicateCopy(@PathParam("cardId") int cardCode) {
		try {
			StudyCard card = cards.get(cardCode);
			Response.ResponseBuilder response = Response.ok(getDiplome(card, true, true));
			return response.build();
		} catch (Exception e) {
			throw new NotFoundException();
		}
	}

	@GET
	@Path("{cardId: \\d+}/reference")
	@Produces("application/pdf")
	@Restricted(allowedRoles = {AccountRole.DEPARTMENT, AccountRole.DEPOT})
	@WithLog
	public Response reference(@PathParam("cardId") int cardCode) {
		try {
			StudyCard card = cards.get(cardCode);
			Response.ResponseBuilder response = Response.ok(getReference(card));
			return response.build();
		} catch (Exception e) {
			throw new NotFoundException();
		}
	}

	/**
	 * Подготавливает шрифты для использования в документе
	 */
	@PostConstruct
	private void prepareFonts() {
		try {
			BaseFont baseFont = BaseFont.createFont("fonts/times.ttf", BaseFont.IDENTITY_H,
					BaseFont.EMBEDDED);
			regularFont = new Font(baseFont, 10);
			smallFont = new Font(baseFont, 6);
			bigFont = new Font(baseFont, 16);
			hugeFont = new Font(baseFont, 20);
		} catch (IOException | DocumentException e) {
			throw new EJBException("Error during class preparation!");
		}
	}

	/**
	 * Готовит таблицу оценок за курсовые проекты
	 * @param marks       оценки
	 * @param isReference признак того, что курсовые готовятся для академической справки
	 * @return таблица курсовых проектов
	 * @throws DocumentException
	 */
	private PdfPTable prepareCourseWorkTable(final List<CourseWorkMark> marks, final boolean isReference)
			throws DocumentException {
		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100.0f);
		table.setWidths(new int[]{8, 2});
		PdfPCell nameCell;
		PdfPCell markCell;
		if (isReference) {
			// отличия таблицы для справки
			table.setSpacingBefore(10f);
			nameCell = new PdfPCell(getParagraph("Курсовые проекты (работы)",
					smallFont, Paragraph.ALIGN_CENTER));
			nameCell.setMinimumHeight(20f);
			nameCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			nameCell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
			nameCell.setBorderColor(BaseColor.BLACK);
			nameCell.setBorderWidth(.5f);
			markCell = new PdfPCell(getParagraph("Оценка",
					smallFont, Paragraph.ALIGN_CENTER));
			markCell.setMinimumHeight(20f);
			markCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			markCell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
			markCell.setBorderColor(BaseColor.BLACK);
			markCell.setBorderWidth(.5f);
			table.addCell(nameCell);
			table.addCell(markCell);
		}
		for (CourseWorkMark mark : marks) {
			// TODO тут надо сделать надпись в соответствии с образцом
			String title = "дисциплине";
			String name = mark.getSubject().getFullName();
			if (null != mark.getSubject().getModule()) {
				title = "профессиональному модулю";
				name = mark.getSubject().getModule().getName();
			}
			nameCell = new PdfPCell(getParagraph(String.format("Курсовой проект (работа) по %s \"%s\" на тему: %s", title, name, mark.getTheme()),
					smallFont, Paragraph.ALIGN_LEFT));
			nameCell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			markCell = new PdfPCell(getParagraph(Utils.getMarkString(mark.getMark()),
					smallFont, Paragraph.ALIGN_CENTER));
			markCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			if (isReference) {
				// Если справка - есть рамка
				nameCell.setBorderColor(BaseColor.BLACK);
				nameCell.setBorderWidth(.5f);
				markCell.setBorderColor(BaseColor.BLACK);
				markCell.setBorderWidth(.5f);
			} else {
				// Если диплом - нет рамки
				nameCell.setBorder(PdfPCell.NO_BORDER);
				markCell.setBorder(PdfPCell.NO_BORDER);
			}
			table.addCell(nameCell);
			table.addCell(markCell);
		}
		return table;
	}

	/**
	 * Готовит таблицу оценок для справки
	 * @param marks список оценок
	 * @param table таблица, в которую они будут добавлены
	 * @throws DocumentException
	 */
	private void prepareMarkTable(final List<MarkItem> marks,
	                              PdfPTable table) throws DocumentException {

		PdfPCell nameCell;
		PdfPCell hoursCell;
		PdfPCell markCell;

		// Формируем заголовок
		nameCell = new PdfPCell(getParagraph("Наименование учебных предметов, курсов, дисциплин (модулей), практик",
				smallFont, Paragraph.ALIGN_CENTER));
		nameCell.setMinimumHeight(30f);
		nameCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		nameCell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
		nameCell.setBorderColor(BaseColor.BLACK);
		nameCell.setBorderWidth(.5f);
		hoursCell = new PdfPCell(getParagraph("Общее количество часов", smallFont, Paragraph.ALIGN_CENTER));
		hoursCell.setMinimumHeight(30f);
		hoursCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		hoursCell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
		hoursCell.setBorderColor(BaseColor.BLACK);
		hoursCell.setBorderWidth(.5f);
		markCell = new PdfPCell(getParagraph("Оценка", smallFont, Paragraph.ALIGN_CENTER));
		markCell.setMinimumHeight(30f);
		markCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		markCell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
		markCell.setBorderColor(BaseColor.BLACK);
		markCell.setBorderWidth(.5f);
		table.addCell(nameCell);
		table.addCell(hoursCell);
		table.addCell(markCell);

		// выводим список оценок
		for (MarkItem mark : marks) {
			nameCell = new PdfPCell(getParagraph(mark.subject,
					smallFont, Paragraph.ALIGN_LEFT));
			nameCell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			nameCell.setPaddingRight(getPt(3));
			nameCell.setLeading(0.5f, 0.7f);
			nameCell.setBorderColor(BaseColor.BLACK);
			nameCell.setBorderWidth(.5f);
			hoursCell = new PdfPCell(getParagraph(mark.load, smallFont, Paragraph.ALIGN_CENTER));
			hoursCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			hoursCell.setLeading(0.5f, 0.7f);
			hoursCell.setBorderColor(BaseColor.BLACK);
			hoursCell.setBorderWidth(.5f);
			markCell = new PdfPCell(getParagraph(mark.mark, smallFont, Paragraph.ALIGN_CENTER));
			markCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			markCell.setLeading(0.5f, 0.7f);
			markCell.setBorderColor(BaseColor.BLACK);
			markCell.setBorderWidth(.5f);
			table.addCell(nameCell);
			table.addCell(hoursCell);
			table.addCell(markCell);
		}
	}

	/**
	 * Готовит таблицу оценок для диплома
	 * @param marks  список оценок
	 * @param table1 таблица 1 (страница 2 приложение)
	 * @param table2 таблица 2 (страница 3 приложения)
	 * @throws DocumentException
	 */
	private void prepareMarkTables(final List<MarkItem> marks,
	                               PdfPTable table1, PdfPTable table2) throws DocumentException {

		boolean firstPage = true;
		PdfPCell nameCell;
		PdfPCell hoursCell;
		PdfPCell markCell;
		for (MarkItem mark : marks) {
			nameCell = new PdfPCell(getParagraph(mark.subject,
					smallFont, Paragraph.ALIGN_LEFT));
			nameCell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			nameCell.setPaddingRight(getPt(3));
			nameCell.setLeading(0.5f, 0.7f);
			nameCell.setBorder(PdfPCell.NO_BORDER);
			hoursCell = new PdfPCell(getParagraph(mark.load, smallFont, Paragraph.ALIGN_CENTER));
			hoursCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			hoursCell.setLeading(0.5f, 0.7f);
			hoursCell.setBorder(PdfPCell.NO_BORDER);
			markCell = new PdfPCell(getParagraph(mark.mark, smallFont, Paragraph.ALIGN_CENTER));
			markCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			markCell.setLeading(0.5f, 0.7f);
			markCell.setBorder(PdfPCell.NO_BORDER);
			if (firstPage) {
				table1.addCell(nameCell);
				table1.addCell(hoursCell);
				table1.addCell(markCell);
				if (table1.calculateHeights() > getPt(152)) {
					// Опа! высота таблицы зашкаливает. Удаляем последнюю строку
					firstPage = false;
					table1.deleteLastRow();
				}
			}
			if (!firstPage) {
				// Выводим данные на вторую страницу
				table2.addCell(nameCell);
				table2.addCell(hoursCell);
				table2.addCell(markCell);
			}
		}
	}

	/**
	 * Готовит таблицу переименований учебного заведения.
	 * @param renamingList список переименований
	 * @return таблица для добавления в pdf документ
	 * @throws DocumentException
	 */
	private PdfPTable prepareRenamingTable(List<Renaming> renamingList)
			throws DocumentException {
		PdfPTable table = new PdfPTable(1);
		table.setWidthPercentage(100.0f);
		PdfPCell cell;
		for (Renaming item : renamingList) {
			cell = new PdfPCell();
			cell.setBorder(PdfPCell.NO_BORDER);
			cell.addElement(getParagraph(
					"Образовательная организация переименована в "
					+ Utils.getYear(item.getDate()) + " году;", smallFont,
					Paragraph.ALIGN_LEFT));
			cell.addElement(getParagraph(
					"старое полное наименование образовательной организации: "
					+ item.getOldName(), smallFont,
					Paragraph.ALIGN_LEFT));
			table.addCell(cell);
		}
		return table;
	}

	/**
	 * Готовит отчет в виде pdf документа.
	 * @param card        Личная карточка, требуемая для отчета
	 * @param isCopy      если истина, то будет выведена копия
	 * @param isDuplicate если истина, то будет выведен дубликат
	 * @return массив байт (содержимое pdf-документа)
	 */
	private byte[] getDiplome(final StudyCard card, final boolean isCopy, final boolean isDuplicate) {
		try {
			Document document = new Document(PageSize.A4.rotate(), 15f, 15f,
					75f, 15f);
			PdfWriter writer = PdfWriter.getInstance(document, stream);
			document.open();
			document.addTitle("Диплом о среднеспециальном образовании и приложение к нему.");
			document.addAuthor("weducation project");

			// Данные для вывода (возможно лучше тут считать всё с карточки)
			School scl = card.getSchool();
			Person psn = card.getPerson();
			String sclName = scl.getFullName() + "\n" + scl.getPlace();
			// Корректируем надпись с учетом дубликата
			if (isDuplicate) {
				sclName = sclName + "\n" + "ДУБЛИКАТ";
			}
			String comissionDate = "от " + Utils.getDateString(card.getComissionDate()) + " года";
			String diplomeDate = Utils.getDateString(card.getDiplomeDate()) + " года";
			String birthDate = Utils.getDateString(psn.getBirthDate()) + " года";
			String spo = "о среднем профессиональном образовании";
			String schoolDirector = scl.getDirector();
			String comissionDirector = card.getComissionDirector();
			String speciality = card.getPlan().getSpecialityName();
			String learnLength = getYearString(card.getPlan().getYears()) + " "
			                     + getMonthString(card.getPlan().getMonths());
			String oldDocument = card.getDocumentName() + ", "
			                     + Utils.getYear(card.getDocumentDate()) + " год.";
			List<MarkItem> marks = new ArrayList<>();
			// Добавляем итоговые оценки
			int aload = 0;
			int mload = 0;
			// Сначала добавляем дисциплины, не входящие в модули
			for (FinalMark fm : finalMarks.fetchOnlySubjects(card)) {
				marks.add(new MarkItem(fm));
				aload += fm.getAuditoryLoad();
				mload += fm.getMaximumLoad();
			}
			// Потом добавим модули с их содержимым
			for (FinalMark fm : finalMarks.fetchModules(card)) {
				marks.add(new MarkItem(fm));
				marks.add(new MarkItem("в том числе:", "", ""));
				// Дисциплины конкретного модуля
				for (FinalMark sfm : finalMarks.fetchModuleSubjects(card, fm.getModule())) {
					marks.add(new MarkItem(sfm));
					aload += sfm.getAuditoryLoad();
					mload += sfm.getMaximumLoad();
				}
			}
			marks.add(new MarkItem("ВСЕГО часов теоретического обучения:", mload, 0));
			marks.add(new MarkItem("в том числе аудиторных часов:", aload, 0));
			marks.add(new MarkItem("Практика", practicMarks.getSummaryLoad(card), 0));
			marks.add(new MarkItem("в том числе:", "", ""));
			// Добавляем оценки за практику
			for (FinalPracticMark pm : practicMarks.fetchAll(card)) {
				marks.add(new MarkItem(pm));
			}
			marks.add(new MarkItem("Государственная итоговая аттестация", card.getDiplomeLength(), 0));
			marks.add(new MarkItem("в том числе:", "", ""));
			// TODO Уточнить содержание строки "дипломный проект" и внести изменения.
			String title = (card.isGosExam()) ? "Итоговый междисциплинарный государственный экзамен"
					: (String.format("Выпускная квалификационная работа (дипломный проект, работа) \"%s\")", card.getDiplomeTheme()));
			marks.add(new MarkItem(title, "x", Utils.getMarkString(card.getDiplomeMark())));
			for (GOSMark gm : gosMarks.fetchAll(card)) {
				marks.add(new MarkItem(gm));
			}

			// ============================================================
			// Бланк диплома
			// ============================================================
			PdfPTable mainTable = new PdfPTable(2);
			mainTable.setWidthPercentage(100.0f);

			// Первая колонка
			PdfPCell firstTableCell = new PdfPCell();
			firstTableCell.setPaddingLeft(20f);
			firstTableCell.setPaddingRight(10f);
			firstTableCell.setBorder(PdfPCell.NO_BORDER);

			// Отступ от верхнего края страницы
			firstTableCell.addElement(wrapElement(new Phrase(" ", regularFont), getPt(42)));
			// Наименование учебного заведения
//      firstTableCell.addElement(wrapElement(getParagraph(sclName, regularFont, Paragraph.ALIGN_CENTER), 188));
			firstTableCell.addElement(wrapElement(getParagraph(sclName, regularFont, Paragraph.ALIGN_CENTER), getPt(63)));
			// Квалификация
//      firstTableCell.addElement(wrapElement(getParagraph(spc.getKvalification(), regularFont, Paragraph.ALIGN_CENTER), 97));
			firstTableCell.addElement(wrapElement(getParagraph(card.getPlan().getKvalification(), regularFont, Paragraph.ALIGN_CENTER), getPt(42)));
			// Регистрационный номер
			firstTableCell.addElement(wrapElement(
					getParagraph(card.getRegistrationNumber(), regularFont,
							Paragraph.ALIGN_CENTER), getPt(14)));
			// Дата выдачи
			firstTableCell.addElement(wrapElement(
					getParagraph(diplomeDate, regularFont,
							Paragraph.ALIGN_CENTER), getPt(5)));

			// Добавляем первую колонку в основную таблицу. Страница диплома создана.
			mainTable.addCell(firstTableCell);
			// Вторая колонка
			PdfPCell secondTableCell = new PdfPCell();
			secondTableCell.setBorder(PdfPCell.NO_BORDER);
			// Добавляем данные
			// Отступ от верхнего края страницы
			secondTableCell.addElement(wrapElement(new Phrase(" ", regularFont), getPt(20)));
			// ФИО
			secondTableCell.addElement(wrapElement(getParagraph(psn.getFirstName(), regularFont, Paragraph.ALIGN_CENTER), getPt(5)));
			secondTableCell.addElement(wrapElement(getParagraph(psn.getMiddleName(), regularFont, Paragraph.ALIGN_CENTER), getPt(5)));
			secondTableCell.addElement(wrapElement(getParagraph(psn.getLastName(), regularFont, Paragraph.ALIGN_CENTER), getPt(40)));
			// образовательная программа
			secondTableCell.addElement(wrapElement(getParagraph(speciality, regularFont, Paragraph.ALIGN_CENTER), getPt(30)));
			// Дата комиссии
			secondTableCell.addElement(wrapElement(getParagraph(comissionDate, regularFont, Paragraph.ALIGN_CENTER), getPt(25)));
			// Председатель комиссии
			secondTableCell.addElement(wrapElement(getParagraph(comissionDirector, regularFont, Paragraph.ALIGN_RIGHT), getPt(15)));
			// Руководитель организации
			secondTableCell.addElement(wrapElement(getParagraph(schoolDirector, regularFont, Paragraph.ALIGN_RIGHT), getPt(15)));

			mainTable.addCell(secondTableCell);
			// Добавляем подложку в виде картинки, если требуется копия выписки
			if (isCopy) {
				URL resource = this.getClass().getClassLoader().getResource("images/02.jpg");
				if (null != resource) {
					Image img = Image.getInstance(resource);
					img.scaleAbsolute(getPt(297), getPt(210));
					img.setAbsolutePosition(0, 0);
					document.add(img);
				}
			}
			document.add(mainTable);
			// Добавляем приложение к диплому
			document.newPage();
			// ============================================================
			// Приложение к диплому
			// ============================================================
			// Страницы 1 и 4 приложения к диплому (сторона 1 листа)
			// Основная таблица из двух столбцов. Так мы реализуем колонки
			mainTable = new PdfPTable(2);
			mainTable.setWidthPercentage(100.0f);

			// Первая колонка (страница 4 приложения к диплому)
			firstTableCell = new PdfPCell();
			firstTableCell.setPaddingLeft(15f);
			firstTableCell.setPaddingRight(15f);
//      firstTableCell.setPaddingLeft(15f);
//      firstTableCell.setPaddingRight(10f);
			firstTableCell.setBorder(PdfPCell.NO_BORDER);

			// Добавим таблицу курсовых в колонку
			PdfPTable courseWorkTable = wrapElement(
					prepareCourseWorkTable(courseWorks.fetchAll(card), false), getPt(105));
			courseWorkTable.setSpacingBefore(getPt(5));
			firstTableCell.addElement(courseWorkTable);

			// Добавим дополнительные сведения в колонку
			PdfPTable renamingTable = wrapElement(
					prepareRenamingTable(renamings.findByDates(card.getBeginDate(), card.getEndDate())), getPt(30));
			renamingTable.setSpacingBefore(getPt(9));
			firstTableCell.addElement(renamingTable);

			// Добавим данные о руководителе организации
			firstTableCell.addElement(wrapElement(
					getParagraph(schoolDirector, regularFont,
							Paragraph.ALIGN_RIGHT), getPt(15)));

			// Добавляем первую колонку в основную таблицу. Страница 4 создана.
			mainTable.addCell(firstTableCell);
			// Вторая колонка (страница 1 приложения к диплому)
			secondTableCell = new PdfPCell();
			secondTableCell.setBorder(PdfPCell.NO_BORDER);
			PdfPTable secondTable = new PdfPTable(2);
			secondTable.setWidthPercentage(100.0f);
			secondTable.setWidths(new int[]{3, 7});
//      secondTable.setWidths(new int[]{3, 6});
			secondTable.setSpacingBefore(getPt(7));
			// Маленькая колонка (под гербом РФ)
			PdfPCell innerCell1 = new PdfPCell();
			innerCell1.setBorder(PdfPCell.NO_BORDER);
			innerCell1.addElement(wrapElement(new Phrase("", smallFont), getPt(35)));
			innerCell1.addElement(wrapElement(getParagraph(sclName, regularFont, Paragraph.ALIGN_CENTER), getPt(65)));
			innerCell1.addElement(wrapElement(
					getParagraph(spo,
							regularFont, Paragraph.ALIGN_CENTER), getPt(32)));
			innerCell1.addElement(wrapElement(
					getParagraph(card.getRegistrationNumber(), regularFont,
							Paragraph.ALIGN_CENTER), getPt(17)));
			innerCell1.addElement(wrapElement(
					getParagraph(diplomeDate, regularFont,
							Paragraph.ALIGN_CENTER), getPt(5)));
			// Большая колонка (сведения об обладателе диплома и т.п.)
			PdfPCell innerCell2 = new PdfPCell();
			innerCell2.setBorder(PdfPCell.NO_BORDER);
			innerCell2.addElement(wrapElement(new Phrase("", smallFont), getPt(7)));
			innerCell2
					.addElement(wrapElement(
							getParagraph(psn.getFirstName(), regularFont,
									Paragraph.ALIGN_CENTER), getPt(16)));
			innerCell2.addElement(wrapElement(
					getParagraph(psn.getMiddleName(), regularFont,
							Paragraph.ALIGN_CENTER), getPt(16)));
			innerCell2.addElement(wrapElement(
					getParagraph(psn.getLastName(), regularFont,
							Paragraph.ALIGN_CENTER), getPt(17)));
			innerCell2.addElement(wrapElement(
					getParagraph(birthDate, regularFont,
							Paragraph.ALIGN_CENTER), getPt(18)));
			innerCell2.addElement(wrapElement(
					getParagraph(oldDocument,
							regularFont, Paragraph.ALIGN_LEFT), getPt(40)));
			innerCell2.addElement(wrapElement(
					getParagraph(learnLength, regularFont,
							Paragraph.ALIGN_CENTER), getPt(17)));
			innerCell2.addElement(wrapElement(
					getParagraph(card.getPlan().getKvalification(), regularFont,
							Paragraph.ALIGN_CENTER), getPt(17)));
			innerCell2.addElement(wrapElement(
					getParagraph(speciality, regularFont, Paragraph.ALIGN_CENTER), getPt(10)));
			// Добавляем колонки
			secondTable.addCell(innerCell1);
			secondTable.addCell(innerCell2);
			// Добавляем таблицу в колонку 2
			secondTableCell.addElement(secondTable);
			mainTable.addCell(secondTableCell);
			// Добавляем подложку в виде картинки, если требуется копия выписки
			if (isCopy) {
				URL resource = this.getClass().getClassLoader().getResource("images/03.jpg");
				if (null != resource) {
					Image img = Image.getInstance(resource);
					img.scaleAbsolute(getPt(297), getPt(210));
					img.setAbsolutePosition(0, 0);
					document.add(img);
				}
			}
			document.add(mainTable);
			// Выведем номера страниц
			PdfContentByte canvas = writer.getDirectContent();
			putText(canvas, regularFont, "4", 55, 12); // Страниц всего
			putText(canvas, regularFont, "4", 27, 6); // Страница 4
			putText(canvas, regularFont, "1", 289, 6); // Страница 1

			// ===========================================================================
			// Сторона 1 приложения к диплому (страницы 1 и 4) сформированы. Приступаем к
			// стороне 2.
			document.newPage();
			mainTable = new PdfPTable(2);
			mainTable.setWidthPercentage(100.0f);
			// Первая колонка (страница 2 приложения к диплому)
			firstTableCell = new PdfPCell();
			firstTableCell.setPaddingLeft(15f);
			firstTableCell.setPaddingRight(15f);
			firstTableCell.setBorder(PdfPCell.NO_BORDER);
			firstTableCell.setMinimumHeight(getPt(152));
			PdfPTable firstTable = new PdfPTable(3);
			firstTable.setWidthPercentage(100.0f);
			firstTable.setTotalWidth(getPt(135));
			firstTable.setWidths(new int[]{10, 2, 2});
			firstTable.setSpacingBefore(getPt(20));
			// Вторая колонка (страница 1 приложения к диплому)
			secondTableCell = new PdfPCell();
			secondTableCell.setPaddingLeft(17f);
			secondTableCell.setPaddingRight(12f);
			secondTableCell.setBorder(PdfPCell.NO_BORDER);
			secondTableCell.setMinimumHeight(getPt(152));
			secondTable = new PdfPTable(3);
			secondTable.setWidthPercentage(100.0f);
			firstTable.setTotalWidth(getPt(135));
			secondTable.setWidths(new int[]{10, 2, 2});
			secondTable.setSpacingBefore(getPt(8));

			// Заполняем данными страницы 2 и 3
			prepareMarkTables(marks, firstTable, secondTable);
			firstTableCell.addElement(firstTable);
			secondTableCell.addElement(secondTable);
			mainTable.addCell(firstTableCell);
			mainTable.addCell(secondTableCell);
			// Если требуется копия выписки, то отображаем картинку в качестве подложки
			if (isCopy) {
				URL resource = this.getClass().getClassLoader().getResource("images/04.jpg");
				if (null != resource) {
					Image img = Image.getInstance(resource);
					img.scaleAbsolute(getPt(297), getPt(210));
					img.setAbsolutePosition(0, 0);
					document.add(img);
				}
			}
			document.add(mainTable);
			// Выводим номера страниц
			canvas = writer.getDirectContent();
			putText(canvas, regularFont, "2", 27, 6); // Страница 4
			putText(canvas, regularFont, "3", 289, 6); // Страница 1
			// Закрываем документ
			document.close();
		} catch (IOException | DocumentException e) {
			throw new EJBException(e.getMessage());
		}
		return stream.toByteArray();
	}

	/**
	 * Готовит справку об успеваемости в виде pdf документа.
	 * @param card Личная карточка, требуемая для отчета
	 * @return массив байт (содержимое pdf-документа)
	 */
	private byte[] getReference(final StudyCard card) {
		try {
			System.out.println("Генерируем содержимое академической справки...");
			Document document = new Document(PageSize.A4, getPt(5), getPt(5),
					getPt(10), getPt(5));
			PdfWriter writer = PdfWriter.getInstance(document, stream);
			document.open();
			document.addTitle("Справка об успеваемости");
			document.addAuthor("weducation project");

			// Данные для вывода (возможно лучше тут считать всё с карточки)
			School scl = card.getSchool();
			Person psn = card.getPerson();
			String sclName = scl.getFullName() + "\n" + scl.getPlace();
			String comissionDate = Utils.getDateString(card.getComissionDate()) + " года";
			String diplomeDate = Utils.getDateString(card.getDiplomeDate()) + " года";
			String birthDate = Utils.getDateString(psn.getBirthDate()) + " года";
			String schoolDirector = scl.getDirector();
			String speciality = card.getPlan().getSpecialityName();
			String studyForm = card.getExtramuralString();
			String oldDocument = card.getDocumentName() + ", "
			                     + Utils.getYear(card.getDocumentDate()) + " год.";
			/**
			 * Формируем таблицу оценок, выводимых в справку
			 */
			List<MarkItem> marks = new ArrayList<>();
			// Добавляем итоговые оценки
			int aload = 0;
			int mload = 0;
			// Сначала добавляем дисциплины, не входящие в модули
			for (FinalMark fm : finalMarks.fetchOnlySubjects(card)) {
				marks.add(new MarkItem(fm));
				aload += fm.getAuditoryLoad();
				mload += fm.getMaximumLoad();
			}
			// Потом добавим модули с их содержимым
			for (FinalMark fm : finalMarks.fetchModules(card)) {
				marks.add(new MarkItem(fm));
				marks.add(new MarkItem("в том числе:", "", ""));
				// Дисциплины конкретного модуля
				for (FinalMark sfm : finalMarks.fetchModuleSubjects(card, fm.getModule())) {
					marks.add(new MarkItem(sfm));
					aload += sfm.getAuditoryLoad();
					mload += sfm.getMaximumLoad();
				}
			}
			marks.add(new MarkItem("ВСЕГО часов теоретического обучения:", mload, 0));
			marks.add(new MarkItem("в том числе аудиторных часов:", aload, 0));
			marks.add(new MarkItem("Практика", practicMarks.getSummaryLoad(card), 0));
			marks.add(new MarkItem("в том числе:", "", ""));
			// Добавляем оценки за практику
			for (FinalPracticMark pm : practicMarks.fetchAll(card)) {
				marks.add(new MarkItem(pm));
			}
			// TODO Проверить, надо ли добавлять эти данные?
//      marks.add(new MarkItem("Государственная итоговая аттестация", card.getDiplomeLength(), 0));
//      marks.add(new MarkItem("в том числе:", "", ""));
//      String title = (card.isGosExam()) ? "Итоговый междисциплинарный государственный экзамен"
//              : ("Дипломный проект на тему \"" + card.getDiplomeTheme() + "\"");
//      marks.add(new MarkItem(title, "x", Utils.getMarkString(card.getDiplomeMark())));
			marks.add(new MarkItem("Государственные экзамены", "", ""));
			marks.add(new MarkItem("в том числе:", "", ""));
			for (GOSMark gm : gosMarks.fetchAll(card)) {
				marks.add(new MarkItem(gm));
			}

			// ============================================================
			// Бланк справки
			// ============================================================
			// Основная таблица из двух столбцов. Так мы реализуем колонки
			PdfPTable mainTable = new PdfPTable(2);
			mainTable.setWidthPercentage(100.0f);
			mainTable.setWidths(new int[]{3, 6});
//      mainTable.setSpacingBefore(20f);
			// Маленькая колонка (РОССИЙСКАЯ ФЕДЕРАЦИЯ...)
			PdfPCell innerCell1 = new PdfPCell();
			innerCell1.setBorder(PdfPCell.NO_BORDER);
			innerCell1.addElement(getParagraph("РОССИЙСКАЯ\nФЕДЕРАЦИЯ", regularFont, Paragraph.ALIGN_CENTER));
			innerCell1.addElement(wrapElement(
					getParagraph(sclName, regularFont, Paragraph.ALIGN_CENTER),
					150, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_BOTTOM));
			innerCell1.addElement(getParagraph("\nСПРАВКА", hugeFont, Paragraph.ALIGN_CENTER));
			innerCell1.addElement(getParagraph("ОБ УСПЕВАЕМОСТИ", bigFont, Paragraph.ALIGN_CENTER));
			innerCell1.addElement(wrapElement(
					getParagraph(card.getRegistrationNumber(), regularFont, Paragraph.ALIGN_CENTER),
					50, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_BOTTOM));
			innerCell1.addElement(getParagraph("(регистрационный номер)", smallFont, Paragraph.ALIGN_CENTER));
			innerCell1.addElement(wrapElement(
					getParagraph(diplomeDate, regularFont, Paragraph.ALIGN_CENTER),
					30, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_BOTTOM));
			innerCell1.addElement(getParagraph("(дата выдачи)", smallFont, Paragraph.ALIGN_CENTER));
			innerCell1.addElement(wrapElement(
					getParagraph(card.getRemandCommand(), regularFont, Paragraph.ALIGN_CENTER),
					30, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_BOTTOM));
			innerCell1.addElement(getParagraph("(номер приказа)", smallFont, Paragraph.ALIGN_CENTER));
			innerCell1.addElement(wrapElement(
					getParagraph(comissionDate, regularFont, Paragraph.ALIGN_CENTER),
					30, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_BOTTOM));
			innerCell1.addElement(getParagraph("(дата приказа)", smallFont, Paragraph.ALIGN_CENTER));
			innerCell1.addElement(wrapElement(
					getParagraph(card.getRemandReason(), regularFont, Paragraph.ALIGN_CENTER),
					150, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_BOTTOM));
			innerCell1.addElement(getParagraph("(причина отчисления)", smallFont, Paragraph.ALIGN_CENTER));
			// Добавим данные о руководителе организации
			innerCell1.addElement(wrapElement(
					getParagraph("Руководитель образовательной организации", regularFont, Paragraph.ALIGN_CENTER),
					130, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_BOTTOM));
			innerCell1.addElement(wrapElement(
					getParagraph(schoolDirector, regularFont, Paragraph.ALIGN_CENTER),
					70, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_BOTTOM));

			// Большая колонка (СВЕДЕНИЯ О ЛИЧНОСТИ ОБЛАДАТЕЛЯ СПРАВКИ...)
			PdfPCell innerCell2 = new PdfPCell();
			innerCell2.setBorder(PdfPCell.NO_BORDER);
			innerCell2.addElement(getParagraph("СВЕДЕНИЯ О ЛИЧНОСТИ ОБЛАДАТЕЛЯ СПРАВКИ", regularFont, Paragraph.ALIGN_CENTER));
			innerCell2.addElement(wrapElement(
					getParagraph(psn.getFullName(), regularFont, Paragraph.ALIGN_CENTER),
					30, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_BOTTOM));
			innerCell2.addElement(getParagraph("(фамилия, имя, отчество)", smallFont, Paragraph.ALIGN_CENTER));
			innerCell2.addElement(wrapElement(
					getParagraph(birthDate, regularFont, Paragraph.ALIGN_CENTER),
					30, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_BOTTOM));
			innerCell2.addElement(getParagraph("(дата рождения)", smallFont, Paragraph.ALIGN_CENTER));
			innerCell2.addElement(wrapElement(
					getParagraph(oldDocument, regularFont, Paragraph.ALIGN_CENTER),
					30, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_BOTTOM));
			innerCell2.addElement(getParagraph("(предыдущий документ об образовании)", smallFont, Paragraph.ALIGN_CENTER));
			innerCell2.addElement(wrapElement(
					getParagraph("СВЕДЕНИЯ ОБ ОБРАЗОВАТЕЛЬНОЙ ПРОГРАММЕ "
					             + "СРЕДНЕГО ПРОФЕССИОНАЛЬНОГО ОБРАЗОВАНИЯ И О КВАЛИФИКАЦИИ", regularFont, Paragraph.ALIGN_CENTER),
					50, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_BOTTOM));
			innerCell2.addElement(wrapElement(
					getParagraph(speciality, regularFont, Paragraph.ALIGN_CENTER),
					50, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_BOTTOM));
			innerCell2.addElement(getParagraph("(специальность)", smallFont, Paragraph.ALIGN_CENTER));
			innerCell2.addElement(wrapElement(
					getParagraph(card.getPlan().getSpecialization(), regularFont, Paragraph.ALIGN_CENTER),
					30, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_BOTTOM));
			innerCell2.addElement(getParagraph("(специализация)", smallFont, Paragraph.ALIGN_CENTER));
			innerCell2.addElement(wrapElement(
					getParagraph(studyForm, regularFont, Paragraph.ALIGN_CENTER),
					30, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_BOTTOM));
			innerCell2.addElement(getParagraph("(форма обучения)", smallFont, Paragraph.ALIGN_CENTER));
			// Добавим дополнительные сведения в колонку
			innerCell2.addElement(wrapElement(
					getParagraph("ДОПОЛНИТЕЛЬНЫЕ СВЕДЕНИЯ", regularFont, Paragraph.ALIGN_CENTER),
					50, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_BOTTOM));
			innerCell2.addElement(wrapElement(prepareRenamingTable(
					renamings.findByDates(card.getBeginDate(), card.getEndDate())), 150.0f));
			// Добавим таблицу курсовых в колонку
			innerCell2.addElement(wrapElement(
					getParagraph("СВЕДЕНИЯ О СОДЕРЖАНИИ И РЕЗУЛЬТАТАХ ОСВОЕНИЯ "
					             + "ОБРАЗОВАТЕЛЬНОЙ ПРОГРАММЫ СРЕДНЕГО ПРОФЕССИОНАЛЬНОГО ОБРАЗОВАНИЯ",
							regularFont, Paragraph.ALIGN_CENTER), 60, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_BOTTOM));
			PdfPTable courseWorkTable = wrapElement(
					prepareCourseWorkTable(courseWorks.fetchAll(card), true), 150.0f);
			innerCell2.addElement(courseWorkTable);

			// Добавляем колонки
			mainTable.addCell(innerCell1);
			mainTable.addCell(innerCell2);
			document.add(mainTable);

			// Вторая страница
			document.newPage();

			PdfPTable marksTable = new PdfPTable(3);
			marksTable.setWidthPercentage(100.0f);
			marksTable.setTotalWidth(getPt(135));
			marksTable.setWidths(new int[]{10, 2, 2});
			marksTable.setSpacingBefore(35);
			// Заполняем данными страницы 2 и 3
			prepareMarkTable(marks, marksTable);
			document.add(marksTable);
			document.close();
		} catch (DocumentException e) {
			throw new EJBException(e.getMessage());
		}
		return stream.toByteArray();
	}

	/**
	 * Конструктор класса.
	 */
	public CardReportsEJB() {
	}
}
