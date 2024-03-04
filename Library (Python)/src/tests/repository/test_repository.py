from unittest import TestCase

from src.ro.csubb.library.domain.client import Client
from src.ro.csubb.library.repository.repository import Repository


class TestRepository(TestCase):
    def setUp(self):
        self.repository = Repository()
        self.repository.add_entity(Client(1, 'Joe', 123))
        self.repository.add_entity(Client(2, 'Gilde', 202))

    def test_get_all(self):
        client_list = self.repository.get_all()
        self.assertTrue(len(client_list) == 2, 'Lungimea listei de clienti ar trebui sa fie 2')

    def test_get_by_id(self):
        client = self.repository.get_by_id(2)
        self.assertTrue(client.id == 2, 'ID-ul clientului ar trebui sa fie 2')
        self.assertTrue(client.name == 'Gilde', 'Numele clientului ar trebui sa fie Gilde')
        self.assertTrue(client.ssn == 202, 'CNP-ul clientului ar trebui sa fie 202')

    def test_add_entity(self):
        self.repository.add_entity(Client(3, 'Wald', 420))
        client_list = self.repository.get_all()
        self.assertTrue(len(client_list) == 3, 'Lungimea listei de clienti ar trebui sa fie 3')

    def test_update_entity(self):
        self.repository.update_entity(Client(1, 'Wald', 420))
        client = self.repository.get_by_id(1)
        self.assertTrue(client.id == 1, 'ID-ul clientului ar trebui sa fie 1')
        self.assertTrue(client.name == 'Wald', 'Numele clientului ar trebui sa fie Wald')
        self.assertTrue(client.ssn == 420, 'CNP-ul clientului ar trebui sa fie 420')

    def test_delete_entity(self):
        self.repository.delete_entity(1)
        client_list = self.repository.get_all()
        self.assertTrue(len(client_list) == 1, 'Lungimea listei de clienti ar trebui sa fie 1')

    def tearDown(self) -> None:
        pass
