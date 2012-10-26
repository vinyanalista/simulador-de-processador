package br.com.vinyanalista.simulador.gui.jse.editor;
//http://zetcode.com/gui/qtjambi/
//http://doc.qt.digia.com/qtjambi-4.5.2_01/com/trolltech/qt/qtjambi-codeeditor.html
//http://doc.qt.digia.com/qtjambi-4.5.2_01/com/trolltech/qt/qtjambi-syntaxhighlighter.html

import java.text.DateFormat;
import java.util.Date;

import br.com.vinyanalista.simulador.gui.jse.editor.CodeHighlighter;
import br.com.vinyanalista.simulador.gui.jse.simulador.ChooseQWindow;
import br.com.vinyanalista.simulador.gui.jse.simulador.SimulatorQMainWindow;

import com.trolltech.qt.QtBlockedSlot;
import com.trolltech.qt.core.*;
//import com.trolltech.qt.core.Qt.Key;
//import com.trolltech.qt.core.Qt.KeyboardModifier;
//import com.trolltech.qt.core.Qt.KeyboardModifiers;
import com.trolltech.qt.gui.*;
import com.trolltech.qt.gui.QKeySequence.StandardKey;

public class ProgramEditor extends QMainWindow {

	private CodeEditor editor = new CodeEditor();
	public String fileName = null;

	private boolean textHasChanged = false;

	private String wordToFind = null;

	private MyAction undoAction, redoAction;
	private MyAction cutAction, copyAction, pasteAction, deleteAction;
	private MyAction findAction, findNextAction, replaceAction;

	private void setTitle(String title) {
		setWindowTitle(title + " - Program Editor");
	}

	private void textChanged() {
		textHasChanged = true;
		findAction.setEnabled(!editor.toPlainText().isEmpty());
		replaceAction.setEnabled(findAction.isEnabled());
	}

	private boolean askToSave() {
		// http://doc.qt.digia.com/4.7-snapshot/qmessagebox.html
		boolean result = true;
		QMessageBox msgBox = new QMessageBox(this);
		msgBox.setWindowTitle("Save changes");
		msgBox.setText("The text has been modified.");
		msgBox.setInformativeText("Do you want to save the changes?");
		msgBox.setStandardButtons(QMessageBox.StandardButton.Save,
				QMessageBox.StandardButton.Discard,
				QMessageBox.StandardButton.Cancel);
		msgBox.setDefaultButton(QMessageBox.StandardButton.Save);
		int msgBoxResult = msgBox.exec();
		if (msgBoxResult == QMessageBox.StandardButton.Save.value()) {
			if (fileName == null) {
				saveAs();
			} else {
				save();
			}
		} else if (msgBoxResult == QMessageBox.StandardButton.Cancel.value()) {
			result = false;
		}
		return result;
	}

	private void newFile() {
		if (textHasChanged) {
			if (!askToSave()) {
				return;
			}
		}
		fileName = null;
		editor.setPlainText("");
		setTitle("Unnamed");
	}

	private void open() {
		// http://qt-project.org/doc/qt-4.8/gettingstartedqt.html#hello-notepad
		if (textHasChanged) {
			if (!askToSave()) {
				return;
			}
		}
		String fileName = QFileDialog.getOpenFileName(this, tr("Open"), "",
				new QFileDialog.Filter("Text files (*.txt)"));
		if (!fileName.equals("")) {
			QFile file = new QFile(fileName);
			if (!file.open(new QFile.OpenMode(QFile.OpenModeFlag.ReadWrite))) {
				QMessageBox.critical(this, tr("Error"),
						tr("Could not open file"));
				return;
			}
			this.fileName = fileName;
			textHasChanged = false;
			setTitle(fileName);
			editor.setPlainText(file.readAll().toString());
		}
	}

	private void save() {
		// http://qt-project.org/doc/qt-4.8/gettingstartedqt.html#hello-notepad
		if (fileName == null) {
			saveAs();
		} else {
			QFile file = new QFile(fileName);
			if (!file.open(QFile.OpenModeFlag.WriteOnly)) {
				QMessageBox.critical(this, tr("Error"),
						tr("Could not write to file"));
				return;
			}
			QTextStream stream = new QTextStream(file);
			stream.writeString(editor.toPlainText());
			stream.flush();
			file.close();
			textHasChanged = false;
		}
	}

	private void saveAs() {
		// http://qt-project.org/doc/qt-4.8/gettingstartedqt.html#hello-notepad
		String fileName = QFileDialog.getSaveFileName(this, tr("Save As"), "",
				new QFileDialog.Filter("Text files (*.txt)"));
		if (!fileName.equals("")) {
			QFile file = new QFile(fileName);
			if (!file.open(QFile.OpenModeFlag.WriteOnly)) {
				QMessageBox.critical(this, tr("Error"),
						tr("Could not write to file"));
				return;
			}
			QTextStream stream = new QTextStream(file);
			stream.writeString(editor.toPlainText());
			stream.flush();
			file.close();
			this.fileName = fileName;
			setTitle(fileName);
			textHasChanged = false;
		}
	}

	private void doPrint() {
		// http://doc.qt.digia.com/4.7-snapshot/printing.html
		QPrinter printer = new QPrinter();
		QPrintDialog dialog = new QPrintDialog(printer, this);
		dialog.setWindowTitle("Print");
		if (editor.textCursor().hasSelection()) {
			dialog.addEnabledOption(QAbstractPrintDialog.PrintDialogOption.PrintSelection);
		}
		if (dialog.exec() != QDialog.DialogCode.Accepted.value()) {
			return;
		}
		editor.print(printer);
	}

	@Override
	@QtBlockedSlot
	protected void closeEvent(QCloseEvent event) {
		// http://stackoverflow.com/questions/10417914/handling-exit-without-saving-in-qt
		if (textHasChanged) {
			if (!askToSave()) {
				event.ignore();
				return;
			}
		}
		event.accept();
	}

	private void redo() {
		editor.redo();
	}

	private void redoAvailable(boolean available) {
		redoAction.setEnabled(available);
	}

	private void undo() {
		editor.undo();
	}

	private void undoAvailable(boolean available) {
		undoAction.setEnabled(available);
		if (!available) {
			textHasChanged = false;
		}
	}

	private void delete() {
		editor.textCursor().deleteChar();
	}

	private void insertDateAndTime() {
		editor.insertPlainText(DateFormat.getDateTimeInstance().format(
				new Date(System.currentTimeMillis())));
	}

	public void showFindReplaceDialog(int operation) {
		FindReplaceDialog frd = new FindReplaceDialog(this, operation,
				editor);
		frd.exec();
		if (frd.getLastSearch() != null) {
			wordToFind = frd.getLastSearch();
			findNextAction.setEnabled(true);
		}
	}

	private void find() {
		showFindReplaceDialog(FindReplaceDialog.OPERATION_FIND);
	}

	private void findNext() {
		FindReplaceDialog.findNext(editor, wordToFind, true);
	}

	private void replace() {
		showFindReplaceDialog(FindReplaceDialog.OPERATION_REPLACE);
	}
	
	private void play() {
		new ChooseQWindow();
	}

	// @Override
	// @QtBlockedSlot
	// protected void keyPressEvent(QKeyEvent event) {
	//
	// boolean CTRL = event.modifiers()
	// .isSet(KeyboardModifier.ControlModifier);
	// boolean SHIFT = event.modifiers().isSet(KeyboardModifier.ShiftModifier);
	// boolean KEY_EQUAL = (event.key() == Key.Key_Equal.value());
	//
	// System.out.println(CTRL);
	// System.out.println(SHIFT);
	// System.out.println(KEY_EQUAL);
	//
	// if (CTRL && SHIFT && KEY_EQUAL) {
	// zoomIn();
	// }
	// }

	private void about() {
		QMessageBox.about(this, "About", "Copyright 2012 Antônio Vinícius, Eric Moura and Saulo Lordão");
	}

	public ProgramEditor() {
		setTitle("Unnamed");
		setWindowIcon(new QIcon("icons/accessories-text-editor.png"));

		QToolBar toolbar = addToolBar("main toolbar");

		QMenu fileMenu = menuBar().addMenu("&File");

		QAction newFileAction = new MyAction(
				new QIcon("icons/document-new.png"), "New", this, this,
				"newFile()");
		newFileAction.setShortcut(StandardKey.New);
		fileMenu.addAction(newFileAction);
		toolbar.addAction(newFileAction);

		QAction openAction = new MyAction(new QIcon("icons/document-open.png"),
				"Open...", this, this, "open()");
		openAction.setShortcut(StandardKey.Open);
		fileMenu.addAction(openAction);
		toolbar.addAction(openAction);

		QAction saveAction = new MyAction(new QIcon("icons/document-save.png"),
				"Save", this, this, "save()");
		saveAction.setShortcut(StandardKey.Save);
		fileMenu.addAction(saveAction);
		toolbar.addAction(saveAction);

		QAction saveAsAction = new MyAction(new QIcon(
				"icons/document-save-as.png"), "Save as...", this, this,
				"saveAs()");
		fileMenu.addAction(saveAsAction);
		toolbar.addAction(saveAsAction);

		fileMenu.addSeparator();

		QAction printAction = new MyAction(
				new QIcon("icons/document-print.png"), "Print...", this, this,
				"doPrint()");
		printAction.setShortcut(StandardKey.Print);
		fileMenu.addAction(printAction);
		toolbar.addAction(printAction);
		
		toolbar.addSeparator();
		
		QAction playAction = new MyAction(
				new QIcon("icons/arrow_right.png"), "Valida o codigo e inicia a simulação", this, this,
				"play()");//TODO
		playAction.setShortcut(StandardKey.Print);
		fileMenu.addAction(playAction);
		toolbar.addAction(playAction);
		
		fileMenu.addSeparator();

		QAction exitAction = new MyAction(new QIcon(
				"icons/application-exit.png"), "Exit", this, this, "close()");
		exitAction.setShortcut("Alt+F4");
		fileMenu.addAction(exitAction);

		toolbar.addSeparator();

		QMenu editMenu = menuBar().addMenu("&Edit");

		undoAction = new MyAction(new QIcon("icons/edit-undo.png"), "Undo",
				this, editor, "undo()");
		undoAction.setShortcut(StandardKey.Undo);
		undoAction.setEnabled(false);
		editor.undoAvailable.connect(this, "undoAvailable(boolean)");
		editMenu.addAction(undoAction);
		toolbar.addAction(undoAction);

		redoAction = new MyAction(new QIcon("icons/edit-redo.png"), "Redo",
				this, editor, "redo()");
		redoAction.setShortcut(StandardKey.Redo);
		redoAction.setEnabled(false);
		editor.redoAvailable.connect(this, "redoAvailable(boolean)");
		editMenu.addAction(redoAction);
		toolbar.addAction(redoAction);

		editMenu.addSeparator();
		toolbar.addSeparator();

		cutAction = new MyAction(new QIcon("icons/edit-cut.png"), "Cut", this,
				editor, "cut()");
		cutAction.setShortcut(StandardKey.Cut);
		cutAction.setEnabled(false);
		editMenu.addAction(cutAction);
		toolbar.addAction(cutAction);

		copyAction = new MyAction(new QIcon("icons/edit-copy.png"), "Copy",
				this, editor, "copy()");
		copyAction.setShortcut(StandardKey.Copy);
		copyAction.setEnabled(false);
		editMenu.addAction(copyAction);
		toolbar.addAction(copyAction);

		QAction pasteAction = new MyAction(new QIcon("icons/edit-paste.png"),
				"Paste", this, editor, "paste()");
		pasteAction.setShortcut(StandardKey.Paste);
		editMenu.addAction(pasteAction);
		toolbar.addAction(pasteAction);

		QAction deleteAction = new MyAction(new QIcon("icons/edit-delete.png"),
				"Delete", this, this, "delete()");
		deleteAction.setShortcut(StandardKey.Delete);
		editMenu.addAction(deleteAction);
		toolbar.addAction(deleteAction);

		editMenu.addSeparator();

		QAction selectAllAction = new MyAction(new QIcon(
				"icons/edit-select-all.png"), "Select all", this, editor,
				"selectAll()");
		selectAllAction.setShortcut(StandardKey.SelectAll);
		editMenu.addAction(selectAllAction);

		QAction dateTimeAction = new MyAction(new QIcon("icons/user-away.png"),
				"Date/time", this, this, "insertDateAndTime()");
		dateTimeAction.setShortcut("F5");
		editMenu.addAction(dateTimeAction);

		toolbar.addSeparator();

		QMenu searchMenu = menuBar().addMenu("&Search");

		findAction = new MyAction(new QIcon("icons/edit-find.png"), "Find...",
				this, this, "find()");
		findAction.setShortcut(StandardKey.Find);
		findAction.setEnabled(false);
		searchMenu.addAction(findAction);
		toolbar.addAction(findAction);

		findNextAction = new MyAction(null, "Find next", this, this,
				"findNext()");
		findNextAction.setShortcut(StandardKey.FindNext);
		findNextAction.setEnabled(false);
		searchMenu.addAction(findNextAction);

		replaceAction = new MyAction(new QIcon("icons/edit-find-replace.png"),
				"Replace...", this, this, "replace()");
		replaceAction.setShortcut(StandardKey.Replace);
		replaceAction.setEnabled(false);
		searchMenu.addAction(replaceAction);
		toolbar.addAction(replaceAction);

		toolbar.addSeparator();

		QMenu viewMenu = menuBar().addMenu("&View");

		QAction zoomInAction = new MyAction(new QIcon("icons/zoom-in.png"),
				"Zoom in", this, editor, "zoomIn()");
		zoomInAction.setShortcut(StandardKey.ZoomIn);
		viewMenu.addAction(zoomInAction);
		toolbar.addAction(zoomInAction);

		QAction defaultZoomAction = new MyAction(new QIcon(
				"icons/zoom-original.png"), "Default zoom", this, editor,
				"defaultZoom()");
		viewMenu.addAction(defaultZoomAction);

		QAction zoomOutAction = new MyAction(new QIcon("icons/zoom-out.png"),
				"Zoom out", this, editor, "zoomOut()");
		zoomOutAction.setShortcut(StandardKey.ZoomOut);
		viewMenu.addAction(zoomOutAction);
		toolbar.addAction(zoomOutAction);

		toolbar.addSeparator();

		QMenu helpMenu = menuBar().addMenu("&Help");

		QAction aboutAction = new MyAction(new QIcon("icons/help-about.png"),
				"About &Notepad", this, this, "about()");
		aboutAction.setShortcut(StandardKey.HelpContents);
		helpMenu.addAction(aboutAction);
		toolbar.addAction(aboutAction);

		helpMenu.addSeparator();

		helpMenu.addAction(new MyAction(null, "About Qt &Jambi", this,
				QApplication.instance(), "aboutQtJambi()"));

		helpMenu.addAction(new MyAction(null, "About &Qt", this,
				QApplication.instance(), "aboutQt()"));

		toolbar.addSeparator();

		toolbar.addAction(exitAction);

		editor.textChanged.connect(this, "textChanged()");

		editor.selectionChanged.connect(this, "selectionChanged()");

		new CodeHighlighter(editor.document());
		
		setCentralWidget(editor);

		resize(600, 300);
		// http://www.qtcentre.org/threads/3399-set-QMainWindow-in-the-center-of-my-desktop
		move(QApplication.desktop().screen().rect().center().x()
				- this.rect().center().x(), QApplication.desktop().screen()
				.rect().center().y()
				- this.rect().center().y());
		show();
	}

	public static void main(String[] args) {
		QApplication.initialize(args);
		new ProgramEditor();
		QApplication.exec();
	}

	private void selectionChanged() {
		cutAction.setEnabled(editor.textCursor().hasSelection());
		copyAction.setEnabled(cutAction.isEnabled());
	}

}