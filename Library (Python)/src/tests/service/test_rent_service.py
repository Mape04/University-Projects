from unittest import TestCase

from src.ro.csubb.library.domain.client import Client
from src.ro.csubb.library.domain.rent import Rent
from src.ro.csubb.library.repository.repository import Repository
from src.ro.csubb.library.service.rent_service import RentService


class TestRentService(TestCase):
    def setUp(self) -> None:
        self.repository = Repository()
        self.client_repo = Repository()
        self.rent_service = RentService(self.repository, self.client_repo)
        self.rent_service.add_entity(Rent(1, 1, 1))
        self.rent_service.add_entity(Rent(2, 2, 1))
        self.rent_service.add_entity(Rent(3, 2, 2))
        self.client_repo.add_entity(Client(1, 'Edy', 26012004))
        self.client_repo.add_entity(Client(2, 'Gilde', 326))

    def test_create_rent(self):
        rent = RentService.create_entity('1', '2', '3')
        self.assertTrue(isinstance(rent, Rent), 'Inchirierea rent trebuie sa apartina Rent')
        self.assertTrue(rent.id == 1, 'ID-ul inchirierii ar trebui sa fie 1')
        self.assertTrue(rent.id_client == 2, 'ID-ul clientului ar trebui sa fie  2')
        self.assertTrue(rent.id_book == 3, 'ID-ul cartii ar trebui sa fie 3')


    def test_load_from_file(self):
        pass

    def test_get_book_frequency(self):
        frequency = self.rent_service.get_book_frequency()
        self.assertTrue(frequency[1] == 2, 'Frecventa lui 1 ar trebui sa fie 2')
        self.assertTrue(frequency[2] == 1, 'Frecventa lui 2 ar trebui sa fie 1')

    def test_get_max_book(self):
        max_book = self.rent_service.get_max_book()
        self.assertTrue(len(max_book) == 1, 'Lungimea max_book ar trebui sa fie 1')
        self.assertTrue(max_book[0] == 1, 'max_book ar trebui sa aiba numai 1 in lista')

    def test_get_client_frequency(self):
        frequency = self.rent_service.get_client_frequency()
        self.assertTrue(len(frequency) == 2, 'Lungimea frecventei ar trebui sa fie 2')
        self.assertTrue(frequency[1] == 1, 'Frecventa lui 1 ar trebui sa fie 2')
        self.assertTrue(frequency[2] == 2, 'Frecventa lui 2 ar trebui sa fie 2')

    def test_get_client_names(self):
        client_list = self.rent_service.get_client_names()
        self.assertTrue(len(client_list) == 2)
        self.assertTrue(client_list[0]['id'] == 1)
        self.assertTrue(client_list[1]['id'] == 2)
        self.assertTrue(client_list[0]['name'] == 'Edy')
        self.assertTrue(client_list[1]['name'] == 'Gilde')

    def test_sort_clients_by_name(self):
        client_list = self.rent_service.sort_clients_by_name()
        self.assertTrue(len(client_list) == 2)
        self.assertTrue(client_list[0]['id'] == 2)
        self.assertTrue(client_list[0]['name'] == 'Edy')
        self.assertTrue(client_list[1]['id'] == 1)
        self.assertTrue(client_list[1]['name'] == 'Gilde')

    def test_sort_clients_by_frequency(self):
        client_list = self.rent_service.sort_clients_by_frequency()
        self.assertTrue(len(client_list) == 2)
        self.assertTrue(client_list[0][0] == 2)
        self.assertTrue(client_list[0][1] == 2)
        self.assertTrue(client_list[1][0] == 1)
        self.assertTrue(client_list[1][1] == 1)

    def test_most_active_clients(self):
        client_list = self.rent_service.most_active_clients()
        self.assertTrue(len(client_list) == 1)
        self.assertTrue(client_list[0][0] == 2)
        self.assertTrue(client_list[0][1] == 2)

    def test_list_length(self):
        self.assertTrue(RentService.list_length(100, 25) == 25)

    def tearDown(self) -> None:
        pass
