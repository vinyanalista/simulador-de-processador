operando(X) :- X >= -128, X < 256.

noOprdCode(-256).

endereco_de_instrucao(X) :- X >= 0, X < 128.
endereco_de_dado(X) :- X >= 128, X < 256.

dado(X) :- X >= -128, X < 128.

opcode('NOP').
opcode('STA').
opcode('LDA').
opcode('ADD').
opcode('SUB').
opcode('OR').
opcode('AND').
opcode('NOT').
opcode('JMP').
opcode('JN').
opcode('JZ').
opcode('JNZ').
opcode('IN').
opcode('OUT').
opcode('LDI').
opcode('HLT').

pede_endereco_de_dado('STA').
pede_endereco_de_dado('LDA').
pede_endereco_de_dado('ADD').
pede_endereco_de_dado('SUB').
pede_endereco_de_dado('OR').
pede_endereco_de_dado('AND').
pede_endereco_de_dado('OUT').
pede_endereco_de_dado('IN').

pede_endereco_de_instrucao('JMP').
pede_endereco_de_instrucao('JN').
pede_endereco_de_instrucao('JZ').
pede_endereco_de_instrucao('JNZ').

nao_pede_valor('NOP').
nao_pede_valor('NOT').
nao_pede_valor('HLT').

pede_dado('LDI').

comando_valido(X, Y) :- 
    nao_pede_valor(X), !,
    noOprdCode(Y).
comando_valido(X, Y) :- 
    pede_endereco_de_dado(X), !, 
    endereco_de_dado(Y).
comando_valido(X, Y) :- 
    pede_endereco_de_instrucao(X), !, 
    endereco_de_instrucao(Y).
comando_valido(X, Y) :- 
    pede_dado(X), !, 
    dado(Y).

/*
 * Checa se o comando passado é inválido e aponta o erro do comando.
 */
comando_invalido(X,_,'INVALID_OPCODE') :-
    \+ opcode(X).
comando_invalido(X,Y,'INVALID_DATA') :- 
    pede_dado(X),!, 
    \+ dado(Y).
comando_invalido(X,Y,'INVALID_DATA_ADDRESS') :- 
    pede_endereco_de_dado(X), !, 
    \+ endereco_de_dado(Y).
comando_invalido(X,Y,'INVALID_INSTRUCTION_ADDRESS') :- 
    pede_endereco_de_instrucao(X), !, 
    \+ endereco_de_instrucao(Y).
comando_invalido(X,Y,'OPERATION_DOES_NOT_REQUIRE_VALUE') :- 
    nao_pede_valor(X), !, 
    \+ noOprdCode(Y).

/*
 * Quebra um bloco de texto pelos caracteres de nova linha (\n) e checa se todas 
 * as linhas são válidas. As isntruções e os erros são armazenados em Es e Is, 
 * respectivamente. 
 */
valida_bloco(X,Is,Es) :- 
    lines(X,ProgS), 
    map_valida(ProgS,Fs,Is,1), 
    parse_errors(Fs,Es).

parse_errors([],[]).
parse_errors([[L | F] | Fs],[[L | E] | Es]) :- 
    extrai_comando(F,C,V), 
    comando_invalido(C,V,E),
    parse_errors(Fs,Es).

map_valida([],[],[],_).
map_valida([X| Xs],Fs,Is,C) :- 
    Co is C + 1,
    (vazia(X); 
    comentario(X)), !,
    map_valida(Xs,Fs,Is,Co).
map_valida([X | Xs],Fs,[[Oprd | Opcd] | Is],C) :- 
    Co is C + 1,
    valida_linha(X,Opcd,Oprd), !,
    map_valida(Xs,Fs,Is,Co).
map_valida([X | Xs],[[C | X] | Fs],Is,C) :- 
    Co is C + 1,
    map_valida(Xs,Fs,Is,Co).

/*
 * Código auto-explicativo, mas se você precisa de instruções para usar palitos de 
 * dente (Até mais e obrigado pelos peixes), vamos lá.
 * Checa se uma linha é válida, podendo sê-la apenas em 3 casos: 
 *   1) Há um comando linha e ele é válido;
 *   2) A linha é um comentário;
 *   3) A linha está em branco.
 */
valida_linha(X,Opcd,Oprd) :- 
    (extrai_comando(X,Opcd,Oprd), 
    comando_valido(Opcd,Oprd));
    comentario(X);
    (trim_init(X,Y),
    vazia(Y)).
 
vazia('').

/*
 *  Recebe uma String (Inpt) e extrai o opcode (Opcd) e operando (Oprd).
 *  No caso de ser um comando sem operando, acontece que Oprd = -256.
 */
extrai_comando(Inpt,Opcd,Oprd) :- 
    extrai_comentario(Inpt,_,RestRaw),
    trim_init(RestRaw,Rest),
    break_at(Rest,' ',L),
    first_and_last(L,Opcd,OprdStr), !,
    catch(num_atom(Oprd,OprdStr),_,noOprdCode(Oprd)).

/* 
 * Checa se o primeiro caractere válido (que não é espaço) 
 * da String "X" é igual a (;).
 */
comentario(X) :- 
    trim_init(X,Y), 
    atom_chars(Y,[';' | _]).

/*
 * Extrai o comentário Assembly da String Inpt, se houver, e armazena uma 
 * lista contendo-o em Cmnt. O que restar da String é armazenado em Rest.
 */
extrai_comentario(Inpt,Cmnt,Rest) :- break_at(Inpt,';',[Rest | [Cmnt]]).

/*
 * Quebra uma String em todas as ocorrências de um separador "Sep" qualquer.
 * Usado para os predicados "lines" ('\n') e "extrai_comando" (' ').
 */
break_at('',_,[]) :- !.
break_at(Str,Sep,[X | Xs]) :-
    atom_chars(Str,ChrS),
    break_at_(Sep,ChrS,Xd,ChrS2), !,
    atom_chars(X,Xd),
    atom_chars(Str2,ChrS2),
    break_at(Str2,Sep,Xs).

%Auxiliar, que procura a primeira ocorrência do separador e quebra a String.
break_at_(_,[],[],[]).
break_at_(Sep,[Sep | Cs], [], Cs).
break_at_(Sep,[C | Cs], [C | Os], Rs) :- break_at_(Sep,Cs,Os,Rs).

/*
 *  Divide uma String em itens de uma lista, definindo os 
 *  caracteres de nova linha ('\n', '\r') como pontos de divisão.
 */
lines(In,Xs) :- break_at(In,'\n',Xs).

first_and_last(Xs,F,L) :- 
    first(Xs,F), 
    last(Xs,L).

first([X|_],X).                                  

last([X],X).
last([_ | Xs],Y) :- last(Xs,Y).

% Remove espaços no início da String.
trim_init('','').
trim_init(X,X) :- \+ atom_chars(X,[' ' | _]), !.
trim_init(X,Y) :- 
    atom_chars(X,[' ' | Cs]),  
    atom_chars(Z,Cs), 
    trim_init(Z,Y).

