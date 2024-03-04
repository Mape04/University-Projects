from unittest import TestCase

from src.ro.csubb.library.domain.client import Client


class TestClient(TestCase):
    def setUp(self):
        self.client = Client(1, "Name_1", 123)

    def test_id(self):
        self.assertTrue(self.client.id == 1,
                        "ID-ul clientului ar trebui sa fie 1")

    def test_name(self):
        self.assertTrue(self.client.name == 'Name_1',
                        "Numele clientului ar trebui sa fie Name_1")

    def test_ssn(self):
        self.assertTrue(self.client.ssn == 123,
                        "CNP-ul clientului ar trebui sa fie 123")

    def tearDown(self) -> None:
        pass
