package alice.tuprolog.lib;

/**
 * @author: Sara Sabioni
 */

import alice.tuprolog.*;
import alice.tuprolog.Number;
import alice.tuprolog.Long;

import java.util.*;
import java.io.*;

/**
 * This class provides basic ISO I/O predicates.
 * 
 * Library/Theory Dependency: IOLibrary
 * 
 * 
 */

@SuppressWarnings("serial")
public class ISOIOLibrary extends Library{
    
    protected final int files = 1000; //numero casuale abbastanza alto per evitare eccezioni
    protected Hashtable<InputStream, Hashtable<String, Term>> inputStreams = new Hashtable<InputStream,Hashtable<String,Term>>(files);
    protected Hashtable<OutputStream, Hashtable<String, Term>> outputStreams = new Hashtable<OutputStream,Hashtable<String,Term>>(files);
    
    protected InputStream inputStream = null;
    protected OutputStream outputStream = null;
    protected String inputStreamName = null;
    protected String outputStreamName = null;
    protected IOLibrary IOLib = null;
    
    private int flag = 0;
    private int write_flag = 1;

    public ISOIOLibrary(){
        
    }
    
    public boolean open_4(Term source_sink, Term mode, Term stream, Term options)throws PrologError{
        initLibrary();
        source_sink = source_sink.getTerm();
        mode = mode.getTerm();
        
        if (source_sink instanceof Var) {
            throw PrologError.instantiation_error(engine.getEngineManager(), 1);
        }
        
        File file = new File(((Struct)source_sink).getName());
        if(!file.exists()){
            throw PrologError.existence_error(engine.getEngineManager(), 1, "source_sink", source_sink, new Struct("File not exists"));
        }

        if(mode instanceof Var){
            throw PrologError.instantiation_error(engine.getEngineManager(), 2);
        }
        else if(!mode.isAtom()){
            throw PrologError.type_error(engine.getEngineManager(), 1, "atom",mode);
        }
        
        if(!(stream instanceof Var)){
            throw PrologError.type_error(engine.getEngineManager(), 3, "variable", stream);
        }
        
        Hashtable<String, Term> properties = new Hashtable<String, Term>(10);
        boolean result = inizialize_properties(properties);
        BufferedOutputStream output = null;
        BufferedInputStream input = null;        
        
        if(result == true){
            Struct openOptions = (Struct)options;
            Struct in_out = (Struct)source_sink;
            if(openOptions.isList()){
                if(!openOptions.isEmptyList()){
                    Iterator<? extends Term> i = openOptions.listIterator();
                    while(i.hasNext()){
                        Struct option = null;
                        Object obj = i.next();
                        if(obj instanceof Var){
                            throw PrologError.instantiation_error(engine.getEngineManager(), 4);
                        }
                        option = (Struct)obj;
                        if(!properties.containsKey(option.getName())){
                            throw PrologError.domain_error(engine.getEngineManager(), 4, "stream_option", option);
                        }
                        if(option.getName().equals("alias")){ //controllo che alias non sia gi? associato ad uno stream aperto
                        //ciclo su inputStreams
                            for(Map.Entry<InputStream, Hashtable<String, Term>> currentElement : inputStreams.entrySet()){
                                for(Map.Entry<String, Term> currentElement2 : currentElement.getValue().entrySet()){
                                    if(currentElement2.getKey().equals("alias")){
                                        Term alias = currentElement2.getValue();
                                        for(int k = 0; k< option.getArity();k++){
                                            if(((Struct)alias).getArity()>1){
                                                for(int z = 0;z<((Struct)alias).getArity();z++){
                                                    if((((Struct)alias).getArg(z)).equals(option.getArg(k))){
                                                        throw PrologError.permission_error(engine.getEngineManager(), "open", "source_sink", alias, new Struct("Alias is already associated with an open stream"));
                                                    }
                                                }
                                            }
                                            else if(alias.equals(option.getArg(k))){
                                                throw PrologError.permission_error(engine.getEngineManager(), "open", "source_sink", alias, new Struct("Alias is already associated with an open stream"));
                                            }
                                        }
                                    }
                                }
                            }
                        //ciclo su outputStreams
                            for(Map.Entry<OutputStream, Hashtable<String, Term>> currentElement : outputStreams.entrySet()){
                                for(Map.Entry<String, Term> currentElement2 : currentElement.getValue().entrySet()){
                                    if(currentElement2.getKey().equals("alias")){
                                        Term alias = currentElement2.getValue();
                                        for(int k = 0; k< option.getArity();k++){
                                            if(((Struct)alias).getArity()>1){
                                                for(int z = 0;z<((Struct)alias).getArity();z++){
                                                    if((((Struct)alias).getArg(z)).equals(option.getArg(k))){
                                                        throw PrologError.permission_error(engine.getEngineManager(), "open", "source_sink", alias, new Struct("Alias is already associated with an open stream"));
                                                    }
                                                }
                                            }
                                            else if(alias.equals(option.getArg(k))){
                                                throw PrologError.permission_error(engine.getEngineManager(), "open", "source_sink", alias, new Struct("Alias is already associated with an open stream"));
                                            }
                                        }
                                    }
                                }
                            }
                            int arity = option.getArity();
                            if(arity > 1){
                                Term[] arrayTerm = new Term[arity];
                                for(int k = 0; k<arity; k++){
                                    arrayTerm[k] = option.getArg(k);
                                }
                                properties.put(option.getName(),new Struct(".",arrayTerm));
                            }
                            else{
                                properties.put(option.getName(),option.getArg(0));
                            }
                        }
                        else{
                            Struct value = null;
                            value =(Struct) option.getArg(0);
                            properties.put(option.getName(),value);
                        }
                    }
                    properties.put("mode", mode);
                    properties.put("file_name", source_sink);
                }
            }
            else{
                throw PrologError.type_error(engine.getEngineManager(), 4, "list", openOptions);
            }
            
            Struct structMode = (Struct)mode;
            if(structMode.getName().equals("write")){
                try{
                    output = new BufferedOutputStream(new FileOutputStream(((Struct) in_out).getName()));
                }
                catch(Exception e){
                    //potrebbe essere sia FileNotFoundException sia SecurityException
                    throw PrologError.permission_error(engine.getEngineManager(), "open", "source_sink", source_sink,
                            new Struct("The source_sink specified by Source_sink cannot be opened"));
                }
                properties.put("output", new Struct("true"));    
                outputStreams.put(output, properties);
                return unify(stream, new Struct(output.toString()));
            }
            else if(structMode.getName().equals("read")){
                    try {
                        input = new BufferedInputStream(new FileInputStream(((Struct) in_out).getName()));
                    } catch (Exception e) {
                        throw PrologError.permission_error(engine.getEngineManager(), "open", "source_sink", source_sink,
                                new Struct("The source_sink specified by Source_sink cannot be opened"));
                    }
                    properties.put("input", new Struct("true"));
                    
                    //mi servono queste istruzioni per set_stream_position
                    //faccio una mark valida fino alla fine del file, appena lo apro in modo che mi possa
                    //permettere di fare una reset all'inizio del file. Il +5 alloca un po di spazio in pi?
                    //nel buffer, mi serve per? per evitare che la mark non sia pi? valida quando leggo la fine del file
                    if(((Struct)properties.get("reposition")).getName().equals("true")){
                        try {
                            input.mark((input.available())+5);
                        } catch (IOException e) {
                            throw PrologError.system_error(new Struct("An error has occurred in open"));
                        }
                    }
                    inputStreams.put(input, properties);
                    return unify(stream, new Struct(input.toString()));
            }
            else if(structMode.getName().equals("append")){
                try{
                    output = new BufferedOutputStream(new FileOutputStream(((Struct) in_out).getName(),true));
                }
                catch(Exception e){
                    throw PrologError.permission_error(engine.getEngineManager(), "open", "source_sink", source_sink,
                            new Struct("The source_sink specified by Source_sink cannot be opened"));
                }
                properties.put("output", new Struct("true"));                    
                outputStreams.put(output, properties);
                return unify(stream, new Struct(output.toString()));
            }
            else{
                throw PrologError.domain_error(engine.getEngineManager(),2,"io_mode", mode);    
            }
        }
        else{
            PrologError.system_error(new Struct("A problem has occurred with the initialization of the hashmap properties"));
            return false;
        }
    }
    
    public boolean open_3(Term source_sink, Term mode, Term stream)throws PrologError{
        initLibrary();
        
        source_sink = source_sink.getTerm();
        File file = new File(((Struct)source_sink).getName());
        if(!file.exists()){
            throw PrologError.existence_error(engine.getEngineManager(), 1, "source_sink", source_sink, new Struct("File not exists"));
        }
        mode = mode.getTerm();
        if (source_sink instanceof Var) {
            throw PrologError.instantiation_error(engine.getEngineManager(), 1);
        }

        if(mode instanceof Var){
            throw PrologError.instantiation_error(engine.getEngineManager(), 2);
        }
        else if(!mode.isAtom()){
            throw PrologError.type_error(engine.getEngineManager(), 1, "atom",mode);
        }
        
        if(!(stream instanceof Var)){
            throw PrologError.type_error(engine.getEngineManager(), 3, "variable", stream);
        }
        
        //siccome ? una open con la lista delle opzioni vuota, inizializzo comunque le opzioni
        //e inoltre inserisco i valori che gi? conosco come file_name,mode,input,output e type.
        Hashtable<String, Term> properties = new Hashtable<String, Term>(10);
        boolean result = inizialize_properties(properties);
        
        BufferedOutputStream output = null;
        BufferedInputStream input = null;
        Struct structMode = (Struct)mode;
        
        if(result == true){
            Struct in_out = (Struct)source_sink;
            Struct value = new Struct(in_out.getName());
            properties.put("file_name", value);
            properties.put("mode", mode);
                
            if(structMode.getName().equals("write")){
                try{
                    output = new BufferedOutputStream(new FileOutputStream(((Struct) in_out).getName()));
                }
                catch(Exception e){
                    //potrebbe essere sia FileNotFoundException sia SecurityException
                    throw PrologError.permission_error(engine.getEngineManager(), "open", "source_sink", source_sink,
                            new Struct("The source_sink specified by Source_sink cannot be opened"));
                }
                properties.put("output", new Struct("true"));                    
                outputStreams.put(output, properties);
                return unify(stream, new Struct(output.toString()));
            }
            else if(structMode.getName().equals("read")){
                    try {
                        input = new BufferedInputStream(new FileInputStream(((Struct) in_out).getName()));
                    } catch (Exception e) {
                        throw PrologError.permission_error(engine.getEngineManager(), "open", "source_sink", source_sink,
                                new Struct("The source_sink specified by Source_sink cannot be opened"));
                    }
                    properties.put("input", new Struct("true"));
                    
                    //vedi open_4 per spiegazione
                    if(((Struct)properties.get("reposition")).getName().equals("true")){
                        try {
                            input.mark((input.available())+5);
                        } catch (IOException e) {
                            throw PrologError.system_error(new Struct("An error has occurred in open"));
                        }
                    }
                    
                    inputStreams.put(input, properties);
                    return unify(stream, new Struct(input.toString()));
            }
            else if(structMode.getName().equals("append")){
                try{
                    output = new BufferedOutputStream(new FileOutputStream(((Struct) in_out).getName(),true));
                }
                catch(Exception e){
                    throw PrologError.permission_error(engine.getEngineManager(), "open", "source_sink", source_sink,
                            new Struct("The source_sink specified by Source_sink cannot be opened"));
                }
                properties.put("output", new Struct("true"));                    
                outputStreams.put(output, properties);
                return unify(stream, new Struct(output.toString()));
            }
            else{
                throw PrologError.domain_error(engine.getEngineManager(), 1,"stream", in_out);    
            }
        }
        else{
            PrologError.system_error(new Struct("A problem has occurred with the initialization of the hashmap properties"));
            return false;
        }
    }
    
    public boolean close_2(Term stream_or_alias, Term closeOptions)throws PrologError{
        initLibrary();
        //Struct result = null;
        OutputStream out = null;
        InputStream in = null;
        
        boolean force = false;
        Struct closeOption = (Struct) closeOptions;
        
        if(closeOptions.isList()){
            if(!closeOptions.isEmptyList()){
                Iterator<? extends Term> i = closeOption.listIterator();
                while(i.hasNext()){
                    Struct option = null;
                    Object obj = i.next();
                    if(obj instanceof Var){
                        throw PrologError.instantiation_error(engine.getEngineManager(), 4);
                    }
                    option = (Struct)obj;
                    if(option.getName().equals("force")){
                        Struct closeOptionValue = (Struct)option.getArg(0); 
                        force = closeOptionValue.getName().equals("true") ? true : false;
                    }
                    else{
                        throw PrologError.domain_error(engine.getEngineManager(), 2, "close_option", option);
                    }
                }
            }
        }
        else{
            throw PrologError.type_error(engine.getEngineManager(), 4, "list", closeOptions);
        }
        
        //Siccome non so di quale natura ? lo stream, provo a cercarlo sia in inputStreams che
        //in outputStreams se in inputStreams non c'? la funzione lancia un errore
        //raccolgo l'eccezione e controllo in out. Se anche l? non c'? non raccolgo l'eccezione
        //perch? significa che lo stream che mi ? stato passato non ? aperto.
        try{
            in = find_input_stream(stream_or_alias);
        }
        catch(PrologError p){
            out = find_output_stream(stream_or_alias);
        }
        
        if(out != null){
            String out_name = get_output_name(out);
            if(out_name.equals("stdout")){
                return true;
            }
            try {
                flush_output_1(stream_or_alias);
                out.close();
            } catch (IOException e) {
                if(force == true){//devo forzare la chiusura 
                    //siccome in java non c'? modo di forzare la chiusura ho modellato il problema
                    //eliminando ogni riferimento all'oggetto stream, in modo tale che venga eliminato dal
                    //dal garabage colletor.
                    outputStreams.remove(in);
                    out = null;
                    //nel caso in cui lo stream che viene chiuso ? lo stream corrente, riassegno stdin o stdout
                    //ai riferimenti dello stream corrente
                    if(out_name.equals(outputStreamName)){
                        outputStreamName = "stdout";
                        outputStream = System.out;
                    }
                }    
                else{//lo stream rimane aperto,avverto che si sono verificati errori
                    throw PrologError.system_error(new Struct("An error has occurred on stream closure"));
                }
            }
        }
        else if(in != null){
            String in_name = get_input_name(in);
            if(in_name.equals("stdin")){
                return true;
            }
            try {
                in.close();
            } catch (IOException e) {
                if(force == true){
                    inputStreams.remove(in);
                    in = null;
                    if(in_name.equals(inputStreamName)){
                        inputStreamName = "stdin";
                        inputStream = System.in;
                    }
                }
                else{
                    throw PrologError.system_error(new Struct("An error has occurred on stream closure"));
                }
            }
            inputStreams.remove(in);
        }
        return true;
    }
    
    public boolean close_1(Term stream_or_alias)throws PrologError{
        initLibrary();
        //Struct result = null;
        OutputStream out = null;
        InputStream in = null;
        
        try{
            in = find_input_stream(stream_or_alias);
        }
        catch(PrologError p){
            out = find_output_stream(stream_or_alias);
        }
        
        if(out != null){
            String out_name = get_output_name(out);
            if(out_name.equals("stdout")){
                return true;
            }
            flush_output_1(stream_or_alias);
            try {
                out.close();
            } catch (IOException e) {
                throw PrologError.system_error(new Struct("An error has occurred on stream closure"));
            }
            if(out_name.equals(outputStreamName)){
                outputStreamName = "stdout";
                outputStream = System.out;
            }
            outputStreams.remove(out);
        }
        else if(in != null){
            String in_name = get_input_name(in);
            if(in_name.equals("stdin")){
                return true;
            }
            try {
                in.close();
            } catch (IOException e) {
                throw PrologError.system_error(new Struct("An error has occurred on stream closure"));
            }
            if(in_name.equals(inputStreamName)){
                inputStreamName = "stdin";
                inputStream = System.in;
            }
            inputStreams.remove(in);
        }
        return true;
    }
    
    public boolean set_input_1(Term stream_or_alias)throws PrologError{
        initLibrary();
        InputStream stream = find_input_stream(stream_or_alias);
        Hashtable<String,Term> entry = inputStreams.get(stream);
        Struct name = (Struct)entry.get("file_name");
        inputStream = stream;
        inputStreamName = name.getName();
        return true;
    }
    
    public boolean set_output_1(Term stream_or_alias)throws PrologError{
        initLibrary();
        OutputStream stream = find_output_stream(stream_or_alias);
        Hashtable<String,Term> entry = outputStreams.get(stream);
        Struct name = (Struct)entry.get("file_name");
        outputStream = stream;
        outputStreamName = name.getName();
        return true;
    }
    
    //funzione integrata con codice Prolog: ricerca, data una propriet?, tutti gli stream
    //che la soddisfano
    public boolean find_property_2(Term list, Term property)throws PrologError{
        initLibrary();
        if(outputStreams.isEmpty() && inputStreams.isEmpty()){
            return false;
        }

        if(!(list instanceof Var)){
            throw PrologError.instantiation_error(engine.getEngineManager(), 1);
        }
        
        property = property.getTerm();
        Struct prop = (Struct)property;
        String propertyName = prop.getName();
        Struct propertyValue = null;
        if(!propertyName.equals("input") && !propertyName.equals("output")){
            propertyValue = (Struct)prop.getArg(0);
        }
        List<Struct> resultList = new ArrayList<Struct>(); //object generico perch? sono sia inputStream che outputStream
            
        if(propertyName.equals("input")){
            for(Map.Entry<InputStream,Hashtable<String, Term>> stream:inputStreams.entrySet()){
                resultList.add(new Struct(stream.getKey().toString()));
            }
            Struct result = new Struct(resultList.toArray(new Struct[1]));
            return unify(list,result);
        }
        else if(propertyName.equals("output")){
            for(Map.Entry<OutputStream,Hashtable<String, Term>> stream:outputStreams.entrySet()){
                resultList.add(new Struct(stream.getKey().toString()));
            }
            Struct result = new Struct(resultList.toArray(new Struct[1]));
            return unify(list,result);
        }
        else{
            for(Map.Entry<InputStream, Hashtable<String, Term>> currentElement : inputStreams.entrySet()){
                for(Map.Entry<String, Term> currentElement2 : currentElement.getValue().entrySet()){
                    if(currentElement2.getKey().equals(propertyName)){
                        if(propertyName.equals("alias")){
                            int arity = ((Struct)currentElement2.getValue()).getArity();
                            if(arity == 0){
                                if(propertyValue.equals(((Struct)currentElement2.getValue()))){
                                    resultList.add(new Struct(currentElement.getKey().toString()));
                                    break;
                                }
                            }
                            for(int i = 0; i<arity; i++){
                                if(propertyValue.equals(((Struct)currentElement2.getValue()).getArg(i))){
                                    resultList.add(new Struct(currentElement.getKey().toString()));
                                    break;
                                }
                            }
                        }
                        else if(currentElement2.getValue().equals(propertyValue)){
                            resultList.add(new Struct(currentElement.getKey().toString()));
                        }
                    }
                }
            }
            
            for(Map.Entry<OutputStream, Hashtable<String, Term>> currentElement : outputStreams.entrySet()){
                for(Map.Entry<String, Term> currentElement2 : currentElement.getValue().entrySet()){
                    if(currentElement2.getKey().equals(propertyName)){
                        if(propertyName.equals("alias")){
                            int arity = ((Struct)currentElement2.getValue()).getArity();
                            for(int i = 0; i<arity; i++){
                                if(propertyValue.equals(((Struct)currentElement2.getValue()).getArg(i))){
                                    resultList.add(new Struct(currentElement.getKey().toString()));
                                    break;
                                }
                            }
                        }
                        else if(currentElement2.getValue().equals(propertyValue)){
                            resultList.add(new Struct(currentElement.getKey().toString()));
                        }
                    }
                }
            }
        } 
        Struct result = new Struct(resultList.toArray(new Struct[1]));
        return unify(list,result);
    }
    
    //stream_property_2(Stream, Property)
    public  String  getTheory(){ 
        return  "stream_property(S,P) :- "+
                "find_property(L,P),"+
                "member(S,L)."; 
    } 
    
    public boolean at_end_of_stream_0()throws PrologError{
        initLibrary();
        Hashtable<String,Term> entry = inputStreams.get(inputStream);
        Term value = entry.get("end_of_stream");
        Struct eof = (Struct) value;
        if(eof.getName().equals("not")){
            return false;
        }
        else{
            return true;
        }
    }
    
    public boolean at_end_of_stream_1(Term stream_or_alias) throws PrologError{
        initLibrary();
        InputStream stream = find_input_stream(stream_or_alias);
        Hashtable<String,Term> entry = inputStreams.get(stream);
        Term value = entry.get("end_of_stream");
        Struct eof = (Struct) value;
        if(eof.getName().equals("not")){
            return false;
        }
        else{
            return true;
        }
    }
    
    //modificare la posizione dello stream se la propriet? reposition ? true
    public boolean set_stream_position_2(Term stream_or_alias, Term position)throws PrologError{
        //soltanto per inputStream!
        initLibrary();
        InputStream in = find_input_stream(stream_or_alias);
        Term reposition = null;
        BufferedInputStream buffer = null;
        
        if(position instanceof Var){
            throw PrologError.instantiation_error(engine.getEngineManager(), 2);
        }
        else{
            if(!(position instanceof Number)){
                throw PrologError.domain_error(engine.getEngineManager(), 2, "stream_position", position);
            }
        }
        
        Hashtable<String,Term> entry = inputStreams.get(in);
        reposition = entry.get("reposition");
            
        Struct value = (Struct)reposition;
        if(value.getName().equals("false")){
            throw PrologError.permission_error(engine.getEngineManager(), "reposition", "stream", stream_or_alias, new Struct("Stream has reposition(false)"));
        }
        
        if(in instanceof BufferedInputStream){
            buffer = (BufferedInputStream) in;
        }
        
        if(buffer.markSupported()){
            try {                                
                buffer.reset();
                                
                Number n = (Number)position;
                long pos = n.longValue();
                if(pos < 0){
                    throw PrologError.domain_error(engine.getEngineManager(), 2, "+long", position);
                }
                
                int size;
                size = in.available();
                
                if(pos > size){
                    throw PrologError.system_error(new Struct("Invalid operation. Input position is greater than file size"));
                }
                if(pos == size){
                    entry.put("end_of_file", new Struct("at"));
                }
                
                buffer.skip(pos);
                int new_pos = (new Long(pos)).intValue();
                entry.put("position", new Int(new_pos));
                inputStreams.put(buffer, entry);
                
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                throw PrologError.system_error(new Struct("An error has occurred in method 'set_stream_position'"));
            }
        }
        return true;
    }
    
    public boolean flush_output_0()throws PrologError{
        initLibrary();
        try {
            outputStream.flush();
        } catch (IOException e) {
            throw PrologError.system_error(new Struct("An error has occurred in method 'flush_output_0'"));
        }
        return true;
    }
    
    public boolean flush_output_1(Term stream_or_alias) throws PrologError{
        initLibrary();
        OutputStream stream = find_output_stream(stream_or_alias);
        try {
            stream.flush();
        } catch (IOException e) {
            throw PrologError.system_error(new Struct("An error has occurred in method 'flush_output_1'"));
        }
        return true;
    }
    
    public boolean get_char_2(Term stream_or_alias, Term arg) throws PrologError{
        initLibrary();
        InputStream stream = find_input_stream(stream_or_alias);
        Character c = null;
        int value = 0;
        
        if (!(arg instanceof Var)){
            throw PrologError.instantiation_error(engine.getEngineManager(), 1);
        }
        
        Hashtable<String,Term> element = (Hashtable<String, Term>) inputStreams.get(stream);
        Struct type =(Struct) element.get("type");
        if(type.getName().equals("binary")){
            throw PrologError.permission_error(engine.getEngineManager(), "input", "binary_stream", stream_or_alias, new Struct("The target stream is associated with a binary stream"));
        }
        
        try{
                
                Number position  = (Number) (element.get("position"));
                Struct eof = (Struct) element.get("end_of_stream");
                if((eof.getName()).equals("past")){
                    Term actionTemp = element.get("eof_action");
                    String action = ((Struct)actionTemp).getName();
                    if(action.equals("error")){
                        throw PrologError.permission_error(engine.getEngineManager(), "input","past_end_of_stream", new Struct("reader"), new Struct("The end of file is reach"));
                    }
                    else if(action.equals("eof_code")){
                        return unify(arg,new Struct("-1"));
                    }
                    else if(action.equals("reset")){
                        element.put("end_of_stream", new Struct("not"));
                        element.put("position", new Int(0));
                        stream.reset();
                    }
                }
                //devo sempre leggere anche se ho fatto reset
                value = stream.read();
                
                if(!Character.isDefined(value)){
                    if(value == -1){
                        element.put("end_of_stream", new Struct("past"));
                    }
                    else{
                        throw PrologError.representation_error(engine.getEngineManager(), 2, "character");
                    }
                }
                Int i = (Int)position;
                int i2 = i.intValue();
                i2++;
                element.put("position",new Int(i2));
                
                if(value != -1){
                    //vado a controllare il prossimo carattere
                    //se ? fine file, end_of_stream diventa "at"
                    Var nextChar = new Var();
                    peek_code_2(stream_or_alias,nextChar);
                    Term nextCharTerm = nextChar.getTerm();
                    Number nextCharValue = (Number)nextCharTerm;
                    if(nextCharValue.intValue() == -1){
                        element.put("end_of_stream", new Struct("at"));
                    }
                }
                
                inputStreams.put(stream, element);
                
                if(value == -1){
                    return unify(arg, Term.createTerm(value+""));
                }
                c = new Character((char)value);
                return unify(arg,new Struct(c.toString()));
        }catch(IOException ioe){
                ioe.printStackTrace();
                throw PrologError.system_error(new Struct("An I/O error has occurred"));
        }
    }
    
    public boolean get_code_1(Term char_code)throws PrologError{
        initLibrary();
        Struct s_or_a = new Struct(inputStream.toString());
        return get_code_2(s_or_a,char_code);
    }
    
    public boolean get_code_2(Term stream_or_alias, Term char_code) throws PrologError{
        initLibrary();
        InputStream stream = find_input_stream(stream_or_alias);
        
        int value = 0;
        
        if (!(char_code instanceof Var)){
            throw PrologError.instantiation_error(engine.getEngineManager(), 1);
        }
        
        Hashtable<String,Term> element = (Hashtable<String, Term>) inputStreams.get(stream);
        Struct type =(Struct) element.get("type");
        if(type.getName().equals("binary")){
            throw PrologError.permission_error(engine.getEngineManager(), "input", "binary_stream", stream_or_alias, new Struct("The target stream is associated with a binary stream"));
        }
        
        try{    
                Number position  = (Number) (element.get("position"));
                Struct eof = (Struct) element.get("end_of_stream");
                if(eof.equals("past")){
                    Term actionTemp = element.get("eof_action");
                    String action = ((Struct)actionTemp).getName();
                    if(action.equals("error")){
                        throw PrologError.permission_error(engine.getEngineManager(), "input","past_end_of_stream", new Struct("reader"), new Struct("The end of file is reach"));
                    }
                    else if(action.equals("eof_code")){
                        return unify(char_code,new Struct("-1"));
                    }
                    else if(action.equals("reset")){
                        element.put("end_of_stream", new Struct("not"));
                        element.put("position", new Int(0));
                        stream.reset();
                    }
                }
                
                value = stream.read();
                    
                if(!Character.isDefined(value)){
                    if(value == -1){
                        element.put("end_of_stream", new Struct("past"));
                    }
                    else{
                        throw PrologError.representation_error(engine.getEngineManager(), 2, "character");
                    }
                }
                Int i = (Int)position;
                int i2 = i.intValue();
                i2++;
                element.put("position",new Int(i2));
                
                if(value != -1){
                    Var nextChar = new Var();
                    peek_code_2(stream_or_alias,nextChar);
                    Term nextCharTerm = nextChar.getTerm();
                    Number nextCharValue = (Number)nextCharTerm;
                    if(nextCharValue.intValue() == -1){
                        element.put("end_of_stream", new Struct("at"));
                    }
                }

                inputStreams.put(stream, element);
                return unify(char_code,new Int(value));
        }catch(IOException ioe){
                ioe.printStackTrace();
                throw PrologError.system_error(new Struct("An I/O error has occurred"));
        }
    }
    
    public boolean peek_char_1(Term in_char)throws PrologError{
        initLibrary();
        Struct s_or_a = new Struct(inputStream.toString());
        if(inputStreamName.equals("stdin")){
            inputStream.mark(5);
            boolean var = get_char_2(s_or_a,in_char);
            try {
                inputStream.reset();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                    e.printStackTrace();
                PrologError.system_error(new Struct("Some error has occurred"));
            }
            return var;
        }
        else{
            return peek_char_2(s_or_a,in_char);
        }
    }
    
    public boolean peek_char_2(Term stream_or_alias, Term in_char)throws PrologError{
        //come la get_char soltanto non cambio la posizione di lettura
        initLibrary();
        InputStream stream = find_input_stream(stream_or_alias);
        Hashtable<String,Term> element = (Hashtable<String, Term>) inputStreams.get(stream);
        String file_name = ((Struct)element.get("file_name")).getName();
        
        FileInputStream stream2=null;
        try {
            stream2 = new FileInputStream(file_name);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            PrologError.system_error(new Struct("File not found."));
        }
        Character c = null;
        int value = 0;
        
        if (!(in_char instanceof Var)){
            throw PrologError.instantiation_error(engine.getEngineManager(), 1);
        }
        Struct type =(Struct) element.get("type");
        if(type.getName().equals("binary")){
            throw PrologError.permission_error(engine.getEngineManager(), "input", "binary_stream", stream_or_alias, new Struct("The target stream is associated with a binary stream"));
        }
        
        try{
                Number position  = (Number) (element.get("position"));
                Struct eof = (Struct) element.get("end_of_stream");
                if(eof.equals("past")){
                    Term actionTemp = element.get("eof_action");
                    String action = ((Struct)actionTemp).getName();
                    if(action.equals("error")){
                        throw PrologError.permission_error(engine.getEngineManager(), "input","past_end_of_stream", new Struct("reader"), new Struct("The end of file is reach"));
                    }
                    else if(action.equals("eof_code")){
                        return unify(in_char,new Struct("-1"));
                    }
                    else if(action.equals("reset")){
                        element.put("end_of_stream", new Struct("not"));
                        element.put("position", new Int(0));
                        stream.reset();
                    }
                }
                else{
                    Int i = (Int)position;
                    long nBytes = i.longValue();
                    stream2.skip(nBytes);
                    value = stream2.read();
                    
                    stream2.close();
                }
                if(!Character.isDefined(value)&& value != -1){     //non devo nemmeno settare a eof la propriet? perch? la posizone
                                                //dello stream deve rimanere inalterata.
                    throw PrologError.representation_error(engine.getEngineManager(), 2, "character");
                }
                inputStreams.put(stream, element);
                
                if(value == -1){
                    return unify(in_char, Term.createTerm(value+""));
                }
                
                c = new Character((char)value);
                return unify(in_char,Term.createTerm(c.toString()));
        }catch(IOException ioe){
                ioe.printStackTrace();
                throw PrologError.system_error(new Struct("An I/O error has occurred"));
        }
    }
    
    public boolean peek_code_1(Term char_code)throws PrologError{
        initLibrary();
        Struct stream = new Struct(inputStream.toString());
        if(inputStreamName.equals("stdin")){
            inputStream.mark(5);
            boolean var = get_code_2(stream,char_code);
            try {
                inputStream.reset();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                PrologError.system_error(new Struct("Some error has occurred"));
            }
            return var;
        }
        else{
            return peek_char_2(stream,char_code);
        }
    }
    
    public boolean peek_code_2(Term stream_or_alias, Term char_code)throws PrologError{
        initLibrary();
        //come la get_char soltanto non cambio la posizione di lettura
        InputStream stream = find_input_stream(stream_or_alias);
        Hashtable<String,Term> element = (Hashtable<String, Term>) inputStreams.get(stream);
        String file_name = ((Struct)element.get("file_name")).getName();
        
        FileInputStream stream2=null;
        try {
            stream2 = new FileInputStream(file_name);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            PrologError.system_error(new Struct("File not found."));
        }
        int value = 0;
        
        if (!(char_code instanceof Var)){
            throw PrologError.instantiation_error(engine.getEngineManager(), 1);
        }
        Struct type =(Struct) element.get("type");
        if(type.getName().equals("binary")){
            throw PrologError.permission_error(engine.getEngineManager(), "input", "binary_stream", stream_or_alias, new Struct("The target stream is associated with a binary stream"));
        }
        
        try{
                Number position  = (Number) (element.get("position"));
                Struct eof = (Struct) element.get("end_of_stream");
                if(eof.equals("past")){
                    Term actionTemp = element.get("eof_action");
                    String action = ((Struct)actionTemp).getName();
                    if(action.equals("error")){
                        throw PrologError.permission_error(engine.getEngineManager(), "input","past_end_of_stream", new Struct("reader"), new Struct("The end of file is reach"));
                    }
                    else if(action.equals("eof_code")){
                        return unify(char_code,new Struct("-1"));
                    }
                    else if(action.equals("reset")){
                        element.put("end_of_stream", new Struct("not"));
                        element.put("position", new Int(0));
                        stream.reset();
                    }
                }
                else{
                    Int i = (Int)position;
                    long nBytes = i.longValue();
                    stream2.skip(nBytes);
                    value = stream2.read();
                    stream2.close();
                }
                if(!Character.isDefined(value) && value != -1){     //non devo nemmeno settare a eof la propriet? perch? la posizone
                                                //dello stream deve rimanere inalterata.
                    throw PrologError.representation_error(engine.getEngineManager(), 2, "character");
                }
                inputStreams.put(stream, element);
                return unify(char_code,new Int(value));
        }catch(IOException ioe){
                ioe.printStackTrace();
                throw PrologError.system_error(new Struct("An I/O error has occurred"));
        }
    }
    
    public boolean put_char_2(Term stream_or_alias, Term in_char)throws PrologError{
        initLibrary();
        OutputStream stream = find_output_stream(stream_or_alias);
        String stream_name = get_output_name(stream);
        
        Hashtable<String,Term> element = (Hashtable<String, Term>) outputStreams.get(stream);
        Struct type =(Struct) element.get("type");
        if(type.getName().equals("binary")){
            throw PrologError.permission_error(engine.getEngineManager(), "input", "binary_stream", stream_or_alias, new Struct("The target stream is associated with a binary stream"));
        }
        
        Struct arg0 = (Struct) in_char.getTerm();
        
        if(arg0.isVar())
            throw PrologError.instantiation_error(engine.getEngineManager(), 2);
        else if (!arg0.isAtom()) {
            throw PrologError.type_error(engine.getEngineManager(), 2, "character", arg0);
        } 
        else {            
            String ch = arg0.getName();
            if(Character.isDefined(ch.charAt(0))){
                throw PrologError.representation_error(engine.getEngineManager(), 2, "character");
            }
            if (ch.length() > 1) {
                throw PrologError.type_error(engine.getEngineManager(), 2, "character", new Struct(ch));
            } else {
                if (stream_name.equals("stdout")) {
                    getEngine().stdOutput(ch);
                } else {
                    try {
                        stream.write((byte) ch.charAt(0));
                    }catch(IOException ioe){
                        ioe.printStackTrace();
                        throw PrologError.system_error(new Struct("An I/O error has occurred"));
                    }
                }
                return true;
            }
        }
    }
    
    public boolean put_code_1(Term char_code)throws PrologError{
        initLibrary();
        Struct stream = new Struct(outputStream.toString());
        return put_code_2(stream,char_code);
    }
    
    public boolean put_code_2(Term stream_or_alias, Term char_code) throws PrologError{
        initLibrary();
        OutputStream stream = find_output_stream(stream_or_alias);
        String stream_name = get_output_name(stream);
        
        Hashtable<String,Term> element = (Hashtable<String, Term>) outputStreams.get(stream);
        Struct type =(Struct) element.get("type");
        if(type.getName().equals("binary")){
            throw PrologError.permission_error(engine.getEngineManager(), "input", "binary_stream", stream_or_alias, new Struct("The target stream is associated with a binary stream"));
        }
        
        Number arg0 = (Number) char_code.getTerm();
        
        if(arg0.isVar()){
            throw PrologError.instantiation_error(engine.getEngineManager(), 2);
        }
        else if (!arg0.isNumber()) {
            throw PrologError.type_error(engine.getEngineManager(), 2, "character", arg0);
        } 
        else {
                if(Character.isDefined(arg0.intValue())){
                    throw PrologError.representation_error(engine.getEngineManager(), 2, "character_code");
                }
                if (stream_name.equals("stdout")) {
                    getEngine().stdOutput(""+arg0.intValue());
                } else {
                    try {
                        stream.write(arg0.intValue());
                    }catch(IOException ioe){
                        ioe.printStackTrace();
                        throw PrologError.system_error(new Struct("An I/O error has occurred"));
                    }
                }
        }
        return true;
    }
    
    public boolean nl_1(Term stream_or_alias)throws PrologError{
        initLibrary();
        OutputStream stream = find_output_stream(stream_or_alias);
        String stream_name = get_output_name(stream);
        if (stream_name.equals("stdout")) {
            getEngine().stdOutput("\n");
        } else {
            try {
                stream.write('\n');
            } catch (IOException e) {
                throw PrologError.permission_error(engine.getEngineManager(),
                        "output", "stream", new Struct(outputStreamName),
                        new Struct(e.getMessage()));
            }
        }
        return true;
    }
    
    public boolean get_byte_1(Term in_byte) throws PrologError{
        //non faccio la stessa struttura della get_char perch? stdin e stdout sono type=text e non posso fare la get_byte su di loro
        //lo stesso vale per tutti gli altri predicati
        initLibrary();
        Struct stream_or_alias = new Struct(inputStream.toString());
        return get_byte_2(stream_or_alias,in_byte);
    }
    
    public boolean get_byte_2(Term stream_or_alias, Term in_byte)throws PrologError{
        initLibrary();
        InputStream stream = find_input_stream(stream_or_alias);
        Byte b = null;
        Hashtable<String,Term> element = (Hashtable<String, Term>) inputStreams.get(stream);
        Struct type =(Struct) element.get("type");
        if(type.getName().equals("text")){
            throw PrologError.permission_error(engine.getEngineManager(), "input", "text_stream", stream_or_alias, new Struct("The target stream is associated with a text stream"));
        }
        
        if (!(in_byte instanceof Var))
            throw PrologError.instantiation_error(engine.getEngineManager(), 1);
        
        try{
            DataInputStream reader = new DataInputStream(stream);
            Number position  = (Number) (element.get("position"));
            Int i = (Int)position;
            int i2 = i.intValue();
            reader.skipBytes(i2-1);
            Struct eof = (Struct) element.get("end_of_stream");
            if(eof.equals("past")){
                Term actionTemp = element.get("eof_action");
                String action = ((Struct)actionTemp).getName();
                if(action.equals("error")){
                    throw PrologError.permission_error(engine.getEngineManager(), "input","past_end_of_stream", new Struct("reader"), new Struct("The end of file is reach"));
                }
                else if(action.equals("eof_code")){
                    return unify(in_byte,new Struct("-1"));
                }
                else if(action.equals("reset")){
                    element.put("end_of_stream", new Struct("not"));
                    element.put("position", new Int(0));
                    reader.reset();
                }
                    
            }
            
            b = reader.readByte();
                        
            i2++; //incremento la posizione dello stream
            element.put("position",new Int(i2));
            
            //if(b != -1){
                Var nextByte = new Var();
                peek_byte_2(stream_or_alias,nextByte);
                Term nextByteTerm = nextByte.getTerm();
                Number nextByteValue = (Number)nextByteTerm;
                if(nextByteValue.intValue() == -1){
                    element.put("end_of_stream", new Struct("at"));
                }
            //}
            
            inputStreams.put(stream, element); 
            return unify(in_byte,Term.createTerm(b.toString()));
        }
        catch(IOException ioe){
            element.put("end_of_stream", new Struct("past"));
            return unify(in_byte, Term.createTerm("-1"));
        }
    }
    
    public boolean peek_byte_1(Term in_byte)throws PrologError{
        initLibrary();
        Struct stream_or_alias = new Struct(inputStream.toString());
        return peek_char_2(stream_or_alias,in_byte);
    }
    
    public boolean peek_byte_2(Term stream_or_alias, Term in_byte)throws PrologError{
        initLibrary();
        InputStream stream = find_input_stream(stream_or_alias);
        Byte b = null;
        Hashtable<String,Term> element = (Hashtable<String, Term>) inputStreams.get(stream);
        Struct type =(Struct) element.get("type");
        if(type.getName().equals("text")){
            throw PrologError.permission_error(engine.getEngineManager(), "input", "text_stream", stream_or_alias, new Struct("The target stream is associated with a text stream"));
        }
        
        if (!(in_byte instanceof Var))
            throw PrologError.instantiation_error(engine.getEngineManager(), 1);
        
        try{
            DataInputStream reader = new DataInputStream(stream);
            Number position  = (Number) (element.get("position"));
            Int i = (Int)position;
            int i2 = i.intValue();
            reader.skipBytes(i2-2);
            Struct eof = (Struct) element.get("end_of_stream");
            if(eof.equals("past")){
                Term actionTemp = element.get("eof_action");
                String action = ((Struct)actionTemp).getName();
                if(action.equals("error")){
                    throw PrologError.permission_error(engine.getEngineManager(), "input","past_end_of_stream", new Struct("reader"), new Struct("The end of file is reach"));
                }
                else if(action.equals("eof_code")){
                    return unify(in_byte,new Struct("-1"));
                }
                else if(action.equals("reset")){
                    element.put("end_of_stream", new Struct("not"));
                    element.put("position", new Int(0));
                    reader.reset();
                }
                    
            }
            else{
                b = reader.readByte();
            } 
                        
            inputStreams.put(stream, element); 
            return unify(in_byte,Term.createTerm(b.toString()));
        }
        catch(IOException e){
            element.put("end_of_stream", new Struct("past"));
            return unify(in_byte, Term.createTerm("-1"));
        }
    }
    
    public boolean put_byte_1(Term out_byte)throws PrologError{
        //richiamo il metodo di IOLibrary che lavora sullo stream corrente
        //la posso utilizzare cos? com'? in quanto presenta gi? tutti i controlli sul parametro arg richiesti
        initLibrary();
        out_byte = out_byte.getTerm();
        return IOLib.put_1(out_byte);
    }
    
    public boolean put_byte_2(Term stream_or_alias, Term out_byte)throws PrologError{
        initLibrary();
        OutputStream stream = find_output_stream(stream_or_alias);
        out_byte = out_byte.getTerm();
        Number b = (Number)out_byte.getTerm();
        
        Hashtable<String,Term> element = (Hashtable<String, Term>) outputStreams.get(stream);
        Struct type =(Struct) element.get("type");
        if(type.getName().equals("text")){
            throw PrologError.permission_error(engine.getEngineManager(), "output", "text_stream", stream_or_alias, new Struct("The target stream is associated with a text stream"));
        }
        
        if (out_byte instanceof Var)
            throw PrologError.instantiation_error(engine.getEngineManager(), 1);
        try{
            DataOutputStream writer = new DataOutputStream(stream);
            Number position  = (Number) (element.get("position"));
            Int i = (Int)position;
            int i2 = i.intValue();
            
            writer.writeByte(b.intValue());
            
            i2++;
            element.put("position",new Int(i2));
            outputStreams.put(stream, element); 
            return unify(out_byte,Term.createTerm(b.toString()));
        }
        catch(IOException ioe){
                ioe.printStackTrace();
                throw PrologError.system_error(new Struct("An I/O error has occurred"));
        }
    }
    
    public boolean read_term_2(Term in_term, Term options)throws PrologError{
        initLibrary();
        Struct stream_or_alias = new Struct(inputStream.toString());
        return read_term_3(stream_or_alias,in_term,options);
    }
    
    public boolean read_term_3(Term stream_or_alias, Term in_term, Term options)throws PrologError{
        initLibrary();
        InputStream stream = find_input_stream(stream_or_alias);
                
        if(options instanceof Var){
            throw PrologError.instantiation_error(engine.getEngineManager(), 3); 
        }
        
        Hashtable<String,Term> element = (Hashtable<String, Term>) inputStreams.get(stream);
        Struct type =(Struct) element.get("type");
        Struct eof = (Struct) element.get("end_of_stream");
        Struct action = (Struct) element.get("eof_action");
        Number position = (Number) element.get("position");
        if(type.getName().equals("binary")){
            throw PrologError.permission_error(engine.getEngineManager(), "input", "binary_stream", stream_or_alias, new Struct("The target stream is associated with a binary stream"));
        }
        if((eof.getName()).equals("past") && (action.getName()).equals("error")){
            throw PrologError.permission_error(engine.getEngineManager(), "past_end_of_stream", "stream", stream_or_alias, new Struct("Target stream has position at past_end_of_stream"));
        }
        
        Struct variables = null;
        Struct variable_names = null;
        Struct singletons = null;
        
        boolean variables_bool=false;
        boolean variable_names_bool=false;
        boolean singletons_bool=false;
        
        Struct readOptions = (Struct)options; 
        if(readOptions.isList()){
            if(!readOptions.isEmptyList()){
                Iterator<? extends Term> i = readOptions.listIterator();
                while(i.hasNext()){
                    Struct option = null;
                    Object obj = i.next();
                    if(obj instanceof Var){
                        throw PrologError.instantiation_error(engine.getEngineManager(), 3);
                    }                    
                    option = (Struct)obj;
                    if(option.getName().equals("variables")){
                        variables_bool=true;
                    }
                    else if(option.getName().equals("variable_name")){
                        variable_names_bool=true;
                    }
                    else if(option.getName().equals("singletons")){
                        singletons_bool=true;
                    }
                    else{
                        PrologError.domain_error(engine.getEngineManager(), 3, "read_option", option);
                    }
                }
            }
        }
        else{
            throw PrologError.type_error(engine.getEngineManager(), 3, "list", options);
        }
        
        try {
            int ch = 0;
            
            boolean open_apices = false;
            boolean open_apices2 = false;
            
            in_term = in_term.getTerm();
            String st = "";
            do {
                ch = stream.read();
                
                if (ch == -1) {
                    break;
                }
                boolean can_add = true;
                
                if (ch=='\''){
                    if (!open_apices){
                        open_apices = true;
                    } else {
                        open_apices = false;
                    }
                } else if (ch=='\"'){
                    if (!open_apices2){
                        open_apices2 = true;
                    } else {
                        open_apices2 = false;
                    }
                } else {
                    if (ch=='.'){
                        if (!open_apices && !open_apices2){
                            break;
                        }
                    }
                }
                if (can_add){
                    st += new Character(((char) ch)).toString();
                }
            } while (true);
            
            Int p = (Int)position;
            int p2 = p.intValue();
            p2 += (st.getBytes()).length;
            
            if(ch == -1){
                st = "-1";
                element.put("end_of_stream",new Struct("past"));
                element.put("position",new Int(p2));
                inputStreams.put(stream, element);
                return unify(in_term,Term.createTerm(st));
            }
                                    
            if(variables_bool == false && variable_names_bool == false && singletons_bool == false){
                return unify(in_term, getEngine().toTerm(st));
            }
            Var input_term = new Var();
            unify(input_term,Term.createTerm(st));
                    
            //opzione variables + variables_name
            List<Term> variables_list = new ArrayList<Term>();
            analize_term(variables_list,input_term);
            
            Hashtable<Term,String> associations_table = new Hashtable<Term,String>(variables_list.size());

            //la hashtable sottostante la costruisco per avere le associazioni 
            //con le variabili '_' Queste infatti non andrebbero inserite all'interno della
            //read_option variable_name, ma vanno sostituite comunque da variabili nel termine letto.
            Hashtable<Term,String> association_for_replace = new Hashtable<Term,String>(variables_list.size());
            
            LinkedHashSet<Term> set = new LinkedHashSet<Term>(variables_list);
            List<Var> vars = new ArrayList<Var>();

            if(variables_bool == true){
                int num = 0;
                for(Term t:set){
                    num++;
                    if(variable_names_bool == true){
                        association_for_replace.put(t, "X"+num);
                        if(!((t.toString()).startsWith("_"))){
                            associations_table.put(t, "X"+num);
                        }
                    }
                    vars.add(new Var("X"+num));
                }
            }
                        
            //opzione singletons
            List<Term> singl = new ArrayList<Term>();
            int flag = 0;
            if(singletons_bool == true){
                List<Term> temporanyList = new ArrayList<Term>(variables_list);
                for(Term t:variables_list){
                    temporanyList.remove(t);
                    flag = 0;
                    for(Term temp:temporanyList){
                        if(temp.equals(t)){
                            flag = 1;
                        }
                    }
                    if(flag == 0){
                        if(!((t.toString()).startsWith("_"))){
                            singl.add(t);
                        }
                    }
                    temporanyList.add(t);
                }
            }
                        
            //unisco le liste con i relativi termini
            Iterator<? extends Term> i = readOptions.listIterator();
            Struct option = null;
            while(i.hasNext()){
                Object obj = i.next();
                option = (Struct)obj;
                if(option.getName().equals("variables")){
                    variables = new Struct();
                    variables = (Struct) Term.createTerm(vars.toString());
                    unify(option.getArg(0),variables);
                }
                else if(option.getName().equals("variable_name")){
                    variable_names = new Struct();
                    variable_names = (Struct)Term.createTerm(associations_table.toString());
                    unify(option.getArg(0),variable_names);
                }
                else if(option.getName().equals("singletons")){
                    singletons = new Struct();
                    singletons = (Struct)Term.createTerm(singl.toString());
                    unify(option.getArg(0),singletons);
                }
            }
            
            String string_term = input_term.toString();
            
            for(Map.Entry<Term,String> entry:association_for_replace.entrySet()){
                String regex = entry.getKey().toString();
                String replacement = entry.getValue();
                string_term = string_term.replaceAll(regex, replacement);
            }
            
            //vado a modificare la posizione di lettura
            element.put("position",new Int(p2));
            inputStreams.put(stream, element); 
            return unify(in_term, getEngine().toTerm(string_term));
        } catch (Exception ex){
            return false;
        }
    }
    
    private void analize_term(List<Term> variables,Term t){
        if(!t.isCompound()){
            variables.add(t);
        }
        else{
            Struct term_struct = (Struct)t.getTerm();
            for(int i = 0;i<term_struct.getArity();i++){
                analize_term(variables,term_struct.getArg(i));
            }
        }
    }
    
    public boolean read_2(Term stream_or_alias, Term in_term)throws PrologError{
        initLibrary();
        Struct options = new Struct(".",new Struct());
        return read_term_3(stream_or_alias,in_term,options);
    }
    
    public boolean write_term_2(Term out_term, Term options)throws PrologError{
        initLibrary();
        Struct stream_or_alias = new Struct(outputStream.toString());
        return write_term_3(stream_or_alias,out_term,options);
    }
    
    public boolean write_term_3(Term stream_or_alias, Term out_term, Term optionsTerm)throws PrologError{
        initLibrary();
        out_term = out_term.getTerm();
                
        OutputStream output = find_output_stream(stream_or_alias);
        //String outputName = get_output_name(output);
        Struct writeOptionsList = (Struct)optionsTerm.getTerm(); 
        
        boolean quoted = false;
        boolean ignore_ops = false;
        boolean numbervars = false;
        Struct writeOption = null;
        
        Hashtable<String,Term> element = (Hashtable<String, Term>) outputStreams.get(output);
        Struct type =(Struct) element.get("type");
        if(type.getName().equals("binary")){
            throw PrologError.permission_error(engine.getEngineManager(), "output", "binary_stream", stream_or_alias, new Struct("The target stream is associated with a binary stream"));
        }
        
        if(writeOptionsList.isList()){
            if(!writeOptionsList.isEmptyList()){
                Iterator<? extends Term> i = writeOptionsList.listIterator();
                while(i.hasNext()){
                    //siccome queste opzioni sono true o false analizzo direttamente il loro valore
                    //e restituisco il loro valore all'interno dell'opzione corrispondente
                    Object obj = i.next();
                    if(obj instanceof Var){
                        throw PrologError.instantiation_error(engine.getEngineManager(), 3);
                    }
                    writeOption = (Struct)obj;
                    if(writeOption.getName().equals("quoted")){
                        quoted = ((Struct) writeOption.getArg(0)).getName().equals("true")? true:false;
                    }
                    else if(writeOption.getName().equals("ignore_ops")){
                        ignore_ops =((Struct) writeOption.getArg(0)).getName().equals("true")? true:false;
                    }
                    else if(writeOption.getName().equals("numbervars")){
                        numbervars = ((Struct) writeOption.getArg(0)).getName().equals("true")? true:false;
                    }
                    else{
                        throw PrologError.domain_error(engine.getEngineManager(), 3, "write_options", writeOptionsList.getTerm());
                    }
                }
            }
        }
        else{
            PrologError.type_error(engine.getEngineManager(), 3, "list", writeOptionsList);
        }
        try{
            if(!out_term.isCompound() && !(out_term instanceof Var)){
                if(quoted == true){
                    output.write((alice.util.Tools.removeApices(out_term.toString())).getBytes());
                }
                else{
                    output.write((out_term.toString()).getBytes());
                }
                return true;
            }
            if(out_term instanceof Var){
                if(quoted == true){
                    output.write((alice.util.Tools.removeApices(out_term.toString())+" ").getBytes());
                    return true;
                }
                else{
                    output.write((out_term.toString()+" ").getBytes());
                    return true;
                }
            }
            Struct term = (Struct)out_term;
            String result = "";
            Hashtable<String,Boolean> options = new Hashtable<String,Boolean>(3);
            options.put("numbervars", numbervars);
            options.put("ignore_ops", ignore_ops);
            options.put("quoted", quoted);
            
            result = create_string(options,term);
            
            output.write((result+" ").getBytes());
        }
        catch(IOException ioe){
            PrologError.system_error(new Struct("Write error has occurred"));
        }
        return true;
    }
    
    private String create_string(Hashtable<String,Boolean> options, Struct term){
        
        boolean numbervars = options.get("numbervars");
        boolean quoted = options.get("quoted");
        boolean ignore_ops = options.get("ignore_ops");
        
        String result = "";
        String list = "";
        if(term.isList()){
            list = print_list(term,options);
            if(ignore_ops==false)
                return "[" + list +"]";
            else
                return list;
        }
                
        List<Operator> operatorList = engine.getCurrentOperatorList();
        String operator = "";
        int flagOp = 0;
        for(Operator op : operatorList){
            if(op.name.equals(term.getName())){
                operator = op.name;
                flagOp = 1;
                break;
            }
        }
        
        if(flagOp == 0){
            result+=term.getName()+"(";
        }
        
        int arity = term.getArity();
        for(int i = 0; i<arity; i++){
            if(i > 0 && flagOp==0)
                result += ",";
            Term arg = term.getArg(i);
            if(arg instanceof Number){
                if(term.getName().contains("$VAR")){
                //sono nel tipo $VAR
                    if(numbervars == true){
                        Int argNumber = (Int)term.getArg(i);
                        int res = argNumber.intValue() % 26;
                        int div = argNumber.intValue()/26;
                        Character ch = 'A';
                        int num = (ch+res);
                        result = new String(Character.toChars(num));
                        if(div != 0){
                            result += div;
                        }
                    }
                    else{
                        if(quoted == true){
                            return term.toString();
                        }
                        else{
                            result += alice.util.Tools.removeApices(arg.toString()); 
                        }
                    }
                    continue;
                }
                else{
                //? un numero da solo o un operando
                    if(ignore_ops == false){
                        result += arg.toString();
                        if(i%2 == 0 && operator != ""){
                            result +=" "+operator+" ";
                        }
                        continue;
                    }
                    else{
                        result = term.toString();
                        return result;
                    }
                }
            }
            else if(arg instanceof Var){
            // stampo il toString della variabile
                if(ignore_ops == false){
                    result+= arg.toString();
                    if(i%2 == 0 && operator != ""){
                        result +=" "+operator+" ";
                    }
                    continue;
                }
                else{
                    result+= arg.toString();
                }
                continue;
            }
            else if(arg.isCompound()){
                if(ignore_ops == false){
                    result+= create_string(options,(Struct)arg);
                    if(i%2 == 0 && operator != ""){
                        result +=" "+operator+" ";
                    }
                    continue;
                }
                else{
                    result+= create_string(options,(Struct)arg);
                }
                
            }
            else{
                if(quoted == true){
                    if(ignore_ops == false){
                        result += arg.toString();
                        if(i%2 == 0 && operator != ""){
                            result +=" "+operator+" ";
                        }
                        continue;
                    }
                    else{
                        result += arg.toString();
                    }
                }
                    
                else{
                    if(ignore_ops == false){
                        result += alice.util.Tools.removeApices(arg.toString());
                        if(i%2 == 0 && operator != ""){
                            result +=" "+operator+" ";
                        }
                        continue;
                    }
                    else{
                        result += alice.util.Tools.removeApices(arg.toString());
                    }
                }
            }
        }
        
        if(flagOp == 0 && result.contains("(")){
            result += ")";
        }
        return result;        
    }
    

    private String print_list(Struct term, Hashtable<String, Boolean> options) {
        
        //boolean numbervars = options.get("numbervars");
        //boolean quoted = options.get("quoted");
        boolean ignore_ops = options.get("ignore_ops");
        
        String result = "";
        
        if(ignore_ops == true){
            result="'"+term.getName()+"'"+" (";
            for(int i = 0; i<term.getArity(); i++){
                if(i > 0){
                    result+=",";
                }
                if(term.getArg(i).isList() && !(term.getArg(i).isEmptyList())){
                    result += print_list((Struct)term.getArg(i),options);
                }
                else{
                    result += term.getArg(i);
                }
            }
            return result + ")";
        }
        else{
            for(int i = 0; i<term.getArity(); i++){
                if(i > 0 && !(term.getArg(i).isEmptyList())){
                    result+=",";
                }
                if((term.getArg(i)).isCompound() && !(term.getArg(i).isList())){
                    result += create_string(options,(Struct)term.getArg(i));
                }
                else{
                    //costruito cos? per un problema di rappresentazione delle []
                    if((term.getArg(i).isList()) && !(term.getArg(i).isEmptyList()))
                        result+=print_list((Struct)term.getArg(i),options);
                    else{
                        if(!(term.getArg(i).isEmptyList()))
                            result+= term.getArg(i).toString();
                    }
                        
                }
            }
            return  result ;
        }
    }

    public boolean write_2(Term stream_or_alias, Term out_term) throws PrologError{
        initLibrary();
        Struct options = new Struct(".",new Struct("quoted",new Struct("false")),
                new Struct(".",new Struct("ignore_ops",new Struct("false")),
                new Struct(".",new Struct("numbervars",new Struct("true")),new Struct())));
        return write_term_3(stream_or_alias,out_term,options);
    }
    
    public boolean write_1(Term out_term) throws PrologError{
        if(write_flag == 0){
            return write_iso_1(out_term);
        }
        else{
            return IOLib.write_base_1(out_term);
        }
    }
    
    public boolean write_iso_1(Term out_term) throws PrologError{
        initLibrary();
        Struct stream_or_alias = new Struct(outputStream.toString());
        Struct options = new Struct(".",new Struct("quoted",new Struct("false")),
                new Struct(".",new Struct("ignore_ops",new Struct("false")),
                new Struct(".",new Struct("numbervars",new Struct("true")),new Struct())));
        return write_term_3(stream_or_alias,out_term,options);
    }
    
    public boolean writeq_1(Term out_term)throws PrologError{
        initLibrary();
        Struct stream_or_alias = new Struct(outputStream.toString());
        Struct options = new Struct(".",new Struct("quoted",new Struct("true")),
                    new Struct(".",new Struct("ignore_ops",new Struct("false")),
                    new Struct(".",new Struct("numbervars",new Struct("true")),new Struct())));
        return write_term_3(stream_or_alias,out_term,options);
    }
    
    public boolean writeq_2(Term stream_or_alias, Term out_term) throws PrologError{
        initLibrary();
        Struct options = new Struct(".",new Struct("quoted",new Struct("true")),
                new Struct(".",new Struct("ignore_ops",new Struct("false")),
                new Struct(".",new Struct("numbervars",new Struct("true")),new Struct())));
        return write_term_3(stream_or_alias,out_term,options);
    }
    
    public boolean write_canonical_1(Term out_term) throws PrologError{
        initLibrary();
        Struct stream_or_alias = new Struct(outputStream.toString());
        Struct options = new Struct(".",new Struct("quoted",new Struct("true")),
                new Struct(".",new Struct("ignore_ops",new Struct("true")),
                new Struct(".",new Struct("numbervars",new Struct("false")),new Struct())));
        return write_term_3(stream_or_alias,out_term,options);
    }
    
    public boolean write_canonical_2(Term stream_or_alias, Term out_term)throws PrologError{
        initLibrary();
        Struct options = new Struct(".",new Struct("quoted",new Struct("true")),
                new Struct(".",new Struct("ignore_ops",new Struct("true")),
                new Struct(".",new Struct("numbervars",new Struct("false")),new Struct())));
        return write_term_3(stream_or_alias,out_term,options);
    }
    
    //per forzare il caricamento dell'I/O library 
    private void initLibrary(){
        if(flag == 1)
            return;
        
        Library library=null;
        
        library = engine.getLibrary("alice.tuprolog.lib.IOLibrary");
        if(library == null){
            try {
                library = engine.loadLibrary("alice.tuprolog.lib.IOLibrary");
            } catch (InvalidLibraryException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                PrologError.system_error(new Struct("IOLibrary does not exists."));
            }
        }
        
        IOLib = (IOLibrary)library;
        inputStream = IOLib.inputStream;
        outputStream = IOLib.outputStream;
        inputStreamName = IOLib.inputStreamName;
        outputStreamName = IOLib.outputStreamName;
        flag = 1;
        
        //inserisco anche stdin e stdout all'interno dell'hashtable con le sue propriet?
        Hashtable<String, Term> propertyInput = new Hashtable<String, Term>(10);
        inizialize_properties(propertyInput);
        propertyInput.put("input", new Struct("true"));
        propertyInput.put("mode", new Struct("read"));
        propertyInput.put("alias", new Struct("user_input"));
        //per essere coerente con la rappresentazione in IOLibrary dove stdin ? inputStreamName
        propertyInput.put("file_name", new Struct("stdin"));
        propertyInput.put("eof_action", new Struct("reset"));
        propertyInput.put("type", new Struct("text"));
        Hashtable<String, Term> propertyOutput = new Hashtable<String, Term>(10);
        inizialize_properties(propertyOutput);
        propertyOutput.put("output", new Struct("true"));
        propertyOutput.put("mode", new Struct("append"));
        propertyOutput.put("alias", new Struct("user_output"));
        propertyOutput.put("eof_action", new Struct("reset"));
        propertyOutput.put("file_name", new Struct("stdout"));
        propertyOutput.put("type", new Struct("text"));
        inputStreams.put(inputStream,propertyInput);
        outputStreams.put(outputStream,propertyOutput);    
        
        return;
    }
    
    //serve per inizializzare la hashmap delle propriet?
    private boolean inizialize_properties(Hashtable<String,Term> map){
        Struct s = new Struct();
        map.put("file_name", s);
        map.put("mode", s);
        map.put("input", new Struct("false"));
        map.put("output", new Struct("false"));
        map.put("alias", s);
        map.put("position", new Int(0));
        map.put("end_of_stream", new Struct("not"));
        map.put("eof_action", new Struct("error"));
        map.put("reposition", new Struct("false"));
        map.put("type", s);
        return true;
    }
    
    
    //funzioni ausiliarie per effettuare controlli sugli stream in ingresso e per
    //restituire lo stream aperto che sto cercando
    
    private InputStream find_input_stream(Term stream_or_alias) throws PrologError{
        int flag = 0;
        InputStream result = null;
        stream_or_alias = stream_or_alias.getTerm();
        Struct stream = (Struct)stream_or_alias;
        
        if (stream_or_alias instanceof Var) { //controlla che non sia una variabile
            throw PrologError.instantiation_error(engine.getEngineManager(), 1);
        }
        
        //se mi viene passato un alias lo cerco subito e restituisco subito lo lo stream
        for(Map.Entry<InputStream, Hashtable<String, Term>> currentElement : inputStreams.entrySet()){
            for(Map.Entry<String, Term> currentElement2 : currentElement.getValue().entrySet()){
                if(currentElement2.getKey().equals("alias")){ 
                        Struct alias = (Struct)currentElement2.getValue();
                        for(int k = 0; k< alias.getArity();k++){
                            if((alias.getArg(k)).equals(stream_or_alias)){
                                return currentElement.getKey();
                            }
                        }
                }
            }
        }
        
        //altrimenti vado a fare tutti i controlli sul nome dello stream che mi ? stato passato
        if((stream.getName()).contains("Stream")){
            if((stream.getName()).contains("Output")){
                throw PrologError.permission_error(engine.getEngineManager(), "output", "stream", stream_or_alias, new Struct("S_or_a is an output stream"));
            }
            if((stream.getName()).contains("Input")){
                for(Map.Entry<InputStream, Hashtable<String, Term>> currentElement : inputStreams.entrySet()){
                    if((currentElement.getKey().toString()).equals(stream.getName())){
                        result = currentElement.getKey();
                        flag = 1;
                        break;
                    }
                }
                if(flag == 0){
                    //se lo stream non si trova all'interno della hashtable, significa che non ? mai stato aperto
                    throw PrologError.existence_error(engine.getEngineManager(), 1, "stream", stream_or_alias, new Struct("The input stream must be open"));
                }
            }
        }
        else{
            throw PrologError.domain_error(engine.getEngineManager(), 1, "stream_or_alias", stream_or_alias);
        }
        return result;
    }
    
    private OutputStream find_output_stream(Term stream_or_alias)throws PrologError{    
        int flag = 0;
        OutputStream result = null;
        stream_or_alias = stream_or_alias.getTerm();
        Struct stream = (Struct)stream_or_alias;
        
        if (stream_or_alias instanceof Var) {
            throw PrologError.instantiation_error(engine.getEngineManager(), 1);
        }
        
        for(Map.Entry<OutputStream, Hashtable<String, Term>> currentElement : outputStreams.entrySet()){
            for(Map.Entry<String, Term> currentElement2 : currentElement.getValue().entrySet()){
                if(currentElement2.getKey().equals("alias")){ 
                    Struct alias = (Struct)currentElement2.getValue();
                    for(int k = 0; k< alias.getArity();k++){
                        if((alias.getArg(k)).equals(stream_or_alias)){
                            return currentElement.getKey();
                        }
                    }
                }
            }
        }
        
        if((stream.getName()).contains("Stream")){
            if((stream.getName()).contains("Input")){
                throw PrologError.permission_error(engine.getEngineManager(), "output", "stream", stream_or_alias, new Struct("S_or_a is an input stream"));
            }
            //devo controllare che contenga anche Print perch? stdout ? di tipo PrintStream
            if((stream.getName()).contains("Output") ||(stream.getName()).contains("Print")){
                for(Map.Entry<OutputStream, Hashtable<String, Term>> currentElement : outputStreams.entrySet()){
                    if((currentElement.getKey().toString()).equals(stream.getName())){
                        result = currentElement.getKey();
                        flag = 1;
                        break;
                    }
                }
                if(flag == 0){
                    throw PrologError.existence_error(engine.getEngineManager(), 1, "stream", stream_or_alias, new Struct("The output stream must be open"));
                }
            }
        }
        else{
            throw PrologError.domain_error(engine.getEngineManager(), 1, "stream_or_alias", stream_or_alias);
        }
        return result;
    }
    
    
    //come nome dello stream viene utilizzata la propriet? file_name
    private String get_output_name(OutputStream output){
        Term file_name = null;
        //per reperire quello stream specifico devo per forza confrontare il nome degli stream ogni volta
        //perch? la get non fuziona in quanto non ho lo stesso oggetto che ? all'interno dell'hashtable
        //? lo stesso ma lo passa l'utente dall'esterno quindi devo trovarlo scorrendo ogni membro dell'Hashtable
        for(Map.Entry<OutputStream, Hashtable<String, Term>> element : outputStreams.entrySet()){
            if((element.getKey().toString()).equals(output.toString())){
                Hashtable<String,Term>properties = element.getValue();
                file_name = properties.get("file_name");
                break;
            }
        }
        Struct returnElement = (Struct)file_name;
        return returnElement.getName();
    }
    
    private String get_input_name(InputStream input){
        Term file_name = null;
        for(Map.Entry<InputStream, Hashtable<String, Term>> element : inputStreams.entrySet()){
            if((element.getKey().toString()).equals(input.toString())){
                input = element.getKey();
                Hashtable<String,Term>properties = element.getValue();
                file_name = properties.get("file_name");
                break;
            }
        }
        Struct returnElement = (Struct)file_name;
        return returnElement.getName();
    }
    
    public boolean set_write_flag_1(Term number) throws PrologError{
        Number n = (Number)number;
        if(n.intValue() == 1){
            write_flag = 1;
            return true;
        }
        else if(n.intValue() == 0){
            write_flag = 0;
            return true;
        }
        else{
            throw PrologError.domain_error(engine.getEngineManager(), 1, "0-1", number);
        }
    }
}
