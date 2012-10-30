package alice.tuprolog.interfaces;

import alice.tuprolog.Library;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Term;
import alice.tuprolog.Theory;
import alice.tuprolog.event.ExceptionListener;
import alice.tuprolog.event.OutputListener;
import alice.tuprolog.event.SpyListener;

/**
 * @author  ale
 */
public interface IProlog {
	
	/**
	 * Gets the component managing operators
	 */
	IOperatorManager getOperatorManager();
		
	/**
	 * Gets the component managing primitives
	 */
	IPrimitiveManager getPrimitiveManager();
	
	/**
	 * Gets current theory
	 * @return   current(dynamic) theory
	 */
	Theory getTheory();
	
	/**
	 * Adds (appends) a theory
	 *
	 * @param th is the theory to be added
	 * @throws InvalidTheoryException if the new theory is not valid
	 */
	void addTheory(Theory th) throws Exception;
	
	/**
	 * Clears current theory
	 */
	void clearTheory();
	
	/**
	 * Gets the list of current libraries loaded
	 *
	 * @return the list of the library names
	 */
	String[] getCurrentLibraries();
	
	/**
	 * Gets the reference to a loaded library
	 *
	 * @param name the name of the library already loaded
	 * @return the reference to the library loaded, null if the library is
	 *         not found
	 */
	Library getLibrary(String name);
	
	/**
	 * Loads a library.
	 *
	 * If a library with the same name is already present,
	 * a warning event is notified and the request is ignored.
	 *
	 * @param className name of the Java class containing the library to be loaded
	 * @return the reference to the Library just loaded
	 * @throws InvalidLibraryException if name is not a valid library
	 */
	Library loadLibrary(String className) throws Exception;
	
	/**
	 * Unloads a previously loaded library
	 *
	 * @param name of the library to be unloaded
	 * @throws InvalidLibraryException if name is not a valid loaded library
	 */
	void unloadLibrary(String name) throws Exception;
	
	/**
	 * Solves a query
	 *
	 * @param st the string representing the goal to be demonstrated
	 * @return the result of the demonstration
	 * @see SolveInfo
	 **/
	SolveInfo solve(String st) throws Exception;
	
	/**
	 * Gets next solution
	 *
	 * @return the result of the demonstration
	 * @throws NoMoreSolutionException if no more solutions are present
	 * @see SolveInfo
	 **/
	SolveInfo solveNext() throws Exception;
	
	/**
	 * Halts current solve computation
	 */
	void solveHalt();
	
	/**
	 * Accepts current solution
	 */
	void solveEnd();
	
	/**
	 * Asks for the presence of open alternatives to be explored
	 * in current demostration process.
	 *
	 * @return true if open alternatives are present
	 */
	boolean hasOpenAlternatives();
	
	/**
	 * Gets the string representation of a term, using operators
	 * currently defined by engine
	 *
	 * @param term      the term to be represented as a string
	 * @return the string representing the term
	 */
	String toString(Term term);
		
	/**
	 * Adds a listener to ouput events
	 *
	 * @param l the listener
	 */
	void addOutputListener(OutputListener l);
	
	/**
	 * Removes a listener to ouput events
	 *
	 * @param l the listener
	 */
	void removeOutputListener(OutputListener l);
	
	/**
	 * Removes all output event listeners
	 */
	void removeAllOutputListeners();
	
	/**
	 * Switches on/off the notification of spy information events
	 *
	 * @param state - true for enabling the notification of spy event
	 */
	void setSpy(boolean state);
	
	/**
	 * Adds a listener to spy events
	 *
	 * @param l the listener
	 */
	void addSpyListener(SpyListener l);
	
	/**
	 * Removes a listener to spy events
	 *
	 * @param l the listener
	 */
	void removeSpyListener(SpyListener l);
	
	/**
	 * Removes all spy event listeners
	 */
	void removeAllSpyListeners();

	
	/*Castagna 06/2011*/
	/**
	 * Adds a listener to exception events
	 *
	 * @param l the listener
	 */
	void addExceptionListener(ExceptionListener l);

	/**
	 * Removes a listener to exception events
	 *
	 * @param l the listener
	 */
	void removeExceptionListener(ExceptionListener l);

	/**
	 * Removes all exception event listeners
	 */
	void removeAllExceptionListeners();
	/**/
}
