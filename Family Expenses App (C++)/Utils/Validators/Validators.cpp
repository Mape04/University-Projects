//
// Created by mazil on 6/26/2023.
//

#include "Validators.h"

void Validators::validateDay(int day) {
    if (day < 0 || day > 31)
        throw InvalidDayException("Day must be in between 1 and 31!");
}

void Validators::validateSum(int sum) {
    if (sum < 0)
        throw InvalidSumException("Sum must be a positive number!");
}

void Validators::validateType(string type) {
    vector<string> strings = {"menaj", "mancare", "transport", "haine", "internet", "altele"};
    bool ok = 0;
    for (auto string: strings)
        if (string == type)
            ok = 1;
    if(ok == 0)
        throw InvalidTypeException("Type must be one of the following: menaj, mancare, transport, altele !");
}

void Validators::validatePosition(vector<Expense> expenses, int position) {
    if(position < 0 || position >= expenses.size())
        throw InvalidPositionException("Position must be in between 0 and the size of the list!");
}
