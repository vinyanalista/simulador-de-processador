package br.com.vinyanalista.simulador.examples;

public enum Example {
	ADD {
		@Override
		public String toString() {
			return "Soma";
		}
	},
	OVERFLOW {
		@Override
		public String toString() {
			return "Overflow";
		}
	},
	NEGATIVE {
		@Override
		public String toString() {
			return "Negativo";
		}
	},
	OR {
		@Override
		public String toString() {
			return "OU lógico";
		}
	},
	AND {
		@Override
		public String toString() {
			return "E lógico";
		}
	},
	NOT {
		@Override
		public String toString() {
			return "NÃO lógico";
		}
	},
	CRASH {
		@Override
		public String toString() {
			return "Encerramento por falta do HLT";
		}
	};
}
