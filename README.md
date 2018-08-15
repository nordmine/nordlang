nordlang
========
Пример скрипта:
```C#
const string ABC_CAPITALIZED = "ABCDEFGHIJKLMNOPQRSTUVWXYZАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
const string ABC_LOWERED = "abcdefghijklmnopqrstuvwxyzабвгдеёжзийклмнопрстуфхцчшщъыьэюя";
const string DIGITS = "0123456789";

int main() {
    string text = "Красный; Жёлтый; Зелёный";
    echo "source: *" + text + "*" + newLine;
    string[] parts = split(text, ';');
    echo "Split parts: " + parts + newLine;
    string trimmedText = trim(parts[1]);
    echo "Trimmed: *" + trimmedText + "*" + newLine;
    echo "Reversed: " + reverseString(trimmedText) + newLine;
    echo "Upper: " + toUpper(trimmedText) + newLine;
    echo "Lower: " + toLower(trimmedText) + newLine;
    return 0;
}

// String utils

string substring(string source, int start) {
    int i = start;
    int len = size(source);
    string result = "";
    while(i < len) {
        result = result + source[i];
        i++;
    }
    return result;
}

string substringLimited(string source, int start, int length) {
    int i = start;
    int len = size(source);
    string result = "";
    while(i < len and i < length) {
        result = result + source[i];
        i++;
    }
    return result;
}

string trim(string source) {
    return trimLeft(trimRight(source));
}

string trimLeft(string source) {
    int len = size(source);
    int i = 0;
    string trimmed = "";
    while(i < len and source[i] == ' ') {
        i++;
    }
    while(i < len) {
        trimmed = trimmed + source[i];
        i++;
    }
    return trimmed;
}

string trimRight(string source) {
    int len = size(source);
    int i = len - 1;
    string trimmed = "";
    while(i >= 0 and source[i] == ' ') {
        i--;
    }
    int j = 0;
    while(j <= i) {
        trimmed = trimmed + source[j];
        j++;
    }
    return trimmed;
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
        i++;
    }
    return joined;
}

string reverseString(string source) {
    int len = size(source);
    string result = "";
    int i = len - 1;
    while(i >= 0) {
        result = result + source[i];
        i--;
    }
    return result;
}

int findChar(string source, char c) {
    return findCharFrom(source, c, 0);
}

int findCharFrom(string source, char c, int from) {
    int len = size(source);
    int i = from;
    while(i < len) {
        if (source[i] == c) {
            return i;
        }
        i++;
    }
    return -1;
}

string[] split(string source, char delimiter) {
    string[] parts = [];
    int from = 0;
    int to = 0;
    int len = size(source);
    while(from < len) {
        to = findCharFrom(source, delimiter, from);
        if (to == -1) {
            parts[] = substring(source, from);
            break;
        } else {
            parts[] = substringLimited(source, from, to);
        }
        from = to + 1;
    }
    return parts;
}

string toUpper(string source) {
    int i = 0;
    int len = size(source);
    string upper = "";
    while(i < len) {
        upper = upper + toUpperChar(source[i]);
        i++;
    }
    return upper;
}

char toUpperChar(char c) {
    int pos = findChar(ABC_LOWERED, c);
    if (pos > 0) {
        c = ABC_CAPITALIZED[pos];
    }
    return c;
}

string toLower(string source) {
    int i = 0;
    int len = size(source);
    string lower = "";
    while(i < len) {
        lower = lower + toLowerChar(source[i]);
        i++;
    }
    return lower;
}

char toLowerChar(char c) {
    int pos = findChar(ABC_CAPITALIZED, c);
    if (pos > 0) {
        c = ABC_LOWERED[pos];
    }
    return c;
}

bool containsChar(string where, char c) {
    return findChar(where, c) >= 0;
}

bool isDigit(char c) {
    return containsChar(DIGITS, c);
}

// Math utils

int pow(int val, int power) {
    int i = 1;
    int result = val;
    while(i < power) {
        result = result * val;
        i++;
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
