package ru.edu.pgtk.weducation.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Класс для сущности "Детали тестирования"
 * @author Voronin Leonid
 * @since 17.05.2016
 */
@Entity
@Table(name = "testdetails")
public class TestDetails implements Serializable {

	@Id
	@Column(name = "tdt_pcode")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne
	@JoinColumn(name = "tdt_qstcode", nullable = false)
	private Question question;

	@ManyToOne
	@JoinColumn(name = "tdt_anscode", nullable = false)
	private Answer answer;

	@ManyToOne
	@JoinColumn(name = "tdt_tsscode", nullable = false)
	private TestSession testSession;

	public int getId() {
		return id;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public Answer getAnswer() {
		return answer;
	}

	public void setAnswer(Answer answer) {
		this.answer = answer;
	}

	public TestSession getTestSession() {
		return testSession;
	}

	public void setTestSession(TestSession testSession) {
		this.testSession = testSession;
	}
}
