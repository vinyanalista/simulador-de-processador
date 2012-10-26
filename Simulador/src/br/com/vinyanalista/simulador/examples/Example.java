package br.com.vinyanalista.simulador.examples;

public enum Example {
	ADD {
		@Override
		public String toString() {
			return "Soma";
		}
	},
	SUB {
		@Override
		public String toString() {
			return "Subtração";
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
	};
}