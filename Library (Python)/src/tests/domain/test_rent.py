from unittest import TestCase

from src.ro.csubb.library.domain.rent import Rent


class TestRent(TestCase):
    def setUp(self):
        self.rent = Rent(1, 2, 3)

    def test_id(self):
        self.assertTrue(self.rent.id == 1,
                        "ID-ul inchirierii ar trebui sa fie 1")

    def test_title(self):
        self.assertTrue(self.rent.id_client == 2,
                        "ID-ul clientului care inchiriaza ar trebui sa fie 2")

    def test_description(self):
        self.assertTrue(self.rent.id_book == 3,
                        "ID-ul cartii inchiriate ar trebui sa fie 3")

    def tearDown(self) -> None:
        pass
