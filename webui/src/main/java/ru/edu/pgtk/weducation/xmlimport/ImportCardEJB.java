package ru.edu.pgtk.weducation.xmlimport;

import ru.edu.pgtk.weducation.data.entity.*;
import ru.edu.pgtk.weducation.ejb.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Stateless
public class ImportCardEJB {

  @EJB
  private PlacesEJB places;
  @EJB
  private SchoolsDAO schools;
  @EJB
  private SpecialitiesDAO specialities;
  @EJB
  private PersonsDAO persons;
  @EJB
  private StudycardsDAO cards;
  @EJB
  private DelegatesEJB delegates;
  @EJB
  private StudyGroupsDAO groups;
  private Connection con;

  private static final String NOT_FOUND = "Запрос не вернул ни одной записи.";
  private static final String NULL = "При обработке возникло NullPointerException.";
  private static final String ERROR = "Ошибка при импорте ";
  private static final String PLACE = " населенного пункта. ";
  private static final String SCHOOL = " учебного заведения. ";
  private static final String CARD = " личной карточки. ";
  private static final String PERSON = " персоны. ";
  private static final String SPECIALITY = " специальности. ";
  private static final Date FAKE_DATE = new Date();

  @PostConstruct
  private void openConnection() {
    try {
      InitialContext initialContext = new InitialContext();
      DataSource dataSource = (DataSource) initialContext.lookup("jdbc/pgtk_mssql");
      if (null != dataSource) {
        con = dataSource.getConnection();
      }
    } catch (SQLException | NamingException e) {
      throw new EJBException("Ошибка! " + e.toString());
    }
  }

  @PreDestroy
  private void closeConnection() {
    try {
      con.close();
    } catch (Exception e) {
      // И что тут делать?
    }
  }

  /**
   * Получаем специальность по коду группы
   *
   * @param groupCode код группы
   * @return
   */
  private Speciality getSpeciality(final String groupCode) {
    try {
      PreparedStatement stmt = con
              .prepareStatement(
                      "SELECT * FROM Specialities, Groups WHERE (sp_pcode = gr_speccode) AND (gr_pcode=?);",
                      ResultSet.TYPE_SCROLL_INSENSITIVE,
                      ResultSet.CONCUR_READ_ONLY);
      stmt.setString(1, groupCode);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.first()) {
          // Что-то нашли, выбираем данные
          Speciality result = new Speciality();
          result.setDescription(rs.getString("sp_name"));
          result.setName(rs.getString("sp_shortName"));
          return result;
        }
      }
      throw new EJBException(ERROR + SPECIALITY + NOT_FOUND);
    } catch (SQLException e) {
      throw new EJBException(ERROR + SPECIALITY + e.getMessage());
    } catch (NullPointerException e) {
      throw new EJBException(ERROR + SPECIALITY + NULL);
    }
  }

  private School getSchool(final String personCode) {
    try {
      PreparedStatement stmt = con.prepareStatement(
              "SELECT schools.* FROM students, schools WHERE (st_pcode = ?) AND (st_sccode = sc_pcode);",
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      stmt.setString(1, personCode);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.first()) {
          School result = new School();
          result.setFullName(rs.getString("sc_Name"));
          result.setShortName(rs.getString("sc_ShortName"));
          result.setPlace(rs.getString("sc_place"));
          result.setDirector(rs.getString("sc_DName"));
          return result;
        }
      }
      throw new EJBException(ERROR + SCHOOL + NOT_FOUND);
    } catch (SQLException e) {
      throw new EJBException(ERROR + SCHOOL + e.getMessage());
    } catch (NullPointerException e) {
      throw new EJBException(ERROR + SCHOOL + NULL);
    }
  }

  private Place getPlace(final String personCode) {
    try {
      PreparedStatement stmt = con.prepareStatement(
              "SELECT * FROM places, Students WHERE (pl_pcode = st_plcode) AND (st_pcode = ?);",
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      stmt.setString(1, personCode);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.first()) {
          // Читаем поля записи
          Place result = new Place();
          result.setName(rs.getString("pl_Name"));
          result.setType(PlaceType.forValue(rs.getInt("pl_kind") - 1));
          return result;
        }
      }
      return null;
    } catch (SQLException e) {
      throw new EJBException(ERROR + PLACE + e.getMessage());
    } catch (NullPointerException e) {
      throw new EJBException(ERROR + PLACE + NULL);
    }
  }

  private Person getPerson(final String personCode) {
    try {
      PreparedStatement stmt = con.prepareStatement(
              "SELECT * FROM students WHERE (st_pcode=?);",
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      stmt.setString(1, personCode);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.first()) {
          Person result = new Person();
          result.setPassportSeria(rs.getString("st_passptSeria"));
          result.setPassportNumber(rs.getString("st_PassptNum"));
          result.setFirstName(rs.getString("st_FName"));
          result.setMiddleName(rs.getString("st_MName"));
          result.setLastName(rs.getString("st_LName"));
          Date birthDate = rs.getDate("st_birthDate");
          if (null == birthDate) {
            // Если дата рождения не указана, то она будет текущей
            birthDate = new Date();
          }
          result.setBirthDate(birthDate);
          result.setBirthPlace(rs.getString("st_birthPlace"));
          result.setMale(rs.getBoolean("st_ismale"));
          result.setForeign(false);
          result.setInvalid(false);
          result.setPhones(rs.getString("st_Phones"));
          result.setPassportDate(rs.getDate("st_passptDate"));
          result.setPassportDept(rs.getString("st_passptDept"));
          result.setAddress(rs.getString("st_Address"));
          result.setInn(rs.getString("st_INN"));
          result.setSnils(rs.getString("st_socialNum"));
          result.setOrphan(rs.getBoolean("st_ParentLess"));
          result.setLanguage(ForeignLanguage.forValue(rs.getInt("st_Lang") - 1));
          return result;
        }
      }
      throw new EJBException(ERROR + PERSON + NOT_FOUND);
    } catch (SQLException e) {
      throw new EJBException(ERROR + PERSON + e.getMessage());
    } catch (NullPointerException e) {
      throw new EJBException(ERROR + PERSON + NULL);
    }
  }

  private List<Delegate> getDelegates(final String personCode) {
    List<Delegate> result = new ArrayList<>();
    try {
      PreparedStatement stmt = con.prepareStatement(
              "SELECT * FROM parents WHERE (pr_stcode=?);",
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      stmt.setString(1, personCode);
      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          Delegate item = new Delegate();
          item.setFullName(rs.getString("pr_Name"));
          item.setJob(rs.getString("pr_JobPlace"));
          item.setPost(rs.getString("pr_Job"));
          item.setPhones(rs.getString("pr_Phones"));
          item.setDescription(rs.getString("pr_Note"));
          result.add(item);
        }
      }
    } catch (SQLException e) {
      throw new EJBException(ERROR + SCHOOL + e.getMessage());
    } catch (NullPointerException e) {
      throw new EJBException(ERROR + SCHOOL + NULL);
    }
    return result;
  }

  private StudyCard getCard(final String personCode) {
    try {
      PreparedStatement stmt = con.prepareStatement(
              "SELECT *, "
              + "(SELECT com_PDirector FROM comissions, sessions WHERE (ss_comcode = com_pcode) AND (ss_stcode = st_pcode)) AS com_PDirector, "
              + "(SELECT com_Date FROM comissions, sessions WHERE (ss_comcode = com_pcode) AND (ss_stcode = st_pcode)) AS com_Date "
              + "FROM students LEFT JOIN commands ON (cm_stcode = st_pcode) "
              + "WHERE (st_pcode= ?)",
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      stmt.setString(1, personCode);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.first()) {
          StudyCard card = new StudyCard();
          SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
          try {
            card.setBeginDate(sdf.parse("01.09." + rs.getString("st_inYear")));
          } catch (ParseException | NullPointerException e) {
            card.setBeginDate(FAKE_DATE);
          }
          try {
            card.setEndDate(sdf.parse("30.06." + rs.getString("st_outYear")));
          } catch (ParseException | NullPointerException e) {
            card.setEndDate(FAKE_DATE);
          }
          try {
            card.setDocumentDate(sdf.parse("25.06." + rs.getString("st_documentsYear").trim()));
          } catch (ParseException | NullPointerException e) {
            card.setDocumentDate(FAKE_DATE);
          }
          int attributes = rs.getInt("st_Attributes");
          card.setActive(attributes == 0);
          card.setRemanded((attributes & 127) > 0);
          if (card.isRemanded()) {
            card.setRemandReason(rs.getString("cm_Text"));
            card.setRemandCommand(rs.getString("cm_Number"));
            card.setEndDate(rs.getDate("cm_Date"));
          }
          card.setRed(rs.getBoolean("st_isRed"));
          card.setComissionDirector(rs.getString("com_PDirector"));
          card.setComissionDate(rs.getDate("com_Date"));
          card.setDocumentName(rs.getString("st_documents"));
          card.setDocumentOrganization(""); // Ну нет сведений в старой базе!
          card.setDiplomeNumber(rs.getString("st_DiplNum"));
          card.setRegistrationNumber(rs.getString("st_DiplRegNum"));
          card.setDiplomeDate(rs.getDate("st_diplGetDate"));
          card.setBiletNumber(rs.getString("st_studNumber"));
          card.setCommercial(rs.getBoolean("st_isCommercial"));
          String theme = rs.getString("st_DProject");
          card.setGosExam(true);
          card.setDiplomeMark(rs.getInt("st_GOSMark"));
          if ((null != theme) && (!theme.isEmpty())) {
            card.setDiplomeTheme(theme);
            card.setGosExam(false);
            card.setDiplomeMark(rs.getInt("st_DMark"));
          }
          card.setExtramural(rs.getBoolean("st_isOutZaoch"));
          return card;
        }
      }
      throw new EJBException(ERROR + CARD + NOT_FOUND);
    } catch (SQLException e) {
      throw new EJBException(ERROR + CARD + e.getMessage());
    } catch (NullPointerException e) {
      throw new EJBException(ERROR + CARD + NULL);
    }
  }

  /**
   * Импортирует все ыозможные данные.
   *
   */
  public void importAll() {
    try {
      PreparedStatement grpStatement = con.prepareStatement(
              "SELECT * FROM groups WHERE ((SELECT COUNT(*) FROM Students WHERE (st_grcode = gr_pcode)) > 0);",
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      ResultSet grpRS = grpStatement.executeQuery();
      while (grpRS.next()) {
        importGroup(grpRS.getString("gr_pcode"));
      }
    } catch (SQLException e) {
      throw new EJBException("Исключение при импорте данных " + e.getMessage());
    }
  }

  public void importGroup(final String grpCode) {
    try {
      StudyGroup group;
      // Специальность
      Speciality spc = getSpeciality(grpCode);
      Speciality exSpeciality = specialities.findLike(spc);
      if (exSpeciality != null) {
        spc = exSpeciality;
      } else {
        specialities.save(spc);
      }
      // Получим данные для группы
      PreparedStatement stmt = con.prepareStatement(
              "SELECT *, (SELECT wk_Name FROM workers WHERE (wk_pcode = gr_mastercode))AS gr_masterName FROM groups WHERE (gr_pcode=?);",
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      stmt.setString(1, grpCode);
      try (ResultSet groupRS = stmt.executeQuery()) {
        if (groupRS.first()) {
          // Группа есть, читаем детали...
          String name = groupRS.getString("gr_Name");
          group = groups.findByName(name);
          if (group == null) {
            group = new StudyGroup();
            group.setName(name);
            group.setMaster(groupRS.getString("gr_masterName"));
            group.setExtramural(groupRS.getBoolean("gr_isZaoch"));
            group.setSpeciality(spc);
            group.setCommercial(groupRS.getBoolean("gr_Commercial"));
            group.setActive(groupRS.getInt("gr_Attributes") == 0);
            group.setYear(groupRS.getInt("gr_CreateYear"));
            group.setCourse(groupRS.getInt("gr_Course"));
            groups.save(group);
          }
        } else {
          throw new EJBException("Детали группы не обнаружены!");
        }
      }
      // Получаем студентов
      stmt = con.prepareStatement(
              "SELECT st_pcode FROM students WHERE (st_grcode=?);",
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      stmt.setString(1, grpCode);
      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          String personCode = rs.getString("st_pcode");
          // Импортируем населенный пункт
          Place place = getPlace(personCode);
          if (place != null) {
            Place exPlace = places.findLike(place);
            if (null != exPlace) {
              place = exPlace;
            } else {
              places.save(place);
            }
          }
          // Импортируем учебное заведение
          School scl = getSchool(personCode);
          School exSchool = schools.findLike(scl);
          if (null != exSchool) {
            scl = exSchool;
          } else {
            schools.save(scl);
          }
          Person psn = getPerson(personCode);
          Person exPerson = persons.findLike(psn);
          if (exPerson != null) {
            psn = exPerson;
          }
          if (psn.getId() > 0) {
            // А вдруг есть старые карточки? Удалим!
            for (StudyCard c : cards.findByPerson(psn)) {
              cards.delete(c);
            }
            // Удаляем имеющихся делегатов у персоны
            for (Delegate d : delegates.fetchAll(psn)) {
              delegates.delete(d);
            }
          }
          psn.setPlace(place);
          persons.save(psn);
          // Собираем карточку...
          StudyCard card = getCard(personCode);
          card.setSpeciality(spc);
          card.setGroup(group);
          card.setPerson(psn);
          card.setSchool(scl);
          // Сохраняем...
          cards.save(card);
          // Импортируем делегатов
          for (Delegate d : getDelegates(personCode)) {
            d.setPerson(psn);
            delegates.save(d);
          }
        }
      }
    } catch (SQLException e) {
      throw new EJBException("Исключение при импорте группы " + e.getMessage());
    }
  }

  public Map<String, String> getGroups() {
    LinkedHashMap<String, String> result = new LinkedHashMap<>();
    try {
      PreparedStatement grpStatement = con.prepareStatement(
              "SELECT * FROM groups WHERE ((SELECT COUNT(*) FROM Students WHERE (st_grcode = gr_pcode)) > 0) ORDER BY gr_Name;",
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      try (ResultSet grpRS = grpStatement.executeQuery()) {
        while (grpRS.next()) {
          result.put(grpRS.getString("gr_Name"), grpRS.getString("gr_pcode"));
        }
      }
    } catch (SQLException e) {
      throw new EJBException("Исключение при импорте данных " + e.getMessage());
    }
    return result;
  }
}
