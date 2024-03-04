//
// Created by mazil on 6/26/2023.
//

#ifndef FAMILIE_EXPENSE_H
#define FAMILIE_EXPENSE_H
#include <string>
#include <fstream>
#include <sstream>
#include <vector>
#include "../Utils/Exceptions/Exceptions.h"

using namespace std;

class Expense {
private:
    int day, sum;
    string type;
public:
    //default constructor
    Expense() = default;
    //constructor with params
    Expense(int day, int sum, string type);
    //copy constructor
    Expense(const Expense& otherExpense);
    //string constructor
    Expense(string args, char sep);
    //default destructor
    ~Expense() = default;

    //getters & setters
    int getDay() const;
    void setDay(int day);

    int getSum() const;
    void setSum(int sum);

    const string &getType() const;
    void setType(const string &type);

    //operator overloads
    bool operator==(const Expense &rhs) const;
    bool operator!=(const Expense &rhs) const;

    friend ostream &operator<<(ostream &os, const Expense &expense);
    friend istream &operator>>(istream &is, Expense &expense);

    //utils
    void loadFromString(string args, char sep);
    vector<string> split(const string &s, char delim);
};


#endif //FAMILIE_EXPENSE_H
