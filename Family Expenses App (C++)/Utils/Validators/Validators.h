//
// Created by mazil on 6/26/2023.
//

#ifndef FAMILIE_VALIDATORS_H
#define FAMILIE_VALIDATORS_H
#include <string>
#include <vector>
#include <algorithm>
#include "../Exceptions/Exceptions.h"
#include "../../Domain/Expense.h"

using namespace std;

class Validators {
public:
    void validateDay(int day);
    void validateSum(int sum);
    void validateType(string type);
    void validatePosition(vector<Expense> expenses, int position);

};


#endif //FAMILIE_VALIDATORS_H
