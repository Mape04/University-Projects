import sys

from ro.csubb.library.exceptions.exceptions import EntityError


class Console:
    def __init__(self, client_service, book_service, rent_service):
        self.__client_service = client_service
        self.__book_service = book_service
        self.__rent_service = rent_service

    @staticmethod
    def id_input():
        return input("Introduceti ID: ")

    @staticmethod
    def option_input():
        return input("Alegeti optiune: ")

    @staticmethod
    def book_input():
        id_book = Console.id_input()
        title = input("Introduceti titlu: ")
        description = input("Introduceti descriere: ")
        author = input("Introduceti autor: ")
        return id_book, title, description, author

    @staticmethod
    def client_input():
        id_client = Console.id_input()
        name = input("Introduceti nume: ")
        ssn = input("Introduceti cnp: ")
        return id_client, name, ssn

    @staticmethod
    def rent_input():
        id_rent = Console.id_input()
        id_client = input("Introduceti ID-ul clientului: ")
        id_book = input("Introduceti ID-ul cartii: ")
        return id_rent, id_client, id_book

    @staticmethod
    def entity_format(entity):
        entity = str(entity)
        entity = entity.strip()
        return entity.split(" ")

    @staticmethod
    def print_book(book):
        book = Console.entity_format(book)
        print(f" ID: {book[0]}\n"
              f" Titlu: {book[1]}\n"
              f" Descriere: {book[2]}\n"
              f" Autor: {book[3]}\n")

    @staticmethod
    def print_client(client):
        client = Console.entity_format(client)
        print(f" ID: {client[0]}\n"
              f" Nume: {client[1]}\n"
              f" CNP: {client[2]}\n")

    @staticmethod
    def print_rent(rent):
        rent = Console.entity_format(rent)
        print(f"ID: {rent[0]}"
              f"ID Client: {rent[1]}"
              f"ID Carte: {rent[2]}")

    @staticmethod
    def print_menu():
        print("""
        1. Adaugati entitate
        2. Actualizati entitate
        3. Stergeti entitate
        4. Cautati entitate
        5. Afisati entitate
        6. Rapoarte
        7. Incarcati din repository
        x. Iesire""")

    @staticmethod
    def quit():
        sys.exit("La revedere!")

    def menu(self):
        while True:
            Console.print_menu()
            options = {'1': self.add_sub_menu,
                       '2': self.update_sub_menu,
                       '3': self.delete_sub_menu,
                       '4': self.lookup_sub_menu,
                       '5': self.print_sub_menu,
                       '6': self.print_statistics_sub_menu,
                       '7': self.load_repos,
                       'x': Console.quit}
            option = Console.option_input()
            option = option.lower()
            try:
                options[option]()
            except KeyError as ke:
                print('Optine invalida!', ke)

    def load_repos(self):
        self.__book_service.load_from_file()
        self.__client_service.load_from_file()
        self.__rent_service.load_from_file()

    def add_sub_menu(self):
        print("""
        1. Adaugati carte
        2. Adaugati client
        3. Adaugati inchiriere""")
        options = {'1': self.add_book,
                   '2': self.add_client,
                   '3': self.add_rent}
        option = Console.option_input()
        options[option]()

    def update_sub_menu(self):
        print("""
        1. Actualizati carte
        2. Actualizati client
        3. Actualizati inchiriere""")
        options = {'1': self.update_book,
                   '2': self.update_client,
                   '3': self.update_rent}
        option = Console.option_input()
        options[option]()

    def delete_sub_menu(self):
        print("""
        1. Stergeti book
        2. Stergeti client
        3. Stergeti rent""")
        options = {'1': self.delete_book,
                   '2': self.delete_client,
                   '3': self.delete_rent}
        option = Console.option_input()
        options[option]()

    def lookup_sub_menu(self):
        print("""
        1. Cautati carte
        2. Cautati client
        3. Cautati inchiriere""")
        options = {'1': self.lookup_book,
                   '2': self.lookup_client,
                   '3': self.lookup_rent}
        option = Console.option_input()
        options[option]()

    def print_sub_menu(self):
        print("""
        1. Afisati carti
        2. Afisati clienti
        3. Afisati inchirieri""")
        options = {'1': self.print_books,
                   '2': self.print_clients,
                   '3': self.print_rents}
        option = Console.option_input()
        options[option]()

    def print_statistics_sub_menu(self):
        print("""
        1. Afisati clientii care au inchiriat cele mai multe carti
        2. Afisati clientii ordonati dupa criteriu
        3. Afisati clientii cei mai activi""")
        options = {'1': self.print_max_book,
                   '2': self.order_clients_sub_menu,
                   '3': self.print_most_active_clients}
        option = Console.option_input()
        options[option]()

    def order_clients_sub_menu(self):
        print("""
        1. Afisati clientii ordonati dupa nume
        2. Afisati clientii dupa numarul de carti inchiriate pe care le au""")
        options = {'1': self.print_clients_ordered_by_name,
                   '2': self.print_clients_ordered_by_frequency}
        option = Console.option_input()
        options[option]()

    def add_book(self):
        try:
            id_book, title, description, author = Console.book_input()
            self.__book_service.add_entity(self.__book_service.create_entity(id_book, title, description, author))
        except EntityError as err:
            print(err, '\n')

    def add_client(self):
        try:
            id_client, name, ssn = Console.client_input()
            self.__client_service.add_entity(self.__client_service.create_entity(id_client, name, ssn))
        except EntityError as err:
            print(err, '\n')

    def add_rent(self):
        try:
            id_rent, id_client, id_book = Console.rent_input()
            self.__rent_service.add_entity(self.__rent_service.create_entity(id_rent, id_client, id_book))
        except EntityError as err:
            print(err, '\n')

    def update_book(self):
        try:
            id_book, title, description, author = Console.book_input()
            self.__book_service.update_entity(self.__book_service.create_entity(id_book, title, description, author))
        except EntityError as err:
            print(err, '\n')

    def update_client(self):
        try:
            id_client, name, ssn = Console.client_input()
            self.__client_service.update_entity(self.__client_service.create_entity(id_client, name, ssn))
        except EntityError as err:
            print(err, '\n')

    def update_rent(self):
        try:
            id_rent, id_client, id_book = Console.rent_input()
            self.__rent_service.update_entity(self.__rent_service.create_entity(id_rent, id_client, id_book))
        except EntityError as err:
            print(err, '\n')

    def delete_book(self):
        try:
            id_book = Console.id_input()
            self.__book_service.delete_entity(id_book)
        except EntityError as err:
            print(err, '\n')

    def delete_client(self):
        try:
            id_client = Console.id_input()
            self.__client_service.delete_entity(id_client)
        except EntityError as err:
            print(err, '\n')

    def delete_rent(self):
        try:
            id_rent = Console.id_input()
            self.__rent_service.delete_entity(id_rent)
        except EntityError as err:
            print(err, '\n')

    def lookup_book(self):
        try:
            id_book = Console.id_input()
            book = self.__book_service.get_by_id(id_book)
            Console.print_book(book)
        except EntityError as err:
            print(err, '\n')

    def lookup_client(self):
        try:
            id_client = Console.id_input()
            client = self.__client_service.get_by_id(id_client)
            Console.print_book(client)
        except EntityError as err:
            print(err, '\n')

    def lookup_rent(self):
        try:
            id_rent = Console.id_input()
            rent = self.__rent_service.get_by_id(id_rent)
            Console.print_rent(rent)
        except EntityError as err:
            print(err, '\n')

    def print_books(self):
        book_list = self.__book_service.get_all()
        for book in book_list:
            Console.print_book(book)

    def print_clients(self):
        client_list = self.__client_service.get_all()
        for client in client_list:
            Console.print_client(client)

    def print_rents(self):
        rent_list = self.__rent_service.get_all()
        for rent in rent_list:
            Console.print_rent(rent)

    def print_clients_ordered_by_name(self):
        client_list = self.__rent_service.sort_clients_by_name()
        for client in client_list:
            Console.print_client(self.__client_service.get_by_id(client['id']))

    def print_clients_ordered_by_frequency(self):
        client_list = self.__rent_service.sort_clients_by_frequency()
        for client_id in client_list:
            Console.print_client(self.__client_service.get_by_id(client_id))

    def print_most_active_clients(self):
        for id_client in self.__rent_service.most_active_clients():
            Console.print_client(self.__client_service.get_by_id(id_client))

    def print_max_book(self):
        for id_book in self.__rent_service.get_max_book():
            Console.print_book(self.__book_service.get_by_id(id_book))
