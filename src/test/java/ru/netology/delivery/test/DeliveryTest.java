//Для запуска тестируемого приложения, находясь в корне проекта,
// выполните в терминале команду: java -jar ./artifacts/app-replan-delivery.jar
// для получения отчёта Allure выполнить в терминале IDEA команду gradle allureServe

// Используется selenide

package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

class DeliveryTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        // TODO: добавить логику теста в рамках которого будет выполнено планирование и перепланирование встречи.
        // Для заполнения полей формы можно использовать пользователя validUser и строки с датами в переменных
        // firstMeetingDate и secondMeetingDate. Можно также вызывать методы generateCity(locale),
        // generateName(locale), generatePhone(locale) для генерации и получения в тесте соответственно города,
        // имени и номера телефона без создания пользователя в методе generateUser(String locale) в датагенераторе

        //Запланирована встреча на первую дату
        $("[data-test-id='city'] input").setValue(validUser.getCity());
        $("[data-test-id ='date'] input").sendKeys(Keys.SHIFT, Keys.HOME); // выделение текста кнопками Shift+Home
        $("[data-test-id ='date'] input").sendKeys(firstMeetingDate);
        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement']").click();
        $(".button").click();

        //Проверка подтверждения успешного планирования на первую дату
        $("[data-test-id='success-notification'] .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15)) //ассерт проверки видимости
                .shouldHave(Condition.text("Встреча успешно запланирована на " + firstMeetingDate)) //ассерт проверки текста
        ;

        //Повторная заявка того же пользователя на другую дату
        $("[data-test-id ='date'] input").sendKeys(Keys.SHIFT, Keys.HOME); // выделение текста кнопками Shift+Home
        $("[data-test-id ='date'] input").sendKeys(secondMeetingDate);
        $(".button").click();

        //Проверка предложения о переносе встречи на вторую дату
        $("[data-test-id='replan-notification'] .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15)) //ассерт проверки видимости
                .shouldHave(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?")) //ассерт проверки текста
        ;

        $("[data-test-id='replan-notification'] .button").click();

        //Проверка подтверждения успешного планирования на вторую дату
        $("[data-test-id='success-notification'] .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15)) //ассерт проверки видимости
                .shouldHave(Condition.text("Встреча успешно запланирована на " + secondMeetingDate)) //ассерт проверки текста
        ;

    }
}
