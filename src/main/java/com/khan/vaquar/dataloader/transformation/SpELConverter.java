package com.khan.vaquar.dataloader.transformation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

/**
 * 
 * @author vaquar khan
 *
 */
@Component
public class SpELConverter implements Converter<Object, Object> {

	@Value("${keySpELExpression}")
	private String expression;

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	@Override
	public Object convert(Object source) {

		ExpressionParser parser = new SpelExpressionParser();

		Expression exp = parser.parseExpression(expression);
		Object output = exp.getValue(source);
		return output;
	}

}
