//
// Created by mazil on 6/26/2023.
//

#ifndef FAMILIE_CONSOLEREPOSITORY_H
#define FAMILIE_CONSOLEREPOSITORY_H
#include <vector>
#include <algorithm>
using namespace std;

template <typename T>
class ConsoleRepository {
private:
    vector <T> expenses;
public:
    //default constructor
    ConsoleRepository() = default;
    //copy constructor
    ConsoleRepository(const ConsoleRepository& otherConsoleRepository);
    //default destructor
    ~ConsoleRepository() = default;

    virtual //adds an expense
    void addExpense(T expense);

    virtual //deletes an expense
    void deleteExpense(int position);

    virtual //returns all the expenses
    vector<T> getAll();

    virtual //returns an expense by position
    T& getByPosition(int position);
};

template<typename T>
T &ConsoleRepository<T>::getByPosition(int position) {
    return this->expenses.at(position);
}

template<typename T>
vector<T> ConsoleRepository<T>::getAll() {
    return this->expenses;
}

template<typename T>
void ConsoleRepository<T>::addExpense(T expense) {
    this->expenses.push_back(expense);
}

template<typename T>
void ConsoleRepository<T>::deleteExpense(int position) {
    this->expenses.erase(expenses.begin() + position);
}

template<typename T>
ConsoleRepository<T>::ConsoleRepository(const ConsoleRepository &otherConsoleRepository) {
    this->expenses = otherConsoleRepository.expenses;
}



#endif //FAMILIE_CONSOLEREPOSITORY_H
