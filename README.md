nordlang
========
Пример скрипта:
```C#
int main() {
    string[] parts = ["раз", "два", "три", "четыре"];
    echo implode(parts, "-");
    return 0;
}

string implode(string[] parts, string glue) {
    int len = size(parts);
    int i = 0;
    string joined = "";
    while(i < len) {
        joined = joined + parts[i];
        if (i < len - 1) {
            joined = joined + glue;
        }
        i = i + 1;
    }
    return joined;
}

string reverseString(string original) {
    int len = size(original);
    string result = "";
    int i = len - 1;
    while(i >= 0) {
        result = result + original[i];
        i = i - 1;
    }
    return result;
}

int findChar(char c, string where) {
    int len = size(where);
    int i = 0;
    while(i < len) {
        if (where[i] == c) {
            return i;
        }
        i = i + 1;
    }
    return -1;
}

bool containsChar(char c, string where) {
    return findChar(c, where) >= 0;
}

bool isDigit(char c) {
    return containsChar(c, "0123456789");
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