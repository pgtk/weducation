package ru.edu.pgtk.weducation.webui.jsf

import ru.edu.pgtk.weducation.core.ejb.AccountsDAO
import ru.edu.pgtk.weducation.core.ejb.MessagesDAO
import ru.edu.pgtk.weducation.core.entity.Account
import ru.edu.pgtk.weducation.core.entity.Message
import spock.lang.Specification

/**
 * Тестовый класс для проверки функционала MessagesMB
 * Created by leonid on 23.05.16.
 */
class MessagesMBSpec extends Specification {

    // пользователь-отправитель для тестирования
    def sender = new Account(id: 1, login: "sender", fullName: "Тестовый отправитель сообщения")
    // пользователь-получатель для тестирования
    def receiver = new Account(id: 1, login: "receiver", fullName: "Тестовый получатель сообщения")

    // Фейковые классы входящих сообщений
    def inc1 = new Message(id: 1, sourceAccount: null, destinationAccount: sender, title: "Test1",
            text: "Test message 1", from: "Test subsystem", received: false, deleted: false)
    def inc2 = new Message(id: 2, sourceAccount: null, destinationAccount: sender, title: "Test2",
            text: "Test message 2", from: "Test subsystem", received: false, deleted: true)
    // Фейковые классы для исходящих сообщений
    def out1 = new Message(id: 3, sourceAccount: sender, destinationAccount: receiver, title: "Out1",
            text: "Outgoing message 1", from: "Тестовый отправитель сообщения", received: false, deleted: false)
    def out2 = new Message(id: 4, sourceAccount: sender, destinationAccount: receiver, title: "Out2",
            text: "Outgoing message 2", from: "Тестовый отправитель сообщения", received: true, deleted: false)

    def messages = Mock(MessagesDAO) {
        _ * getIncoming(sender) >> [inc1, inc2]
        _ * getOutgoing(sender) >> [out1, out2]
    }

    def accounts = Mock(AccountsDAO) {
        _ * fetchAll() >> [receiver]
    }

    def MessagesMB messagesMB

    def setup() {
        messagesMB = new MessagesMB(messagesDao: messages, accountsDao: accounts)
        messagesMB.setUser(sender)
    }

    // Проверим, что класс меняет переменную состояния входящие/исходящие
    def "Test that method changeThread() works"() {
        when:
        messagesMB.changeThread()

        then:
        messagesMB.outgoing != old(messagesMB.outgoing)
    }

    // Проверим, что запросы для входящих и исходящих сообщений будут разными
    def "Test that incoming and outgoing lists will different"() {
        setup:
        def incomingList = messagesMB.messages

        when:
        messagesMB.changeThread()
        def outgoingList = messagesMB.messages

        then:
        noExceptionThrown()
        assert !isSameList(incomingList, outgoingList)
    }

    // При удалении исходящего сообщения будет меняться переменная deleted, но сообщение
    // не будет удалено из базы данных
    def "Test that hide() method will call setDeleted(true) for outgoing"() {
        when:
        messagesMB.changeThread() // switch to outgoing
        messagesMB.hide(out1)

        then:
        noExceptionThrown()
        !out1.received
        out1.deleted
        1 * messages.save(out1)
    }

    // При удалении входящего сообщения будет меняться переменная received, но сообщение
    // не будет удалено из базы данных
    def "Test that hide() method will call setReceived(true) for incoming"() {
        when:
        messagesMB.hide(inc1)

        then:
        noExceptionThrown()
        inc1.received
        !inc1.deleted
        1 * messages.save(inc1)
    }

    // При удалении входящего сообщения будет меняться переменная received, и сообщение
    // будет удалено из базы данных
    def "Test that hide() method will delete incoming that deleted by sender"() {
        when:
        messagesMB.hide(inc2)

        then:
        noExceptionThrown()
        inc2.received
        inc2.deleted
        1 * messages.delete(inc2)
    }

    // При удалении исходящего сообщения будет меняться переменная deleted, и сообщение
    // будет удалено из базы данных
    def "Test that hide() method will delete outgoing that has received by receiver"() {
        when:
        messagesMB.changeThread()
        messagesMB.hide(out2)

        then:
        noExceptionThrown()
        out2.received
        out2.deleted
        1 * messages.delete(out2)
    }

    // Сравнение списков сообщений
    def isSameList(List<Message> first, List<Message> second) {
        if (first == null && second == null) {
            return true
        }
        if (first == null) {
            return false
        }
        if (second == null) {
            return false
        }
        if (first.size() != second.size()) {
            return false;
        }
        for (int i = 0; i < first.size(); i++) {
            Message fmsg = first.get(i);
            Message smsg = second.get(i);
            if (fmsg.id != smsg.id) {
                return false;
            }
        }
        return true;
    }
}
