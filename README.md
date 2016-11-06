nordlang
========
Пример скрипта:
```C#
int main() {
    string text = "    Some Text    ";
    string trimmedText = trim(text);
    echo "Original: *" + text + "*" + newLine;
    echo "Trimmed: *" + trimmedText + "*" + newLine;
    echo "Reversed: " + reverseString(trimmedText) + newLine;
    echo "Upper: " + toUpper(trimmedText) + newLine;
    echo "Lower: " + toLower(trimmedText) + newLine;
    return 0;
}

// String utils

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

string reverseString(string original) {
    int len = size(original);
    string result = "";
    int i = len - 1;
    while(i >= 0) {
        result = result + original[i];
        i--;
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
        i++;
    }
    return -1;
}

string toUpper(string original) {
    int i = 0;
    int len = size(original);
    string upper = "";
    while(i < len) {
        upper = upper + toUpperChar(original[i]);
        i++;
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
        i++;
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
