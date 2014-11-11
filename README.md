nordlang
========
Пример скрипта:

def main() {
	show 'Hello, world!';
	def counter = 1;
	while(counter <= 10) {
		show counter * counter;
		counter = counter + 1;
	}
	show 'end';
}
