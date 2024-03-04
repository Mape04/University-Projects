cmake_minimum_required(VERSION 3.25)
project(Familie)

set(CMAKE_CXX_STANDARD 17)

add_executable(Familie main.cpp Domain/Expense.cpp Domain/Expense.h Tests/tests.cpp Tests/tests.h Repository/ConsoleRepository.cpp Repository/ConsoleRepository.h Service/Service.cpp Service/Service.h UI/UI.cpp UI/UI.h Utils/Exceptions/Exceptions.cpp Utils/Exceptions/Exceptions.h Utils/Validators/Validators.cpp Utils/Validators/Validators.h Repository/FileRepository.cpp Repository/FileRepository.h)
