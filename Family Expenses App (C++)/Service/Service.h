//
// Created by mazil on 6/26/2023.
//

#ifndef FAMILIE_SERVICE_H
#define FAMILIE_SERVICE_H
#include "../Domain/Expense.h"
#include "../Repository/ConsoleRepository.h"
#include "../Repository/FileRepository.h"
#include "../Utils/Validators/Validators.h"

class Service {
private:
    //ConsoleRepository<Expense> repo;
    FileRepository<Expense> repo = FileRepository<Expense>(R"(C:\Users\mazil\Documents\0_C++_Clion\Familie\Text Files\expenses.txt)");
    Validators validate;
public:
    Service() = default;
    //Service(const ConsoleRepository<Expense>& otherRepo);
    Service(const FileRepository<Expense>& otherRepo);
    ~Service() = default;

    //adds an expense
    void addExpense(Expense expense);
    //deletes an expense
    void deleteExpense(int position);
    //returns all the expenses
    vector<Expense> getAll();
    //returns an expense by position
    Expense& getByPosition(int position);
    //returns expenses by day
    vector<Expense> getByDay(int day);
    //deletes an expense by day
    void deleteByDay(int day);
    //returns sum of the expense type
    int sumType(string type);
    //returns the day with the most spent money
    int maxDay();
    //returns only the food expenses
    vector<Expense> filterType(string type);
};


#endif //FAMILIE_SERVICE_H
