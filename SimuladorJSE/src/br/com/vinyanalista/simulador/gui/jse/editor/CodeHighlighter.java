package br.com.vinyanalista.simulador.gui.jse.editor;

import java.util.*;

import br.com.vinyanalista.simulador.data.OpCode;

import com.trolltech.qt.core.QRegExp;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QSyntaxHighlighter;
import com.trolltech.qt.gui.QTextCharFormat;
import com.trolltech.qt.gui.QTextDocument;

public class CodeHighlighter extends QSyntaxHighlighter {

	public class HighlightingRule {
		public QRegExp pattern;
		public QTextCharFormat format;

		public HighlightingRule(QRegExp pattern, QTextCharFormat format) {
			this.pattern = pattern;
			this.format = format;
		}
	}

	Vector<HighlightingRule> highlightingRules = new Vector<HighlightingRule>();

	QRegExp commentStartExpression;
	QRegExp commentEndExpression;

	QTextCharFormat keywordFormat = new QTextCharFormat();
	QTextCharFormat commentFormat = new QTextCharFormat();
	QTextCharFormat numberFormat = new QTextCharFormat();

	public CodeHighlighter(QTextDocument parent) {

		super(parent);

		HighlightingRule rule;
		QBrush brush;
		QRegExp pattern;

		brush = new QBrush(QColor.blue, Qt.BrushStyle.SolidPattern);
		keywordFormat.setForeground(brush);
		keywordFormat.setFontWeight(QFont.Weight.Bold.value());

		// All the opcodes
		List<String> keywords = new ArrayList<String>();
		OpCode auxOpCode = new OpCode(0);
		for (int i = auxOpCode.getMinValue(); i <= auxOpCode.getMaxValue(); i++) {
			keywords.add(OpCode.toMnemonic(i));
		}

		for (String keyword : keywords) {
			pattern = new QRegExp("\\b" + keyword + "\\b");
			rule = new HighlightingRule(pattern, keywordFormat);
			highlightingRules.add(rule);
		}

		// All the addresses
		brush = new QBrush(QColor.darkGreen, Qt.BrushStyle.SolidPattern);
		pattern = new QRegExp("\\d*[0-9]");
		numberFormat.setForeground(brush);
		rule = new HighlightingRule(pattern, numberFormat);
		highlightingRules.add(rule);

		// Comment starting with ;
		brush = new QBrush(QColor.gray, Qt.BrushStyle.SolidPattern);
		pattern = new QRegExp(";[^\n]*");
		commentFormat.setForeground(brush);
		rule = new HighlightingRule(pattern, commentFormat);
		highlightingRules.add(rule);
	}

	@Override
	public void highlightBlock(String text) {

		for (HighlightingRule rule : highlightingRules) {
			QRegExp expression = rule.pattern;
			int index = expression.indexIn(text);
			while (index >= 0) {
				int length = expression.matchedLength();
				setFormat(index, length, rule.format);
				index = expression.indexIn(text, index + length);
			}
		}
	}
}