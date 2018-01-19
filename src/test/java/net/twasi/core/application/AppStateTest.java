package net.twasi.core.application;

import org.junit.Assert;
import org.junit.Test;

public class AppStateTest {

    @Test
    public void initialStateTest() {
        AppState state = new AppState();

        Assert.assertTrue("isStarting is true", state.isStarting());
        Assert.assertFalse("isOperating is false", state.isOperating());
        Assert.assertFalse("isClosing is false", state.isClosing());
    }

    @Test
    public void isOperatingTest() {
        AppState state = new AppState();
        state.setState(ApplicationState.OPERATING);

        Assert.assertFalse("isStarting is false", state.isStarting());
        Assert.assertTrue("isOperating is true", state.isOperating());
        Assert.assertFalse("isClosing is false", state.isClosing());
    }

    @Test
    public void closingTest() {
        AppState state = new AppState();
        state.setState(ApplicationState.CLOSING);

        Assert.assertFalse("isStarting is false", state.isStarting());
        Assert.assertFalse("isOperating is false", state.isOperating());
        Assert.assertTrue("isClosing is true", state.isClosing());
    }

}
