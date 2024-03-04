from unittest import TestCase

from src.ro.csubb.library.domain.client import Client
from src.ro.csubb.library.repository.repository import Repository
from src.ro.csubb.library.service.client_service import ClientService


class TestClientService(TestCase):
    def setUp(self) -> None:
        self.repository = Repository()
        self.client_service = ClientService(self.repository)

    def test_create_client(self):
        client = ClientService.create_entity('1', 'Gilde', '120')
        self.assertTrue(isinstance(client, Client), 'Clientul client trebuie sa apartina Client')
        self.assertTrue(client.id == 1, 'ID-ul clientului ar trebui sa fie 1')
        self.assertTrue(client.name == 'Gilde', 'Numele clientului ar trebui sa fie Gilde')
        self.assertTrue(client.ssn == 120, 'CNP-ul clientului ar trebui sa fie 120')

    def tearDown(self) -> None:
        pass
