package ru.edu.pgtk.weducation.webtest.jsf;

import ru.edu.pgtk.weducation.core.ejb.*;
import ru.edu.pgtk.weducation.core.entity.*;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Компонент-подложка для конкретного теста, выбранного пользователем
 * Created by leonid on 21.05.16.
 */
@Named("testBean")
@ViewScoped
public class TestBean extends AbstractBean implements Serializable {

    static final long MINUTE = 60000;// минута в милисекундах
    @EJB
    private transient TestsDAO testsDao;
    @EJB
    private transient QuestionsDAO questionsDao;
    @EJB
    private transient AnswersDAO answersDao;
    @EJB
    private transient TestSessionsDAO testSessionsDao;
    @EJB
    private transient TestDetailsDAO testDetailsDao;
    @Inject
    private transient SessionBean sessionBean;
    private int testCode;
    private Test test;
    private Person person;
    private SortedMap<Question, List<Answer>> questions;
    private boolean testing;
    private Date beginDate;
    private Date endDate;
    private TestSession currentSession;
    private Question currentQuestion;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private void reset() {
        testing = false;
        beginDate = null;
        endDate = null;
        currentSession = null;
        questions = null;
    }

    public void loadTest() {
        if (testsDao != null) {
            try {
                test = testsDao.get(testCode);
                person = sessionBean.getPerson();
            } catch (Exception e) {
                test = null;
                person = null;
                addErrorMessage(e);
            }
        }
    }

    public void beginTest() {
        if (!isError() && isCanRunTest()) {
            try {
                questions = new TreeMap<>();
                // Выбираем вопросы с вариантами ответов
                for (Question question : questionsDao.fetchForTest(test)) {
                    List<Answer> answers = answersDao.fetchForQuestion(question);
                    questions.put(question, answers);
                }
                if (!questions.isEmpty()) {
                    // Вычислить время начала и окончания
                    beginDate = new Date();
                    if (test.getTimeForTest() > 0) {
                        endDate = new Date(beginDate.getTime() + test.getTimeForTest() * MINUTE);
                    }
                    // Записать результат тестирования
                    currentSession = new TestSession();
                    currentSession.setPerson(person);
                    currentSession.setTest(test);
                    currentSession.setQuestions(questions.size());
                    currentSession.setTimestamp(beginDate);
                    testSessionsDao.save(currentSession);
                    testing = true;
                }
            } catch (Exception e) {
                testing = false;
                addErrorMessage(e);
            }
        }
    }

    public void select(Question selected) {
        if (selected != null) {
            currentQuestion = selected;
        }
    }

    public void answer() {
        // Сохранить результат в базу, удалить вопрос из мапы
        if (currentQuestion != null) {
            boolean rightAnswer = true;
            for (Answer answer : questions.get(currentQuestion)) {
                if (answer.isRight() != answer.isSelected()) {
                    rightAnswer = false;
                }
                if (answer.isSelected()) {
                    // Сохраним детали
                }
            }
            if (rightAnswer) {
                currentSession.setRightAnswers(currentSession.getRightAnswers() + 1);
            }
            testSessionsDao.save(currentSession);
            questions.remove(currentQuestion);
            if (!questions.isEmpty()) {
                currentQuestion = questions.firstKey();
            }
        }
    }

    public boolean isUseTimer() {
        return endDate != null;
    }

    public String getStartTime() {
        return dateFormat.format(beginDate);
    }

    public String getFinishTime() {
        return dateFormat.format(endDate);
    }

    public boolean isCanRunTest() {
        if (test == null || testSessionsDao == null) {
            return false;
        }
        try {
            return testSessionsDao.getSessionsCount(test, person) < test.getMaxTries();
        } catch (Exception e) {
            addErrorMessage(e);
            return false;
        }
    }

    public boolean isTimeOut() {
        return endDate != null && (new Date()).getTime() > endDate.getTime();
    }

    public List<Question> getQuestionsList() {
        if (questions == null || questions.isEmpty()) {
            return Collections.emptyList();
        }
        List<Question> result = new ArrayList<>();
        for (Map.Entry<Question, List<Answer>> entry : questions.entrySet()) {
            result.add(entry.getKey());
        }
        return result;
    }

    public List<Answer> getAnswersList() {
        if (currentQuestion != null && questions != null) {
            return questions.get(currentQuestion);
        }
        return Collections.emptyList();
    }

    public List<TestSession> getTestSessionsList() {
        return isError() || testSessionsDao == null ? Collections.emptyList() : testSessionsDao.fetch(test, person);
    }

    public int getTestCode() {
        return testCode;
    }

    public void setTestCode(int testCode) {
        this.testCode = testCode;
    }

    public boolean isError() {
        // Если не задан тест или персона - ошибка!
        return test == null || person == null;
    }

    public Test getTest() {
        return test;
    }

    public Person getPerson() {
        return person;
    }

    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    public TestSession getCurrentSession() {
        return currentSession;
    }

    public boolean isTesting() {
        return testing;
    }
}
