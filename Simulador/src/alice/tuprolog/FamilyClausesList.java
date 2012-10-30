package alice.tuprolog;

import alice.util.ReadOnlyLinkedList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * <code>FamilyClausesList</code> is a common <code>LinkedList</code>
 * which stores {@link ClauseInfo} objects. Internally it indexes stored data
 * in such a way that, knowing what type of clauses are required, only
 * goal compatible clauses are returned
 *
 * @author Paolo Contessi
 * @since 2.2
 * 
 * @see LinkedList
 */
@SuppressWarnings("serial")
class FamilyClausesList extends LinkedList<ClauseInfo> {
	
	private FamilyClausesIndex<Number> numCompClausesIndex;
	private FamilyClausesIndex<String> constantCompClausesIndex;
	private FamilyClausesIndex<String> structCompClausesIndex;
	private LinkedList<ClauseInfo> listCompClausesList;

	//private LinkedList<ClauseInfo> clausesList;

	public FamilyClausesList(){
		super();

		numCompClausesIndex = new FamilyClausesIndex<Number>();
		constantCompClausesIndex = new FamilyClausesIndex<String>();
		structCompClausesIndex = new FamilyClausesIndex<String>();

		listCompClausesList = new LinkedList<ClauseInfo>();
	}

	/**
	 * Adds the given clause as first of the family
	 *
	 * @param ci    The clause to be added (with related informations)
	 */
	@Override
	public void addFirst(ClauseInfo ci){
		super.addFirst(ci);

		// Add first in type related storage
		register(ci, true);
	}

	/**
	 * Adds the given clause as last of the family
	 *
	 * @param ci    The clause to be added (with related informations)
	 */
	@Override
	public void addLast(ClauseInfo ci){
		super.addLast(ci);

		// Add last in type related storage
		register(ci, false);
	}

	@Override
	public boolean add(ClauseInfo o) {
		addLast(o);

		return true;
	}

	/**
	 * @deprecated 
	 */
	@Override
	public boolean addAll(int index, Collection<? extends ClauseInfo> c) {
		throw new UnsupportedOperationException("Not supported.");
	}

	/**
	 * @deprecated
	 */
	@Override
	public void add(int index, ClauseInfo element) {
		throw new UnsupportedOperationException("Not supported.");
	}

	/**
	 * @deprecated
	 */
	@Override
	public ClauseInfo set(int index, ClauseInfo element) {
		throw new UnsupportedOperationException("Not supported.");
	}

	@Override
	public ClauseInfo removeFirst() {
		ClauseInfo ci = getFirst();
		if (remove(ci)){
			return ci;
		}

		return null;
	}

	@Override
	public ClauseInfo removeLast() {
		ClauseInfo ci = getLast();
		if (remove(ci)){
			return ci;
		}

		return null;
	}

	@Override
	public ClauseInfo remove(){
		return removeFirst();
	}

	@Override
	public ClauseInfo remove(int index){
		ClauseInfo ci = super.get(index);

		if(remove(ci)){
			return ci;
		}

		return null;
	}

	@Override
	public boolean remove(Object ci){
		if(super.remove((ClauseInfo) ci))
		{
			unregister((ClauseInfo) ci);

			return true;
		}
		return false;
	}

	@Override
	public void clear(){
		while(size() > 0){
			removeFirst();
		}
	}

	/**
	 * Retrieves a sublist of all the clauses of the same family as the goal
	 * and which, in all probability, could match with the given goal
	 *
	 * @param goal  The goal to be resolved
	 * @return      The list of goal-compatible predicates
	 */
	public List<ClauseInfo> get(Term goal){
		// Gets the correct list and encapsulates it in ReadOnlyLinkedList
		if(goal instanceof Struct){
			Struct g = (Struct) goal.getTerm();

			/*
			 * If no arguments no optimization can be applied
			 * (and probably no optimization is needed)
			 */
			if(g.getArity() == 0){
				return new ReadOnlyLinkedList<ClauseInfo>(this);
			}

			/* Retrieves first argument and checks type */
			Term t = g.getArg(0).getTerm();
			if(t instanceof Var){
				/*
				 * if first argument is an unbounded variable,
				 * no reasoning is possible, all family must be returned
				 */
				return new ReadOnlyLinkedList<ClauseInfo>(this);
			} else if(t.isAtomic()){
				if(t instanceof Number){
					/* retrieves clauses whose first argument is numeric (or Var)
					 * and same as goal's first argument, if no clauses
					 * are retrieved, all clauses with a variable
					 * as first argument
					 */
					return new ReadOnlyLinkedList<ClauseInfo>(numCompClausesIndex.get((Number) t));
				} else if(t instanceof Struct){
					/* retrieves clauses whose first argument is a constant (or Var)
					 * and same as goal's first argument, if no clauses
					 * are retrieved, all clauses with a variable
					 * as first argument
					 */
					return new ReadOnlyLinkedList<ClauseInfo>(constantCompClausesIndex.get(((Struct) t).getName()));
				}
			} else if(t instanceof Struct){
				if(isAList((Struct) t)){
					/* retrieves clauses which has a list  (or Var) as first argument */
					return new ReadOnlyLinkedList<ClauseInfo>(listCompClausesList);
				} else {
					/* retrieves clauses whose first argument is a struct (or Var)
					 * and same as goal's first argument, if no clauses
					 * are retrieved, all clauses with a variable
					 * as first argument
					 */
					return new ReadOnlyLinkedList<ClauseInfo>(structCompClausesIndex.get(((Struct) t).getPredicateIndicator()));
				}
			}
		}

		/* Default behaviour: no optimization done */
		return new ReadOnlyLinkedList<ClauseInfo>(this);
	}

	@Override
	public Iterator<ClauseInfo> iterator(){
		return listIterator(0);
	}

	@Override
	public ListIterator<ClauseInfo> listIterator(){
		return new ListItr(this,0).getIt();
	}

	private ListIterator<ClauseInfo> superListIterator(int index){
		return super.listIterator(index);
	}

	@Override
	public ListIterator<ClauseInfo> listIterator(int index){
		return new ListItr(this,index).getIt();
	}

	private boolean isAList(Struct t) {
		/*
		 * Checks if a Struct is also a list.
		 * A list can be an empty list, or a Struct with name equals to "."
		 * and arity equals to 2.
		 */
		return t.isEmptyList() || (t.getName().equals(".") && t.getArity() == 2);

	}

	// Updates indexes, storing informations about the last added clause
	private void register(ClauseInfo ci, boolean first){
		// See FamilyClausesList.get(Term): same concept
		Term clause = ci.getHead();
		if(clause instanceof Struct){
			Struct g = (Struct) clause.getTerm();

			if(g.getArity() == 0){
				return;
			}

			Term t = g.getArg(0).getTerm();
			if(t instanceof Var){
				numCompClausesIndex.insertAsShared(ci, first);
				constantCompClausesIndex.insertAsShared(ci, first);
				structCompClausesIndex.insertAsShared(ci, first);

				if(first){
					listCompClausesList.addFirst(ci);
				} else {
					listCompClausesList.addLast(ci);
				}
			} else if(t.isAtomic()){
				if(t instanceof Number){
					numCompClausesIndex.insert((Number) t,ci, first);
				} else if(t instanceof Struct){
					constantCompClausesIndex.insert(((Struct) t).getName(), ci, first);
				}
			} else if(t instanceof Struct){
				if(isAList((Struct) t)){
					if(first){
						listCompClausesList.addFirst(ci);
					} else {
						listCompClausesList.addLast(ci);
					}
				} else {
					structCompClausesIndex.insert(((Struct) t).getPredicateIndicator(), ci, first);
				}
			}
		}
	}

	// Updates indexes, deleting informations about the last removed clause
	public void unregister(ClauseInfo ci) {
		Term clause = ci.getHead();
		if(clause instanceof Struct){
			Struct g = (Struct) clause.getTerm();

			if(g.getArity() == 0){
				return;
			}

			Term t = g.getArg(0).getTerm();
			if(t instanceof Var){
				numCompClausesIndex.removeShared(ci);
				constantCompClausesIndex.removeShared(ci);
				structCompClausesIndex.removeShared(ci);

				listCompClausesList.remove(ci);
			} else if(t.isAtomic()){
				if(t instanceof Number){
					numCompClausesIndex.delete((Number) t);
				} else if(t instanceof Struct){
					constantCompClausesIndex.delete(((Struct) t).getName());
				}
			} else if(t instanceof Struct){
				if(t.isList()){
					listCompClausesList.remove(ci);
				} else {
					structCompClausesIndex.delete(((Struct) t).getPredicateIndicator());
				}
			}
		}
	}

	private class ListItr implements ListIterator<ClauseInfo> {

		private ListIterator<ClauseInfo> it;
		private LinkedList<ClauseInfo> l;
		private int currentIndex = 0;

		public ListItr(FamilyClausesList list, int index){
			l = list;
			it = list.superListIterator(index);
		}

		public boolean hasNext() {
			return it.hasNext();
		}

		public ClauseInfo next() {
			// Alessandro Montanari - alessandro.montanar5@studio.unibo.it
			currentIndex = it.nextIndex();

			return it.next();
		}

		public boolean hasPrevious() {
			return it.hasPrevious();
		}

		public ClauseInfo previous() {
			// Alessandro Montanari - alessandro.montanar5@studio.unibo.it
			currentIndex = it.previousIndex();

			return it.previous();
		}

		public int nextIndex() {
			return it.nextIndex();
		}

		public int previousIndex() {
			return it.previousIndex();
		}

		public void remove() {
			// Alessandro Montanari - alessandro.montanar5@studio.unibo.it
			ClauseInfo ci = l.get(currentIndex);

			it.remove();

			unregister(ci);
		}

		public void set(ClauseInfo o) {
			it.set(o);
			//throw new UnsupportedOperationException("Not supported.");
		}

		public void add(ClauseInfo o) {
			l.addLast(o);

		}

		public ListIterator<ClauseInfo> getIt(){
			return this;
		}    


	}

	// Short test about the new implementation of the ListItr
	// Alessandro Montanari - alessandro.montanar5@studio.unibo.it
	private static class ListItrTest{

		private static FamilyClausesList clauseList = new FamilyClausesList();

		public static void main(String[] args) {
			ClauseInfo first = new ClauseInfo(new Struct(new Struct("First"),new Struct("First")),"First Element");
			ClauseInfo second = new ClauseInfo(new Struct(new Struct("Second"),new Struct("Second")),"Second Element");
			ClauseInfo third = new ClauseInfo(new Struct(new Struct("Third"),new Struct("Third")),"Third Element");
			ClauseInfo fourth = new ClauseInfo(new Struct(new Struct("Fourth"),new Struct("Fourth")),"Fourth Element");

			clauseList.add(first);
			clauseList.add(second);
			clauseList.add(third);
			clauseList.add(fourth);
			
			// clauseList = [First, Second, Third, Fourh]
			
			ListIterator<ClauseInfo> allClauses = clauseList.listIterator();
			// Get the first object and remove it
			allClauses.next();
			allClauses.remove();
			if(clauseList.contains(first))
			{
				System.out.println("Error!");
				System.exit(-1);
			}

			// First object removed
			// clauseList = [Second, Third, Fourh]

			// Get the second object
			allClauses.next();
			// Get the third object
			allClauses.next();
			// Get the third object
			allClauses.previous();
			// Get the second object and remove it
			allClauses.previous();
			allClauses.remove();
			if(clauseList.contains(second))
			{
				System.out.println("Error!");
				System.exit(-2);
			}
			
			// clauseList = [Third, Fourh]

			System.out.println("Ok!!!");
		}
	}

}


