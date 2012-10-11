package br.com.vinyanalista.simulador.gui.jse.editor;

import com.trolltech.qt.core.Qt.Orientation;
import com.trolltech.qt.gui.*;
import com.trolltech.qt.gui.QDialogButtonBox.ButtonRole;
import com.trolltech.qt.gui.QDialogButtonBox.StandardButton;

public class FindReplaceDialog extends QDialog {
	private CodeEditor editor;

	private QLineEdit findLineEdit1 = new QLineEdit();
	private QLineEdit findLineEdit2 = new QLineEdit();
	private QLineEdit replaceLineEdit = new QLineEdit();

	private class MyButton extends QPushButton {
		public MyButton(QAction action) {
			super();
			addAction(action);
			setIcon(action.icon());
			setText(action.text());
		}
	}

	private MyButton findButton;
	private MyButton replaceButton;
	private MyButton replaceAllButton;

	private QTabWidget tabWidget;

	public static final int OPERATION_FIND = 0;
	public static final int OPERATION_REPLACE = 1;
	
private String lastSearch = null;
	
	public String getLastSearch() {
		return lastSearch;
	}

	public final static boolean findNext(CodeEditor editor, String wordToFind,
			boolean showWarnings) {
		boolean result = editor.find(wordToFind);
		if (!result && showWarnings) {
			QMessageBox.information(editor, "Information", "Can not find \""
					+ wordToFind + "\"");
		}
		return result;
	}

	private void replace() {
		editor.textCursor().insertText(getReplacement());
	}

	private void find() {
		findNext(editor, getWordToFind(), true);
		lastSearch = getWordToFind();
	}

	private void replaceAndFindNext() {
		if (editor.textCursor().hasSelection()
				&& editor.textCursor().selectedText().equals(getWordToFind())) {
			replace();
		}
		findNext(editor, getWordToFind(), true);
		lastSearch = getWordToFind();
	}

	private void replaceAll() {
		int counter = 0;
		while (true) {
			if (findNext(editor, getWordToFind(), false)) {
				replace();
				counter++;
			} else
				break;
		}
		QMessageBox.information(this, "Information", counter
				+ " replacement(s) have been done");
		lastSearch = getWordToFind();
	}

	private class FindTab extends QWidget {
		public FindTab() {
			QHBoxLayout mainLayout = new QHBoxLayout();

			QFormLayout layoutLeft = new QFormLayout();
			layoutLeft.setSpacing(10);

			layoutLeft.addRow(tr("Find what:"), findLineEdit1);

			mainLayout.addItem(layoutLeft);

			QDialogButtonBox buttonBoxRight = new QDialogButtonBox(
					Orientation.Vertical);

			findButton = new MyButton(new MyAction(null, "Find next", null,
					FindReplaceDialog.this, "find()"));
			buttonBoxRight.addButton(findButton, ButtonRole.ActionRole);

			buttonBoxRight.addButton(StandardButton.Cancel);
			buttonBoxRight.rejected.connect(FindReplaceDialog.this,
					"reject()");
			buttonBoxRight.clicked.connect(FindReplaceDialog.this,
					"clicked(QAbstractButton)");
			mainLayout.addWidget(buttonBoxRight);

			setLayout(mainLayout);
		}
	}

	private class ReplaceTab extends QWidget {
		public ReplaceTab() {
			QHBoxLayout mainLayout = new QHBoxLayout();

			QFormLayout layoutLeft = new QFormLayout();
			layoutLeft.setSpacing(10);

			layoutLeft.addRow(tr("Find what:"), findLineEdit2);
			layoutLeft.addRow(tr("Replace:"), replaceLineEdit);

			mainLayout.addItem(layoutLeft);

			QDialogButtonBox buttonBoxRight = new QDialogButtonBox(
					Orientation.Vertical);

			replaceButton = new MyButton(new MyAction(null, "Replace", this,
					FindReplaceDialog.this, "replaceAndFindNext()"));
			buttonBoxRight.addButton(replaceButton, ButtonRole.ActionRole);

			replaceAllButton = new MyButton(new MyAction(null, "Replace All",
					this, FindReplaceDialog.this, "replaceAll()"));
			buttonBoxRight.addButton(replaceAllButton, ButtonRole.ActionRole);

			buttonBoxRight.addButton(StandardButton.Cancel);
			buttonBoxRight.rejected.connect(FindReplaceDialog.this,
					"reject()");
			buttonBoxRight.clicked.connect(FindReplaceDialog.this,
					"clicked(QAbstractButton)");

			mainLayout.addWidget(buttonBoxRight);

			setLayout(mainLayout);
		}
	}

	private void clicked(QAbstractButton button) {
		try {
			MyButton myButton = (MyButton) button;
			if (myButton != null) {
				if (myButton.equals(findButton)) {
					find();
				} else if (myButton.equals(replaceButton)) {
					replaceAndFindNext();
				} else if (myButton.equals(replaceAllButton)) {
					replaceAll();
				}
			}
		} catch (Exception e) {
		}
	}

	public FindReplaceDialog(QWidget owner, int operation, CodeEditor editor) {
		super(owner);
		setWindowTitle("Find/replace");
		// setModal(true);
		this.editor = editor;

		tabWidget = new QTabWidget(this);

		tabWidget.addTab(new FindTab(), new QIcon("icons/edit-find.png"),
				tr("Find"));

		if (editor.textCursor().hasSelection())
			findLineEdit1.setText(editor.textCursor().selectedText());
		findLineEdit1.textChanged.connect(this, "updateButtons()");

		tabWidget.addTab(new ReplaceTab(), new QIcon(
				"icons/edit-find-replace.png"), tr("Replace"));

		if (editor.textCursor().hasSelection())
			findLineEdit2.setText(editor.textCursor().selectedText());
		findLineEdit2.textChanged.connect(this, "updateButtons()");
		replaceLineEdit.textChanged.connect(this, "updateButtons()");

		tabWidget.currentChanged.connect(this, "tabChanged()");

		tabWidget.setCurrentIndex(operation);
		tabWidget.resize(300, 300);
		setFixedSize(tabWidget.size());

		updateButtons();
	}

	public String getWordToFind() {
		switch (tabWidget.currentIndex()) {
		case OPERATION_FIND:
			return findLineEdit1.text();
		case OPERATION_REPLACE:
			return findLineEdit2.text();
		default:
			return null;
		}
	}

	private String getReplacement() {
		return replaceLineEdit.text();
	}

	private void updateButtons() {
		findButton.setEnabled(!getWordToFind().isEmpty());
		replaceButton.setEnabled(findButton.isEnabled());
		replaceAllButton.setEnabled(replaceButton.isEnabled());
	}

	private void tabChanged() {
		switch (tabWidget.currentIndex()) {
		case OPERATION_FIND:
			findLineEdit1.setText(findLineEdit2.text());
			findLineEdit1.setFocus();

			break;
		case OPERATION_REPLACE:
			findLineEdit2.setText(findLineEdit1.text());
			findLineEdit2.setFocus();
			break;
		}
		findButton.setDefault(tabWidget.currentIndex() == OPERATION_FIND);
		replaceButton.setDefault(tabWidget.currentIndex() == OPERATION_REPLACE);
	}

}