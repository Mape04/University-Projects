//
// Created by mazil on 6/26/2023.
//

#include "Service.h"
#include <algorithm>

void Service::addExpense(Expense expense) {
    validate.validateDay(expense.getDay());
    validate.validateSum(expense.getSum());
    validate.validateType(expense.getType());

    this->repo.addExpense(expense);
}

void Service::deleteExpense(int position) {
    validate.validatePosition(this->getAll(), position);

    this->repo.deleteExpense(position);
}

vector<Expense> Service::getAll() {
    return this->repo.getAll();
}

Expense &Service::getByPosition(int position) {
    validate.validatePosition(this->getAll(), position);

    return this->repo.getByPosition(position);
}

vector<Expense> Service::getByDay(int day) {
    validate.validateDay(day);

    vector<Expense> newVector;
    for(auto expense: this->repo.getAll())
        if(expense.getDay() == day)
            newVector.push_back(expense);
    return newVector;
}

//Service::Service(const ConsoleRepository<Expense> &otherRepo) {
//    this->repo = otherRepo;
//}

void Service::deleteByDay(int day) {
    validate.validateDay(day);

    vector<Expense> expenses = this->repo.getAll();
    int k = 0;
    for(auto expense: expenses){
        if(expense.getDay() == day)
            this->repo.deleteExpense(k);
        k++;
    }
}

int Service::sumType(string type) {
    validate.validateType(type);

    vector<Expense> expenses = this->repo.getAll();
    int s = 0;
    for(auto expense: expenses)
        if(expense.getType() == type)
            s+=expense.getSum();
    return s;
}

int Service::maxDay() {
    vector<Expense> expenses = this->repo.getAll();
    int s, max = -1, day = 0;
    for(int i = 1; i < 32; i++) {
        s = 0;
        vector<Expense> days = this->getByDay(i);
        for (auto expense: days)
            s += expense.getSum();
        if (s > max) {
            max = s;
            day = i;
        }
    }
    return day;
}

vector<Expense> Service::filterType(string type) {
    validate.validateType(type);

    vector<Expense> newVector;
    for(auto expense: this->repo.getAll())
        if(expense.getType() == type)
            newVector.push_back(expense);
    return newVector;
}

Service::Service(const FileRepository<Expense> &otherRepo) {
    this->repo = otherRepo;
}
