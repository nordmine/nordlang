nordlang
========
Пример скрипта:
```C#
int main() {
    string name = "Вася";
    echo greeting(name);
    return 0;
}

string greeting(string name) {
    return "Привет, " + name + "!";
}

int pow(int val, int power) {
    int i = 1;
    int result = val;
    while(i < power) {
        result = result * val;
        i = i + 1;
    }
    return result;
}

int max(int a, int b) {
    if (a > b) {
        return a;
    } else {
        return b;
    }
}

int abs(int val) {
    if (val < 0) {
        val = -1 * val;
    }
    return val;
}
```