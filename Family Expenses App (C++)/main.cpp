#include <iostream>
#include "Tests/tests.h"
#include "UI/UI.h"
#include "Repository/FileRepository.h"
int main() {
    testExpense();
    testConsoleRepository();
    testService();

    //ConsoleRepository<Expense> repo;
    FileRepository<Expense> repo(R"(C:\Users\mazil\Documents\0_C++_Clion\Familie\Text Files\expenses.txt)");
    Service service(repo);
    UI ui(service);
    ui.run();
    return 0;
}
