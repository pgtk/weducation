package ru.edu.pgtk.weducation.utils;

import ru.edu.pgtk.weducation.entity.ExamForm;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJBException;
import org.w3c.dom.Node;
import ru.edu.pgtk.weducation.entity.Speciality;
import ru.edu.pgtk.weducation.entity.StudyPlan;
import static ru.edu.pgtk.weducation.utils.Utils.toInt;

/**
 * Класс, реализующий XML парсер файлов учебных планов.
 *
 * @author Воронин Леонид
 */
public class PlanParser {

  // Константы для тегов XML документа и сообщений
  public static String GYP = "GYP";
  public static String PLAN = "Plan";
  public static String BLOCK = "block";
  public static String KV_EXAMS = "kv_exams";
  public static String ITEM = "item";
  public static String DISC = "disc";
  public static String PRACTICE = "practice";
  public static String INDEX = "index";
  public static String NAME = "name";
  public static String AUD = "itogo";
  public static String THR = "lect";
  public static String SAM = "samN";
  public static String KP = "KP";
  public static String DOCUMENT = "Document";
  public static String TYPE = "type";
  public static String COURSE = "Course";
  public static String IMESTER = "Imestr";
  public static String SEMESTER = "semester";
  public static String SEMESTERS = "semesters_info";
  public static String NUM = "num";
  public static String GRAF = "graf";
  public static String RUP = "РУП";
  public static String TITLE = "Title";
  public static String SPEC_KEY = "spec_shifr";
  public static String SPEC_NAME = "spec_name";
  public static String SPEC_KV = "qualification";
  public static String ED_YEAR = "ed_years";
  public static String ED_MONTH = "ed_month";
  public static String PLAN_DATE = "ratif_date";
  public static String ED_TYPE = "ed_type";
  public static String BLOCK_NOT_FOUND = "Не обнаружено ни одного блока с именем ";
  public static String FILE_READ_ERROR = "Невозможно прочитать XML файл!";
  // Конец констант
  private final XMLDocument plan;

  /**
   * Определяет, является ли XML документ учебным планом.
   *
   * @return исмтина в случае, если документ является учебным планом. Иначе -
   * ложь.
   */
  public boolean isCorrect() {
    try {
      // Есть ли в документе хотя бы один блок Document?
      Node document = plan.getRootNode(DOCUMENT);
      // Есть ли в этом блоке атрибут type?
      String type = plan.getAttributeValue(document, TYPE);
      if (type.contains(RUP)) {
        // Будем считать, что это план, если в типе документа есть фрагмент РУП
        return true;
      }
      return false;
    } catch (NullPointerException e) {
      return false;
    }
  }

  /**
   * Возвращает список курсов, в виде экземпляров класса XMLCourse
   *
   * @return список экземпляров класса XMLCourse а-ля ArrayList
   */
  public List<XMLCourse> getCourses() {
    try {
      List<XMLCourse> result = new ArrayList<>();
      // Получим элемент документа (тег GYP)
      Node gyp = plan.getRootNode(GYP);
      // Получим все его дочерние элементы (курсы)
      for (Node cn : plan.getChildNodes(gyp, COURSE)) {
        // Создадим для каждого курса экземпляр Java класса XMLCourse
        XMLCourse c = new XMLCourse(toInt(plan.getAttributeValue(cn, NUM), 0));
        // Получим список семестров курса и пробежимся по ним в цикле
        for (Node sn : plan.getChildNodes(cn, IMESTER)) {
          // Считаем параметры семестра (номер и график недельной нагрузки)
          int number = toInt(plan.getAttributeValue(sn, NUM), 0);
          String graph = plan.getAttributeValue(sn, GRAF).replace("I", "");
          // Если количество недель равно нулю, то нам такой семестр не нужен
          if (graph.length() > 0) {
            c.addSemester(new XMLSemester(number, graph.length()));
          }
        }
        // Если в курсе нет семестров, то нам такой курс не нужен
        if (c.getSemestersCount() > 0) {
          result.add(c);
        }
      }
      return result;
    } catch (NullPointerException e) {
      throw new EJBException("NullPointerException в процессе импорта информации о курсах и семестрах!");
    }
  }

  /**
   * Читает заголовок учебного плана и возвращает его в виде класса
   * "Специальность".
   *
   * @return экземпляр класса Speciality
   */
  public Speciality getSpeciality() {
    try {
      Node title = plan.getRootNode(TITLE);
      Speciality sp = new Speciality();
      sp.setKey(plan.getAttributeValue(title, SPEC_KEY));
      sp.setFullName(plan.getAttributeValue(title, SPEC_NAME));
      sp.setKvalification(plan.getAttributeValue(title, SPEC_KV));
      sp.setShortName("FIXME");
      sp.setSpecialization("не предусмотрено");
      return sp;
    } catch (NullPointerException e) {
      throw new EJBException("NullPointerException в процессе получения информации о специальности!");
    }
  }

  /**
   * Возвращает шифр специальности
   *
   * @return Шифр специальности в виде строки
   */
  public String getSpecialityKey() {
    try {
      Node title = plan.getRootNode(TITLE);
      return plan.getAttributeValue(title, SPEC_KEY);
    } catch (NullPointerException e) {
      throw new EJBException("NullPointerException в процессе получения информации о специальности!");
    }
  }

  /**
   * Читает заголовок учебного плана и возвращает его в виде специального типа
   * "Учебный план".
   *
   * @return экземпляр класса StudyPlan
   */
  public StudyPlan getStudyPlan() {
    try {
      Node title = plan.getRootNode(TITLE);
      StudyPlan sp = new StudyPlan();
      sp.setName(plan.getAttributeValue(title, SPEC_KEY));
      sp.setDescription(plan.getAttributeValue(title, SPEC_NAME));
      sp.setYears(toInt(plan.getAttributeValue(title, ED_YEAR), 0));
      sp.setMonths(toInt(plan.getAttributeValue(title, ED_MONTH), 0));
      try {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        sp.setDate(sdf.parse(plan.getAttributeValue(title, PLAN_DATE)));
      } catch (ParseException e) {
        // do nothing;
      }
      sp.setExtramural(toInt(plan.getAttributeValue(title, ED_TYPE), 0) > 1);
      return sp;
    } catch (NullPointerException e) {
      throw new EJBException("NullPointerException в процессе получения информации о специальности!");
    }
  }

  /**
   * Извлекает из плана готовый список модулей с дисциплинами, практиками и
   * нагрузкой.
   *
   * @return
   */
  public List<XMLModule> getModules() {
    try {
      List<XMLModule> result = new ArrayList<>();
      // Получим элемент документа (тег Plan)
      Node p = plan.getRootNode(PLAN);
      // Получим все его дочерние элементы (модули)
      for (Node moduleNode : plan.getChildNodes(p, BLOCK)) {
        // Создадим для каждого модуля экземпляр Java класса XMLModule
        XMLModule m = new XMLModule(
                toInt(plan.getAttributeValue(moduleNode, TYPE), 0),
                plan.getAttributeValue(moduleNode, INDEX),
                plan.getAttributeValue(moduleNode, NAME));
        Node kvex = plan.getChildNode(moduleNode, KV_EXAMS);
        if (null != kvex) {
          m.setKvExams(plan.getChildNodes(kvex, ITEM).size());
        }
        // Получим список дисциплин модуля и пробежимся по ним в цикле
        for (Node subjectNode : plan.getChildNodes(moduleNode, DISC)) {
          // Считаем параметры дисциплины
          String index = plan.getAttributeValue(subjectNode, INDEX);
          String name = plan.getAttributeValue(subjectNode, NAME);
          XMLSubject subject = new XMLSubject(m, index, name);
          // Считаем расчасовку. Для этого - пробежимся по блокам semesters_info. Вообще, блок один, но вдруг их несколько...
          for (Node semesterInfoNode : plan.getChildNodes(subjectNode, SEMESTERS)) {
            // для каждого блока semesters_info найдем дочерние элементы. Это и будет расчасовка.
            for (Node loadNode : plan.getChildNodes(semesterInfoNode, SEMESTER)) {
              // Если расчасовка найдена, то парсим
              int aud = toInt(plan.getAttributeValue(loadNode, AUD), 0);
              int thr = toInt(plan.getAttributeValue(loadNode, THR), 0);
              int sam = toInt(plan.getAttributeValue(loadNode, SAM), 0);
              int cpr = toInt(plan.getAttributeValue(loadNode, KP), 0);
              // Вычисляем максимальнеую нагрузку
              int max = aud + sam;
              XMLSubjectLoad load = new XMLSubjectLoad(subject, toInt(plan.getAttributeValue(loadNode, NUM), 0),
                      max, aud, thr, cpr);
              // Предполагаем, что нет никакой формы контроля
              load.setExamType(ExamForm.NONE);
              // Корректируем форму контроля, в зависимости от обнаруженной информации об экзамене
              if (toInt(plan.getAttributeValue(loadNode, "count_ex"), 0) > 0) {
                load.setExamType(ExamForm.EXAM);
              }
              // или дифзачете
              if (toInt(plan.getAttributeValue(loadNode, "count_zdif"), 0) > 0) {
                load.setExamType(ExamForm.DIFZACHET);
              }
              // Если у нас указан другой тип формы контроля, то предполагаем, что это контрольная
              if (toInt(plan.getAttributeValue(loadNode, "count_Other"), 0) > 0) {
                load.setExamType(ExamForm.OTHER);
              }
              if (max > 0) {
                // Если максимальная нагрузка больше нуля, то добавляем расчасовку
                subject.addLoad(load);
              }
            }
          }
          m.addSubject(subject);
        }
        // Добавляем информацию о практиках
        for (Node practiceNode : plan.getChildNodes(moduleNode, PRACTICE)) {
          // Считаем параметры практики
          XMLPractice practice = new XMLPractice(m, plan.getAttributeValue(practiceNode, INDEX),
                  plan.getAttributeValue(practiceNode, NAME));
          // Теперь распарсим семестровую нагрузку
          for (Node semesterInfoNode : plan.getChildNodes(practiceNode, SEMESTERS)) {
            // для каждого блока semesters_info найдем дочерние элементы. Это и будет расчасовка.
            for (Node loadNode : plan.getChildNodes(semesterInfoNode, SEMESTER)) {
              // Если расчасовка найдена, то парсим
              int hours = toInt(plan.getAttributeValue(loadNode, "hours"), 0);
              int weeks = toInt(plan.getAttributeValue(loadNode, "weeks"), 0);
              XMLPracticeLoad load = new XMLPracticeLoad(practice, toInt(plan.getAttributeValue(loadNode, NUM), 0),
                      hours, weeks);
              // Предполагаем, что нет никакой формы контроля
              load.setExamType(ExamForm.NONE);
              // Корректируем форму контроля, в зависимости от обнаруженной информации об экзамене
              if (toInt(plan.getAttributeValue(loadNode, "count_ex"), 0) > 0) {
                load.setExamType(ExamForm.EXAM);
              }
              // или дифзачете
              if (toInt(plan.getAttributeValue(loadNode, "count_zdif"), 0) > 0) {
                load.setExamType(ExamForm.DIFZACHET);
              }
              // или зачете
              if (toInt(plan.getAttributeValue(loadNode, "count_z"), 0) > 0) {
                load.setExamType(ExamForm.ZACHET);
              }
              // Если у нас указан другой тип формы контроля, то предполагаем, что это контрольная
              if (toInt(plan.getAttributeValue(loadNode, "count_Other"), 0) > 0) {
                load.setExamType(ExamForm.OTHER);
              }
              if (hours > 0) {
                // Если максимальная нагрузка больше нуля, то добавляем расчасовку
                practice.addLoad(load);
              }
            }
          }
          m.addPractice(practice);
        }
        // Если в блоке нет ни дисциплин ни практик, то нам такой блок не нужен
        if ((m.getSubjectsCount() > 0) || (m.getPracticesCount() > 0)) {
          result.add(m);
        }
      }
      return result;
    } catch (NullPointerException e) {
      throw new EJBException("NullPointerException в процессе импорта информации о модулях и дисциплинах!");
    } catch (NumberFormatException e) {
      throw new EJBException("Ошибка преобразования строки в число в процессе импорта информации о модулях и дисциплинах!");
    }
  }

  public PlanParser(InputStream inputStream) {
    plan = new XMLDocument(inputStream);
  }
}
