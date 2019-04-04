package ru.edu.pgtk.weducation.core.importcards;

import ru.edu.pgtk.weducation.core.entity.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Бин для доступа к данным старых карточек.
 *
 * @author Voronin Leonid
 * @since 04.04.19
 **/
@Stateless
@Named("oldCardsEJB")
public class OldCardsEJB implements OldCardsDAO {
    private static final String MSSQL_RESOURCE = "jdbc/pgtk_mssql";
    private static final String NOT_FOUND = " (запрос не вернул ни одной записи)";
    private static final String ERROR = "Ошибка при импорте ";
    private static final String PLACE = " населенного пункта. ";
    private static final String SCHOOL = " учебного заведения. ";
    private static final String CARD = " личной карточки для студента с кодом ";
    private static final String PERSON = " персоны. ";
    private static final String GROUP = " группы с кодом ";
    private static final String SPECIALITY = " специальности. ";
    private static final String DELEGATES = " родителя (опекуна). ";
    private static final String STUDENTCODES = " при получении кодов студентов для группы с кодом ";

    private Connection con;


    @PostConstruct
    private void openConnection() {
        try {
            InitialContext initialContext = new InitialContext();
            DataSource dataSource = (DataSource) initialContext.lookup(MSSQL_RESOURCE);
            if (null != dataSource) {
                con = dataSource.getConnection();
                con.setReadOnly(true);
            }
        } catch (SQLException | NamingException e) {
            throw new EJBException("Ошибка при попытке соединения с БД " + MSSQL_RESOURCE, e);
        }
    }

    @PreDestroy
    private void closeConnection() {
        try {
            if (!con.isClosed()) {
                con.close();
            }
        } catch (Exception e) {
            throw new EJBException("Ошибка при закрытии соединения с БД " + MSSQL_RESOURCE, e);
        }
    }


    @Override
    public Map<String, String> getGroups() {
        LinkedHashMap<String, String> result = new LinkedHashMap<>();
        try {
            PreparedStatement grpStatement = prepareStatement("SELECT * FROM groups WHERE ((SELECT COUNT(*) FROM Students WHERE (st_grcode = gr_pcode)) > 0) ORDER BY gr_Name;");
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


    /**
     * Получаем специальность по коду группы
     *
     * @param groupCode код группы
     * @return Специальность
     */
    @Override
    public Speciality getSpeciality(final String groupCode) {
        try {
            PreparedStatement stmt = prepareStatement("SELECT * FROM Specialities, Groups WHERE (sp_pcode = gr_speccode) AND (gr_pcode=?)");
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
        } catch (SQLException | NullPointerException e) {
            throw new EJBException(ERROR + SPECIALITY, e);
        }
    }

    @Override
    public School getSchool(final String personCode) {
        try {
            PreparedStatement stmt = prepareStatement("SELECT schools.* FROM students, schools WHERE (st_pcode = ?) AND (st_sccode = sc_pcode)");
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
        } catch (SQLException | NullPointerException e) {
            throw new EJBException(ERROR + SCHOOL, e);
        }
    }

    @Override
    public Place getPlace(final String personCode) {
        try {
            PreparedStatement stmt = prepareStatement("SELECT * FROM places, Students WHERE (pl_pcode = st_plcode) AND (st_pcode = ?)");
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
        } catch (Exception e) {
            throw new EJBException(ERROR + PLACE, e);
        }
    }

    @Override
    public StudyGroup getGroup(String groupCode) {
        try {
            PreparedStatement stmt = prepareStatement("SELECT *, (SELECT wk_Name FROM workers WHERE (wk_pcode = gr_mastercode))AS gr_masterName FROM groups WHERE (gr_pcode=?);");
            stmt.setString(1, groupCode);
            ResultSet groupRS = stmt.executeQuery();
            if (groupRS.first()) {
                // Группа есть, читаем детали...
                String name = groupRS.getString("gr_Name");
                StudyGroup group = new StudyGroup();
                group.setName(name);
                group.setMaster(groupRS.getString("gr_masterName"));
                group.setExtramural(groupRS.getBoolean("gr_isZaoch"));
                group.setCommercial(groupRS.getBoolean("gr_Commercial"));
                group.setActive(groupRS.getInt("gr_Attributes") == 0);
                group.setYear(groupRS.getInt("gr_CreateYear"));
                group.setCourse(groupRS.getInt("gr_Course"));
                return group;
            } else {
                throw new EJBException("Детали группы не обнаружены!");
            }
        } catch (SQLException | NullPointerException e) {
            throw new EJBException(ERROR + GROUP + groupCode, e);
        }
    }

    /**
     * Возвращает список кодов студентов.
     * @param grpCode код группы
     * @return список кодов студентов, либо пустой список, если ни одного студента в группе нет
     */
    @Override
    public List<String> getStudentCodes(String grpCode) {
        List<String> studentsCodes = new LinkedList<>();
        try {
            PreparedStatement stmt = prepareStatement("SELECT st_pcode FROM students WHERE (st_grcode=?)");
            stmt.setString(1, grpCode);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                studentsCodes.add(rs.getString("st_pcode"));
            }
        } catch (SQLException | NullPointerException e) {
            throw new EJBException(ERROR + STUDENTCODES + grpCode, e);
        }
        return studentsCodes;
    }

    /**
     * Возвращает информацию о персоне
     * @param personCode код персоны в старой БД
     * @return класс персоны для новой БД
     */
    @Override
    public Person getPerson(final String personCode) {
        try {
            PreparedStatement stmt = prepareStatement("SELECT * FROM students WHERE (st_pcode=?)");
            stmt.setString(1, personCode);
            ResultSet rs = stmt.executeQuery();
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
            throw new EJBException(ERROR + PERSON + NOT_FOUND);
        } catch (Exception e) {
            throw new EJBException(ERROR + PERSON, e);
        }
    }

    @Override
    public List<Delegate> getDelegates(final String personCode) {
        List<Delegate> result = new LinkedList<>();
        try {
            PreparedStatement stmt = prepareStatement("SELECT * FROM parents WHERE (pr_stcode=?)");
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
        } catch (Exception e) {
            throw new EJBException(ERROR + DELEGATES, e);
        }
        return result;
    }

    @Override
    public StudyCard getCard(final String personCode) {
        try {
            PreparedStatement stmt = prepareStatement("SELECT *, "
                    + "(SELECT com_PDirector FROM comissions, sessions WHERE (ss_comcode = com_pcode) AND (ss_stcode = st_pcode)) AS com_PDirector, "
                    + "(SELECT com_Date FROM comissions, sessions WHERE (ss_comcode = com_pcode) AND (ss_stcode = st_pcode)) AS com_Date "
                    + "FROM students LEFT JOIN commands ON (cm_stcode = st_pcode) "
                    + "WHERE (st_pcode= ?)");
            stmt.setString(1, personCode);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.first()) {
                    StudyCard card = new StudyCard();
                    card.setBeginDate(getDateFromString("01.09." + rs.getString("st_inYear")));
                    card.setEndDate(getDateFromString("30.06." + rs.getString("st_outYear")));
                    card.setDocumentDate(getDateFromString("25.06." + rs.getString("st_documentsYear")));
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
                    card.setDocumentOrganization(""); // нет сведений в старой базе!
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
            throw new EJBException(ERROR + CARD + personCode + NOT_FOUND);
        } catch (Exception e) {
            throw new EJBException(ERROR + CARD + personCode, e);
        }
    }


    private PreparedStatement prepareStatement(String query) throws SQLException {
        return con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
    }

    private Date getDateFromString(String dateString) {
        if (dateString == null) {
            throw new IllegalArgumentException("Нельзя просто так взять и преобразовать в дату пустую строку!");
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        try {
            return sdf.parse(dateString.trim());
        } catch (Exception e) {
            // Наверное, некорректно возвращать текущую дату, которую могут принять за настоящую и испортить бланк.
            // Поэтому пока бросим исключение. Транзакция откатится и будем разбираться.
            throw new IllegalArgumentException("Ошибка преобразования в дату строки " + dateString, e);
        }
    }
}
