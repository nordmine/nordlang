nordlang
========
Пример скрипта:
```C#
int main() {
    string text = "Some Text";
    echo text + ':' + toUpper(text) + ',' + toLower(text);
    return 0;
}

// String utils

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

string toUpper(string original) {
    int i = 0;
    int len = size(original);
    string upper = "";
    while(i < len) {
        upper = upper + toUpperChar(original[i]);
        i = i + 1;
    }
    return upper;
}

char toUpperChar(char c) {
    string capitalized = "ABCDEFGHIJKLMNOPQRSTUVWXYZАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
    string lowered = "abcdefghijklmnopqrstuvwxyzабвгдеёжзийклмнопрстуфхцчшщъыьэюя";
    int pos = findChar(c, lowered);
    if (pos > 0) {
        c = capitalized[pos];
    }
    return c;
}

string toLower(string original) {
    int i = 0;
    int len = size(original);
    string lower = "";
    while(i < len) {
        lower = lower + toLowerChar(original[i]);
        i = i + 1;
    }
    return lower;
}

char toLowerChar(char c) {
    string capitalized = "ABCDEFGHIJKLMNOPQRSTUVWXYZАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
    string lowered = "abcdefghijklmnopqrstuvwxyzабвгдеёжзийклмнопрстуфхцчшщъыьэюя";
    int pos = findChar(c, capitalized);
    if (pos > 0) {
        c = lowered[pos];
    }
    return c;
}

bool containsChar(char c, string where) {
    return findChar(c, where) >= 0;
}

bool isDigit(char c) {
    return containsChar(c, "0123456789");
}

// Math utils

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

int min(int a, int b) {
    if (a < b) {
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