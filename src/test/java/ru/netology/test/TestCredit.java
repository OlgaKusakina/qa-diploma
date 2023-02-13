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
import static ru.netology.data.DataHelper.getEmptyCvc;
import static ru.netology.data.DbHelper.*;
import static ru.netology.data.DbHelper.checkEmptyOrderEntity;

public class TestCredit {

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
        BuyByCard buyByCard = buyByCredit.buyByCard();
        buyByCard.buyByCreditCard();
    }

    @Test
    void shouldBuyByApprovedCard() {
        HomePage homePage = new HomePage();
        BuyByCredit buyByCredit = homePage.getPageCredit();
        buyByCredit.enterCardData(getApprovedCardInfo(), getValidMonth(), getValidYear(), getValidOwner(), getValidCvc());
        buyByCredit.getSuccessMessage();
        assertEquals(approvedCard.getStatus(), creditData().getStatus());
    }

    @Test
    void shouldNotBuyByDeclineCard() {
        HomePage homePage = new HomePage();
        BuyByCredit buyByCredit = homePage.getPageCredit();
        buyByCredit.enterCardData(getDeclinedCardInfo(), getValidMonth(), getValidYear(), getValidOwner(), getValidCvc());
        buyByCredit.getErrorMessage();
        assertEquals(declinedCard.getStatus(), creditData().getStatus());
        checkEmptyOrderEntity();
    }


    @Test
    void shouldNotSendFormWithInvalidCardNumber() {
        HomePage homePage = new HomePage();
        BuyByCredit buyByCredit = homePage.getPageCredit();
        buyByCredit.enterCardData(getInvalidCardNumber(), getValidMonth(), getValidYear(), getValidOwner(), getValidCvc());
        buyByCredit.formatError();
        checkEmptyCreditEntity();
        checkEmptyOrderEntity();
    }


    @Test
    void shouldNotSendFormWithInvalidMonth1() {
        HomePage homePage = new HomePage();
        BuyByCredit buyByCredit = homePage.getPageCredit();
        buyByCredit.enterCardData(getApprovedCardInfo(), getInvalidMonth1(), getValidYear(), getValidOwner(), getValidCvc());
        buyByCredit.formatError();
        checkEmptyCreditEntity();
        checkEmptyOrderEntity();
    }

    @Test
    void shouldNotSendFormWithInvalidMonth2() {
        HomePage homePage = new HomePage();
        BuyByCredit buyByCredit = homePage.getPageCredit();
        buyByCredit.enterCardData(getApprovedCardInfo(), getInvalidMonth2(), getValidYear(), getValidOwner(), getValidCvc());
        buyByCredit.invalidError();
        checkEmptyCreditEntity();
        checkEmptyOrderEntity();
    }


    @Test
    void shouldNotSendFormWithNullMonth() {
        HomePage homePage = new HomePage();
        var buyByCredit = homePage.getPageCredit();
        buyByCredit.enterCardData(getApprovedCardInfo(), getNullMonth(), getValidYear(), getValidOwner(), getValidCvc());
        buyByCredit.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

    @Test
    void shouldNotSendFormWithInvalidYearCard1() {
        HomePage homePage = new HomePage();
        BuyByCredit buyByCredit = homePage.getPageCredit();
        buyByCredit.enterCardData(getApprovedCardInfo(), getValidMonth(), getInvalidYear(), getValidOwner(), getValidCvc());
        buyByCredit.formatError();
        checkEmptyCreditEntity();
        checkEmptyOrderEntity();
    }

    @Test
    void shouldNotSendFormWithInvalidYearCard2() {
        HomePage homePage = new HomePage();
        BuyByCredit buyByCredit = homePage.getPageCredit();
        buyByCredit.enterCardData(getApprovedCardInfo(), getValidMonth(), getInvalidLastYear(), getValidOwner(), getValidCvc());
        buyByCredit.expiredError();
        checkEmptyCreditEntity();
        checkEmptyOrderEntity();
    }


    @Test
    void shouldNotSendFormWithNullYear() {
        HomePage homePage = new HomePage();
        BuyByCredit buyByCredit = homePage.getPageCredit();
        buyByCredit.enterCardData(getApprovedCardInfo(), getValidMonth(), getNullYear(), getValidOwner(), getValidCvc());
        buyByCredit.expiredError();
        checkEmptyCreditEntity();
        checkEmptyOrderEntity();
    }

    @Test
    void shouldNotSendFormWithOwnerTypedCyrillic() {
        HomePage homePage = new HomePage();
        BuyByCredit buyByCredit = homePage.getPageCredit();
        buyByCredit.enterCardData(getApprovedCardInfo(), getValidMonth(), getValidYear(), getInvalidOwnerCyrillic(),
                getValidCvc());
        buyByCredit.formatError();
        checkEmptyCreditEntity();
        checkEmptyOrderEntity();
    }

    @Test
    void shouldNotSendFormWithOwnerMaths() {
        HomePage homePage = new HomePage();
        BuyByCredit buyByCredit = homePage.getPageCredit();
        buyByCredit.enterCardData(getApprovedCardInfo(), getValidMonth(), getValidYear(), getInvalidOwnerMaths(),
                getValidCvc());
        buyByCredit.formatError();
        checkEmptyCreditEntity();
        checkEmptyOrderEntity();
    }


    @Test
    void shouldNotSendFormWithInvalidCvc() {
        HomePage homePage = new HomePage();
        BuyByCredit buyByCredit = homePage.getPageCredit();
        buyByCredit.enterCardData(getApprovedCardInfo(), getValidMonth(), getValidYear(), getValidOwner(), getInvalidCvc());
        buyByCredit.formatError();
        checkEmptyCreditEntity();
        checkEmptyOrderEntity();
    }


    @Test
    void shouldNotSendFormWithNullCvc() {
        HomePage homePage = new HomePage();
        BuyByCredit buyByCredit = homePage.getPageCredit();
        buyByCredit.enterCardData(getApprovedCardInfo(), getValidMonth(), getValidYear(), getValidOwner(), getNullCvc());
        buyByCredit.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }
    @Test
    void shouldNotSendFormWithoutCardNumber() {
        HomePage homePage = new HomePage();
        BuyByCredit buyByCredit = homePage.getPageCredit();
        buyByCredit.enterCardData(getEmptyCardNumber(), getValidMonth(), getValidYear(), getValidOwner(), getValidCvc());
        buyByCredit.formatError();
        checkEmptyCreditEntity();
        checkEmptyOrderEntity();
    }

    @Test
    void shouldNotSendFormWithoutMonth() {
        HomePage homePage = new HomePage();
        BuyByCredit buyByCredit = homePage.getPageCredit();
        buyByCredit.enterCardData(getApprovedCardInfo(), getEmptyMonth(), getValidYear(), getValidOwner(), getValidCvc());
        buyByCredit.formatError();
        checkEmptyCreditEntity();
        checkEmptyOrderEntity();
    }

    @Test
    void shouldNotSendFormWithoutYear() {
        HomePage homePage = new HomePage();
        BuyByCredit buyByCredit = homePage.getPageCredit();
        buyByCredit.enterCardData(getApprovedCardInfo(), getValidMonth(), getEmptyYear(), getValidOwner(), getValidCvc());
        buyByCredit.formatError();
        checkEmptyCreditEntity();
        checkEmptyOrderEntity();
    }

    @Test
    void shouldNotSendFormWithOwnerEmpty() {
        HomePage homePage = new HomePage();
        BuyByCredit buyByCredit = homePage.getPageCredit();
        buyByCredit.enterCardData(getApprovedCardInfo(), getValidMonth(), getValidYear(), getEmptyOwner(), getValidCvc());
        buyByCredit.emptyError();
        checkEmptyCreditEntity();
        checkEmptyOrderEntity();
    }

    @Test
    void shouldNotSendFormWitheEmptyCvc() {
        HomePage homePage = new HomePage();
        BuyByCredit buyByCredit = homePage.getPageCredit();
        buyByCredit.enterCardData(getApprovedCardInfo(), getValidMonth(), getValidYear(), getValidOwner(), getEmptyCvc());
        buyByCredit.formatError();
        checkEmptyCreditEntity();
        checkEmptyOrderEntity();
    }


    @Test
    void shouldNotSendFormWithAllEmpty() {
        HomePage homePage = new HomePage();
        BuyByCredit buyByCredit =homePage.getPageCredit();
        buyByCredit.enterCardData(getEmptyCardNumber(), getEmptyMonth(),getEmptyYear(),getEmptyOwner(),getEmptyCvc());
        buyByCredit.formatError();
        checkEmptyPaymentEntity();
        checkEmptyOrderEntity();
    }

}
