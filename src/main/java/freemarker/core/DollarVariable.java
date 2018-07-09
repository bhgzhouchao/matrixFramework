/**********************************************************************
 * Copyright (c):    2014 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,15202879502
 **********************************************************************/
package freemarker.core;
import freemarker.template.TemplateException;
import java.io.IOException;
import org.matrix.framework.core.platform.web.freemarker.template.FreemarkerTemplate;

final class DollarVariable extends TemplateElement {

    private final Expression expression;
    private final Expression escapedExpression;

    DollarVariable(Expression expression, Expression escapedExpression) {
        this.expression = expression;
        this.escapedExpression = escapedExpression;
    }

    /**
     * Outputs the string value of the enclosed expression.
     */
    void accept(Environment env) throws TemplateException, IOException {
        String value;
        boolean partialRender = Boolean.TRUE.equals(env.getConfiguration().getCustomAttribute(FreemarkerTemplate.SETTING_PARTIAL_RENDERING));
        try {
            value = escapedExpression.getStringValue(env);
        } catch (TemplateException e) {
            if (partialRender) {
                value = getCanonicalForm();
            } else {
                throw e;
            }
        }
        env.getOut().write(value);
    }

    public String getCanonicalForm() {
        return "${" + expression.getCanonicalForm() + "}";
    }

    public String getDescription() {
        return getSource() + (expression.equals(escapedExpression) ? "" : " escaped ${" + escapedExpression.getCanonicalForm() + "}");
    }

    boolean heedsOpeningWhitespace() {
        return true;
    }

    boolean heedsTrailingWhitespace() {
        return true;
    }
}
