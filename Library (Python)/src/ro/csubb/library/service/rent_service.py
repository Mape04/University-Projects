from ro.csubb.library.domain.rent import Rent
from ro.csubb.library.exceptions.exceptions import Validators
from ro.csubb.library.service.service import Service


class RentService(Service):
    def __init__(self, rent_repository, client_repository):
        Service.__init__(self, rent_repository)
        self.__client_repository = client_repository

    @staticmethod
    def create_entity(id_rent, id_client, id_book):
        Validators.validate_rent(id_rent, id_client, id_book)
        return Rent(int(id_rent), int(id_client), int(id_book))

    def load_from_file(self):
        lines = Service.file_get_lines(self)
        Service.file_delete_contents(self)
        for line in lines:
            line = line.split()
            Service.add_entity(self, RentService.create_entity(line[0], line[1], line[2]))

    def get_book_frequency(self):
        frequency = {}
        for rent in Service.get_all(self):
            if rent.id_book in frequency:
                frequency[rent.id_book] += 1
            else:
                frequency[rent.id_book] = 1
        return frequency

    def get_max_book(self):
        id_list = []
        max_frequency = 0
        book_frequency = self.get_book_frequency()
        for id_book in book_frequency:
            if book_frequency[id_book] > max_frequency:
                id_list.clear()
                id_list.append(id_book)
                max_frequency = book_frequency[id_book]
            elif book_frequency[id_book] == max_frequency:
                id_list.append(id_book)
        return id_list

    def get_client_frequency(self):
        frequency = {}
        for rent in Service.get_all(self):
            if rent.id_client in frequency:
                frequency[rent.id_client] += 1
            else:
                frequency[rent.id_client] = 1
        return frequency

    def get_client_names(self):
        client_list = []
        client_frequency = self.get_client_frequency()
        for client in client_frequency:
            client_name = self.__client_repository.get_by_id(client).name
            client_list.append({'id': client, 'name': client_name})
        return client_list

    def sort_clients_by_name(self):
        client_list = self.get_client_names()
        return sorted(client_list, key=lambda d: d['name'])

    def sort_clients_by_frequency(self):
        client_list = self.get_client_frequency()
        return sorted(client_list.items(), key=lambda d: d[1], reverse=True)

    @staticmethod
    def list_length(list_length, percent):
        if int(percent / 100 * list_length) == 0:
            return int(percent / 100 * list_length) + 1
        return int(percent / 100 * list_length)

    def most_active_clients(self):
        client_list = self.sort_clients_by_frequency()
        return client_list[:RentService.list_length(len(client_list), 20)]
