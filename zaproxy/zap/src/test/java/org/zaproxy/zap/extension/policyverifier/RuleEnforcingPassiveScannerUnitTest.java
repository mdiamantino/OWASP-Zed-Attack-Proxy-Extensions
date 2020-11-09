package org.zaproxy.zap.extension.policyverifier;

import org.junit.jupiter.api.BeforeEach;
import org.parosproxy.paros.core.scanner.Alert;
import org.zaproxy.zap.extension.policyverifier.models.RuleEnforcingPassiveScanner;
import org.zaproxy.zap.extension.policyverifier.rules.EmailRule;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import org.parosproxy.paros.model.Model;
import org.parosproxy.paros.model.OptionsParam;
import org.zaproxy.zap.extension.pscan.PassiveScanData;
import org.zaproxy.zap.extension.pscan.PassiveScanThread;

import java.util.ArrayList;
import java.util.List;

public class RuleEnforcingPassiveScannerUnitTest {
    protected RuleEnforcingPassiveScanner rule;
    protected PassiveScanThread parent;
    protected PassiveScanData passiveScanData = mock(PassiveScanData.class);
    protected List<Alert> alertsRaised;

    /*
    @BeforeEach
    public void setUp() throws Exception {
        setUpZap();

        alertsRaised = new ArrayList<>();
        parent =
                new PassiveScanThread(null, null, new ExtensionAlert(), null) {
                    @Override
                    public void raiseAlert(int id, Alert alert) {
                        defaultAssertions(alert);
                        alertsRaised.add(alert);
                    }
                };
        rule = createScanner();
        rule.setParent(parent);
    }


    @Override
    protected RuleEnforcingPassiveScanner createScanner() {
        rule = new RuleEnforcingPassiveScanner();
        // Mock the model and options
        model = mock(Model.class, withSettings().lenient());
        OptionsParam options = new OptionsParam();
        ZapXmlConfiguration conf = new ZapXmlConfiguration();
        options.load(conf);
        when(model.getOptionsParam()).thenReturn(options);
        rule.setModel(model);
        return rule;
    }
    */
}
