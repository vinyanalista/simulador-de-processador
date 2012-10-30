/*
 * tuProlog - Copyright (C) 2001-2002  aliCE team at deis.unibo.it
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package alice.tuprolog.lib;

import alice.tuprolog.*;
import alice.tuprolog.Number;

import java.util.*;
import java.io.*;

/**
 * This class provides basic I/O predicates.
 * 
 * Library/Theory Dependency: BasicLibrary
 * 
 * 
 * 
 */
@SuppressWarnings("serial")
public class IOLibrary extends Library {

    protected String inputStreamName = "stdin";
    protected InputStream inputStream = System.in;
    protected String outputStreamName = "stdout";
    protected OutputStream outputStream = System.out;
    private Random gen = new Random();

    public IOLibrary() {
        gen.setSeed(System.currentTimeMillis());
    }

    public boolean see_1(Term arg) throws PrologError {
        arg = arg.getTerm();
        if (arg instanceof Var)
            throw PrologError.instantiation_error(engine.getEngineManager(), 1);
        if (!arg.isAtom()) {
            throw PrologError.type_error(engine.getEngineManager(), 1, "atom",
                    arg);
        }
        Struct arg0 = (Struct) arg.getTerm();
        if (inputStream != System.in)
            try {
                inputStream.close();
            } catch (IOException e) {
                return false;
            }
        if (arg0.getName().equals("stdin")) {
            inputStream = System.in;
        } else {
            try {
                inputStream = new FileInputStream(((Struct) arg0).getName());
            } catch (FileNotFoundException e) {
                throw PrologError.domain_error(engine.getEngineManager(), 1,
                        "stream", arg0);
            }
        }
        inputStreamName = ((Struct) arg0).getName();
        return true;
    }

    public boolean seen_0() {
        if (inputStream != System.in) {
            try {
                inputStream.close();
            } catch (IOException e) {
                return false;
            }
            inputStream = System.in;
            inputStreamName = "stdin";
        }
        return true;
    }

    public boolean seeing_1(Term t) {
        return unify(t, new Struct(inputStreamName));
    }

    public boolean tell_1(Term arg) throws PrologError {
        arg = arg.getTerm();
        if (arg instanceof Var)
            throw PrologError.instantiation_error(engine.getEngineManager(), 1);
        if (!arg.isAtom()) {
            throw PrologError.type_error(engine.getEngineManager(), 1, "atom",
                    arg);
        }
        Struct arg0 = (Struct) arg.getTerm();
        if (outputStream != System.out)
            try {
                outputStream.close();
            } catch (IOException e) {
                return false;
            }
        if (arg0.getName().equals("stdout")) {
            outputStream = System.out;
        } else {
            try {
                outputStream = new FileOutputStream(((Struct) arg0).getName());
            } catch (FileNotFoundException e) {
                throw PrologError.domain_error(engine.getEngineManager(), 1,
                        "stream", arg);
            }
        }
        outputStreamName = ((Struct) arg0).getName();
        return true;
    }

    public boolean told_0() {
        if (outputStream != System.out) {
            try {
                outputStream.close();
            } catch (IOException e) {
                return false;
            }
            outputStream = System.out;
            outputStreamName = "stdout";
        }
        return true;
    }

    public boolean telling_1(Term arg0) {
        return unify(arg0, new Struct(outputStreamName));
    }

    public boolean put_1(Term arg) throws PrologError {
        arg = arg.getTerm();
        if (arg instanceof Var)
            throw PrologError.instantiation_error(engine.getEngineManager(), 1);
        if (!arg.isAtom()) {
            throw PrologError.type_error(engine.getEngineManager(), 1,
                    "character", arg);
        } else {
            Struct arg0 = (Struct) arg.getTerm();
            String ch = arg0.getName();
            if (ch.length() > 1) {
                throw PrologError.type_error(engine.getEngineManager(), 1,
                        "character", arg);
            } else {
                if (outputStreamName.equals("stdout")) {
                    getEngine().stdOutput(ch);
                } else {
                    try {
                        outputStream.write((byte) ch.charAt(0));
                    } catch (IOException e) {
                        throw PrologError.permission_error(engine
                                .getEngineManager(), "output", "stream",
                                new Struct(outputStreamName), new Struct(e
                                        .getMessage()));
                    }
                }
                return true;
            }
        }
    }

    public boolean get0_1(Term arg0) throws PrologError {
        int ch = -2;
        try {
            ch = inputStream.read();
        } catch (IOException e) {
            throw PrologError.permission_error(engine.getEngineManager(),
                    "input", "stream", new Struct(inputStreamName), new Struct(
                            e.getMessage()));
        }
        if (ch == -1) {
            return unify(arg0, new Int(-1));
        } else {
            return unify(arg0, new Struct(new Character((char) ch).toString()));
        }
    }

    public boolean get_1(Term arg0) throws PrologError {
        int ch = 0;
        do {
            try {
                ch = inputStream.read();
            } catch (IOException e) {
                throw PrologError.permission_error(engine.getEngineManager(),
                        "input", "stream", new Struct(inputStreamName),
                        new Struct(e.getMessage()));
            }
        } while (ch < 0x20 && ch >= 0);
        if (ch == -1) {
            return unify(arg0, new Int(-1));
        } else {
            return unify(arg0,
                    new Struct(new Character(((char) ch)).toString()));
        }
    }

    public boolean tab_1(Term arg) throws PrologError {
        arg = arg.getTerm();
        if (arg instanceof Var)
            throw PrologError.instantiation_error(engine.getEngineManager(), 1);
        if (!(arg instanceof Int))
            throw PrologError.type_error(engine.getEngineManager(), 1,
                    "integer", arg);
        // int n = ((Int)arg).intValue(); // OLD BUGGED  VERSION (signaled by MViroli) 
        int n = ((Int)arg.getTerm()).intValue(); // NEW CORRECT VERSION (by MViroli, EDenti)
        if (outputStreamName.equals("stdout")) {
            for (int i = 0; i < n; i++) {
                getEngine().stdOutput(" ");
            }
        } else {
            for (int i = 0; i < n; i++) {
                try {
                    outputStream.write(0x20);
                } catch (IOException e) {
                    throw PrologError.permission_error(engine
                            .getEngineManager(), "output", "stream",
                            new Struct(outputStreamName), new Struct(e
                                    .getMessage()));
                }
            }
        }
        return true;
    }

    public boolean read_1(Term arg0) throws PrologError {
        arg0 = arg0.getTerm();
        int ch = 0;

        boolean open_apices = false;
        // boolean just_open_apices = false;
        boolean open_apices2 = false;
        // boolean just_open_apices2 = false;

        String st = "";
        do {
            try {
                ch = inputStream.read();
            } catch (IOException e) {
                throw PrologError.permission_error(engine.getEngineManager(),
                        "input", "stream", new Struct(inputStreamName),
                        new Struct(e.getMessage()));
            }

            if (ch == -1) {
                break;
            }
            boolean can_add = true;

            if (ch == '\'') {
                if (!open_apices) {
                    open_apices = true;
                } else {
                    open_apices = false;
                }
            } else if (ch == '\"') {
                if (!open_apices2) {
                    open_apices2 = true;
                } else {
                    open_apices2 = false;
                }
            } else {
                if (ch == '.') {
                    if (!open_apices && !open_apices2) {
                        break;
                    }
                }
            }

            if (can_add) {
                st += new Character(((char) ch)).toString();
            }
        } while (true);
        try {
            unify(arg0, getEngine().toTerm(st));
        } catch (InvalidTermException e) {
            /*Castagna 06/2011*/
        	//throw PrologError.syntax_error(engine.getEngineManager(), -1, -1, new Struct(st));
        	throw PrologError.syntax_error(engine.getEngineManager(),-1, e.line, e.pos, new Struct(st));
        	/**/
        }
        return true;
    }

    public boolean write_1(Term arg0) throws PrologError {
        arg0 = arg0.getTerm();
        if (arg0 instanceof Var)
            throw PrologError.instantiation_error(engine.getEngineManager(), 1);
        if (outputStreamName.equals("stdout")) {
            getEngine().stdOutput(arg0.toString());
        } else {
            try {
                outputStream.write(arg0.toString().getBytes());
            } catch (IOException e) {
                throw PrologError.permission_error(engine.getEngineManager(),
                        "output", "stream", new Struct(outputStreamName),
                        new Struct(e.getMessage()));
            }
        }
        return true;
    }

    public boolean print_1(Term arg0) throws PrologError {
        arg0 = arg0.getTerm();
        if (arg0 instanceof Var)
            throw PrologError.instantiation_error(engine.getEngineManager(), 1);
        if (outputStreamName.equals("stdout")) {
            getEngine().stdOutput(
                    alice.util.Tools.removeApices(arg0.toString()));
        } else {
            try {
                outputStream.write(alice.util.Tools.removeApices(
                        arg0.toString()).getBytes());
            } catch (IOException e) {
                throw PrologError.permission_error(engine.getEngineManager(),
                        "output", "stream", new Struct(outputStreamName),
                        new Struct(e.getMessage()));
            }
        }
        return true;

    }

    public boolean nl_0() throws PrologError {
        if (outputStreamName.equals("stdout")) {
            getEngine().stdOutput("\n");
        } else {
            try {
                outputStream.write('\n');
            } catch (IOException e) {
                throw PrologError.permission_error(engine.getEngineManager(),
                        "output", "stream", new Struct(outputStreamName),
                        new Struct(e.getMessage()));
            }
        }
        return true;
    }

    /**
     * reads a source text from a file.
     * <p>
     * It's useful used with agent predicate: text_from_file(File,Source),
     * agent(Source).
     * 
     * @throws PrologError
     */
    public boolean text_from_file_2(Term file_name, Term text)
            throws PrologError {
        file_name = file_name.getTerm();
        if (file_name instanceof Var)
            throw PrologError.instantiation_error(engine.getEngineManager(), 1);
        if (!file_name.isAtom())
            throw PrologError.type_error(engine.getEngineManager(), 1, "atom",
                    file_name);
        Struct fileName = (Struct) file_name.getTerm();
        Struct goal = null;
        try {
            goal = new Struct(alice.util.Tools.loadText(alice.util.Tools
                    .removeApices(((Struct) fileName).toString())));
        } catch (IOException e) {
            throw PrologError.existence_error(engine.getEngineManager(), 1,
                    "stream", file_name, new Struct(e.getMessage()));
        }
        return unify(text, goal);
    }

    // miscellanea
    /**
     * Sets an arbitrary seed for the Random object.
     * 
     * @param seed Seed to use
     * @return true if seed Term has a valid long value, false otherwise
     */
    public boolean set_seed_1(Term t) throws PrologError {
        t = t.getTerm();
        if( !(t instanceof Number) ) {
            throw PrologError.type_error(engine.getEngineManager(), 1, "Integer Number", t);
        }
        Number seed = (Number)t;
        if( !seed.isInteger() ){
            throw PrologError.type_error(engine.getEngineManager(), 1, "Integer Number", t);
        }
        gen.setSeed(((Number)seed).longValue());
        return true;
    }

    public boolean rand_float_1(Term t) {
        return unify(t, new alice.tuprolog.Double(gen.nextFloat()));
    }

    public boolean rand_int_2(Term argNum, Term num) {
        alice.tuprolog.Number arg = (alice.tuprolog.Number) argNum.getTerm();
        return unify(num, new Int(gen.nextInt(arg.intValue())));
    }

    public String getTheory() {
        return "consult(File) :- text_from_file(File,Text), add_theory(Text).\n"
                + "reconsult(File) :- text_from_file(File,Text), set_theory(Text).\n"
                + "solve_file(File,Goal) :- solve_file_goal_guard(File,Goal),text_from_file(File,Text),text_term(Text,Goal),call(Goal).\n"
                + "agent_file(X)  :- text_from_file(X,Y),agent(Y).\n";
    }

    // Java guards for Prolog predicates

    public boolean solve_file_goal_guard_2(Term arg0, Term arg1)
            throws PrologError {
        arg0 = arg0.getTerm();
        arg1 = arg1.getTerm();
        if (arg1 instanceof Var)
            throw PrologError.instantiation_error(engine.getEngineManager(), 2);
        if (!arg1.isAtom() && !arg1.isCompound()) {
            throw PrologError.type_error(engine.getEngineManager(), 2,
                    "callable", arg1);
        }
        return true;
    }

    // to allow serialization -> nullify streams before serialization

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        InputStream inputStreamBak = inputStream;
        OutputStream outputStreamBak = outputStream;
        inputStream = null;
        outputStream = null;
        try {
            out.defaultWriteObject();
        } catch (IOException ex) {
            inputStream = inputStreamBak;
            outputStream = outputStreamBak;
            throw new IOException();
        }
        inputStream = inputStreamBak;
        outputStream = outputStreamBak;
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException,
            ClassNotFoundException {
        in.defaultReadObject();
        if (outputStreamName.equals("user")) {
            outputStream = System.out;
        }
        if (inputStreamName.equals("user")) {
            inputStream = System.in;
        }
    }
    

    public boolean write_base_1(Term arg0) throws PrologError {
        arg0 = arg0.getTerm();
        
        if (arg0 instanceof Var)
            throw PrologError.instantiation_error(engine.getEngineManager(), 1);
        if (outputStreamName.equals("stdout")) {
            getEngine().stdOutput(arg0.toString());
        } else {
            try {
                outputStream.write(arg0.toString().getBytes());
            } catch (IOException e) {
                throw PrologError.permission_error(engine.getEngineManager(),
                        "output", "stream", new Struct(outputStreamName),
                        new Struct(e.getMessage()));
            }
        }
        return true;
    }
}