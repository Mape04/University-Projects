from unittest import TestCase

from src.ro.csubb.library.domain.client import Client
from src.ro.csubb.library.repository.repository import Repository
from src.ro.csubb.library.service.service import Service


class TestService(TestCase):
    def setUp(self) -> None:
        self.repository = Repository()
        self.service = Service(self.repository)
        self.service.add_entity(Client(1, 'Gilde', 69))
        self.service.add_entity(Client(2, 'Alex', 420))

    def test_get_all(self):
        client_list = self.service.get_all()
        self.assertTrue(len(client_list) == 2, 'Lungimea listei de clienti ar trebui sa fie 2')

    def test_get_by_id(self):
        client = self.service.get_by_id('1')
        self.assertTrue(client.id == 1, 'ID-ul clientului ar trebui sa fie 1')
        self.assertTrue(client.name == 'Gilde', 'Numele clientului ar trebui sa fie Gilde')
        self.assertTrue(client.ssn == 69, 'CNP-ul clientului ar trebui sa fie 69')

    def test_add_entity(self):
        self.service.add_entity(Client(3, 'Wald', 420))
        client_list = self.service.get_all()
        self.assertTrue(len(client_list) == 3, 'Lungimea listei de clienti trebuie sa fie 3')

    def test_update_entity(self):
        self.service.update_entity(Client(2, 'Ichigo', 69))
        client = self.service.get_by_id('1')
        self.assertTrue(client.id == 1, 'Id of client should be 1')
        self.assertTrue(client.name == 'Ichigo', 'Name of client should be Ichigo')
        self.assertTrue(client.ssn == 69, 'Ssn of client should be 69')

    def test_delete_entity(self):
        self.service.delete_entity('1')
        client_list = self.service.get_all()
        self.assertTrue(len(client_list) == 1, 'Length of client list should be 1')

    def tearDown(self) -> None:
        pass
