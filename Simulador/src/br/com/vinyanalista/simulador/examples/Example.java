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
			return "OU l�gico";
		}
	},
	AND {
		@Override
		public String toString() {
			return "E l�gico";
		}
	},
	NOT {
		@Override
		public String toString() {
			return "N�O l�gico";
		}
	},
	CRASH {
		@Override
		public String toString() {
			return "Encerramento por falta do HLT";
		}
	};
}
