from unittest import TestCase

from src.ro.csubb.library.domain.book import Book
from src.ro.csubb.library.repository.repository import Repository
from src.ro.csubb.library.service.book_service import BookService


class TestBookService(TestCase):
    def setUp(self) -> None:
        self.book_repository = Repository()
        self.book_service = BookService(self.book_repository)

    def test_create_book(self):
        book = self.book_service.create_entity('1', 'Generic Title', 'Generic Description', 'Generic Author')
        self.assertTrue(isinstance(book, Book), 'Cartea book trebuie sa apartina Book')
        self.assertTrue(book.id == 1, 'ID-ul cartii ar trebui sa fie 1')
        self.assertTrue(book.title == 'Generic Title', 'Titlul cartii ar trebui sa fie Generic Title')
        self.assertTrue(book.description == 'Generic Description', 'Descrierea cartii ar trebui sa fie Generic Description')
        self.assertTrue(book.author == 'Generic Author', 'Autorul cartii ar trebui sa fie Generic Author')

    def tearDown(self) -> None:
        pass
