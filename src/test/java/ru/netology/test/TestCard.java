package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.data.DbHelper;
import ru.netology.page.BuyByCard;
import ru.netology.page.BuyByCredit;
import ru.netology.page.HomePage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataHelper.*;
import static ru.netology.data.DbHelper.*;


public class TestCard {

    DataHelper.CardNumber approvedCard = DataHelper.approvedCardInfo();
    DataHelper.CardNumber declinedCard = DataHelper.declinedCardInfo();

    @BeforeEach
    public void cleanTables() {
        DbHelper.cleanData();
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setupClass() {
        open("http://localhost:8080");
    }

    @Test
    void shouldSwitchBetweenPages() {
        HomePage homePage = new HomePage();
        BuyByCredit buyByCredit = homePage.getPageCredit();
        BuyByCard buyByCard = buyByCredit.switchOnDebitCardForm();
        buyByCard.switchOnCreditCardForm();
    }

    @Test
    void shouldBuyByApprovedCard() {
        HomePage homePage = new HomePage();
        BuyByCard buyByCard = homePage.getPageByCard();
        buyByCard.enterCardData(getApprovedCardInfo(), getValidMonth(), getValidYear(), getValidOwner(), getValidCvc());
        buyByCard.checkSuccessMessage();
        assertEquals(approvedCard.getStatus(), payData().getStatus());
    }
    @Test
    void shouldNotBuyByDeclineCard() {
        var homePage = new HomePage();
        var buyByCard = homePage.getPageByCard();
        buyByCard.enterCardData(getDeclinedCardInfo(), getValidMonth(), getValidYear(), getValidOwner(), getValidCvc());
        buyByCard.checkErrorMessage();
        assertEquals(declinedCard.getStatus(), payData().getStatus());
        checkEmptyOrderEntity();
    }

    @Test
    void shouldNotSendFormWithInvalidCardNumber() {
        HomePage homePage = new HomePage();
        BuyByCard buyByCard = homePage.getPageByCard();
        buyByCard.enterCardData(getInvalidCardNumber(), getValidMonth(), getValidYear(), getValidOwner(), getValidCvc());
        buyByCard.checkFormatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }


    @Test
    void shouldNotSendFormWithInvalidMonth1() {
        HomePage homePage = new HomePage();
        var buyByCard = homePage.getPageByCard();
        buyByCard.enterCardData(getApprovedCardInfo(), getInvalidMonthOneNumber(), getValidYear(), getValidOwner(), getValidCvc());
        buyByCard.checkFormatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    void shouldNotSendFormWithInvalidMonth2() {
        HomePage homePage = new HomePage();
        var buyByCard = homePage.getPageByCard();
        buyByCard.enterCardData(getApprovedCardInfo(), getInvalidMonthTwoNumbers(), getValidYear(), getValidOwner(), getValidCvc());
        buyByCard.checkInvalidError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    void shouldNotSendFormWithNullMonth() {
        HomePage homePage = new HomePage();
        var buyByCard = homePage.getPageByCard();
        buyByCard.enterCardData(getApprovedCardInfo(), getZeroMonth(), getValidYear(), getValidOwner(), getValidCvc());
        buyByCard.checkFormatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    void shouldNotSendFormWithNullYear() {
        HomePage homePage = new HomePage();
        BuyByCard buyByCard = homePage.getPageByCard();
        buyByCard.enterCardData(getApprovedCardInfo(), getValidMonth(), getZeroYear(), getValidOwner(), getValidCvc());
        buyByCard.checkExpiredError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    void shouldNotSendFormWithInvalidYearCard1() {
        HomePage homePage = new HomePage();
        BuyByCard buyByCard = homePage.getPageByCard();
        buyByCard.enterCardData(getApprovedCardInfo(), getValidMonth(), getInvalidYear(), getValidOwner(), getValidCvc());
        buyByCard.checkFormatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    void shouldNotSendFormWithInvalidYearCard2() {
        HomePage homePage = new HomePage();
        BuyByCard buyByCard = homePage.getPageByCard();
        buyByCard.enterCardData(getApprovedCardInfo(), getValidMonth(), getInvalidLastYear(), getValidOwner(), getValidCvc());
        buyByCard.checkExpiredError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }


    @Test
    void shouldNotSendFormWithOwnerTypedCyrillic() {
        HomePage homePage = new HomePage();
        BuyByCard buyByCard = homePage.getPageByCard();
        buyByCard.enterCardData(getApprovedCardInfo(), getValidMonth(), getValidYear(), getInvalidOwnerCyrillic(),
                getValidCvc());
        buyByCard.checkFormatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    void shouldNotSendFormWithOwnerMaths() {
        HomePage homePage = new HomePage();
        BuyByCard buyByCard = homePage.getPageByCard();
        buyByCard.enterCardData(getApprovedCardInfo(), getValidMonth(), getValidYear(), getInvalidOwnerMaths(),
                getValidCvc());
        buyByCard.checkFormatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }



    @Test
    void shouldNotSendFormWithInvalidCvc() {
        HomePage homePage = new HomePage();
        BuyByCard buyByCard = homePage.getPageByCard();
        buyByCard.enterCardData(getApprovedCardInfo(), getValidMonth(), getValidYear(), getValidOwner(), getInvalidCvc());
        buyByCard.checkFormatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }


    @Test
    void shouldNotSendFormWithNullCvc() {
        HomePage homePage = new HomePage();
        BuyByCard buyByCard = homePage.getPageByCard();
        buyByCard.enterCardData(getApprovedCardInfo(), getValidMonth(), getValidYear(), getValidOwner(), getZeroCvc());
        buyByCard.checkFormatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    void shouldNotSendFormWithoutCardNumber() {
        HomePage homePage = new HomePage();
        BuyByCard buyByCard = homePage.getPageByCard();
        buyByCard.enterCardData(getEmptyString(), getValidMonth(), getValidYear(), getValidOwner(), getValidCvc());
        buyByCard.checkFormatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }
    @Test
    void shouldNotSendFormWithoutMonth() {
        HomePage homePage = new HomePage();
        BuyByCard buyByCard = homePage.getPageByCard();
        buyByCard.enterCardData(getApprovedCardInfo(), getEmptyString(), getValidYear(), getValidOwner(), getValidCvc());
        buyByCard.checkFormatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    void shouldNotSendFormWithoutYear() {
        HomePage homePage = new HomePage();
        BuyByCard buyByCard = homePage.getPageByCard();
        buyByCard.enterCardData(getApprovedCardInfo(), getValidMonth(), getEmptyString(), getValidOwner(), getValidCvc());
        buyByCard.checkFormatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }
    @Test
    void shouldNotSendFormWithOwnerEmpty() {
        HomePage homePage = new HomePage();
        BuyByCard buyByCard = homePage.getPageByCard();
        buyByCard.enterCardData(getApprovedCardInfo(), getValidMonth(), getValidYear(), getEmptyString(), getValidCvc());
        buyByCard.checkEmptyError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    void shouldNotSendFormWitheEmptyCvc() {
        HomePage homePage = new HomePage();
        BuyByCard buyByCard = homePage.getPageByCard();
        buyByCard.enterCardData(getApprovedCardInfo(), getValidMonth(), getValidYear(), getValidOwner(), getEmptyString());
        buyByCard.checkFormatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }
    @Test
    void shouldNotSendFormWithAllEmpty() {
        HomePage homePage = new HomePage();
        BuyByCard buyByCard =homePage.getPageByCard();
        buyByCard.enterCardData(getEmptyString(), getEmptyString(),getEmptyString(),getEmptyString(),getEmptyString());
        buyByCard.checkFormatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

}