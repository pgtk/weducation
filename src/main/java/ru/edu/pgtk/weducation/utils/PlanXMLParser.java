package ru.edu.pgtk.weducation.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.edu.pgtk.weducation.entity.Speciality;

/**
 * Класс, реализующий XML парсер файлов учебных планов.
 *
 * @author Воронин Леонид
 */
public class PlanXMLParser {

  // Константы для тегов XML документа и сообщений
  public static String GYP = "GYP";
  public static String PLAN = "Plan";
  public static String BLOCK = "block";
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
  public static String BLOCK_NOT_FOUND = "Не обнаружено ни одного блока с именем ";
  public static String FILE_READ_ERROR = "Невозможно прочитать XML файл!";
  // Конец констант
  private Document studyPlan;  // XML документ учебного плана

  /**
   * иннициализирует XMLDocument.
   *
   * @throws ImportException при невозможности иннициализации
   */
  private void initDocument(InputStream istream) {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      studyPlan = builder.parse(istream);
    } catch (IOException | ParserConfigurationException | SAXException | NullPointerException e) {
      throw new EJBException(e.getMessage());
    }
  }

  /**
   * Получает список коренных элементов (тегов) по имени.
   *
   * Элементы получаются посредством getElementsByTagName(tagName) и копируются
   * в ArrayList
   *
   * @param tagName имя тега
   * @return список элементов в виде NodeList
   * @throws ImportException при отсутствии тегов с таким именем
   */
  private List<Node> getRootNodes(String tagName) {
    try {
      List<Node> result = new ArrayList<>();
      NodeList nodes = studyPlan.getElementsByTagName(tagName);
      if (nodes.getLength() > 0) {
        for (int n = 0; n < nodes.getLength(); n++) {
          result.add(nodes.item(n));
        }
      } else {
        throw new EJBException(BLOCK_NOT_FOUND + tagName);
      }
      return result;
    } catch (NullPointerException e) {
      throw new EJBException(BLOCK_NOT_FOUND + tagName);
    }
  }

  /**
   * Возвращает один элемент с указаным именем.
   *
   * Метод получает все элементы документа с указаным именем и возвращает первый
   * из них.
   *
   * @param tagName имя тега
   * @return экземпляр класса Node
   * @throws ImportException При невозможности вернуть элемент
   */
  private Node getRootNode(String tagName) {
    try {
      NodeList nodes = studyPlan.getElementsByTagName(tagName);
      if (nodes.getLength() > 0) {
        return nodes.item(0);
      } else {
        throw new EJBException(BLOCK_NOT_FOUND + tagName);
      }
    } catch (NullPointerException e) {
      throw new EJBException(BLOCK_NOT_FOUND + tagName);
    }
  }

  /**
   * Возвращает список дочерних элементов с указанным именем
   *
   * @param node элемент, среди дочерних элементов которого проводится поиск
   * @param tagName имя дочернего элемента
   * @return список экземпляров класса Node
   * @throws ImportException при возникновении NullPointerException
   */
  private List<Node> getChildNodes(Node node, String tagName) {
    try {
      List<Node> result = new ArrayList<>();
      if (node.hasChildNodes()) {
        // Если есть дочерние элементы, то получаем их
        NodeList nodes = node.getChildNodes();
        for (int n = 0; n < nodes.getLength(); n++) {
          // Пробегаем по ним циклом и отбираем только те, имя которых соответствует требуемому
          if (nodes.item(n).getNodeName().contentEquals(tagName)) {
            result.add(nodes.item(n));
          }
        }
      }
      return result;
    } catch (NullPointerException e) {
      throw new EJBException("NullPointerException при поиске дочерних элементов с именем " + tagName);
    }
  }

  /**
   * Возвращает значение атрибута тега по имени атрибута
   *
   * @param node тег в виде экземпляра класса Node
   * @param attributeName наименование атрибута
   * @return Значение атрибута в виде строки
   * @throws ImportException
   */
  private String getAttributeValue(Node node, String attributeName) {
    try {
      // TODO Может стоит возвращать null при отсутствии атрибута с таким именем?
      if (node.hasAttributes()) {
        NamedNodeMap attributes = node.getAttributes();
        for (int a = 0; a < attributes.getLength(); a++) {
          if (attributes.item(a).getNodeName().contentEquals(attributeName)) {
            return attributes.item(a).getNodeValue();
          }
        }
      }
      return null;
    } catch (NullPointerException e) {
      throw new EJBException("NullPointerException при попытке получения значения атрибута " + attributeName);
    }
  }

  /**
   * Проверяет наличие атрибута с указанным именем.
   *
   * @param node Элемент, у которого проверяется наличие атрибута
   * @param attributeName имя атрибута
   * @return истина тогда в случае, если атрибут с таким именем присутствует.
   * Иначе - ложь.
   * @throws ImportException
   */
  private boolean attributeExists(Node node, String attributeName) {
    try {
      if (node.hasAttributes()) {
        NamedNodeMap attributes = node.getAttributes();
        for (int a = 0; a < attributes.getLength(); a++) {
          if (attributes.item(a).getNodeName().contentEquals(attributeName)) {
            return true;
          }
        }
      }
      return false;
    } catch (NullPointerException e) {
      throw new EJBException("NullPointerException при попытке получения значения атрибута " + attributeName);
    }
  }

  /**
   * Преобразует значение из строкового в целое. Если преобразование невозможно,
   * то присваивается значение по умолчанию.
   *
   * @param stringValue строка, которую надо преобразовать в число
   * @param defaultValue значение по умолчанию
   * @return целое число
   */
  private int toInt(String stringValue, int defaultValue) {
    int result;
    try {
      if (stringValue == null) {
        return defaultValue;
      }
      result = Integer.parseInt(stringValue);
    } catch (NumberFormatException e) {
      result = defaultValue;
    }
    return result;
  }

  /**
   * Определяет, является ли XML документ учебным планом.
   *
   * @return исмтина в случае, если документ является учебным планом. Иначе -
   * ложь.
   */
  public boolean isCorrectXML() {
    try {
      // Есть ли в документе хотя бы один блок Document?
      Node document = getRootNode(DOCUMENT);
      // Есть ли в этом блоке атрибут type?
      String type = getAttributeValue(document, TYPE);
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
   * Возвращает список курсов, в виде экземпляров класса Course
   *
   * @return список экземпляров класса Course а-ля ArrayList
   */
  public List<Course> getCourses() {
    try {
      List<Course> result = new ArrayList<>();
      // Получим элемент документа (тег GYP)
      Node gyp = getRootNode(GYP);
      // Получим все его дочерние элементы (курсы)
      for (Node cn : getChildNodes(gyp, COURSE)) {
        // Создадим для каждого курса экземпляр Java класса Course
        Course c = new Course(toInt(getAttributeValue(cn, NUM), 0));
        // Получим список семестров курса и пробежимся по ним в цикле
        for (Node sn : getChildNodes(cn, IMESTER)) {
          // Считаем параметры семестра (номер и график недельной нагрузки)
          int number = toInt(getAttributeValue(sn, NUM), 0);
          String graph = getAttributeValue(sn, GRAF).replace("I", "");
          // Если количество недель равно нулю, то нам такой семестр не нужен
          if (graph.length() > 0) {
            c.addSemester(new Semester(number, graph.length()));
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
  
  public Speciality getSpeciality() {
    try {
      Node title = getRootNode(TITLE);
      Speciality sp = new Speciality();
      sp.setKey(getAttributeValue(title, SPEC_KEY));
      sp.setFullName(getAttributeValue(title, SPEC_NAME));
      sp.setKvalification(getAttributeValue(title, SPEC_KV));
      return sp;
    } catch (NullPointerException e) {
      throw new EJBException("NullPointerException в процессе получения информации о специальности!");
    }
  }

  /**
   * Извлекает из плана готовый список модулей с дисциплинами, практиками и нагрузкой.
   * @return 
   */
  public List<Module> getModules() {
    try {
      List<Module> result = new ArrayList<>();
      // Получим элемент документа (тег Plan)
      Node plan = getRootNode(PLAN);
      // Получим все его дочерние элементы (модули)
      for (Node moduleNode : getChildNodes(plan, BLOCK)) {
        // Создадим для каждого модуля экземпляр Java класса Module
        Module m = new Module(getAttributeValue(moduleNode, INDEX), getAttributeValue(moduleNode, NAME));
        // Получим список дисциплин модуля и пробежимся по ним в цикле
        for (Node subjectNode : getChildNodes(moduleNode, DISC)) {
          // Считаем параметры дисциплины
          String index = getAttributeValue(subjectNode, INDEX);
          String name = getAttributeValue(subjectNode, NAME);
          Subject subject = new Subject(m, index, name);
          // Считаем расчасовку. Для этого - пробежимся по блокам semesters_info. Вообще, блок один, но вдруг их несколько...
          for (Node semesterInfoNode : getChildNodes(subjectNode, SEMESTERS)) {
            // для каждого блока semesters_info найдем дочерние элементы. Это и будет расчасовка.
            for (Node loadNode : getChildNodes(semesterInfoNode, SEMESTER)) {
              // Если расчасовка найдена, то парсим
              int aud = toInt(getAttributeValue(loadNode, AUD), 0);
              int thr = toInt(getAttributeValue(loadNode, THR), 0);
              int sam = toInt(getAttributeValue(loadNode, SAM), 0);
              int cpr = toInt(getAttributeValue(loadNode, KP), 0);
              // Вычисляем максимальнеую нагрузку
              int max = aud + sam;
              SubjectLoad load = new SubjectLoad(subject, toInt(getAttributeValue(loadNode, NUM), 0),
                      max, aud, thr, cpr);
              // Предполагаем, что нет никакой формы контроля
              load.setExamType(ExamType.NONE);
              // Корректируем форму контроля, в зависимости от обнаруженной информации об экзамене
              if (toInt(getAttributeValue(loadNode, "count_ex"), 0) > 0) {
                load.setExamType(ExamType.EXAM);
              }
              // или дифзачете
              if (toInt(getAttributeValue(loadNode, "count_zdif"), 0) > 0) {
                load.setExamType(ExamType.ZDIF);
              }
              // Если у нас указан другой тип формы контроля, то предполагаем, что это контрольная
              if (toInt(getAttributeValue(loadNode, "count_Other"), 0) > 0) {
                load.setExamType(ExamType.KNTR);
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
        for (Node practiceNode : getChildNodes(moduleNode, PRACTICE)) {
          // Считаем параметры практики
          Practice practice = new Practice(m, getAttributeValue(practiceNode, INDEX),
                  getAttributeValue(practiceNode, NAME));
          // Теперь распарсим семестровую нагрузку
          for (Node semesterInfoNode : getChildNodes(practiceNode, SEMESTERS)) {
            // для каждого блока semesters_info найдем дочерние элементы. Это и будет расчасовка.
            for (Node loadNode : getChildNodes(semesterInfoNode, SEMESTER)) {
              // Если расчасовка найдена, то парсим
              int hours = toInt(getAttributeValue(loadNode, "hours"), 0);
              int weeks = toInt(getAttributeValue(loadNode, "weeks"), 0);
              PracticeLoad load = new PracticeLoad(practice, toInt(getAttributeValue(loadNode, NUM), 0),
                      hours, weeks);
              // Предполагаем, что нет никакой формы контроля
              load.setExamType(ExamType.NONE);
              // Корректируем форму контроля, в зависимости от обнаруженной информации об экзамене
              if (toInt(getAttributeValue(loadNode, "count_ex"), 0) > 0) {
                load.setExamType(ExamType.EXAM);
              }
              // или дифзачете
              if (toInt(getAttributeValue(loadNode, "count_zdif"), 0) > 0) {
                load.setExamType(ExamType.ZDIF);
              }
              // или зачет
              if (toInt(getAttributeValue(loadNode, "count_z"), 0) > 0) {
                load.setExamType(ExamType.ZCHT);
              }
              // Если у нас указан другой тип формы контроля, то предполагаем, что это контрольная
              if (toInt(getAttributeValue(loadNode, "count_Other"), 0) > 0) {
                load.setExamType(ExamType.KNTR);
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

//  public PlanXMLParser(File file) {
//    this.xmlFile = file;
//    initDocument();
//  }

  public PlanXMLParser(InputStream inputStream) {
    initDocument(inputStream);
  }
}
