package br.com.vinyanalista.simulador.examples;

public enum Example {
	ADD {
		@Override
		public String toString() {
			return "Soma";
		}
	},
	NEGATIVE {
		@Override
		public String toString() {
			return "Negativo";
		}
	},
	OVERFLOW {
		@Override
		public String toString() {
			return "Overflow";
		}
	},
	NOT {
		@Override
		public String toString() {
			return "Negação lógica";
		}
	},
	CRASH {
		@Override
		public String toString() {
			return "Encerramento por falta do HLT";
		}
	};
}