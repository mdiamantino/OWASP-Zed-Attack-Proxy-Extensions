package org.zaproxy.zap.extension.policyverifier.rules;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.parosproxy.paros.network.HttpMessage;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Unit test for {@link NoBannedDomains}.
 */
public class NoBannedDomainsUnitTest {
    private NoBannedDomains rule;
    private HttpMessage msg;

    @BeforeEach
    public void setUp() throws Exception {
        rule = new NoBannedDomains();
        msg = mock(HttpMessage.class);
    }

    private HttpMessage getMockMessage(String hostname) {
        HttpMessage msg = mock(HttpMessage.class, RETURNS_DEEP_STUBS);
        when(msg.getRequestHeader().getHostName()).thenReturn(hostname);
        return msg;
    }

    @Test
    public void isValid_DomainNotInList_Valid() {
        HttpMessage msg = getMockMessage("this-domain-is-not-in-list.com");
        assertTrue(rule.isValid(msg));
    }

    @Test
    public void isValid_DomainIsInList_NotValid() {
        HttpMessage msg = getMockMessage(getRandomHostInList());
        assertFalse(rule.isValid(msg));
    }

    private String getRandomHostInList() {
        Random rand = new Random();
        int ransomIndex = rand.nextInt(rule.getBANNED_DOMAINS().size());
        return rule.getBANNED_DOMAINS().get(ransomIndex);
    }

    @Test
    public void isValid_SubDomainOfDomainInList_NotValid() {
        HttpMessage msg = getMockMessage("www." + getRandomHostInList());
        assertFalse(rule.isValid(msg));
    }
}
