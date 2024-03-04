from unittest import TestCase

from src.ro.csubb.library.domain.book import Book


class TestBook(TestCase):
    def setUp(self):
        self.book = Book(1, "Title_1", "Description_1", "Author_1")

    def test_id(self):
        self.assertTrue(self.book.id == 1,
                        "ID-ul cartii ar trebui sa fie 1")

    def test_title(self):
        self.assertTrue(self.book.title == 'Title_1',
                        "Titlul cartii ar trebui sa fie Title_1")

    def test_description(self):
        self.assertTrue(self.book.description == 'Description_1',
                        "Descrierea cartii ar trebui sa fie Description_1")

    def test_author(self):
        self.assertTrue(self.book.author == 'Author_1',
                        "Autorul cartii ar trebui sa fie Author_1")

    def tearDown(self) -> None:
        pass
