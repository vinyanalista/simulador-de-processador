/*Castagna 06/2011*/
package alice.tuprolog;

public interface TermVisitor {
	void visit(Struct s);
	void visit(Var v);
	void visit(Number n);
}
/**/