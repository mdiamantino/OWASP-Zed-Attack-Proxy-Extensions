package org.zaproxy.zap.extension.policylanguage.models.expressions.terminal;

import org.parosproxy.paros.network.HttpMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractMatchRegexTerminalExpression extends AbstractTerminalExpression {

    private String pattern;

    public AbstractMatchRegexTerminalExpression(String pattern) {
        super();
        this.pattern = pattern;
    }

    @Override
    public boolean interpret(HttpMessage msg) {
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(getRelevantValue(msg));
        return matcher.find();
    }
}
