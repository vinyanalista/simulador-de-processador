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
			return "Subtra��o";
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
			return "Nega��o l�gica";
		}
	};
}