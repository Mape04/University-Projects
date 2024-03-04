//
// Created by mazil on 6/26/2023.
//

#include "Expense.h"


Expense::Expense(int day, int sum, string type) {
    this->day = day;
    this->sum = sum;
    this->type = type;
}

int Expense::getDay() const {
    return day;
}

void Expense::setDay(int day) {
    Expense::day = day;
}

int Expense::getSum() const {
    return sum;
}

void Expense::setSum(int sum) {
    Expense::sum = sum;
}

const string &Expense::getType() const {
    return type;
}

void Expense::setType(const string &type) {
    Expense::type = type;
}

Expense::Expense(const Expense &otherExpense) {
    this->day = otherExpense.day;
    this->sum = otherExpense.sum;
    this->type = otherExpense.type;
}

bool Expense::operator==(const Expense &rhs) const {
    return day == rhs.day &&
           sum == rhs.sum &&
           type == rhs.type;
}

bool Expense::operator!=(const Expense &rhs) const {
    return !(rhs == *this);
}

ostream &operator<<(ostream &os, const Expense &expense) {
    os << expense.day << "," << expense.sum << "," << expense.type;
    return os;
}

istream &operator>>(istream &is, Expense &expense) {
    is >> expense.day >> expense.sum >> expense.type;
    return is;
}

Expense::Expense(string args, char sep) {
    this->loadFromString(args,sep);
}

void Expense::loadFromString(string args, char sep) {
    vector<string> elements = split(args, sep);
    if (elements.size() == 3){
        stringstream d(elements[0]);
        d >> this->day;
        stringstream s(elements[1]);
        s >> this->sum;
        stringstream t(elements[2]);
        t >> this->type;
    }
    else{
        throw InvalidArgsException("nr invalid de argumente in stringul ce tb parsat");
    }

}

//you can put this in a Utils Folder
vector<string> Expense::split(const string &s, char delim) {
    vector<string> result;
    stringstream ss (s);
    string item;

    while (getline (ss, item, delim)) {
        result.push_back (item);
    }

    return result;
}


