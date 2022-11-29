package root.vending;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class VendingMachineTest {

    private VendingMachine machine;
    private final int max1 = 30;
    private final int max2 = 40;
    private final int maxc1 = 50;
    private final int maxc2 = 50;

    @BeforeEach
    public void createMachineTest() {
        machine = new VendingMachine();
    }

    @Test
    public void testInitialValues() {
        Assertions.assertEquals(machine.getCoins1(), 0);
        Assertions.assertEquals(machine.getCoins2(), 0);
        Assertions.assertEquals(machine.getNumberOfProduct1(), 0);
        Assertions.assertEquals(machine.getNumberOfProduct2(), 0);
        Assertions.assertEquals(machine.getPrice1(), 8);
        Assertions.assertEquals(machine.getPrice2(), 5);
    }

    @Test
    public void testEnterAdminModeIncorrectCode() {
        Assertions.assertEquals(machine.enterAdminMode(1), VendingMachine.Response.INVALID_PARAM);
    }

    @Test
    public void testEnterAdminModeCorrectCode() {
        Assertions.assertEquals(machine.enterAdminMode(117345294655382L), VendingMachine.Response.OK);
    }

    @Test
    public void testFillProductsInAdministrating() {
        machine.enterAdminMode(117345294655382L);
        Assertions.assertEquals(machine.fillProducts(), VendingMachine.Response.OK);
    }

    @Test
    public void testFillProductsInOperation() {
        Assertions.assertEquals(machine.fillProducts(), VendingMachine.Response.ILLEGAL_OPERATION);
    }

    private void fillProducts(VendingMachine machine) {
        machine.enterAdminMode(117345294655382L);
        machine.fillProducts();
        machine.exitAdminMode();
    }

    @Test
    public void testPutCoin1InAdministrating() {
        machine.enterAdminMode(117345294655382L);
        Assertions.assertEquals(machine.putCoin1(), VendingMachine.Response.ILLEGAL_OPERATION);
    }

    @Test
    public void testPutCoin1InOperation() {
        Assertions.assertEquals(machine.putCoin1(), VendingMachine.Response.OK);
    }

    @Test
    public void testPutCoin1InOperationMoreThanMax() {
        for (int i = 0; i < maxc1; ++i) {
            machine.putCoin1();
        }
        Assertions.assertEquals(machine.putCoin1(), VendingMachine.Response.CANNOT_PERFORM);
    }

    @Test
    public void testPutCoin2InAdministrating() {
        machine.enterAdminMode(117345294655382L);
        Assertions.assertEquals(machine.putCoin2(), VendingMachine.Response.ILLEGAL_OPERATION);
    }

    @Test
    public void testPutCoin2InOperation() {
        Assertions.assertEquals(machine.putCoin2(), VendingMachine.Response.OK);
    }

    @Test
    public void testPutCoin2InOperationMoreThanMax() {
        for (int i = 0; i < maxc2; ++i) {
            machine.putCoin2();
        }
        Assertions.assertEquals(machine.putCoin2(), VendingMachine.Response.CANNOT_PERFORM);
    }

    @Test
    public void testFillCoinsInOperation() {
        Assertions.assertEquals(machine.fillCoins(1, 1), VendingMachine.Response.ILLEGAL_OPERATION);
    }

    @Test
    public void testFillCoinsBelowZeroFirst() {
        machine.enterAdminMode(117345294655382L);
        Assertions.assertEquals(machine.fillCoins(-1, 1), VendingMachine.Response.INVALID_PARAM);
    }

    @Test
    public void testFillCoinsBelowZeroSecond() {
        machine.enterAdminMode(117345294655382L);
        Assertions.assertEquals(machine.fillCoins(1, -1), VendingMachine.Response.INVALID_PARAM);
    }

    @Test
    public void testFillCoinsZeroFirst() {
        machine.enterAdminMode(117345294655382L);
        Assertions.assertEquals(machine.fillCoins(0, 1), VendingMachine.Response.INVALID_PARAM);
    }

    @Test
    public void testFillCoinsZeroSecond() {
        machine.enterAdminMode(117345294655382L);
        Assertions.assertEquals(machine.fillCoins(1, 0), VendingMachine.Response.INVALID_PARAM);
    }

    @Test
    public void testFillCoinsOverMaxFirst() {
        machine.enterAdminMode(117345294655382L);
        Assertions.assertEquals(machine.fillCoins(maxc1 + 1, 1), VendingMachine.Response.INVALID_PARAM);
    }

    @Test
    public void testFillCoinsMaxFirst() {
        machine.enterAdminMode(117345294655382L);
        Assertions.assertEquals(machine.fillCoins(maxc1, 1), VendingMachine.Response.OK);
    }

    @Test
    public void testFillCoinsOverMaxSecond() {
        machine.enterAdminMode(117345294655382L);
        Assertions.assertEquals(machine.fillCoins(1, maxc2 + 1), VendingMachine.Response.INVALID_PARAM);
    }

    @Test
    public void testFillCoinsMaxSecond() {
        machine.enterAdminMode(117345294655382L);
        Assertions.assertEquals(machine.fillCoins(1, maxc2), VendingMachine.Response.OK);
    }

    @Test
    public void testEnterAdminModeNonZeroBalance() {
        machine.putCoin1();
        machine.putCoin2();
        Assertions.assertEquals(machine.enterAdminMode(117345294655382L), VendingMachine.Response.CANNOT_PERFORM);
    }

    @Test
    public void testGetNumberOfProduct1() {
        fillProducts(machine);
        Assertions.assertEquals(machine.getNumberOfProduct1(), max1);
    }

    @Test
    public void testGetNumberOfProduct2() {
        fillProducts(machine);
        machine.fillProducts();
        Assertions.assertEquals(machine.getNumberOfProduct2(), max2);
    }

    @Test
    public void testGetCurrentBalance() {
        Assertions.assertEquals(machine.getCurrentBalance(), 0);
    }

    @Test
    public void testGetCurrentBalanceNotZero() {
        machine.putCoin1();
        machine.putCoin2();
        Assertions.assertEquals(machine.getCurrentBalance(), 3);
    }

    @Test
    public void testGetCurrentModeShouldReturnOperation() {
        Assertions.assertEquals(machine.getCurrentMode(), VendingMachine.Mode.OPERATION);
    }

    @Test
    public void testGetCurrentSumInOperationModeShouldReturnO() {
        Assertions.assertEquals(machine.getCurrentSum(), 0);
    }

    @Test
    public void testGetCurrentSumInOperationModeWithNonZeroBalanceShouldReturnO() {
        machine.putCoin1();
        machine.putCoin2();
        Assertions.assertEquals(machine.getCurrentSum(), 0);
    }

    @Test
    public void testGetCurrentSumInAdministeringModeShouldReturnSum() {
        machine.enterAdminMode(117345294655382L);
        int first_mon = 4, second_mon = 2;
        machine.fillCoins(first_mon, second_mon);
        int sum = machine.getCurrentSum();
        Assertions.assertEquals(sum, first_mon*VendingMachine.coinval1 + second_mon*VendingMachine.coinval2);
        Assertions.assertEquals(machine.getCoins1(), first_mon);
        Assertions.assertEquals(machine.getCoins2(), second_mon);
    }

    @Test
    public void testGetCurrentSumInAdministeringModeShouldReturnZeroSum() {
        machine.enterAdminMode(117345294655382L);
        Assertions.assertEquals(machine.getCurrentSum(), 0);
    }

    @Test
    public void testGetCoins1InOperationModeShouldReturnO() {
        machine.putCoin1();
        Assertions.assertEquals(machine.getCoins1(), 0);
    }

    @Test
    public void testGetCoins1InAdministeringModeShouldReturnSum() {
        machine.enterAdminMode(117345294655382L);
        int first_mon = 1, second_mon = 1;
        machine.fillCoins(first_mon, second_mon);
        Assertions.assertEquals(machine.getCoins1(), first_mon);
    }

    @Test
    public void testGetCoins2InOperationModeShouldReturnO() {
        machine.putCoin2();
        Assertions.assertEquals(machine.getCoins2(), 0);
    }

    @Test
    public void testGetCoins2InAdministeringModeShouldReturnSum() {
        machine.enterAdminMode(117345294655382L);
        int first_mon = 1, second_mon = 1;
        machine.fillCoins(first_mon, second_mon);
        Assertions.assertEquals(machine.getCoins2(), second_mon);
    }
    @Test
    public void testSetPricesInAdministratingShouldReturnIll() {
        Assertions.assertEquals(machine.setPrices(5, 8), VendingMachine.Response.ILLEGAL_OPERATION);
    }

    @Test
    public void testSetPricesInOperationBelowZero() {
        machine.enterAdminMode(117345294655382L);
        Assertions.assertEquals(machine.setPrices(1, -2), VendingMachine.Response.INVALID_PARAM);
    }

    @Test
    public void testSetPricesInOperationZeroSecond() {
        machine.enterAdminMode(117345294655382L);
        Assertions.assertEquals(machine.setPrices(1, 0), VendingMachine.Response.INVALID_PARAM);
    }

    @Test
    public void testSetPricesInOperationBelowZeroFirst() {
        machine.enterAdminMode(117345294655382L);
        Assertions.assertEquals(machine.setPrices(-2, 1), VendingMachine.Response.INVALID_PARAM);
    }

    @Test
    public void testSetPricesInOperationZeroFirst() {
        machine.enterAdminMode(117345294655382L);
        Assertions.assertEquals(machine.setPrices(0, 1), VendingMachine.Response.INVALID_PARAM);
    }

    @Test
    public void testSetPricesInOperationOK(){
        machine.enterAdminMode(117345294655382L);
        Assertions.assertEquals(machine.setPrices(5, 8), VendingMachine.Response.OK);
    }

    @Test
    public void testGetPricesAfterSets(){
        machine.enterAdminMode(117345294655382L);
        machine.setPrices(5, 8);
        Assertions.assertEquals(machine.getPrice1(), 5);
    }

    @Test
    public void testGetPricesAfterSetsSecond(){
        machine.enterAdminMode(117345294655382L);
        machine.setPrices(5, 8);
        Assertions.assertEquals(machine.getPrice2(), 8);
    }

    @Test
    public void testGiveProduct1inAdministrating() {
        machine.enterAdminMode(117345294655382L);
        Assertions.assertEquals(machine.giveProduct1(1), VendingMachine.Response.ILLEGAL_OPERATION);
    }

    @Test
    public void testGiveProduct1BelowZero() {
        Assertions.assertEquals(machine.giveProduct1(-1), VendingMachine.Response.INVALID_PARAM);
    }

    @Test
    public void testGiveProduct1Zero() {
        Assertions.assertEquals(machine.giveProduct1(0), VendingMachine.Response.INVALID_PARAM);
    }

    @Test
    public void testGiveProduct1OverMax() {
        Assertions.assertEquals(machine.giveProduct1(max1 + 1), VendingMachine.Response.INVALID_PARAM);
    }

    @Test
    public void testGiveProduct1Max() {
        machine.enterAdminMode(117345294655382L);
        machine.fillProducts();
        machine.setPrices(1, 1);
        machine.exitAdminMode();
        for (int i = 0; i < max1; ++i) {
            machine.putCoin1();
        }
        Assertions.assertEquals(machine.giveProduct1(max1), VendingMachine.Response.OK);
    }

    @Test
    public void testGiveProduct1OverProductBalance() {
        machine.enterAdminMode(117345294655382L);
        machine.fillProducts();
        machine.setPrices(1, 1);
        machine.exitAdminMode();
        for (int i = 0; i < max1; ++i) {
            machine.putCoin1();
            machine.putCoin2();
        }
        machine.giveProduct1(max1 - 1);
        Assertions.assertEquals(machine.giveProduct1(2), VendingMachine.Response.INSUFFICIENT_PRODUCT);
    }

    @Test
    public void testGiveProduct1MaxProductBalance() {
        machine.enterAdminMode(117345294655382L);
        machine.fillProducts();
        machine.setPrices(1, 1);
        machine.exitAdminMode();
        for (int i = 0; i < max1; ++i) {
            machine.putCoin1();
            machine.putCoin2();
        }
        Assertions.assertEquals(machine.giveProduct1(max1), VendingMachine.Response.OK);
    }

    @Test
    public void testGiveProduct1WithoutEnoughSecondTypeMoney() {
        machine.enterAdminMode(117345294655382L);
        machine.fillProducts();
        machine.setPrices(4, 1);
        machine.fillCoins(4, 1);
        machine.exitAdminMode();
        for (int i = 0; i < 8; ++i) {
            machine.putCoin1();
        }
        Assertions.assertEquals(machine.giveProduct1(1), VendingMachine.Response.OK);
        Assertions.assertEquals(machine.getCurrentBalance(), 0);
        machine.enterAdminMode(117345294655382L);
        Assertions.assertEquals(machine.getCoins1(), 10);
        Assertions.assertEquals(machine.getCoins2(), 0);
    }

    @Test
    public void testGiveProduct1WithEnoughSecondTypeMoney() {
        machine.enterAdminMode(117345294655382L);
        machine.fillProducts();
        machine.setPrices(4, 1);
        machine.fillCoins(4, 2);
        machine.exitAdminMode();
        for (int i = 0; i < 8; ++i) {
            machine.putCoin1();
        }
        Assertions.assertEquals(machine.giveProduct1(1), VendingMachine.Response.OK);
        Assertions.assertEquals(machine.getCurrentBalance(), 0);
        machine.enterAdminMode(117345294655382L);
        Assertions.assertEquals(machine.getCoins1(), 12);
        Assertions.assertEquals(machine.getCoins2(), 0);
    }

    @Test
    public void testGiveProduct1WithMoreSecondTypeMoney() {
        machine.enterAdminMode(117345294655382L);
        machine.fillProducts();
        machine.setPrices(4, 1);
        machine.fillCoins(4, 3);
        machine.exitAdminMode();
        for (int i = 0; i < 8; ++i) {
            machine.putCoin1();
        }
        Assertions.assertEquals(machine.giveProduct1(1), VendingMachine.Response.OK);
        Assertions.assertEquals(machine.getCurrentBalance(), 0);
        machine.enterAdminMode(117345294655382L);
        Assertions.assertEquals(machine.getCoins1(), 12);
        Assertions.assertEquals(machine.getCoins2(), 1);
    }

    @Test
    public void testGiveProduct1WithoutSecondTypeMoney() {
        machine.enterAdminMode(117345294655382L);
        machine.fillProducts();
        machine.setPrices(5, 1);
        machine.exitAdminMode();
        for (int i = 0; i < 6; ++i) {
            machine.putCoin1();
        }
        Assertions.assertEquals(machine.giveProduct1(1), VendingMachine.Response.OK);
        Assertions.assertEquals(machine.getCurrentBalance(), 0);
        machine.enterAdminMode(117345294655382L);
        Assertions.assertEquals(machine.getCoins1(), 5);
        Assertions.assertEquals(machine.getCoins2(), 0);
    }

    @Test
    public void testGiveProduct1WithSecondMultiplicity() {
        machine.enterAdminMode(117345294655382L);
        machine.fillProducts();
        machine.setPrices(8, 1);
        machine.exitAdminMode();
        for (int i = 0; i < 5; ++i) {
            machine.putCoin2();
        }
        Assertions.assertEquals(machine.giveProduct1(1), VendingMachine.Response.OK);
        Assertions.assertEquals(machine.getCurrentBalance(), 0);
        Assertions.assertEquals(machine.getNumberOfProduct1(), max1 - 1);
        machine.enterAdminMode(117345294655382L);
        Assertions.assertEquals(machine.getCoins1(), 0);
        Assertions.assertEquals(machine.getCoins2(), 4);
    }

    @Test
    public void testGiveProduct1WithoutSecondMultiplicity() {
        machine.enterAdminMode(117345294655382L);
        machine.fillProducts();
        machine.setPrices(11, 1);
        machine.exitAdminMode();
        for (int i = 0; i < 6; ++i) {
            machine.putCoin2();
        }
        Assertions.assertEquals(machine.giveProduct1(1), VendingMachine.Response.UNSUITABLE_CHANGE);
    }

    @Test
    public void testGiveProduct1WithoutSecondMultiplicityWithFirstCoins() {
        machine.enterAdminMode(117345294655382L);
        machine.fillProducts();
        machine.setPrices(5, 1);
        machine.exitAdminMode();
        for (int i = 0; i < 4; ++i) {
            machine.putCoin1();
            machine.putCoin2();
        }
        Assertions.assertEquals(machine.giveProduct1(1), VendingMachine.Response.OK);
        Assertions.assertEquals(machine.getCurrentBalance(), 0);
        Assertions.assertEquals(machine.getNumberOfProduct1(), max1 - 1);
        machine.enterAdminMode(117345294655382L);
        Assertions.assertEquals(machine.getCoins1(), 3);
        Assertions.assertEquals(machine.getCoins2(), 1);
    }

    @Test
    public void testGiveProduct1WithoutSecondType() {
        machine.enterAdminMode(117345294655382L);
        machine.fillProducts();
        machine.setPrices(5, 1);
        machine.exitAdminMode();
        for (int i = 0; i < 2; ++i) {
            machine.putCoin2();
            machine.putCoin1();
        }
        Assertions.assertEquals(machine.giveProduct1(1), VendingMachine.Response.OK);
        Assertions.assertEquals(machine.getCurrentBalance(), 0);
        Assertions.assertEquals(machine.getNumberOfProduct1(), max1 - 1);
        machine.enterAdminMode(117345294655382L);
        Assertions.assertEquals(machine.getCoins1(), 1);
        Assertions.assertEquals(machine.getCoins2(), 2);
    }

    @Test
    public void testGiveProduct1OverMoneyReturn() {
        machine.enterAdminMode(117345294655382L);
        machine.fillProducts();
        machine.setPrices(10, 1);
        machine.exitAdminMode();
        for (int i =0; i < 5; ++i) {
            machine.putCoin1();
        }
        machine.putCoin2();
        Assertions.assertEquals(machine.giveProduct1(1), VendingMachine.Response.INSUFFICIENT_MONEY);

    }

    @Test
    public void testGiveProduct2inAdministrating() {
        machine.enterAdminMode(117345294655382L);
        Assertions.assertEquals(machine.giveProduct2(1), VendingMachine.Response.ILLEGAL_OPERATION);
    }

    @Test
    public void testGiveProduct2BelowZero() {
        Assertions.assertEquals(machine.giveProduct2(-1), VendingMachine.Response.INVALID_PARAM);
    }

    @Test
    public void testGiveProduct2Zero() {
        Assertions.assertEquals(machine.giveProduct2(0), VendingMachine.Response.INVALID_PARAM);
    }

    @Test
    public void testGiveProduct2OverMax() {
        Assertions.assertEquals(machine.giveProduct2(max2 + 1), VendingMachine.Response.INVALID_PARAM);
    }

    @Test
    public void testGiveProduct2Max() {
        machine.enterAdminMode(117345294655382L);
        machine.fillProducts();
        machine.setPrices(1, 1);
        machine.exitAdminMode();
        for (int i = 0; i < max2; ++i) {
            machine.putCoin1();
        }
        Assertions.assertEquals(machine.giveProduct2(max2), VendingMachine.Response.OK);
    }

    @Test
    public void testGiveProduct2OverProductBalance() {
        machine.enterAdminMode(117345294655382L);
        machine.fillProducts();
        machine.setPrices(1, 1);
        machine.exitAdminMode();
        for (int i = 0; i < max2; ++i) {
            machine.putCoin1();
            machine.putCoin2();
        }
        machine.giveProduct2(max2 - 1);
        Assertions.assertEquals(machine.giveProduct2(2), VendingMachine.Response.INSUFFICIENT_PRODUCT);
    }

    @Test
    public void testGiveProduct2MaxProductBalance() {
        machine.enterAdminMode(117345294655382L);
        machine.fillProducts();
        machine.setPrices(1, 1);
        machine.exitAdminMode();
        for (int i = 0; i < max2; ++i) {
            machine.putCoin1();
            machine.putCoin2();
        }
        Assertions.assertEquals(machine.giveProduct2(max2), VendingMachine.Response.OK);
    }

    @Test
    public void testGiveProduct2WithoutEnoughSecondTypeMoney() {
        machine.enterAdminMode(117345294655382L);
        machine.fillProducts();
        machine.setPrices(1, 4);
        machine.fillCoins(4, 1);
        machine.exitAdminMode();
        for (int i = 0; i < 8; ++i) {
            machine.putCoin1();
        }
        Assertions.assertEquals(machine.giveProduct2(1), VendingMachine.Response.OK);
        Assertions.assertEquals(machine.getCurrentBalance(), 0);
        machine.enterAdminMode(117345294655382L);
        Assertions.assertEquals(machine.getCoins1(), 10);
        Assertions.assertEquals(machine.getCoins2(), 0);
    }

    @Test
    public void testGiveProduct2WithEnoughSecondTypeMoney() {
        machine.enterAdminMode(117345294655382L);
        machine.fillProducts();
        machine.setPrices(1, 4);
        machine.fillCoins(4, 2);
        machine.exitAdminMode();
        for (int i = 0; i < 8; ++i) {
            machine.putCoin1();
        }
        Assertions.assertEquals(machine.giveProduct2(1), VendingMachine.Response.OK);
        Assertions.assertEquals(machine.getCurrentBalance(), 0);
        machine.enterAdminMode(117345294655382L);
        Assertions.assertEquals(machine.getCoins1(), 12);
        Assertions.assertEquals(machine.getCoins2(), 0);
    }

    @Test
    public void testGiveProduct2WithMoreSecondTypeMoney() {
        machine.enterAdminMode(117345294655382L);
        machine.fillProducts();
        machine.setPrices(1, 4);
        machine.fillCoins(4, 3);
        machine.exitAdminMode();
        for (int i = 0; i < 8; ++i) {
            machine.putCoin1();
        }
        Assertions.assertEquals(machine.giveProduct2(1), VendingMachine.Response.OK);
        Assertions.assertEquals(machine.getCurrentBalance(), 0);
        machine.enterAdminMode(117345294655382L);
        Assertions.assertEquals(machine.getCoins1(), 12);
        Assertions.assertEquals(machine.getCoins2(), 1);
    }

    @Test
    public void testGiveProduct2WithoutSecondTypeMoney() {
        machine.enterAdminMode(117345294655382L);
        machine.fillProducts();
        machine.setPrices(1, 5);
        machine.exitAdminMode();
        for (int i = 0; i < 6; ++i) {
            machine.putCoin1();
        }
        Assertions.assertEquals(machine.giveProduct2(1), VendingMachine.Response.OK);
        Assertions.assertEquals(machine.getCurrentBalance(), 0);
        machine.enterAdminMode(117345294655382L);
        Assertions.assertEquals(machine.getCoins1(), 5);
        Assertions.assertEquals(machine.getCoins2(), 0);
    }

    @Test
    public void testGiveProduct2WithSecondMultiplicity() {
        machine.enterAdminMode(117345294655382L);
        machine.fillProducts();
        machine.setPrices(1, 8);
        machine.exitAdminMode();
        for (int i = 0; i < 5; ++i) {
            machine.putCoin2();
        }
        Assertions.assertEquals(machine.giveProduct2(1), VendingMachine.Response.OK);
        Assertions.assertEquals(machine.getCurrentBalance(), 0);
        Assertions.assertEquals(machine.getNumberOfProduct2(), max2 - 1);
        machine.enterAdminMode(117345294655382L);
        Assertions.assertEquals(machine.getCoins1(), 0);
        Assertions.assertEquals(machine.getCoins2(), 4);
    }

    @Test
    public void testGiveProduct2WithoutSecondMultiplicity() {
        machine.enterAdminMode(117345294655382L);
        machine.fillProducts();
        machine.setPrices(1, 11);
        machine.exitAdminMode();
        for (int i = 0; i < 6; ++i) {
            machine.putCoin2();
        }
        Assertions.assertEquals(machine.giveProduct2(1), VendingMachine.Response.UNSUITABLE_CHANGE);
    }

    @Test
    public void testGiveProduct2WithoutSecondMultiplicityWithFirstCoins() {
        machine.enterAdminMode(117345294655382L);
        machine.fillProducts();
        machine.setPrices(1, 5);
        machine.exitAdminMode();
        for (int i = 0; i < 4; ++i) {
            machine.putCoin1();
            machine.putCoin2();
        }
        Assertions.assertEquals(machine.giveProduct2(1), VendingMachine.Response.OK);
        Assertions.assertEquals(machine.getCurrentBalance(), 0);
        Assertions.assertEquals(machine.getNumberOfProduct2(), max2 - 1);
        machine.enterAdminMode(117345294655382L);
        Assertions.assertEquals(machine.getCoins1(), 3);
        Assertions.assertEquals(machine.getCoins2(), 1);
    }

    @Test
    public void testGiveProduct2WithoutSecondType() {
        machine.enterAdminMode(117345294655382L);
        machine.fillProducts();
        machine.setPrices(1, 5);
        machine.exitAdminMode();
        for (int i = 0; i < 2; ++i) {
            machine.putCoin2();
            machine.putCoin1();
        }
        Assertions.assertEquals(machine.giveProduct2(1), VendingMachine.Response.OK);
        Assertions.assertEquals(machine.getCurrentBalance(), 0);
        Assertions.assertEquals(machine.getNumberOfProduct2(), max2 - 1);
        machine.enterAdminMode(117345294655382L);
        Assertions.assertEquals(machine.getCoins1(), 1);
        Assertions.assertEquals(machine.getCoins2(), 2);
    }

    @Test
    public void testGiveProduct2OverMoneyReturn() {
        machine.enterAdminMode(117345294655382L);
        machine.fillProducts();
        machine.setPrices(1, 10);
        machine.exitAdminMode();
        for (int i =0; i < 5; ++i) {
            machine.putCoin1();
        }
        machine.putCoin2();
        Assertions.assertEquals(machine.giveProduct2(1), VendingMachine.Response.INSUFFICIENT_MONEY);

    }

    @Test
    public void testReturnMoneyInAdministrating() {
        machine.enterAdminMode(117345294655382L);
        Assertions.assertEquals(machine.returnMoney(), VendingMachine.Response.ILLEGAL_OPERATION);
    }

    @Test
    public void testReturnMoneyWithZeroBalance() {
        machine.enterAdminMode(117345294655382L);
        machine.fillProducts();
        machine.setPrices(1, 10);
        machine.exitAdminMode();
        machine.putCoin1();
        machine.giveProduct1(1);
        Assertions.assertEquals(machine.returnMoney(), VendingMachine.Response.OK);
        Assertions.assertEquals(machine.getCurrentBalance(), 0);
    }


    @Test
    public void testReturnMoneyWithoutEnoughSecondType() {
        machine.putCoin2();
        for (int i = 0; i < 4; ++i) {
            machine.putCoin1();
        }
        Assertions.assertEquals(machine.returnMoney(), VendingMachine.Response.OK);
        Assertions.assertEquals(machine.getCurrentBalance(), 0);
        machine.enterAdminMode(117345294655382L);
        Assertions.assertEquals(machine.getCoins1(), 0);
        Assertions.assertEquals(machine.getCoins2(), 0);
    }

    @Test
    public void testReturnMoneyWithMoreEnoughSecondType() {
        machine.enterAdminMode(117345294655382L);
        machine.fillProducts();
        machine.fillCoins(1, 2);
        machine.exitAdminMode();
        machine.putCoin2();
        for (int i = 0; i < 3; ++i) {
            machine.putCoin1();
        }
        Assertions.assertEquals(machine.returnMoney(), VendingMachine.Response.OK);
        Assertions.assertEquals(machine.getCurrentBalance(), 0);
        machine.enterAdminMode(117345294655382L);
        Assertions.assertEquals(machine.getCoins1(), 3);
        Assertions.assertEquals(machine.getCoins2(), 1);
    }

    @Test
    public void testReturnMoneyWithEnoughSecondType() {
        for (int i = 0; i < 2; ++i) {
            machine.putCoin2();
        }
        Assertions.assertEquals(machine.returnMoney(), VendingMachine.Response.OK);
        Assertions.assertEquals(machine.getCurrentBalance(), 0);
        machine.enterAdminMode(117345294655382L);
        Assertions.assertEquals(machine.getCoins1(), 0);
        Assertions.assertEquals(machine.getCoins2(), 0);
    }

    @Test
    public void testReturnMoneyWithoutFirstType() {
        machine.enterAdminMode(117345294655382L);
        machine.fillProducts();
        machine.setPrices(1, 1);
        machine.exitAdminMode();
        machine.putCoin1();
        for (int i = 0; i < 3; ++i) {
            machine.putCoin2();
        }
        Assertions.assertEquals(machine.returnMoney(), VendingMachine.Response.OK);
        Assertions.assertEquals(machine.getCurrentBalance(), 0);
        Assertions.assertEquals(machine.getCoins1(), 0);
        Assertions.assertEquals(machine.getCoins2(), 0);
    }

    @Test
    public void testReturnMoneyOK() {
        machine.enterAdminMode(117345294655382L);
        machine.fillCoins(1, 2);
        machine.exitAdminMode();
        machine.putCoin2();
        machine.putCoin1();
        Assertions.assertEquals(machine.returnMoney(), VendingMachine.Response.OK);
        Assertions.assertEquals(machine.getCurrentBalance(), 0);
    }
}
